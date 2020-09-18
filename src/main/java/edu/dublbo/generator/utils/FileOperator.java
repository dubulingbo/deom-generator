package edu.dublbo.generator.utils;


import edu.dublbo.generator.common.exception.BaseException;
import edu.dublbo.generator.common.result.OptStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author DubLBo
 * @since 2020-09-05 10:17
 * i believe i can i do
 */
public class FileOperator {
    private static final Logger logger = LoggerFactory.getLogger(FileOperator.class);

    private static final int BUFFER_SIZE = 2 * 1024;
    private static final String separator = File.separator;

    /**
     * 读取文件每一行的内容，存入List中
     *
     * @param file 文件
     * @return 文件所有内容
     * @throws IOException 文件读取错误
     */
    public static List<String> readContent(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        List<String> content = new ArrayList<>();
        String rowStr;

        while ((rowStr = reader.readLine()) != null) {
            rowStr += "\n";
            content.add(rowStr);
        }
        return content;
    }

    public static void writeContent(List<String> content, String filename) throws Exception {
        if (content == null || content.size() == 0) {
            throw new Exception("内容为空");
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));
        for (String row : content) {
            writer.write(row);
        }
    }

    /**
     * 生成ZIP文件
     *
     * @param zipFile          压缩文件目标对象
     * @param keepDirStructure 是否保留文件的目录结构
     */
    public static void toZip(File sourcesFiles, File zipFile, boolean keepDirStructure) {
        ZipOutputStream zos = null;
        try {
            // 若磁盘上该文件不存在，则新建；否则直接覆盖磁盘上原来的文件
            // 若磁盘上文件路径不存在，则抛出异常
            FileOutputStream out = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(out);
            compress(sourcesFiles, sourcesFiles.getName(), zos, keepDirStructure);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new BaseException(OptStatus.FAIL.getOptCode(), "生成压缩文件对象时遇到错误");
        } finally {
            IOUtils.closeQuietly(zos);
        }
    }


    /**
     * 添加文件流到ZIP文件
     *
     * @param sourceFiles      要压缩的文件对象（文件或文件夹）
     * @param zos              压缩文件的输出流对象，它绑定了磁盘上的压缩文件
     * @param sourceFileName   要压缩的文件名（若是文件夹那就是带相对路径文件夹名称。文件的话；就是带相对路径的文件名称
     * @param keepDirStructure 是否保留空文件夹
     */
    private static void compress(File sourceFiles, String sourceFileName, ZipOutputStream zos, boolean keepDirStructure) {
        // 压缩的是文件
        if (sourceFiles.isFile()) {
            logger.info("$- {} == {}", sourceFiles.getAbsolutePath(), sourceFileName);

            try {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(sourceFileName));
                FileInputStream in = new FileInputStream(sourceFiles);
                // copy文件到zip输出流中
                int len;
                byte[] buf = new byte[BUFFER_SIZE];
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new BaseException(OptStatus.FAIL.getOptCode(), "写入压缩文件出现异常");
            }
        } else if (sourceFiles.isDirectory()) { // 要压缩的是目录
            logger.info("$d {} == {}", sourceFiles.getPath(), sourceFileName);
            // 列出该目录下的所有文件或文件夹
            File[] fileObjs = sourceFiles.listFiles();
            // 空文件夹
            if (fileObjs == null || fileObjs.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    try {
                        // 空文件夹的处理
                        zos.putNextEntry(new ZipEntry(sourceFileName + separator));
                        // 没有文件，不需要文件的copy
                        zos.closeEntry();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                        throw new BaseException(OptStatus.FAIL.getOptCode(), "写入压缩文件出现异常");
                    }

                }
            } else {
                for (File file : fileObjs) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, sourceFileName + separator + file.getName(), zos, keepDirStructure);
                    } else {
                        compress(file, file.getName(), zos, keepDirStructure);
                    }

                }
            }
        } else { // 源文件对象是其他类型
            logger.error("文件对象【{}, {}】不支持", sourceFiles.getAbsolutePath(), sourceFileName);
            throw new BaseException(OptStatus.FAIL.getOptCode(), "源文件对象暂不支持");
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file 文件对象（文件或文件夹）
     */
    public static void deleteFile(File file) {
        if (!file.isDirectory() && file.exists()) {
            file.delete();
            return;
        }
        // 是目录
        File[] subFiles = file.listFiles();
        if (subFiles != null && subFiles.length != 0) {
            for (File subFile : subFiles) {
                deleteFile(subFile);
            }
        }
        // 说明是空文件夹，也要删除
        file.delete();

    }


    public static void main(String[] args) {
//        logger.info(File.separator + " | " + File.pathSeparator);
        String downloadName = "源码1.zip";
        // 压缩的zip文件和地址
        File zipFile = new File("D:/test" + File.separator + downloadName);
        // 需要压缩的文件夹地址
        File sourceFiles = new File("D:\\demo\\github\\MeetingRoom\\BackEnd");


        FileOperator.toZip(sourceFiles, zipFile, true);
        // zip文件保留七天，源码文件立即删除
        FileOperator.deleteFile(sourceFiles);

//
//
//        try {
//            // 把压缩文件流返回到前端
//            InputStream inputData = new FileInputStream(zipFile);
////            FileUtils.downloadFile(response, downloadName, inputData);
//        } catch (IOException e) {
//            logger.error("返回出错：", e);
//        }
//// 删除本地所有文件


    }

}
