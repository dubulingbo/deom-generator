package edu.dublbo.generator.code.controller;

import edu.dublbo.generator.code.cto.DemoPackageCTO;
import edu.dublbo.generator.code.service.CodeFileService;
import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import edu.dublbo.generator.common.result.ResponseResult;
import edu.dublbo.generator.common.result.Result;
import edu.dublbo.generator.utils.FileOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/code")
public class CodeFileController {
    private static final Logger logger = LoggerFactory.getLogger(CodeFileController.class);

    @Autowired
    private CodeFileService service;

    // 下载源码
    @PostMapping(value = "/download")
    @ResponseBody
    public void downloadCode(@RequestBody @Validated DemoPackageCTO demoPackage, HttpServletResponse response) {
        // 2.压缩文件
        // 3.设置回复的一些参数
        // 4.将压缩文件写入网络流
        logger.info("modelIds: {}", demoPackage.getModelId());
        String filename = service.zipDemoFile(demoPackage);
//        String filename = "./src/main/resources/templates/test/源码233.zip";
        File file = new File(filename);
        // 如果文件存在，则进行下载
        if (file.exists()) {
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                // 配置文件下载
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(file.getName(), "UTF-8"));
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");

                // 实现文件下载
                byte[] buffer = new byte[1024];
                bis = new BufferedInputStream(new FileInputStream(file));
                os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                logger.info("Download  successfully!");
            } catch (Exception e) {
                logger.info("Download  failed: {}", e.getMessage());
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 删除 压缩文件
                FileOperator.deleteFile(file);
            }
        }

    }

    // 通过某个模型的 id 加载生成的代码
    @GetMapping(value = "/load/{modelId}")
    public Result<Map<String, Object>> loadCode(@PathVariable String modelId) {
        logger.info("model id : {}", modelId);
        return ResponseResult.generateSuccessResult(service.loadCode(modelId));
    }

//    public ResponseEntity<FileSystemResource> export(File file) {
//        if (file == null) {
//            return null;
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(new FileSystemResource(file));
//    }

    // 设置定时任务，定时删除临时生成源码文件
    @Scheduled(cron = "59 0 11 ? * 5 ")
    public void deleteTmpDemoFile() {
        logger.info("定时任务执行：{}",new Date());
        service.deleteTmpDemoFile();
    }
}
