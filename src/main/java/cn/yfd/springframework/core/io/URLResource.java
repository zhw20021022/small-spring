package cn.yfd.springframework.core.io;

import cn.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLResource implements Resource{

    private final URL url;

    public URLResource(URL url) {
        Assert.notNull(url, "URL must not be null");
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection urlConnection = url.openConnection();
        try{
            return urlConnection.getInputStream();
        }catch (IOException e){
            if(urlConnection instanceof HttpURLConnection){
                ((HttpURLConnection)urlConnection).disconnect();
            }
            throw e;
        }
    }
}
