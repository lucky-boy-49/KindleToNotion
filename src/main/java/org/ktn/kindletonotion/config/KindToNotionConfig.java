package org.ktn.kindletonotion.config;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.ktn.kindletonotion.Exception.NotionResponseException;
import org.ktn.kindletonotion.notion.config.NotionConfigProperties;
import org.ktn.kindletonotion.notion.utils.HttpHeaderUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ReactorResourceFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.function.Function;

/**
 * 配置
 * @author 贺佳
 */
@Slf4j
@Configuration
public class KindToNotionConfig {

    private final NotionConfigProperties notionConfigProps;

    private final HttpHeaderUtil httpHeaderUtil;

    public KindToNotionConfig(NotionConfigProperties notionConfigProps, HttpHeaderUtil httpHeaderUtil) {
        this.notionConfigProps = notionConfigProps;
        this.httpHeaderUtil = httpHeaderUtil;
    }

    /**
     * 管理 Reactor Netty 资源
     * @return Reactor Netty资源管理器
     */
    @Bean
    public ReactorResourceFactory resourceFactory() {
        return new ReactorResourceFactory();
    }



    /**
     * 创建HttpServiceProxyFactoryBean
     * @return HttpServiceProxyFactory
     */
    @Bean
    public HttpServiceProxyFactory getHttpServiceProxyFactory() {
        Function<HttpClient, HttpClient> mapper = client -> client
                // 设置响应超时时间
                .responseTimeout(Duration.ofSeconds(30))
                // 设置连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        ClientHttpConnector connector =
                new ReactorClientHttpConnector(resourceFactory(), mapper);

        WebClient client = WebClient.builder()
                // 设置请求地址
                .baseUrl(notionConfigProps.apiUrl())
                // 设置默认请求头
                .defaultHeaders(httpHeaders -> httpHeaders.addAll(httpHeaderUtil.getDefaultHeaders()))
                .clientConnector(connector)
                // 设置默认状态码处理
                .defaultStatusHandler(HttpStatusCode::isError, response -> {
                    throw new NotionResponseException(response.statusCode().value(), response.bodyToMono(String.class));
                })
                .build();
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
    }

}
