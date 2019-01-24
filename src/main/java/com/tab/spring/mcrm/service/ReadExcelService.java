package com.tab.spring.mcrm.service;

import java.util.List;
import java.util.Map;

//读取Excel文件服务
public interface ReadExcelService {

  //解密
  void verifyPassword(String fileName, String password);

  //保存密码到缓存
  String getPassword();

  //服务器序列号所在行
  //key: rowNumber
  //value: sn
  List<Map<Integer, String>> getServerSnList();

  //放映机序列号所在行
  List<Map<Integer, String>> getProjectorSnList();
}
