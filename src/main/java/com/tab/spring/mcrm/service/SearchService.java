package com.tab.spring.mcrm.service;

import java.util.List;
import java.util.Map;

public interface SearchService {

  List<Map<String, String>> searchResult(String snType, String sn);
}
