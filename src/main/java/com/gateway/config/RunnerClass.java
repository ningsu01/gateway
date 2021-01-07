package com.gateway.config;

import org.springframework.stereotype.Component;

/**
 * Created by nings on 2020/11/26.
 */
@Component
public class RunnerClass {/*implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        initGatewayRules();
        System.out.println("----------->>>初始化");
    }

    *//**
     * 配置限流规则
     *//*
    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("consumer-router")
                .setCount(1) // 限流阈值
                .setIntervalSec(1) // 统计时间窗口，单位是秒，默认是 1 秒
        );
        GatewayRuleManager.loadRules(rules);
    }*/
}
