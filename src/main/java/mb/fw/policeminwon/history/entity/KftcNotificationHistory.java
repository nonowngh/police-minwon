package mb.fw.policeminwon.history.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KftcNotificationHistory {

    private String processDate;      // 처리일자 (YYYYMMDD)
    private String kftcMessageNo;     // 금결원 센터번호
    private String electronicPaymentNo;         // 전자납부번호
    private String esbTxId;           // ESB 트랜젝션 ID
}