package mb.fw.policeminwon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.netty.util.ResourceLeakDetector;

@ComponentScan(basePackages = {"mb.fw.policeminwon", "mb.fw.adaptor"})
@SpringBootApplication
public class PoliceMinwonApplication {

	public static void main(String[] args) {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        SpringApplication.run(PoliceMinwonApplication.class, args);

	}
}
