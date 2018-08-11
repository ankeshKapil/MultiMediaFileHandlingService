package com.cosmos.fileservice;

import com.cosmos.fileservice.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FileserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileserviceApplication.class, args);
	}
}
