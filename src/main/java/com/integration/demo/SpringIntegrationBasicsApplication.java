package com.integration.demo;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@SpringBootApplication
public class SpringIntegrationBasicsApplication {

	public static void main(String[] args) {		
		AbstractApplicationContext context 
        = new AnnotationConfigApplicationContext(BasicIntegrationConfig.class);
      context.registerShutdownHook();
	}

}
