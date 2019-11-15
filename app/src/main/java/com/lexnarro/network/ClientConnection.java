package com.lexnarro.network;

import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Mayank on 4/27/2017.
 */

public class
ClientConnection {
    private static ClientConnection connect;
    private ApiInterface clientService;

    public static synchronized ClientConnection getInstance() {

        if (connect == null) {
            connect = new ClientConnection();
        }
        return connect;
    }

    public ApiInterface createService() throws Exception {

        if (clientService == null) {

//            new HttpLoggingInterceptor.Logger() {
//                @Override
//                public void log(String message) {
//                    Log.e("Retrofit LOG", message);
//                }
//            }

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            Strategy strategy = new AnnotationStrategy();

            Serializer serializer = new Persister(strategy);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.interceptors().add(logging);
            httpClient.addInterceptor(logging)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()



//                    .baseUrl("http://219.90.67.97:99/services/")
                    .baseUrl("https://lexnarro.com.au/services/")
//                    .baseUrl("http://www.jajtechnologies.com/services/")
//                    .baseUrl("http://www.lexnarro.com.au/Lexnarro/Services/")

                    .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                    .client(httpClient.build())
                    .build();

            clientService = retrofit.create(ApiInterface.class);
        }
        return clientService;
    }
}
