package edu.dublbo.generator.file.controller;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(value = "/file/download")
public class DownloadController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @GetMapping(value = "/code")
    @ResponseBody
    public String downloadCode(String[] modelIds, HttpServletRequest request, HttpServletResponse response) {
        // 1.生成源码文件
        // 2.压缩文件
        // 3.设置回复的一些参数
        // 4.将压缩文件写入网络流

        // 如果文件名不为空，则进行下载
        String filename = "D:\\test\\源码.zip";

            File file = new File(filename);
            // 如果文件存在，则进行下载
            if (file.exists()) {
                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                // 下载文件能正常显示中文
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

                    // 实现文件下载
                    byte[] buffer = new byte[1024];

                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download  successfully!");
                    return "successfully";

                } catch (Exception e) {
                    System.out.println("Download  failed!");
                    return "failed";

                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        return "下载成功";



//        try {
//            ServletOutputStream responseOutputStream = response.getOutputStream();
//
//
//            response.setHeader("Content-Disposition", "attachment;filename=" +
//                    new String("源码.zip".getBytes("GB2312"), StandardCharsets.ISO_8859_1));  // 需要编码否则中文乱码
//            response.setContentType("application/zip;charset=utf-8");
//            response.setCharacterEncoding("UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("文件流操作错误：{}", e.getMessage());
//            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "文件流操作错误");
//        }
    }



    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }
}
