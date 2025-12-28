package mb.fw.policeminwon;

import java.io.UnsupportedEncodingException;

public class FixedLengthCheck {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String data = "0223IGN0990210122001   C   2025122616090CT12346567890940_002IMA121234567  62042116547120175980137202530000015506          000000000075000201807133650000                              6204211654712                    V Q        1";
		System.out.println(data.getBytes("utf-8").length);
	}
}
