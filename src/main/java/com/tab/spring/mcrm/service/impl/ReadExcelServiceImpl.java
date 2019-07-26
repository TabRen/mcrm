package com.tab.spring.mcrm.service.impl;

import com.tab.spring.mcrm.service.ReadExcelService;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReadExcelServiceImpl implements ReadExcelService {

  private String password;
  private XSSFWorkbook workbook;
  private List<Map<String, String>> serverSnList = new ArrayList<>();
  private List<Map<String, String>> projectorSnList = new ArrayList<>();
  //从yaml获取值
  @Value("${file.name}")
  private String fileName;
  @Value("${file.sheets}")
  private String sheets;
  @Value("${file.serverSn.index}")
  private String serverSnIndex;
  @Value("${file.projectorSn.index}")
  private String projectorSnIndex;

  @Override
  public void verifyPassword(String password) {
    try {
      InputStream inp = new FileInputStream(fileName);
      POIFSFileSystem pfs = new POIFSFileSystem(inp);
      inp.close();
      EncryptionInfo encInfo = new EncryptionInfo(pfs);
      Decryptor decryptor = Decryptor.getInstance(encInfo);
      if (decryptor.verifyPassword(password)) {
        log.info("ReadExcelServiceImpl verifyPassword success");
        setPassword(password);
        workbook = new XSSFWorkbook(decryptor.getDataStream(pfs));
        List<Integer> sheetList = new ArrayList<>();
        for (String sheetIndex : sheets.split(",")) {
          sheetList.add(Integer.valueOf(sheetIndex));
        }
        setServerSnList(sheetList);
        setProjectorSnList(sheetList);
      } else {
        //解密失败
        setPassword(null);
        log.info("ReadExcelServiceImpl verifyPassword failed");
      }
    } catch (Exception e) {
      setPassword(null);
      log.error("ReadExcelServiceImpl verifyPassword occur exception: ", e);
    }
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    log.info("ReadExcelServiceImpl setPassword password: {}", password);
    this.password = password;
  }

  @Override
  public List<Map<String, String>> getProjectorSnList() {
    return projectorSnList;
  }

  @Override
  public List<Map<String, String>> getServerSnList() {
    return serverSnList;
  }

  @Override
  public String getCellStringValue(String rowNum, String cellNum) {
    XSSFSheet sheet = workbook.getSheetAt(0);
    XSSFRow row = sheet.getRow(Integer.parseInt(rowNum));
    XSSFCell cell = row.getCell(Integer.parseInt(cellNum));
    return getCellStringValue(cell);
  }

  private void setProjectorSnList(List<Integer> sheets) {
    for (int sheetIndex : sheets) {
      XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
      for (Row row : sheet) {
        if (row.getRowNum() == 0) {
          //跳过表头
          continue;
        }
        XSSFCell cell = ((XSSFRow) row).getCell(Integer.parseInt(projectorSnIndex));
        Map<String, String> map = new HashMap<>();
        map.put("rowNum", String.valueOf(row.getRowNum()));
        map.put("projectorSn", getCellStringValue(cell));
        projectorSnList.add(map);
      }
    }
    log.info("ReadExcelServiceImpl setProjectorSnList count: {}", projectorSnList.size());
  }

  private void setServerSnList(List<Integer> sheets) {
    for (int sheetIndex : sheets) {
      XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
      for (Row row : sheet) {
        if (row.getRowNum() == 0) {
          //跳过表头
          continue;
        }
        XSSFCell cell = ((XSSFRow) row).getCell(Integer.parseInt(serverSnIndex));
        Map<String, String> map = new HashMap<>();
        map.put("rowNum", String.valueOf(row.getRowNum()));
        map.put("serverSn", getCellStringValue(cell));
        serverSnList.add(map);
      }
    }
    log.info("ReadExcelServiceImpl getServerSnList count: {}", serverSnList.size());
  }

  private String getCellStringValue(XSSFCell cell) {
    if (null != cell) {
      //判断数据的类型
      switch (cell.getCellTypeEnum()) {
        case NUMERIC:
          if ((cell.getColumnIndex() == 19) || (cell.getColumnIndex() == 20) || (
              cell.getColumnIndex() == 21)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(cell.getDateCellValue());
          } else {
            DecimalFormat df = new DecimalFormat("0");
            return df.format(cell.getNumericCellValue());
          }
        case STRING:
          return cell.getStringCellValue();
        case BOOLEAN:
          return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
          //公式
          if ((cell.getColumnIndex() == 19) || (cell.getColumnIndex() == 20) || (
              cell.getColumnIndex() == 21)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            evaluator.evaluateInCell(cell);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(cell.getDateCellValue());
          } else {
            return cell.getCellFormula();
          }
        case BLANK:
          //空值
          return "";
        case ERROR:
          return "非法字符";
        default:
          return "未知类型";
      }
    }
    log.info("ReadExcelServiceImpl getCellStringValue cell is null");
    return "";
  }
}
