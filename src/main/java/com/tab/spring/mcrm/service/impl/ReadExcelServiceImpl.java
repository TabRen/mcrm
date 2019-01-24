package com.tab.spring.mcrm.service.impl;

import com.tab.spring.mcrm.service.ReadExcelService;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
  private List<Map<Integer, String>> serverSnList = new ArrayList<>();
  private List<Map<Integer, String>> projectorSnList = new ArrayList<>();
  @Value("${file.serverSn.index}")
  private String serverSnIndex;
  @Value("${file.projectorSn.index}")
  private String projectorSnIndex;

  @Override
  public void verifyPassword(String fileName, String password) {
    try {
      InputStream inp = new FileInputStream(fileName);
      POIFSFileSystem pfs = new POIFSFileSystem(inp);
      inp.close();
      EncryptionInfo encInfo = new EncryptionInfo(pfs);
      Decryptor decryptor = Decryptor.getInstance(encInfo);
      if (decryptor.verifyPassword(password)) {
        log.info("ReadExcelServiceImpl verifyPassword success");
        //保存密码
        this.password = password;
        workbook = new XSSFWorkbook(decryptor.getDataStream(pfs));
        getServerSnList();
        getProjectorSnList();
      } else {
        //解密失败
        log.info("ReadExcelServiceImpl verifyPassword failed");
      }
    } catch (Exception e) {
      log.error("ReadExcelServiceImpl verifyPassword occur exception: ", e);
    }
  }

  public String getPassword() {
    log.info("ReadExcelServiceImpl getPassword password: {}", password);
    return password;
  }

  @Override
  public List<Map<Integer, String>> getServerSnList() {
    XSSFSheet sheet = workbook.getSheetAt(0);
    for (Row row : sheet) {
      if (row.getRowNum() == 0) {
        //跳过表头
        continue;
      }
      XSSFCell cell = ((XSSFRow) row).getCell(Integer.parseInt(serverSnIndex));
      Map<Integer, String> map = new HashMap<>();
      map.put(row.getRowNum(), getCellStringValue(cell));
      serverSnList.add(map);
    }
    log.info("ReadExcelServiceImpl getServerSnList count: {}", serverSnList.size());
    return serverSnList;
  }

  @Override
  public List<Map<Integer, String>> getProjectorSnList() {
    XSSFSheet sheet = workbook.getSheetAt(0);
    for (Row row : sheet) {
      if (row.getRowNum() == 0) {
        //跳过表头
        continue;
      }
      XSSFCell cell = ((XSSFRow) row).getCell(Integer.parseInt(projectorSnIndex));
      Map<Integer, String> map = new HashMap<>();
      map.put(row.getRowNum(), getCellStringValue(cell));
      projectorSnList.add(map);
    }
    log.info("ReadExcelServiceImpl getProjectorSnList count: {}", projectorSnList.size());
    return projectorSnList;
  }

  private String getCellStringValue(XSSFCell cell) {
    if (null != cell) {
      //判断数据的类型
      switch (cell.getCellTypeEnum()) {
        case NUMERIC:
          DecimalFormat df = new DecimalFormat("0");
          return df.format(cell.getNumericCellValue());
        case STRING:
          return cell.getStringCellValue();
        case BOOLEAN:
          return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
          //公式
          return cell.getCellFormula();
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
