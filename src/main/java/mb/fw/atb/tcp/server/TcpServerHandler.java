package mb.fw.atb.tcp.server;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import mb.fw.atb.config.sub.IFContext;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentDetailEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentDetailParser;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentResultEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentResultParser;
import mb.fw.atb.tcp.server.service.TcpServerService;
import mb.fw.atb.util.ATBUtil;

@Slf4j
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	
	private final String remoteHost; 
	private final int remotePort;
	private JmsTemplate jmsTemplate;
	
	public TcpServerHandler(String remoteHost, int remotePort, JmsTemplate jmsTemplate) {
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.jmsTemplate = jmsTemplate;
	}
	
	private Channel channel;
	
	private Channel kftcOutboundChannel; //금결원
	@Autowired
	TcpServerService tcpServerService;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
	    ByteBuf buf = (ByteBuf) msg;
	 // ByteBuf 복사 (release 방지)
        ByteBuf copy = buf.copy();
        String strMsg = copy.toString(StandardCharsets.UTF_8);
    	log.info("수신 msg : {}", strMsg);
    	String headerMsg = strMsg.substring(4, 74);
    	log.info("headerMsg : {}", headerMsg);
    	String trCode = headerMsg.substring(10, 16);
    	log.info("거래구분코드 : {}", trCode);
    	/* 
    	 	◎ 거래구분코드
			통신망 전문(Test Call)				- 000301 
			경찰청 범칙금 - 과태료 고지내역 상세 조회	- 121002
			경찰청 범칙금 - 과태료 납부결과 통지		- 122001
			경찰청 범칙금 - 과태료 납부(재) 취소		- 992001
    	*/
    	String response;
    	String resultCode;
//    	if("000301".equals(trCode)) { // 통신망 전문
//    		PenaltyTestCallEntity req = PenaltyTestCallParser.toEntity(strMsg);
//    		PenaltyTestCallEntity res = PenaltyTestCallParser.makeResponeMessage(req);
//    		if (res != null) {
//                response = res.toFixedLengthString();
//            } else {
//                // 없는 경우, 에러 전문 생성
//                res = new PenaltyTestCallEntity();
//                res.setRespCode("ERR");
//                response = res.toFixedLengthString();
//            }
//    	} else 
    	if("121002".equals(trCode)) { // 상세조회
    		PenaltyPaymentDetailEntity req = PenaltyPaymentDetailParser.toEntity(strMsg);
    		PenaltyPaymentDetailEntity res = tcpServerService.findByOrgMsgNo(req.getOrgMsgNo());
    		if (res != null) {
    			resultCode = "000";
    			response = PenaltyPaymentDetailParser.makeResponeMessage(req, res, resultCode);
            } else {
            	resultCode = "311"; //고지내역 없음
            	response = PenaltyPaymentDetailParser.makeResponeMessage(req, res, resultCode);
            }
    	} else if("122001".equals(trCode)) { // 납부결과
    		PenaltyPaymentResultEntity req = PenaltyPaymentResultParser.toEntity(strMsg);
    		int resultCount = tcpServerService.updateResult(req);
//    		int resultCount = tcpServerService.insertResult(req); // insert 인지 update 인지?
    		if (resultCount > 0) {
    			//update 또는 insert 가 1건이면 정상
    			resultCode = "000";
    			response = PenaltyPaymentResultParser.makeResponeMessage(req, resultCode);
            } else {
                ///TODO 오류 여부에 따라 응답코드 다르게 줘야함. 엑셀 참조
            	resultCode = "332"; //(조회) 원거래 없음
            	response = PenaltyPaymentResultParser.makeResponeMessage(req, resultCode);
            }
    		
    	}  
//    	else if("992001".equals(trCode)) { // 납부(재)취소, proxy 에서 하니깐 필요 없을듯
//    		
//    	} 
    	else {
    		log.error("등록 되지 않은 거래구분 코드 입니다.");
    		response = "";
    	}
    	
        log.info("응답 송신");
        log.info("remoteHost : {}, remotePort : {}", this.remoteHost, this.remotePort);
        
        ByteBuf resBuffer = Unpooled.copiedBuffer(response, StandardCharsets.UTF_8);
        
        //TCPProxy 연결(송신)
        if (kftcOutboundChannel == null || !kftcOutboundChannel.isActive()) {
        	
        	EventLoopGroup group = new NioEventLoopGroup();
        	
        	Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true) // 지연 전송 방지
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new TcpClientHandler());
                        }
                    });

	        ChannelFuture f = bootstrap.connect(this.remoteHost, this.remotePort);
	        kftcOutboundChannel = f.channel();
	        log.info("response : {}", response);
	        
	        f.addListener((ChannelFuture future) -> {
	            if (future.isSuccess()) {
	            	kftcOutboundChannel.writeAndFlush(resBuffer).addListener((ChannelFuture writeFuture) -> {
	                    if (writeFuture.isSuccess()) {
//	                    	writeFuture.channel().close(); // 바로 종료
	                    	ctx.channel().read();
	                    } else {
	                        writeFuture.channel().close();
	                    }
	                });
	            } else {
	                // 연결 실패 시 클라이언트 닫기
	                buf.release();
	                ctx.channel().close();
	            }
	        });
	    } else {
	    	kftcOutboundChannel.writeAndFlush(resBuffer).addListener((ChannelFuture future) -> {
	            if (future.isSuccess()) {
	            	ctx.channel().read();
	            } else {
//	            	kftcOutboundChannel.close();
	            	future.channel().close();
	            }
	        });
	    }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		channel = ctx.channel();
		channel.read();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}
	
    public void startLogging(IFContext context, String txid, String msgCreDt, String sendDt, long count) throws Exception {
        for (String receiverId : context.getReceiverIds()) {
             log.info("startLogging receiverId : {}", receiverId);
        }
        ATBUtil.startLogging(jmsTemplate, context.getInterfaceId(), txid, context.getReceiverIds(), count, context.getSendSystemCode(), context.getReceiveSystemCode(), msgCreDt, sendDt);
    }

    public void endLogging(IFContext context, String txid, long errCount, String receiverId, String resultCd, String resultMessage, String msgRcvDt) throws Exception {
        ATBUtil.endLogging(jmsTemplate, context.getInterfaceId(), txid, receiverId, errCount, resultCd, resultMessage, msgRcvDt);
    }
	
	public static void main(String[] args) throws Exception {
		String strMsg = "0070IGN0990800000301   C   202507290940                        121234567  aaaaabbbb";
		String headerMsg = strMsg.substring(4, 74);
		System.out.println(headerMsg);
		System.out.println(headerMsg.getBytes().length);
		String trCode = headerMsg.substring(10, 16);
		System.out.println(trCode);
	}
}
