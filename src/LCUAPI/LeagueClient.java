package LCUAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class LeagueClient {

    private static final String QUEUE_STATUS_ENDPOINT = "/lol-lobby-team-builder/v1/matchmaking";
    private static final String PICK_AND_BAN_STATUS_ENDPOINT = "/lol-lobby-team-builder/champ-select/v1/session";
    private static final String ACCEPT_QUEUE_ENDPOINT = "lol-lobby-team-builder/v1/ready-check/accept";

    private String leagueExePath;

    private String token;
    private String apiURI;
    private int port;
    private int LeaguePID;

    private CloseableHttpClient client;
    private byte[] encodedAuth;
    private HttpPost httpPost;
    private HttpGet httpGet;
    private HttpPut httpPut;
    private HttpDelete httpDelete;

    public LeagueClient(String leagueExePath){
        this.client = HttpClientBuilder.create().build();
        this.leagueExePath =leagueExePath;
    }

    public void connect(){
        getLockFileCredentials(leagueExePath);
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

    private CloseableHttpResponse request(RequestMethodType methodType, String endpoint, Object Data){
        switch(methodType)
        {
            case GET:
                return executeGetRequest(endpoint, Data);
            case PUT:
                return executePutRequest(endpoint,Data);
            case POST:
                return executePostRequest(endpoint,Data);
            case DELETE:
                return executeDeleteRequest(endpoint,Data);
            default:
                throw new RequestTypeNotFoundException(methodType);
        }
    }

    private void getLockFileCredentials(String PathToLockFile)
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

    private CloseableHttpResponse executeGetRequest(String endPoint, Object data)
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

    private CloseableHttpResponse executePostRequest(String endPoint, Object data)
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

    private CloseableHttpResponse executePutRequest(String endPoint, Object data)
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

    private CloseableHttpResponse executeDeleteRequest(String endPoint, Object data)
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

    private String getResponseEntityString(String endpoint) throws ParseException, IOException{
        JSONObject data = new JSONObject();
        CloseableHttpResponse response = request(RequestMethodType.GET, endpoint, data.toString());
        return EntityUtils.toString(response.getEntity(), "UTF-8");
        
    }

    public String getQueueStatus() throws ParseException, IOException{
        String erg = getResponseEntityString(QUEUE_STATUS_ENDPOINT);
        int startIndex = erg.indexOf("searchState\":\"")+14;
        int stopIndex = erg.indexOf("timeInQueue")-3;
        return erg.substring(startIndex, stopIndex);
    }

    public String getPickBanPhaseStatus() throws ParseException, IOException{
        String erg = getResponseEntityString(PICK_AND_BAN_STATUS_ENDPOINT);
        return erg;
    }

    public void acceptQueue(){
        JSONObject data = new JSONObject();
        request(RequestMethodType.POST, ACCEPT_QUEUE_ENDPOINT, data);
    }
    

}
