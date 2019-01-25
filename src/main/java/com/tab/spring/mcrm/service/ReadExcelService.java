package com.tab.spring.mcrm.service;

import java.util.List;
import java.util.Map;

//读取Excel文件服务
public interface ReadExcelService {

  //解密
  void verifyPassword(String password);

  void setPassword(String password);

  String getPassword();

  //服务器序列号所在行
  //key: rowNumber value: rowNumber
  //key: serverSn value: sn
  List<Map<String, String>> getServerSnList();

  //放映机序列号所在行
  List<Map<String, String>> getProjectorSnList();

  //读取指定单元格的内容
  String getCellStringValue(String rowNum, String cellNum);
}
