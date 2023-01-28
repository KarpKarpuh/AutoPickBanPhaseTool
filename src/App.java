import LCUAPI.LeagueClient;

public class App {

    private final static String QUEUE_FOUND = "Found";
    private final static int QUEUE_CHECK_INTERVALL = 9999;
    private final static int PICK_AND_BAN_CHECK_INTERVALL = 5000;

    public static void main(String[] args) throws Exception {
        LeagueClient leagueClient = new LeagueClient("C:\\Riot Games\\League of Legends\\lockfile");
        leagueClient.connect();
       
        //Queue Event
      while(!leagueClient.getQueueStatus().equals(QUEUE_FOUND)){
            Thread.sleep(QUEUE_CHECK_INTERVALL);
        }
        System.out.println("Queue Found!");


        //Queue Accept
        leagueClient.acceptQueue();

        
        
        for (int i = 0; i < 60; i++) {
            System.out.println(leagueClient.getPickBanPhaseStatus());
            Thread.sleep(PICK_AND_BAN_CHECK_INTERVALL);
        }

        System.out.println("Queue Accepted");
        
        leagueClient.disconnect();

        
    }



}

