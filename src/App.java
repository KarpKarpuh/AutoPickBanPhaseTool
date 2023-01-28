import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import LCUAPI.LeagueClient;
import LCUAPI.RequestMethodType;

public class App {
    public static void main(String[] args) throws Exception {
        LeagueClient leagueClient = new LeagueClient("C:\\Riot Games\\League of Legends\\lockfile");
        leagueClient.connect();
        JSONObject data = new JSONObject();

        CloseableHttpResponse response = leagueClient.request(RequestMethodType.GET, "/lol-lobby-team-builder/v1/matchmaking", data.toString());
        System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
        leagueClient.disconnect();
    }
}