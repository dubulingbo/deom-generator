package edu.dublbo.generator.utils;

/**
 * @author DubLBo
 * @since 2020-09-05 18:22
 * i believe i can i do
 */
public class Constant {
    public static String CURRENT_USER = "admin";

    /**
     * 就是用来生成明细表里的序号啦。。。。
     */
    public static Integer MODEL_DETAIL_SORTNO_MIN_POS = 996;
    public static Integer MODEL_DETAIL_SORTNO_MAX_POS = 1000;

    /**
     * 定义一些模板文件的相对路径
     */
    public static String TABLE_TF_PATH = "./src/main/resources/templates/table_template.sql";
    public static String ENTITY_TF_PATH = "./src/main/resources/templates/model_template.java";
    public static String MAPPER_INTER_TF_PATH = "./src/main/resources/templates/mapper_inter_template.java";
    public static String MAPPER_XML_TF_PATH = "./src/main/resources/templates/mapper_xml_template2.xml";
    public static String SERVICE_TF_PATH = "./src/main/resources/templates/service_template.java";
    public static String CONTROLLER_TF_PATH = "./src/main/resources/templates/controller_template.java";
}
