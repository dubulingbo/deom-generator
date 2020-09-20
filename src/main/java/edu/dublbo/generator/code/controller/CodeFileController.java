package edu.dublbo.generator.code.controller;

import edu.dublbo.generator.code.service.CodeFileService;
import edu.dublbo.generator.common.result.ResponseResult;
import edu.dublbo.generator.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping(value = "/code")
public class CodeFileController {
    private static final Logger logger = LoggerFactory.getLogger(CodeFileController.class);

    @Autowired
    private CodeFileService service;

    // 下载源码
    @GetMapping(value = "/download")
    @ResponseBody
    public void downloadCode(String[] modelIds, HttpServletResponse response) {
        // 1.生成源码文件
        // 2.压缩文件
        // 3.设置回复的一些参数
        // 4.将压缩文件写入网络流
        logger.info("modelIds: {}", Arrays.toString(modelIds));
        String filename = "./src/main/resources/templates/test/源码233.zip";
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
}
