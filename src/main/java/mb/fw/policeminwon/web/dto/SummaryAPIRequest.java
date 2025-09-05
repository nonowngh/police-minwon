package mb.fw.policeminwon.web.dto;

import lombok.Data;

@Data
public class SummaryAPIRequest {

	String interfaceId;
	
	String transactionId;
	
	String bodyData;
}
