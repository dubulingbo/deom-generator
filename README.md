# demo-generator
## I. 生成的内容
1. 生成 Model 层代码
2. 生成 Mapper 层的代码：Mapper Interface & Mapper Xml
3. 生成 Service 层代码
4. 生成 Controller 层代码（不带自定义回复结果）

## II. Model录入要求
a. 命名格式：
```java
》 [组织名.]项目名称.模块名.实体名
    1. 组织名可以没有，但建议加上，若没有，则使用默认的，即：com.example
    2. 项目名称、模块名和实体名称为必填项
    3. 所有的名称命名必须使用字符、数字或下划线，且必须以字母开头
    4. 实体名还必须符合驼峰命名格式，应该尽量做到见名知义；
       实体名应该以T、V或S开头，其中T表示数据表，V表示视图，S表示序列
合理的案例：
    com.example.demo.basic.TDemoModel
    表示demo项目中的基础数据模块的模型表

》 模型明细录入
    1. 属性项为必填项，而列名项为可选录入
    2. 属性名必须满足 *java命名格式*
```