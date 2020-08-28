package com.sabo.catbooru;

import com.sabo.catbooru.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class CatbooruApplication implements CommandLineRunner {

	@Resource
	FileStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(CatbooruApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//storageService.init();
	}
}
