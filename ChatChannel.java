import java.util.*;

public class ChatChannel{
    private ArrayList<String> channelHistory;
    private String channelName;

    public ChatChannel(String s){
        this.channelName =s;
        //this.channelHistory = channelHistory;
        channelHistory = new ArrayList<String>();
        channelHistory.add("Testing HS");
    }

    public String getChannelName(){
        return channelName;
    }
    public ArrayList<String> getChannelHistory(){
        return channelHistory;
    }

    public void setChannelName(String s){
        channelName = s;
    }
    public void addToChannelHistory(String s){
        channelHistory.add(s);
    }
}