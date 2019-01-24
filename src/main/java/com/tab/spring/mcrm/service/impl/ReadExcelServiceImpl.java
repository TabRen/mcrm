package com.tab.spring.mcrm.service.impl;

import com.tab.spring.mcrm.service.ReadExcelService;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReadExcelServiceImpl implements ReadExcelService {

  private String password;
  private Workbook workbook;
  private Map<String, String> serverSnMap = new HashMap<>();
  private Map<String, String> projectorSnMap = new HashMap<>();

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
  public Map<String, String> serverSnMap() {
    return serverSnMap;
  }

  @Override
  public Map<String, String> projectorSnMap() {
    return projectorSnMap;
  }
}
