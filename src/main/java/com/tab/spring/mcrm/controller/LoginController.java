package com.tab.spring.mcrm.controller;

import com.tab.spring.mcrm.service.ReadExcelService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
//@RequestMapping(value = "/")
public class LoginController {

  //从yaml获取值
  @Value("${file.name}")
  private String fileName;

  @Autowired
  private ReadExcelService readExcelService;

  @RequestMapping(value = "/")
  public String Login(Model model, HttpServletRequest request) {
    String password = request.getParameter("password");
    if ((null != password) && !password.trim().isEmpty()) {
      //如果请求中有密码 尝试解密文件
      readExcelService.verifyPassword(fileName, password);
    }
    //检查密码是否存在
    password = readExcelService.getPassword();
    if ((null == password) || password.trim().isEmpty()) {
      model.addAttribute("message", "文件解密失败");
      //密码不存在说明文件还未解密 跳转到enc.html
      return "enc";
    } else {
      //文件已解密 跳转到index.html
      return "index";
    }
  }
}
