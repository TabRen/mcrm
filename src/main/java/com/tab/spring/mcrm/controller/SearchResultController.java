package com.tab.spring.mcrm.controller;

import com.tab.spring.mcrm.service.ReadExcelService;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/setting")
public class SearchResultController {

  //从yaml获取值
  @Value("${file.name}")
  private String fileName;

  @Autowired
  private ReadExcelService readExcelService;

  public SearchResultController(ReadExcelService readExcelService) {
    this.readExcelService = readExcelService;
  }

  @RequestMapping(value = "enc")
  public String enc(HashMap<String, String> map) {
    map.put("hello", "welcome");
    log.info("SearchResultController enc is run");

    String password = "1234567890";
    readExcelService.verifyPassword(fileName, password);
    return "enc";
  }
}
