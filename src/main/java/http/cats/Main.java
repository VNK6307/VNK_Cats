package http.cats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String inputUrl = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
        CloseableHttpClient httpClient = HttpClientBuilder.create()
//                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(inputUrl);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);
//        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

        List<Cat> cats = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<>() {
                }
        );
        System.out.println("Полный список кошек:");
        cats.forEach(System.out::println);

        System.out.println("Кошки, за которых проголосовали:");
        cats.stream()
                .filter(votes -> votes.getUpvotes() > 0)
                .forEach(System.out::println);

        response.close();
        httpClient.close();
    }// main
}