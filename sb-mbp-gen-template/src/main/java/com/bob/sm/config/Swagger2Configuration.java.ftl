package ${packageName}.config;

import com.fasterxml.classmate.TypeResolver;
import ${packageName}.dto.*;
import ${packageName}.dto.criteria.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("dev")
public class Swagger2Configuration {

    // 添加过滤规则，移除嵌套对象
    TypeResolver typeResolver = new TypeResolver();
    AlternateTypeRule[] typeRules = {
            // ======================== add swagger param and return objects here start =========================
////////////////////////////add-rules-here////////////////////////////
            // ======================== add swagger param and return objects here end =========================
    };

    // swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .alternateTypeRules(typeRules)
                .select()
                // 为当前包路径
                .apis(RequestHandlerSelectors.basePackage("${packageName}.web.rest"))
                .paths(PathSelectors.ant("/api/*"))
                .build();
    }

    // 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("XX系统后台API")
                // 创建人
//                .contact(new Contact("TechService", "http://fyy.sdfyy.cn", ""))
                // 版本号
                .version("0.1")
                // 描述
                .description("说明：<br/>" +
                        "1、按照Restful风格：查询使用GET方法，新增使用POST方法，修改使用PUT方法，删除使用DELETE方法。<br/>" +
                        "2、POST/PUT/DELETE方法的Http body均采用了json格式（共通接口的上传文件除外）。<br/>" +
                        "3、新增、修改方法均为级联操作，只需传入嵌套实体的json数据即可。<br/>" +
                        "4、OneToMany的级联修改操作，会先删除被级联的实体（Many一方）相应的数据，再批量插入；OneToOne的级联修改操作，" +
                        "如果正确填写了被级联的实体的ID，则会根据ID更新该级联的实体。<br/>" +
                        "5、查询参数中Data Type类型分为基本类型、基本条件类型和扩展类型。<br/>" +
                        "&nbsp;&nbsp;1) 基本类型均为string类型，包括associationNameList、dictionaryNameList、orderBy、current、size。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;a、associationNameList表示级联查询，可显示多层级联的结果，用.分割各层级。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;b、dictionaryNameList表示数据字典的级联查询（即根据Code查询Value）。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;c、注意：associationNameList和dictionaryNameList只能应用于最外层，例如不能有xxx.associationNameList。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;d、orderBy表示排序的字段。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;e、current表示当前页（从1开始）。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;f、size表示每页的数量。<br/>" +
                        "&nbsp;&nbsp;2) 基本条件类型包括：string、int、long、double。<br>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;a、基本类型均有equals、notEquals、in、notIn、nullable属性。" +
                        "equals表示等于；notEquals表示不等于；in表示在某个列表中，相当于SQL中的in；notIn相当于不在某个列表中，" +
                        "相当于SQL中的not in；nullable为true时表示为空，nullable为false时表示不为空。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;b、string类型还有contains、notContains、startWith、endWith属性。" +
                        "contains相当于SQL中的like，notContains与contains相反，startWith表示以xx开头，endWith表示以xx结尾。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;c、int、long、double都属于数值类型，数值类型属性包括greaterThan、greaterOrEqualThan、lessThan、" +
                        "lessOrEqualThan，分别表示大于、大于等于、小于、小于等于。另外还有betweenFrom和betweenTo表示在这个范围内。" +
                        "notBetweenFrom和notBetweenTo表示不在这个范围内。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;d、其它基本类型及基本类型的其它属性正在不断完善中。<br/>" +
                        "&nbsp;&nbsp;3) 扩展类型均显示为string类型，包括：级联查询、自定义查询。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;a、文档说明中带.?的查询条件就是级联查询，?表示级联的查询条件实体（不同于associationNameList，" +
                        "associationNameList是级联的查询结果实体），可使用多层级联的查询，用.分割各层级。<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;b、其它的就是自定义查询条件（例如带or的条件），可自由发挥。")
                .build();
    }

}
