package com.transfer.backendbankmasr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BackendBankmasrApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackendBankmasrApplication.class, args);
    }

}
