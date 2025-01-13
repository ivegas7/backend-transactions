package com.tenpo.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
	
	//Configurar swagger
	@OpenAPIDefinition(
			info = @Info(
					contact = @Contact(
							name="Ignacio Vegas",
							email="ignaciovegas7@gmail.com"
					),
					description ="API transaction documentation",
					title="API Transaction",
					version="1.0",
					license = @License(
							name="License name",
							url="https://xxxxx.com"
					),
					termsOfService ="Terms of service"
			),
			servers =  {
					@Server(
						description="Local ENV",
						url="/"		
					),
					@Server(
							description="PROD ENV",
							url="/"		
					)
			}
	)

	public class SwaggerConfig{
		
	}

