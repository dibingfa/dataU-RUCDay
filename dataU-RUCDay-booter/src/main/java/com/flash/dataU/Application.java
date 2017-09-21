package com.flash.dataU;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * dataU start Class.
 *
 * @author Flash (18811311416@sina.cn)
 * @since 2017-07-06 13:55
 */

//@SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
@SpringBootApplication
@Controller
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    /**
     * this method is only for a easy-test, make sure that you run spring boot correctly
     * visit "http://localhost:8080/test" after running this application, you can get a message
     * @return
     */
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
