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
public class KftcFileReceiveRequest {
	
	@JsonProperty("file_name")
	private String fileName;
	
	@JsonProperty("compressed_file_name")
	private String compressedFileName;
}
