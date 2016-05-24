package twi;

import oauth.signpost.*;
import oauth.signpost.basic.*;
import java.io.*;


public class Model {
    private String consumerkey="coa8pATuhe3T2vYOEOIp214EO";
    private String consumerSecret="BSFxbPULnAzVTn0MWvtBOkylaCcGu2lJibZo6utuYXPiITxhas";
    private String callback="http://127.0.0.1:8080/exp-project/view2.jsp";
    private String method = "POST";
    private String urlStr = "https://api.twitter.com/oauth/request_token";
    
    private OAuthConsumer consumer = new DefaultOAuthConsumer(
        "coa8pATuhe3T2vYOEOIp214EO",
        "BSFxbPULnAzVTn0MWvtBOkylaCcGu2lJibZo6utuYXPiITxhas");
    
    private OAuthProvider provider = new DefaultOAuthProvider(
        "https://api.twitter.com/oauth/request_token",
        "https://api.twitter.com/oauth/access_token",
        "https://api.twitter.com/oauth/authorize");

    public String getAuth() throws Exception{
        return provider.retrieveRequestToken(consumer, callback);
    }
    
    public void getToken(String pin) throws Exception{
        provider.retrieveAccessToken(consumer, pin);
        consumer.setTokenWithSecret(consumer.getToken(), consumer.getTokenSecret());
    }
    

    public void execute() throws Exception{
        String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
        System.out.println("このURLにアクセスし、表示されるPINを入力してください。");
        System.out.println(authUrl);
        System.out.print("PIN:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String pin = br.readLine();

        provider.retrieveAccessToken(consumer, pin);
        System.out.println("Access token: " + consumer.getToken());
        System.out.println("Token secret: " + consumer.getTokenSecret());
    }
}
