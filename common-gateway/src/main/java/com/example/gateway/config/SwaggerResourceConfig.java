
package com.example.gateway.config;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 开启swagger功能
 * 如果有多个配置文件，以这个为准
 * 实现SwaggerResourcesProvider，配置swagger接口文档的数据源
 * @author haha
 */
@Configuration
@EnableSwagger2
@Primary
@EnableConfigurationProperties(ZuulProperties.class)
public class SwaggerResourceConfig  implements SwaggerResourcesProvider {

    /**
     *
     * RouteLocator可以根据zuul配置的路由列表获取服务
     */
    @Resource
    private ZuulProperties zuulProperties;


    /**
     * 这个方法用来添加swagger的数据源
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
        zuulProperties.getRoutes().values()
                .forEach(route -> resources.add(
                        swaggerResource(route.getId(), route.getPath().replace("**", "v2/api-docs"), "2.0")
                ));

        System.out.println(JSON.toJSONString(resources));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}