import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;
import java.util.ArrayList;

public class Server {

    private static final int PORT = 3000;
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<ChatChannel> channels = new ArrayList<ChatChannel>();
    private static ArrayList<String> names = new ArrayList<String>();
    private static ArrayList<String> groups = new ArrayList<String>();
    private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception {
        ChatChannel gen = new ChatChannel("General");
        ChatChannel offt = new ChatChannel("Off-Topic");

        channels.add(gen);
        channels.add(offt);

        System.out.println("Server is running on port: " + PORT);
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;


        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                writers.add(out);
                while (true) {
                    out.println("004");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (users) {
                        boolean found = false;
                        for (User uN : users) { 
                            if(uN.getName().equals(name))
                                found = true;
                        }

                        if (!found) {
                            User newUser = new User(name, "General");
                            users.add(newUser);
                            for (PrintWriter writer : writers) {
                                writer.println("000," + name + "," + channels.get(0).getChannelName() + "," + " has joined the chat!");

                                //Update Users 
                                String unS = "003";
                                for (User un : users) {                               
                                        unS= unS + "," + un.getName();
                                }
                                writer.println(unS);

                                //Update Groups
                                String cnS = "002";
                                for (ChatChannel channel : channels) {
                                    cnS= cnS + "," + channel.getChannelName();
                                }
                                writer.println(cnS);
                            }
                            System.out.println(name + " has joined the chat!");
                            break;
                        }
                        
                    }
                }

                out.println("005");
                

                while (true) {
                    String input = in.readLine();
                    System.out.println(input);
                    if (input == null) {
                        return;
                    }else if (input.contains("+/w")) {
                        String tName = input.substring(0,input.indexOf("+"));
                        int tc=0;
                        int al=-1;
                        int sender=-2;
                        String temp = "";
                        String mInput = input.substring(input.indexOf("+")+1, input.length());
                        String oSet = "";
                        String rec = "";
                        for (User un : users) {
                            temp = "/w " + un.getName();
                            if(mInput.startsWith(temp)){
                                al=tc;
                                oSet = temp;
                                rec = un.getName();
                            }
                            if(un.getName().equals(tName)){
                                sender=tc;
                            }
                            tc++;
                        }
                        if(al!=-1){

                            if(tName.equals(rec)){
                                writers.get(sender).println("000," + users.get(sender).getName() + "," + channels.get(0).getChannelName() + "," + "Talking to youself again?!");
                                
                            }else{
                            writers.get(al).println("001," + tName + "," + rec + "," + mInput.substring(oSet.length()));
                            writers.get(sender).println("001," + tName + "," + rec + "," +  mInput.substring(oSet.length()));
                            }
                        }else{
                            writers.get(sender).println("000," + users.get(sender).getName() + "," + channels.get(0).getChannelName() + "," + "User is not online!");
                        }
                    } else{
                        
                        for (PrintWriter writer : writers) {
                            writer.println("000," + name + "," + channels.get(0).getChannelName() + "," + input);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    int loc= -1;
                    for(int i =0; i< users.size();i++){
                        if(users.get(i).getName().equals(name))
                            loc = i;
                    }
                    users.remove(loc);

                    for (PrintWriter writer : writers) {
                        writer.println("TX " + name + " has left the chat!");
                        writer.println("006" + name);
                    }
                    System.out.println(name + " has left the chat!");
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}