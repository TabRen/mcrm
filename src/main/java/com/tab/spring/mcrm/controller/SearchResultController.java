package com.tab.spring.mcrm.controller;

import com.tab.spring.mcrm.service.ReadExcelService;
import com.tab.spring.mcrm.service.SearchByCellService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class SearchResultController {

  @Autowired
  private SearchByCellService searchByCellService;
  @Autowired
  private ReadExcelService readExcelService;

  @RequestMapping(value = "search")
  public String search(Model model, HttpServletRequest request) {
    //没有密码时
    String password = readExcelService.getPassword();
    if ((null == password) || password.isEmpty()) {
      //重定向
      return "redirect:/";
    }
    model.addAttribute("status", "success");
    //sn type
    String snType = request.getParameter("snType");
    //sn value
    String sn = request.getParameter("sn");
    //没有选择序列号类型
    if ((snType == null) || snType.isEmpty()) {
      model.addAttribute("message", "请选择设备类型");
    } else {
      //输入的序列号至少4位
      if ((null == sn) || (sn.trim().length() < 4)) {
        model.addAttribute("message", "输入的序列号至少四位");
      } else {
        //auto to upper case
        sn = sn.trim().toUpperCase();
        List<Map<String, String>> cellList = searchByCellService.searchResult(snType, sn);
        model.addAttribute("ceallList", cellList);
      }
    }
    return "index";
  }
}
