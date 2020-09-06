package edu.dublbo.generator.utils;

import edu.dublbo.generator.common.exception.OptErrorException;
import edu.dublbo.generator.common.result.OptStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author DubLBo
 * @since 2020-09-04 18:47
 * i believe i can i do
 */
public class DBOperator {
    private static final Logger logger = LoggerFactory.getLogger(DBOperator.class);
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/demo_generator_db?serverTimezone=Asia/Shanghai";
    private static final String USER_NAME = "dbtester";
    private static final String PASSWORD = "123456789";

    // 数据库连接时的状态
//    Statement stat = null;

    // 数据库连接对象
    private final Connection connection;

    private Connection createConnection() {
        Connection conn = null;
        try {
            //加载mysql的驱动类
            Class.forName(DRIVER_NAME);
            //获取数据库连接
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            logger.info("数据库连接成功，{}", UUID.randomUUID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public Connection getConnection(){
        return connection;
    }

    //构造函数，包括 加载数据库驱动 和 连接数据库 操作
    public DBOperator(){
        connection = createConnection();
        if(connection == null){
            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "数据库连接失败");
        }
    }


    /**
     * 执行单条SQL语句
     *
     * @param sql SQL语句
     * @return 执行成功与否
     * @throws RuntimeException 执行异常
     */
    public boolean executeSql(String sql) throws RuntimeException {
        if(StringUtils.isEmpty(sql)){
            throw new RuntimeException("sql语句为空");
        }
        try {
             Statement statement = connection.createStatement();
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("sql语句执行失败");
        }
    }

    /**
     * 批量执行SQL语句
     * @param sqls 包含待执行的SQL语句的ArrayList集合
     * @return int 成功与否
     */
    private int batchExecute(ArrayList<String> sqls) {
        int res = 0;
        try {
            Statement st = connection.createStatement();
            for (String sql : sqls) {
                st.addBatch(sql);
            }
            st.executeBatch();
            res = 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("sql语句执行失败");
        }
        return res;
    }

    /**
     * 以行为单位读取文件，并将文件的每一行格式化到ArrayList中，常用于读面向行的格式化文件
     */
    private static ArrayList<String> readFileByLines(String filePath) throws RuntimeException {
        ArrayList<String> listStr = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        String tempString;
        boolean emptyFlag = true;  // 标志分号之前sql语句是否为空，初始为空
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.trim().equals("")){ // 当前行没有内容，直接读取下一行
                    continue;
                }
                int k = tempString.indexOf(";");
                // 分号出现在行中或行首
                if(k >= 0 && k < tempString.length() - 1){
                    throw new RuntimeException("文件格式错误，分号不在行尾");
                }
                // 当前行包含分号，表示分号之前是一条SQL语句，分号之后是另一条SQL语句
                if (tempString.endsWith(";")) {
                    if (!emptyFlag) {
                        sql.append(tempString).append("\n");
                        listStr.add(sql.toString());
                        logger.info(sql.toString());
                        sql.delete(0, sql.length()); // 清空SQL语句
                        emptyFlag = true;
                    } else{
                        listStr.add(tempString);
                    }
                } else {
                    sql.append(tempString).append("\n");
                    emptyFlag = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("文件未找到：" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件读取错误：" + filePath);
        }
        return listStr;
    }

    /**
     * 读取文件内容到 SQL中执行
     * @param sqlPath SQL文件的路径：如：D:/TestProject/web/sql/脚本.Sql
     */
    public void runSqlByReadFileContent(String sqlPath) throws Exception{
        ArrayList<String> sqlList = readFileByLines(sqlPath);
        if(sqlList.size() == 0){
            throw new RuntimeException("没有需要执行的SQL语句");
        }
        int num = batchExecute(sqlList);
        if (num <= 0){
            throw  new RuntimeException("有未执行的SQL语句");
        }

    }
}
