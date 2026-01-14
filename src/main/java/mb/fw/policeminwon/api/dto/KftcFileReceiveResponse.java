package mb.fw.policeminwon.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KftcFileReceiveResponse {
	
	@JsonProperty("rsp_code")
	private String rspCode;

	@JsonProperty("rsp_message")
	private String rspMessage;
	
	@JsonProperty("hash_value")
	private String hashValue;

	@JsonProperty("file_name")
	private String fileName;

	@JsonProperty("file_size")
	private String fileSize;
	
	@JsonProperty("compressed_file_name")
	private String compressedFileName;

	@JsonProperty("compressed_file_size")
	private String compressedFileSize;

	@JsonProperty("sftp_one_time_id")
	private String sftpOneTimeId;

	@JsonProperty("sftp_one_time_passwd")
	private String sftpOneTimePasswd;
}
