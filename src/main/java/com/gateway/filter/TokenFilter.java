package com.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by nings on 2020/11/14.
 */
public class TokenFilter implements GlobalFilter,Ordered{


    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        // 获取请求的路径
        // serverWebExchange.getRequest().getURI().getRawPath();
        // 获取参数，如token
        String token = serverWebExchange.getRequest().getQueryParams().getFirst("token");
        System.out.println("--------->>>token:"+token);
        if(token==null || token.isEmpty()) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 未认证
            return serverWebExchange.getResponse().setComplete(); // 设置完成请求
        }
        return gatewayFilterChain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
