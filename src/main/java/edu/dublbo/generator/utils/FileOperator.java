package edu.dublbo.generator.utils;


import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DubLBo
 * @since 2020-09-05 10:17
 * i believe i can i do
 */
public class FileOperator {
    /**
     * 读取文件每一行的内容，存入List中，空行不存储
     * @param filePath 文件路径
     * @return 文件所有内容
     * @throws IOException 文件读取错误
     */
    public List<String> readContent(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
        List<String> content = new ArrayList<>();
        String rowStr;

        while((rowStr = reader.readLine()) != null){
            rowStr += "\n";
            content.add(rowStr);
        }
        return content;
    }
    public void writeContent(List<String> content, String filename) throws Exception {
        if(content == null || content.size() == 0){
            throw new Exception("内容为空");
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),StandardCharsets.UTF_8));
        for(String row : content){
            writer.write(row);
        }
    }
}
