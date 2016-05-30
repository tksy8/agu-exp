package twi;

import oauth.signpost.*;
import oauth.signpost.basic.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.ResponseList;
import twitter4j.Paging;



public class Model {
    private String consumerkey="coa8pATuhe3T2vYOEOIp214EO";
    private String consumerSecret="BSFxbPULnAzVTn0MWvtBOkylaCcGu2lJibZo6utuYXPiITxhas";
    private String callback="http://127.0.0.1:8080/exp_app/view.jsp";
    private String method = "POST";
    private Twitter twitter;
    public ResponseList<Status> myRes;
    public String text="";
    
    
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
    
    public void twi4j(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(consumer.getConsumerKey());
        cb.setOAuthConsumerSecret(consumer.getConsumerSecret());
        cb.setOAuthAccessToken(consumer.getToken());
        cb.setOAuthAccessTokenSecret(consumer.getTokenSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }
    
    public void tweet4(String text) throws Exception{
        Status state = twitter.updateStatus(text);
    }
    
    public void getTimeline() throws Exception{
        String rep="@[a-z0-9_]*"; //リプライの正規表現
        String url="http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        Pattern p=Pattern.compile(rep);
        Pattern p2=Pattern.compile(url);
        Matcher m;
        Matcher m2;
        String tmp;
        Paging page = new Paging(1,20);
        myRes = twitter.getUserTimeline(page);
        for (Status state: myRes){
            if (state.isRetweet() )
                continue;
            tmp=state.getText();
            m=p.matcher(tmp);
            tmp=m.replaceAll("");
            m2=p2.matcher(tmp);
            tmp=m2.replaceAll("");
            text = text+"</br>"+tmp;
        }
    }
    
    public void registerToken(String token, String stoken) throws Exception{
        consumer.setTokenWithSecret(token, stoken);
        OAuthProvider provide = new DefaultOAuthProvider("https://api.twitter.com/oauth/request_token", 
                                "https://api.twitter.com/oauth/access_token", 
                                "https://api.twitter.com/oauth/authorize");    
        provide.setOAuth10a(true);
        provide.retrieveAccessToken(consumer, stoken);        
    }
    
    public void tweet(String text) throws Exception{
        text = URLEncoder.encode(text.toString(), "UTF-8");
        URL url = new URL("https://api.twitter.com/1.1/statuses/update.json?status=" + text);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        consumer.sign(connection);
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
