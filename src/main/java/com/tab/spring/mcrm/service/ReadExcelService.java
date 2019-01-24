package com.tab.spring.mcrm.service;

import java.util.Map;

//读取Excel文件服务
public interface ReadExcelService {

  //解密
  void verifyPassword(String fileName, String password);

  //保存密码到缓存
  String getPassword();

  //服务器序列号所在行
  //key: rowIndex
  //value: sn
  Map<String, String> serverSnMap();

  //放映机序列号所在行
  Map<String, String> projectorSnMap();

}
