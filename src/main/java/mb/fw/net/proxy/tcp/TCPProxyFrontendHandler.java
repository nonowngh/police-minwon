package mb.fw.net.proxy.tcp;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mb.mci.common.util.DateUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
import mb.fw.atb.tcp.server.TcpClientHandler;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentCancleEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentCancleParser;
import mb.fw.atb.tcp.server.entity.PenaltyTestCallEntity;
import mb.fw.atb.tcp.server.entity.PenaltyTestCallParser;
import mb.fw.atb.tcp.server.service.TcpServerService;

@Slf4j
public class TCPProxyFrontendHandler extends ChannelInboundHandlerAdapter {
	private final String remoteHost; // remote-host: TCS:127.0.0.1,SJS:127.0.0.1 (TCS : 교통, SJS : 즉심)
	private final String remotePort; // remote-port: TCS:8081,SJS:8082
	private final int gbnStartlength; // 교통, 즉심을 구분하는 값이 있는 시작위치
	private final int gbnEndlength; // 교통, 즉심을 구분하는 값의 길이
	private Channel tcsOutboundChannel; //교통
	private Channel sjsOutboundChannel; //즉심
	private Channel kftcOutboundChannel; //금결원
	private Channel inboundChannel;

	@Autowired
	TcpServerService tcpServerService;
	
	public TCPProxyFrontendHandler(String remoteHost, String remotePort, int gbnStartlength, int gbnEndlength ) {
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.gbnStartlength = gbnStartlength;
		this.gbnEndlength = gbnEndlength;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		inboundChannel = ctx.channel();
		inboundChannel.read();
	}
	
	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) {
		String remoteAddress = ctx.channel().remoteAddress().toString();
		String [] address = remoteAddress.split(":");
		String remoteIp = address[0].substring(1);
		log.info("송신 기관 IP : {}", remoteIp);
		Map<String, String> targetSysInfoMap = getTargetSystemInfo(this.remoteHost, this.remotePort);
		String targetHost;
        int targetPort;
        ByteBuf buf = (ByteBuf) msg;
        // ByteBuf 복사 (release 방지)
        ByteBuf copy = buf.copy();
        String resultCode = "";
        String strMsg = copy.toString(StandardCharsets.UTF_8);
        log.info("수신 데이터 : msg : {}", strMsg);
        copy.release(); // 복사본 해제
        // 예: 앞 3글자가 GB값
        String headerMsg = strMsg.substring(4, 74); //전문길이 제외
    	log.info("headerMsg : {}", headerMsg);
    	String sendFlag = headerMsg.substring(19, 20);
    	log.info("송수신 Flag : {}", sendFlag);
		if("C".equals(sendFlag)) { // C : 금결원, G : 경찰청
			//수신
	    	String trCode = headerMsg.substring(10, 16);
	    	log.info("거래구분코드 : {}", trCode);
	    	
	    	String gbn = "";
	    	
	    	String year = DateUtil.getYear();
	    	/**
	    	 * TCS : 교통, SJS : 즉심
	    	 */
	    	if("000301".equals(trCode)) { // 통신망 전문
	    		log.info("TestCall");
	    		PenaltyTestCallEntity req = PenaltyTestCallParser.toEntity(strMsg);
	            String response = PenaltyTestCallParser.makeResponeMessage(req, resultCode);
	            ByteBuf resubuf = Unpooled.copiedBuffer(response, StandardCharsets.UTF_8);
	    		//금결원으로 응답전문 전달
	    		targetHost = targetSysInfoMap.get("KFT_IP");
	            targetPort = Integer.parseInt(targetSysInfoMap.get("KFT_PORT"));
	            log.info("targetHost : {}, targetPort : {}", targetHost, targetPort);
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

	    	        ChannelFuture f = bootstrap.connect(targetHost, targetPort);
	    	        kftcOutboundChannel = f.channel();
	    	        log.info("response : {}", response);
	    	        
	    	        f.addListener((ChannelFuture future) -> {
	    	            if (future.isSuccess()) {
	    	            	kftcOutboundChannel.writeAndFlush(resubuf).addListener((ChannelFuture writeFuture) -> {
	    	                    if (writeFuture.isSuccess()) {
//	    	                    	writeFuture.channel().close(); // 바로 종료
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
	    	    	kftcOutboundChannel.writeAndFlush(resubuf).addListener((ChannelFuture future) -> {
	    	            if (future.isSuccess()) {
	    	            	ctx.channel().read();
	    	            } else {
//	    	            	kftcOutboundChannel.close();
	    	            	future.channel().close();
	    	            }
	    	        });
	    	    }
	    	} else if("121002".equals(trCode)) { // 상세조회
	    		log.info("상세조회");
	    		//교통, 즉심 구별
	    		String bodyMsg = strMsg.substring(74);
	    		String elecPayNo = bodyMsg.substring(0, 19); //전자납부번호
	    		//전자납부번호 1307 + yyyy + 3 이 즉심
	    		if((year + "3").equals(elecPayNo.substring(gbnStartlength, gbnEndlength))) {
	    			gbn = "SJS";
	    		} else {
	    			gbn = "TCS";
	    		}
	    	} else if("122001".equals(trCode)) { // 납부결과
	    		log.info("납부결과");
	    		//교통, 즉심 구별
	    		String bodyMsg = strMsg.substring(74);
	    		String elecPayNo = bodyMsg.substring(19, 38);
	    		if((year + "3").equals(elecPayNo.substring(gbnStartlength, gbnEndlength))) {
	    			gbn = "SJS";
	    		} else {
	    			gbn = "TCS";
	    		}
	    	}  else if("992001".equals(trCode)) { // 납부(재)취소
	    		log.info("납부(재)취소");
	    		//교통, 즉심 구별 할수 있는 방법이 없음
	    		// 일단 받아서 우리 쪽 DB 업데이트 하고 TCS 로 보내자.
	    		PenaltyPaymentCancleEntity req = PenaltyPaymentCancleParser.toEntity(strMsg);
	    		int updateCount = tcpServerService.updateCancle(req);
	    		if(updateCount > 0) {
	    			//응답전문 금결원으로 전송
	    			String response = PenaltyPaymentCancleParser.makeResponeMessage(req, resultCode);
		    		//responsMsg 를 ByteBuf 로 변경
		    		ByteBuf resubuf = Unpooled.copiedBuffer(response, StandardCharsets.UTF_8);
		    		//금결원으로 응답전문 전달
		    		targetHost = targetSysInfoMap.get("KFT_IP");
		            targetPort = Integer.parseInt(targetSysInfoMap.get("KFT_PORT"));
		            log.info("targetHost : {}, targetPort : {}", targetHost, targetPort);
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

		    	        ChannelFuture f = bootstrap.connect(targetHost, targetPort);
		    	        kftcOutboundChannel = f.channel();
		    	        log.info("response : {}", response);
		    	        
		    	        f.addListener((ChannelFuture future) -> {
		    	            if (future.isSuccess()) {
		    	            	kftcOutboundChannel.writeAndFlush(resubuf).addListener((ChannelFuture writeFuture) -> {
		    	                    if (writeFuture.isSuccess()) {
//		    	                    	writeFuture.channel().close(); // 바로 종료
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
		    	    	kftcOutboundChannel.writeAndFlush(resubuf).addListener((ChannelFuture future) -> {
		    	            if (future.isSuccess()) {
		    	            	ctx.channel().read();
		    	            } else {
//		    	            	kftcOutboundChannel.close();
		    	            	future.channel().close();
		    	            }
		    	        });
		    	    }
	    		} else {
	    			gbn = "TCS"; //일단 무조건 TCS로 보내도록 ※ 금결원에서는 안쓴다고 생각해도 될꺼 같다고 하긴함..
	    		}
	    	} else {
	    		log.error("등록 되지 않은 거래구분 코드 입니다.");
	    	}
	        
	        if ("TCS".equals(gbn)) { // TCS
	            targetHost = targetSysInfoMap.get("TCS_IP");
	            targetPort = Integer.parseInt(targetSysInfoMap.get("TCS_PORT"));
	            log.info("targetHost : {}, targetPort : {}", targetHost, targetPort);
	            if (tcsOutboundChannel == null || !tcsOutboundChannel.isActive()) {
	    	        Bootstrap b = new Bootstrap();
	    	        b.group(inboundChannel.eventLoop())
	    	         .channel(ctx.channel().getClass())
	    	         .option(ChannelOption.AUTO_READ, false)
	    	         .handler(new TCPProxyBackendHandler(inboundChannel));
	
	    	        ChannelFuture f = b.connect(targetHost, targetPort);
	    	        tcsOutboundChannel = f.channel();
	
	    	        f.addListener((ChannelFuture future) -> {
	    	            if (future.isSuccess()) {
	    	                // 목적지 연결 성공 → 데이터 전송
	    	            	tcsOutboundChannel.writeAndFlush(msg).addListener((ChannelFuture writeFuture) -> {
	    	                    if (writeFuture.isSuccess()) {
	//    	                    	writeFuture.channel().close(); // 바로 종료
	    	                        inboundChannel.read();
	    	                    } else {
	    	                        writeFuture.channel().close();
	    	                    }
	    	                });
	    	            } else {
	    	                // 연결 실패 시 클라이언트 닫기
	    	                buf.release();
	    	                inboundChannel.close();
	    	            }
	    	        });
	    	    } else {
	    	    	tcsOutboundChannel.writeAndFlush(msg).addListener((ChannelFuture future) -> {
	    	            if (future.isSuccess()) {
	    	                ctx.channel().read();
	    	            } else {
	    	                future.channel().close();
	    	            }
	    	        });
	    	    }
	        } else if("SJS".equals(gbn)){
	        	targetHost = targetSysInfoMap.get("SJS_IP");
	            targetPort = Integer.parseInt(targetSysInfoMap.get("SJS_PORT"));
	            log.info("targetHost : {}, targetPort : {}", targetHost, targetPort);
	            if (sjsOutboundChannel == null || !sjsOutboundChannel.isActive()) {
	            	
	    	        Bootstrap b = new Bootstrap();
	    	        b.group(inboundChannel.eventLoop())
	    	         .channel(ctx.channel().getClass())
	    	         .option(ChannelOption.AUTO_READ, false)
	    	         .handler(new TCPProxyBackendHandler(inboundChannel));
	
	    	        ChannelFuture f = b.connect(targetHost, targetPort);
	    	        sjsOutboundChannel = f.channel();
	
	    	        f.addListener((ChannelFuture future) -> {
	    	            if (future.isSuccess()) {
	    	                // 목적지 연결 성공 → 데이터 전송
	    	            	sjsOutboundChannel.writeAndFlush(msg).addListener((ChannelFuture writeFuture) -> {
	    	                    if (writeFuture.isSuccess()) {
	//    	                    	writeFuture.channel().close(); // 바로 종료
	    	                        inboundChannel.read();
	    	                    } else {
	    	                        writeFuture.channel().close();
	    	                    }
	    	                });
	    	            } else {
	    	                // 연결 실패 시 클라이언트 닫기
	    	                buf.release();
	    	                inboundChannel.close();
	    	            }
	    	        });
	    	    } else {
	    	    	sjsOutboundChannel.writeAndFlush(msg).addListener((ChannelFuture future) -> {
	    	            if (future.isSuccess()) {
	    	                ctx.channel().read();
	    	            } else {
	    	                future.channel().close();
	    	            }
	    	        });
	    	    }
	        }
		} else {
			//송신
			targetHost = targetSysInfoMap.get("KFT_IP");
            targetPort = Integer.parseInt(targetSysInfoMap.get("KFT_PORT"));
            log.info("targetHost : {}, targetPort : {}", targetHost, targetPort);
            if (kftcOutboundChannel == null || !kftcOutboundChannel.isActive()) {
            	
    	        Bootstrap b = new Bootstrap();
    	        b.group(inboundChannel.eventLoop())
    	         .channel(ctx.channel().getClass())
    	         .option(ChannelOption.AUTO_READ, false)
    	         .handler(new TCPProxyBackendHandler(inboundChannel));

    	        ChannelFuture f = b.connect(targetHost, targetPort);
    	        kftcOutboundChannel = f.channel();

    	        f.addListener((ChannelFuture future) -> {
    	            if (future.isSuccess()) {
    	                // 목적지 연결 성공 → 데이터 전송
    	            	kftcOutboundChannel.writeAndFlush(msg).addListener((ChannelFuture writeFuture) -> {
    	                    if (writeFuture.isSuccess()) {
//    	                    	writeFuture.channel().close(); // 바로 종료
    	                        inboundChannel.read();
    	                    } else {
    	                        writeFuture.channel().close();
    	                    }
    	                });
    	            } else {
    	                // 연결 실패 시 클라이언트 닫기
    	                buf.release();
    	                inboundChannel.close();
    	            }
    	        });
    	    } else {
    	    	kftcOutboundChannel.writeAndFlush(msg).addListener((ChannelFuture future) -> {
    	            if (future.isSuccess()) {
    	                ctx.channel().read();
    	            } else {
    	                future.channel().close();
    	            }
    	        });
    	    }
		}
	}
	
	public void channelInactive(ChannelHandlerContext ctx) {
		if (this.tcsOutboundChannel != null ) {
			closeOnFlush(this.tcsOutboundChannel);
		} 
		if(this.sjsOutboundChannel != null ) {
			closeOnFlush(this.sjsOutboundChannel);
		}
		if(this.kftcOutboundChannel != null ) {
			closeOnFlush(this.kftcOutboundChannel);
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}

	static void closeOnFlush(Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}

	}
	
	public static Map<String, String> getTargetSystemInfo(String remoteHost, String remotePort) {
		Map<String, String> remoteInfoMap = new HashMap<String, String>();
		String remoteHosts [] = remoteHost.split(",");
		String remotePorts [] = remotePort.split(",");
		for(int i = 0; i< remoteHosts.length; i++) {
			remoteInfoMap.put(remoteHosts[i].split(":")[0] + "_IP", remoteHosts[i].substring(4));
			remoteInfoMap.put(remoteHosts[i].split(":")[0] + "_PORT", remotePorts[i].substring(4));
		}
		return remoteInfoMap;
	}
	
	public static void main(String[] args) {
		String aaa = "IGN0990800000301   C   202507290940                        121234567  ";
		System.out.println(aaa.substring(10,16));
	}
}