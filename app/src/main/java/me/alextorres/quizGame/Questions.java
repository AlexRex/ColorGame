package me.alextorres.quizGame;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class Questions {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public Questions() {

    }

    public String makeServiceCall(String url, int method){
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params){

        try {
            //crear cliente http
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            //Comprobar metodo http POST: Enviar, GET: recibir.

            if(method == POST){
                HttpPost httpPost = new HttpPost(url);
                //anyadir parametros a la peticion
                if(params != null){
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);
            }

            else if (method == GET){
                if(params != null){
                    //anyadir parametros a la url
                    String paramString = URLEncodedUtils.format(params, "UTF-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (ClientProtocolException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return response;

    }
}
