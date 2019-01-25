package com.tab.spring.mcrm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class McrmApplication {

  public static void main(String[] args) {
    SpringApplication.run(McrmApplication.class, args);
    log.info("McrmApplication is start");
  }

}

