package mb.fw.atb.tcp.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import mb.fw.atb.tcp.server.entity.PenaltyPaymentCancleEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentDetailEntity;
import mb.fw.atb.tcp.server.entity.PenaltyPaymentResultEntity;

@Mapper
public interface TcpServerMapper {
    int updateResult(PenaltyPaymentResultEntity entity);
    int insertResult(PenaltyPaymentResultEntity entity);
    int updateCancle(PenaltyPaymentCancleEntity entity);
    PenaltyPaymentDetailEntity selectByOrgMsgNo(@Param("orgMsgNo") String orgMsgNo);
}