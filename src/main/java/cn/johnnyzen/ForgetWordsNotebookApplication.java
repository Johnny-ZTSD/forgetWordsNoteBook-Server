package cn.johnnyzen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
@ServletComponentScan
/*
 * 使用@ServletComponentScan 注解后:
 * 		Servlet、Filter、Listener 可以直接通过
 *  	@WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
 **/
public class ForgetWordsNotebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForgetWordsNotebookApplication.class, args);
	}

	@RequestMapping("/")
	public String index(){
		return "index";
	}
}
