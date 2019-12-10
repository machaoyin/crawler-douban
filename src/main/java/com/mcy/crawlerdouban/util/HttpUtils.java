package com.mcy.crawlerdouban.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    //创建连接池管理器
    private static PoolingHttpClientConnectionManager cm;

    public HttpUtils(){
        cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }

    //配置请求信息
    private static RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(10000)      //创建连接的最长时间，单位毫秒
                .setConnectionRequestTimeout(10000)  //设置获取链接的最长时间，单位毫秒
                .setSocketTimeout(10000)     //设置数据传输的最长时间，单位毫秒
                .build();
        return config;
    }

    /**
     * 根据请求地址下载页面数据
     * @param url   请求路径
     * @param map   请求参数
     * @param mapTile   请求头
     * @return  //页面数据
     * @throws URISyntaxException
     */
    public static String doGetHtml(String url, Map<String, String> map, Map<String, String> mapTile) throws URISyntaxException {
        //创建HTTPClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //设置请求地址
        //创建URLBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        //设置参数
        if(!map.isEmpty()){
            for(String key : map.keySet()){
                uriBuilder.setParameter(key, map.get(key));
            }
        }

        //创建HTTPGet对象，设置url访问地址
        //uriBuilder.build()得到请求地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        //设置请求头信息
        if(!mapTile.isEmpty()){
            for(String key : mapTile.keySet()){
                httpGet.addHeader(key, mapTile.get(key));
            }
        }

        //设置请求信息
        httpGet.setConfig(getConfig());
        System.out.println("发起请求的信息："+httpGet);

        //使用HTTPClient发起请求，获取response
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            //解析响应
            if(response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否不为空，如果不为空就可以使用EntityUtils
                if(response.getEntity() != null) {
                    String content = EntityUtils.toString(response.getEntity(), "utf8");
                    return content;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //关闭response
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public static String doGetImage(String url) throws IOException {
        //获取HTTPClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置HTTPGet请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(getConfig());
        //使用HTTPClient发起请求，获取响应
        CloseableHttpResponse response = null;
        try {
            //使用HTTPClient发起请求，获取响应
            response = httpClient.execute(httpGet);
            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否不为空
                if(response.getEntity() != null) {
                    //下载图片
                    //获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    //创建图片名，重命名图片
                    String picName = UUID.randomUUID().toString() + extName;
                    //下载图片
                    //声明OutputStream
                    OutputStream outputStream = new FileOutputStream(new File("E://imges/" + picName));
                    response.getEntity().writeTo(outputStream);
                    //返回图片名称
                    return picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭response
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
