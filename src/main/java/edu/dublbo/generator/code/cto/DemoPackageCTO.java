package edu.dublbo.generator.code.cto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 代码封装类
 */
public class DemoPackageCTO {
    @NotEmpty @NotBlank
    private String modelId;
    @NotEmpty @NotBlank
    private String tableDemo;
    @NotEmpty @NotBlank
    private String entityDemo;
    @NotEmpty @NotBlank
    private String mapperInterDemo;
    @NotEmpty @NotBlank
    private String mapperXmlDemo;
    @NotEmpty @NotBlank
    private String serviceDemo;
    @NotEmpty @NotBlank
    private String controllerDemo;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getTableDemo() {
        return tableDemo;
    }

    public void setTableDemo(String tableDemo) {
        this.tableDemo = tableDemo;
    }

    public String getEntityDemo() {
        return entityDemo;
    }

    public void setEntityDemo(String entityDemo) {
        this.entityDemo = entityDemo;
    }

    public String getMapperInterDemo() {
        return mapperInterDemo;
    }

    public void setMapperInterDemo(String mapperInterDemo) {
        this.mapperInterDemo = mapperInterDemo;
    }

    public String getMapperXmlDemo() {
        return mapperXmlDemo;
    }

    public void setMapperXmlDemo(String mapperXmlDemo) {
        this.mapperXmlDemo = mapperXmlDemo;
    }

    public String getServiceDemo() {
        return serviceDemo;
    }

    public void setServiceDemo(String serviceDemo) {
        this.serviceDemo = serviceDemo;
    }

    public String getControllerDemo() {
        return controllerDemo;
    }

    public void setControllerDemo(String controllerDemo) {
        this.controllerDemo = controllerDemo;
    }
}
