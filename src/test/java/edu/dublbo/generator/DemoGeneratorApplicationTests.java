package edu.dublbo.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dublbo.generator.code.service.CodeFileService;
import edu.dublbo.generator.common.utils.SnowflakeIdWorker;
import edu.dublbo.generator.basic.entity.TDemoColumnType;
import edu.dublbo.generator.basic.entity.TDemoPropertyType;
import edu.dublbo.generator.basic.mapper.TDemoModelDetailMapper;
import edu.dublbo.generator.basic.service.DemoModelDetailService;
import edu.dublbo.generator.basic.service.DemoModelService;
import edu.dublbo.generator.basic.service.DemoPropertyTypeService;

import edu.dublbo.generator.utils.Constant;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;


@SpringBootTest
class DemoGeneratorApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(DemoGeneratorApplicationTests.class);

    @Autowired
    private CodeFileService service;

    @Autowired
    private DemoModelDetailService detailService;
    @Autowired
    private DemoPropertyTypeService typeService;

    @Autowired
    private TDemoModelDetailMapper detailMapper;

    @Autowired
    private SnowflakeIdWorker idWorker;

//    @Test
//    @Transactional
    void contextLoads() throws JsonProcessingException {
//        String[] beanDefinitionNames = api.getBeanDefinitionNames();
//        if(beanDefinitionNames == null || beanDefinitionNames.length == 0){
//            logger.error("bean is empty!");
//        }
//        for(String beanName : beanDefinitionNames){
//            System.out.println(beanName);  // sqlSessionFactory
//        }
//        try {
//            SqlSessionFactory sessionFactory = (SqlSessionFactory) api.getBean("sqlSessionFactory");
//            SqlSession sqlSession = sessionFactory.openSession();
//            ScriptRunner runner = new ScriptRunner(session.getConnection());
//            runner.setStopOnError(true);
//            runner.runScript(new FileReader(new File("C:\\Users\\dubul\\Desktop\\test.sql")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            throw new OptErrorException(OptStatus.FAIL.getOptCode(), "SQL 文件读取错误");
//        }


//        List<String> list = detailService.demoGenerate("752650572775436288");
//        for (String s : list) {
//            System.out.print(s);
//        }
//        System.out.println(redisUtil.get("abc"));
        /*Map<String, Object> condition = new HashMap<>();
        condition.put("name", "");
        condition.put("_order", "modify_time");
        condition.put("_sort", "DESC");
        List<TDemoModel> list = service.list(condition);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(TDemoModel s : list){
            System.out.println(sdf.format(s.getCreateTime()) + "\t" +sdf.format(s.getModifyTime()));
        }*/

        /*Map<String, Object> list = detailService.list("752656162054615040");

        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(objectMapper.writeValueAsString(list));*/

        ObjectMapper objectMapper = new ObjectMapper();
        logger.info(objectMapper.writeValueAsString(typeService.listAll()));
//        logger.info(typeService.listAll().toString());

    }


//    @Test
    void test01() {
        Date curDate = new Date();

        TDemoPropertyType[] pros = {
                new TDemoPropertyType(idWorker.nextStringId(), "String", "java.lang.String", 3, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoPropertyType(idWorker.nextStringId(), "Integer", "java.lang.Integer", 6, "yyy",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoPropertyType(idWorker.nextStringId(), "Date", "java.util.Date", 9, "yyy",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoPropertyType(idWorker.nextStringId(), "Boolean", "java.lang.Boolean", 12, "yyy",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoPropertyType(idWorker.nextStringId(), "Float", "java.lang.Float", 15, "yyyy",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoPropertyType(idWorker.nextStringId(), "Double", "java.lang.Double", 18, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0)
        };

        TDemoColumnType[] cols = {
                new TDemoColumnType(idWorker.nextStringId(), "varchar", 32, 2, "xxx",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "char", 240, 4, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "text", null, 6, "xxx",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "int", null, 8, "xxx",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "tinyint", null, 10, "xxx",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "smallint", null, 12, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "bigint", null, 14, "xxx",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "float", null, 16, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "double", null, 18, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "date", null, 20, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "datetime", null, 22, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "timestamp", null, 24, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0),
                new TDemoColumnType(idWorker.nextStringId(), "json", null, 26, "",
                        Constant.CURRENT_USER, curDate, Constant.CURRENT_USER, curDate, 0)
        };

        for (TDemoPropertyType pro : pros) {
            detailMapper.addProType(pro);
        }

        for (TDemoColumnType col : cols) {
            detailMapper.addColType(col);
        }
    }

//    @Test
//    void test02(){
//        List<String> list = service.loadCode("756865775700422656");
//        for(String s : list){
//            System.out.print(s);
//        }
//    }
}
