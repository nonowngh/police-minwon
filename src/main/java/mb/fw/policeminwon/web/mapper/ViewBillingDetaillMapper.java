package mb.fw.policeminwon.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import mb.fw.net.common.encryption.ProductEncryption;
import mb.fw.policeminwon.entity.ViewBillingDetaillEntity;

@Mapper
public interface ViewBillingDetaillMapper {

	ViewBillingDetaillEntity selectBillingDetaillByElecPayNo(String elecPayNo);
	
	public static void main(String[] args) {
		System.out.println(ProductEncryption.decryptString("ENC(I1YqvtB1542Z7FFWOIrm595nSO7jMF0kvOhiULJgu7URunH0MPLNAJQIwKeuTQXJ)"));
	}
}
