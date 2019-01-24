package com.tab.spring.mcrm.service.impl;

import com.tab.spring.mcrm.service.CheckCache;
import com.tab.spring.mcrm.service.ReadExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service("CheckCache")
public class CheckCacheImpl implements CheckCache {

  @Autowired
  private ReadExcelService readExcelService;

  @Override
  @Cacheable(value = "password")
  public String CheckPasswordExits() {
    //缓存中没有密码
    log.info("CheckCacheImpl CheckPasswordExits, password not in cache");
    return null;
  }
}
