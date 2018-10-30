package com.youmeng.taoshelf.config;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaobaoClientConfiguration {

    @Bean(name = "client1")
    public TaobaoClient getClient_1() {
        return new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "12322527", "db6e35b4a58543e9f48b34419e278377");
    }

    @Bean(name = "client2")
    public TaobaoClient getClient_2() {
        return new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "12402170", "76f407aef5e1f5ad55542b8a5ae63247");
    }

}
