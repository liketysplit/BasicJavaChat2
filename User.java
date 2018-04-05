import java.util.ArrayList;

public class User{

    private String name;
    private String channel;

    public User(String n, String c){
        name =n;
        channel=c;
    }
    public String getChannel(){
        return channel;
    }
    public String getName(){
        return name;
    }

    public void setName(String s){
        name = s;
    }
    public void setChannel(String s){
        channel=s;
    }
}