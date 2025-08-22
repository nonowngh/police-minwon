package mb.fw.atb.tcp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import mb.fw.atb.tcp.server.entity.PenaltyPaymentCancleEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentDetailEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentResultEntity;
import mb.fw.atb.tcp.server.entity.PenaltyTestCallEntity;
import mb.fw.atb.tcp.server.mapper.TcpServerMapper;

@Service
public class TcpServerService {
	
	@Autowired(required = false)
    @Qualifier("TcpServerMapper")
    private TcpServerMapper tcpServerMapper;

    public int updateResult(PenaltyPaymentResultEntity entity) {
    	return tcpServerMapper.updateResult(entity);
    }
    
    public int insertResult(PenaltyPaymentResultEntity entity) {
    	return tcpServerMapper.insertResult(entity);
    }
    
    public int updateCancle(PenaltyPaymentCancleEntity entity) {
    	return tcpServerMapper.updateCancle(entity);
    }

    public PenaltyPaymentDetailEntity findByOrgMsgNo(String orgMsgNo) {
        return tcpServerMapper.selectByOrgMsgNo(orgMsgNo);
    }
}
