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
import twitter4j.auth.AccessToken;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.atilika.kuromoji.Tokenizer;
import org.atilika.kuromoji.Token;



public class Model {
    private String consumerkey="coa8pATuhe3T2vYOEOIp214EO";
    private String consumerSecret="BSFxbPULnAzVTn0MWvtBOkylaCcGu2lJibZo6utuYXPiITxhas";
    private String callback="http://127.0.0.1:8080/exp_app/view.jsp";
    private String method = "POST";
    final private String Y_id= "dj0zaiZpPUx6ZlZsbzF2ck5GVyZzPWNvbnN1bWVyc2VjcmV0Jng9ZmI-";
    private Twitter twitter;
    public ResponseList<Status> myRes;
    public String text="";
    private List<String> friend=new ArrayList<String>();
    private List<Integer> fnum=new ArrayList<Integer>();
    public String [] ranking=new String[5];
    public int [] ranknum=new int[5];
    private List<String> words=new ArrayList<String>();
    private List<Integer> wnum=new ArrayList<Integer>();
    public String rtxt="";
    public String [] pics=new String[5];
    
    public OAuthConsumer consumer = new DefaultOAuthConsumer(
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
    
    public void twi4jAcc(){
        TwitterFactory factory = new TwitterFactory();
        AccessToken accessToken = new AccessToken(consumer.getToken(),consumer.getTokenSecret());
        twitter = factory.getInstance();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumer.getConsumerKey(), consumer.getConsumerSecret());
        twitter.setOAuthAccessToken(accessToken);
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
        for (int i=1;i<10;i++){
            Paging page = new Paging(i,200);
            myRes = twitter.getUserTimeline(page);
            for (Status state: myRes){
                if (state.isRetweet() )
                    continue;
                tmp=state.getText();
                m=p.matcher(tmp);
                if (m.find()){
                    if (m.group()==null)
                        continue;

                    if (friend.contains(m.group())){
                        fnum.set(friend.indexOf(m.group()), fnum.get(friend.indexOf(m.group()))+1);

                    }else{
                        friend.add(m.group());
                        fnum.add(1);
                    }
                }

                tmp=m.replaceAll("");
                m2=p2.matcher(tmp);
                tmp=m2.replaceAll("");
                text = text+" "+tmp;
            }
        }
    }
    
    
    
    public void printUser() {
        int tmp;
        String stmp="aaa";
        for(int i=0;i<5;i++){
            tmp=0;
            for (int j=0;j<friend.size();j++){
                if(tmp<fnum.get(j) && Arrays.asList(ranking).contains(friend.get(j))==false && friend.get(j).length()>3){
                    tmp=fnum.get(j);
                    stmp=friend.get(j);
                }
            }
            ranking[i]=stmp;
            ranknum[i]=tmp;
            System.out.println(stmp);
            try{
            User user = twitter.showUser(stmp);
            pics[i]=user.getProfileImageURL();
            }catch(Exception e){
                pics[i]="";
            }
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
    
    public void bagword(String text) throws Exception{
        String mytext = URLEncoder.encode(text, "UTF-8");
        URL url = new URL("http://jlp.yahooapis.jp/MAService/V1/parse?"+
                "results=uniq"+
                "&appid="+Y_id+
                "&sentence=" + mytext);
        Document doc=new SAXBuilder().build(url);
        Element root = doc.getRootElement();
        Namespace ns=root.getNamespace();
        List<Element> wlist=root.getChild("uniq_result",ns).getChild("word_list",ns).getChildren("word",ns);
        
        for(Element child : wlist){
            //words.add(child.getChild("word",ns).getText());
            System.out.print(child.getChild("surface",ns).getText()+"    ");
            System.out.println(child.getChild("count",ns).getText());
        }
    }
    
    public void kaiseki(String txt){
        ArrayList<Integer> nums = new ArrayList<Integer>();
	ArrayList<String> key = new ArrayList<String>();
        int page;
        Tokenizer tokenize=Tokenizer.builder().build();
        List<Token> tokens=tokenize.tokenize(txt);
        for (Token token : tokens) {
            String toke = token.getSurfaceForm();
            if (!key.contains(toke)){
                    key.add(toke);
                    nums.add(1);
            }
            else{
                page = key.indexOf(toke);
                nums.set(page, nums.get(page)+1);
            }
        }
        for (int i=0; i<key.size()-1; i++){
            for (int j=key.size()-1; j>i; j--){
                if (nums.get(j-1) < nums.get(j)){
                    int temp=nums.get(j-1);
                    nums.set(j-1, nums.get(j));
                    nums.set(j, temp);

                    String stemp=key.get(j-1);
                    key.set(j-1, key.get(j));
                    key.set(j, stemp);
                }
            }
        }
        
        for (int i=0; i<key.size(); i++){
            System.out.println(key.get(i)+" : "+nums.get(i)+"回");
            rtxt=rtxt+"</br>"+key.get(i)+" : "+nums.get(i)+"回";
        }
    }
    public String urlc (String txt) throws Exception{
        return URLEncoder.encode(txt, "UTF-8");
    }
}
