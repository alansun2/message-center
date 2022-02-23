package vip.tuoyang.push.config;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.querymap.BeanQueryMapEncoder;
import feign.slf4j.Slf4jLogger;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import vip.tuoyang.base.util.AssertUtils;
import vip.tuoyang.push.client.MessageDecoder;
import vip.tuoyang.push.client.MessageEncoder;
import vip.tuoyang.push.client.MessageFeignClient;
import vip.tuoyang.push.config.properties.MessageClientFeignProperties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @author AlanSun
 * @date 2022/1/20 10:36
 */
@ComponentScan(basePackages = "vip.tuoyang.push")
@EnableConfigurationProperties(value = MessageClientFeignProperties.class)
@Configuration(proxyBeanMethods = false)
public class MessageClientFeignAutoConfiguration {
    @Autowired
    private MessageClientFeignProperties messageClientFeignProperties;

    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

    }

    private static SSLSocketFactory sslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        //信任任何链接
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    /**
     * Create a new connection pool with tuning parameters appropriate for a single-user application.
     * The tuning parameters in this pool are subject to change in future OkHttp releases. Currently
     */
    private static ConnectionPool pool() {
        return new ConnectionPool(10, 5, TimeUnit.MINUTES);
    }

    private static OkHttpClient okHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                // 是否开启缓存
                .retryOnConnectionFailure(false)
                // 连接池
                .connectionPool(pool())
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public MessageFeignClient feignClient() throws NoSuchAlgorithmException, KeyManagementException {
        AssertUtils.notNull(messageClientFeignProperties.getServerUrl(), "serverUrl 不能为空");
        return Feign.builder()
                .queryMapEncoder(new BeanQueryMapEncoder())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .retryer(new Retryer.Default(300, 1000, 3))
                .encoder(new MessageEncoder())
                .decoder(new MessageDecoder())
                .client(new feign.okhttp.OkHttpClient(okHttpClient()))
                .target(MessageFeignClient.class, messageClientFeignProperties.getServerUrl());
    }
}
