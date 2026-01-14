package mb.fw.policeminwon.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KftcTransferCloseResponse {

	@JsonProperty("rsp_code")
	private String rspCode;

	@JsonProperty("rsp_message")
	private String rspMessage;

}
