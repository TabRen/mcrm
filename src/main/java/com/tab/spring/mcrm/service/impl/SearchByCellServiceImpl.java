package com.tab.spring.mcrm.service.impl;

import com.tab.spring.mcrm.service.ReadExcelService;
import com.tab.spring.mcrm.service.SearchByCellService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SearchByCellService")
public class SearchByCellServiceImpl implements SearchByCellService {

  @Value("${cell.show.server}")
  String serverCell;
  @Value("${cell.show.projector}")
  String projectorCell;
  @Autowired
  ReadExcelService readExcelService;

  @Override
  public List<Map<String, String>> searchResult(String snType, String sn) {
    List<Map<String, String>> cellList;
    List<Map<String, String>> result = new ArrayList<>();
    String[] cellIndex;
    switch (snType) {
      case "serverSn":
        cellList = readExcelService.getServerSnList();
        cellIndex = serverCell.split(",");
        break;
      case "projectorSn":
        cellList = readExcelService.getProjectorSnList();
        cellIndex = projectorCell.split(",");
        break;
      default:
        log.info("SearchByCellServiceImpl searchResult error, snType: {}, sn: {}", snType, sn);
        return result;
    }
    //先判断有没有这个序列号
    Set<String> rowIndex = new HashSet<>();
    for (Map<String, String> cell : cellList) {
      if (cell.get(snType).contains(sn)) {
        rowIndex.add(cell.get("rowNum"));
      }
    }
    //有这个序列号就读出相应的列
    if (!rowIndex.isEmpty()) {
      for (String rowNum : rowIndex) {
        Map<String, String> map = new HashMap<>();
        for (String cellNum : cellIndex) {
          String cellStringValue = readExcelService.getCellStringValue(rowNum, cellNum);
          map.put(cellNum, cellStringValue);
        }
        result.add(map);
      }
    }
    return result;
  }
}
