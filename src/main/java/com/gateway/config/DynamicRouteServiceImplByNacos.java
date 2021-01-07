package com.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Created by nings on 2020/11/17.
 */
@Component
@DependsOn(value = "gateWayConfig")
public class DynamicRouteServiceImplByNacos {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    private ConfigService configService;



    /**
     * 监听Nacos下发的动态路由配置
     * @param dataId
     * @param group
     */
    public void dynamicRouteByNacosListener (String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    for (RouteDefinition definition : definitionList) {
                        dynamicRouteService.update(definition);
                    }
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
        }
    }


    @PostConstruct
    public void init() {
        try{
            configService = initConfigService();
            if(configService == null){
                return;
            }
            String configInfo = configService.getConfig(GateWayConfig.NACOS_ROUTE_DATA_ID, GateWayConfig.NACOS_ROUTE_GROUP, GateWayConfig.DEFAULT_TIMEOUT);
            List<RouteDefinition> definitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            for(RouteDefinition definition : definitionList){
                dynamicRouteService.add(definition);
            }
        } catch (Exception e) {
        }
        dynamicRouteByNacosListener(GateWayConfig.NACOS_ROUTE_DATA_ID,GateWayConfig.NACOS_ROUTE_GROUP);
    }



    /**
     * 初始化网关路由 nacos config
     * @return
     */
    private ConfigService initConfigService(){
        try{
            Properties properties = new Properties();
            properties.setProperty("serverAddr",GateWayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace",GateWayConfig.NACOS_NAMESPACE);
            return configService= NacosFactory.createConfigService(properties);
        } catch (Exception e) {
            return null;
        }
    }


}
