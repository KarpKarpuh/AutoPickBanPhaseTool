package LCUAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;



public class LeagueClient {

    String leagueExePath;

    String token;
    String apiURI;
    int port;
    int LeaguePID;

    CloseableHttpClient client;
    byte[] encodedAuth;
    HttpPost httpPost;
    HttpGet httpGet;
    HttpPut httpPut;
    HttpDelete httpDelete;

    public LeagueClient(String leagueExePath){
        this.client = HttpClientBuilder.create().build();
        this.leagueExePath =leagueExePath;
    }

    public void connect(){
        GetLockFileCredentials(leagueExePath);
    }

    public void disconnect()
    {
        this.client = null;
        this.encodedAuth = null;
        this.httpPost = null;
        this.httpGet = null;
        this.httpPut = null;
        this.httpDelete = null;
        System.out.println("Disconnected from LCU");
    }

    public CloseableHttpResponse request(RequestMethodType methodType, String endpoint, Object Data){
        switch(methodType)
        {
            case GET:
                return ExecuteGetRequest(endpoint, Data);
            case PUT:
                return ExecutePutRequest(endpoint,Data);
            case POST:
                return ExecutePostRequest(endpoint,Data);
            case DELETE:
                return ExecuteDeleteRequest(endpoint,Data);
            default:
                throw new RequestTypeNotFoundException(methodType);
        }
    }

    private void GetLockFileCredentials(String PathToLockFile)
    {
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(PathToLockFile));
            String line = reader.readLine();
            String[] items = line.split(":");
            this.token = items[3];
            this.port = Integer.parseInt(items[2]);
            this.apiURI = "https://127.0.0.1:" + this.port + "/";
            this.LeaguePID = Integer.parseInt(items[1]);


            String auth = "riot:" + this.token;
            this.encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));

        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private CloseableHttpResponse ExecuteGetRequest(String endPoint, Object data)
    {
        try
        {
            this.httpGet =  new HttpGet(apiURI + endPoint);
            this.httpGet.setHeader("Accept", "application/json");

            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization", "Basic " +  new String(this.encodedAuth));
            CloseableHttpResponse response = this.client.execute(httpGet);
            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    private CloseableHttpResponse ExecutePostRequest(String endPoint, Object data)
    {
        try
        {
            this.httpPost =  new HttpPost(apiURI + endPoint);
            this.httpPost.setHeader("Accept", "application/json");

            String json = (String)data;
            StringEntity entity = new StringEntity(json);
            this.httpPost.setEntity(entity);

            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "Basic " +  new String(this.encodedAuth));
            CloseableHttpResponse response = this.client.execute(httpPost);

            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    private CloseableHttpResponse ExecutePutRequest(String endPoint, Object data)
    {
        try
        {
            this.httpPut =  new HttpPut(apiURI + endPoint);
            this.httpPut.setHeader("Accept", "application/json");

            String json = (String)data;
            StringEntity entity = new StringEntity(json);
            this.httpPut.setEntity(entity);

            httpPut.setHeader("Content-type", "application/json");
            httpPut.setHeader("Authorization", "Basic " +  new String(this.encodedAuth));
            CloseableHttpResponse response = this.client.execute(httpPut);

            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    private CloseableHttpResponse ExecuteDeleteRequest(String endPoint, Object data)
    {
        try
        {
            this.httpDelete =  new HttpDelete(apiURI + endPoint);
            this.httpDelete.setHeader("Accept", "application/json");

            httpDelete.setHeader("Content-type", "application/json");
            httpDelete.setHeader("Authorization", "Basic " +  new String(this.encodedAuth));
            CloseableHttpResponse response = this.client.execute(httpDelete);

            return response;
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
        return null;
    }
}
