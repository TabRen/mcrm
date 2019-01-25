package com.tab.spring.mcrm.controller;

import com.tab.spring.mcrm.service.ReadExcelService;
import com.tab.spring.mcrm.service.SearchByCellService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
//@RequestMapping(value = "/")
public class LoginController {

  @Autowired
  private ReadExcelService readExcelService;
  @Autowired
  private SearchByCellService searchByCellService;

  @RequestMapping(value = "/")
  public String Login(Model model, HttpServletRequest request) {
    String password = request.getParameter("password");
    if ((null != password) && !password.trim().isEmpty()) {
      //如果请求中有密码 尝试解密文件
      readExcelService.verifyPassword(password);
    }
    //检查密码是否存在
    password = readExcelService.getPassword();
    if ((null == password) || password.trim().isEmpty()) {
      model.addAttribute("message", "文件解密失败");
      model.addAttribute("status", "failed");
      //密码不存在说明文件还未解密
    } else {
      //文件已解密
      model.addAttribute("status", "success");
    }
    return "index";
  }
}
