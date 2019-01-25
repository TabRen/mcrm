package com.tab.spring.mcrm.service;

import java.util.List;
import java.util.Map;

public interface SearchByCellService {

  public List<Map<String, String>> searchResult(String snType, String sn);
}
