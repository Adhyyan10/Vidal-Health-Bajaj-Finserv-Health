package com.exam.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.exam.demo.services.WebhookServices;



@Component
public class CodeRunner  implements CommandLineRunner{
	 private final WebhookServices webhookService;

	    public CodeRunner(WebhookServices webhookService) {
	        this.webhookService = webhookService;
	    }

	    

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		 webhookService.executeFlow();
		
	}

}
