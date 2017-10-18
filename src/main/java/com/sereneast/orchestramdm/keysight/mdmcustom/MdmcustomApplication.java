package com.sereneast.orchestramdm.keysight.mdmcustom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MdmcustomApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MdmcustomApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MdmcustomApplication.class, args);
	}
}

/*@SpringBootApplication
public class MdmcustomApplication{
	public static void main(String[] args) {
		SpringApplication.run(MdmcustomApplication.class, args);
	}
}*/
