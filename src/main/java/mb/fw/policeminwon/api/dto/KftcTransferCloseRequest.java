package mb.fw.policeminwon.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KftcTransferCloseRequest {
	
	@JsonProperty("org_api_trx_no")
	private String orgApiTrxNo;
	
	@JsonProperty("org_api_trx_dtm")
	private String orgApiTrxDtm;

	@JsonProperty("result_code")
	private String resultCode;
}
