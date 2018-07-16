package com.example.swagger2demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger注解
 *     Api：修饰整个类，描述Controller的作用
 *     ApiOperation：描述一个类的一个方法，或者说一个接口
 *     ApiParam：单个参数描述
 *     ApiModel：用对象来接收参数
 *     ApiProperty：用对象接收参数时，描述对象的一个字段
 *     ApiResponse：HTTP响应其中1个描述
 *     ApiResponses：HTTP响应整体描述
 *     ApiIgnore：使用该注解忽略这个API
 *     ApiError ：发生错误返回的信息
 *     ApiImplicitParam：一个请求参数
 *     ApiImplicitParams：多个请求参数
 * 访问：http://localhost:8080/swagger-ui.html
 * @author SongQingWei
 * @date 2018年07月13 10:02
 */
@Configuration
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.swagger2demo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     * @return apiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTFul API")
                //创建人
                .contact(new Contact("Song", "http://www.baidu.com", "110110110@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }
}
