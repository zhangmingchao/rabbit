package com.wlqk.listent;


import org.apache.http.Header;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取restTemplate，支持HTTP(S)的访问
 *
 * @version 1.0.0
 */
public class RestUtil {

    /** 总连接数 */
    private static final int MAX_TOTAL = 1000;
    /** 同路由的并发数 */
    private static final int MAX_PER_ROUTE = 1000;
    /** 连接等待时长 */
    private static final int CONNECT_REQUEST_TIME_OUT = 200;
    /** 连接超时 */
    private static final int CONNECT_TIME_OUT = 5000;
    /** 重试次数 */
    private static final int RETRY_COUNT = 5;

    private static RestTemplate restTemplate = null;

    static {
        System.out.println("开始初始化...");
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 保持长连接
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        // 配置头信息
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        // SSL配置开始
        SSLContext sslContext = null;
        Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            httpClientBuilder.setSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // SSL配置结束
        // 创建池
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        pollingConnectionManager.setMaxTotal(MAX_TOTAL); // 总连接数
        pollingConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);// 同路由的并发数
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, true));// 重试次数
        CloseableHttpClient client = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(client);
        clientHttpRequestFactory.setConnectTimeout(CONNECT_TIME_OUT);// 连接超时时长
        clientHttpRequestFactory.setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT);// 连接等待时长
        clientHttpRequestFactory.setBufferRequestBody(false);// 不缓冲请求数据，避免内存消耗
        // 添加内容转换器
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        // 实例化restTemplate
        restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        System.out.println("成功！");
    }

    /**
     * 获取restTemplate实例
     *
     * @return
     */
    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
