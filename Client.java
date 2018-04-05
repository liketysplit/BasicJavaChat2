import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.*;
import java.net.Socket;
import javax.swing.border.Border;
import javax.swing.event.*;




public class Client {

    //To read and write data to and from the server
    public BufferedReader in;
    public PrintWriter out;

    //Used for sending messages to the server
    private String name = "Chat";

    //Stores username
    private String username = "";

    //Main Window
    private JFrame window = new JFrame(name);

    //Text Field where user input data goes 
    JTextField tField = new JTextField(75);

    //Text Area where output data goes
    JTextArea tArea = new JTextArea(30, 75);

    //Top Bar Label
    JLabel chatLabel = new JLabel("General");

    //Buttons to set the theme
    JButton lightB = new JButton("Light");
    JButton darkB = new JButton("Dark");

    //Panels and Panes to be added to GridBags
    JPanel jpMain = new JPanel();
    JPanel jpLeft = new JPanel();
    JPanel jpTopLeft = new JPanel();
    JPanel jpRight = new JPanel();
    JPanel jpTop = new JPanel();
    JPanel jpBottom = new JPanel();
    JPanel jpbTop = new JPanel();
    JPanel jpbCenter = new JPanel();
    JPanel jpbBottom = new JPanel();
    JScrollPane jpCenter = new JScrollPane(tArea);

    //Colors to be used for Default Style
    Color g1 = Color.decode("#353940"); //Top & Message Bar BG
    Color g2 = Color.decode("#35393f"); //Center
    Color g3 = Color.decode("#303136"); //Left & Right & Center Scroll
    Color g4 = Color.decode("#303136"); //Scroll Bar
    Color g5 = Color.decode("#484b53"); //Message Bar
    Color g6 = Color.decode("#42464d"); //Highlight & Font
    Color g7 = Color.decode("#0c0c0c");

    //Grid bags to be put in the window
    GridBagConstraints c = new GridBagConstraints();
    GridBagConstraints c2 = new GridBagConstraints();
    GridBagConstraints c3 = new GridBagConstraints();
    GridBagConstraints c4 = new GridBagConstraints();
    GridBagConstraints c5 = new GridBagConstraints();

    //Models that can be used and updated without repainting the GUI
    DefaultListModel listModel1 = new DefaultListModel();
    DefaultListModel listModel2 = new DefaultListModel();  
    
    //JLists Containing Models
    JList Userlist = new JList(listModel1);
    JList Grouplist = new JList(listModel2); 
    
    String history = "";

    //No Arg Consttructor
    public Client() {
        setupAL();
        buildGUI();


    }

    //Gets the useranme from the User, Sets the local username, and Returns the Username to the server via messageManager
    private String getUserName() {
        String temp = JOptionPane.showInputDialog(window,"Username:","Login-Dialog",JOptionPane.PLAIN_MESSAGE);
        name += " - " + temp;
        window.setTitle(name);
        username =temp;
        return temp;
    }

    //Proccesses Data From the Server
    private void run() throws IOException {

        Socket socket = new Socket("localhost", 3000);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            messageManager(line);



        }

    }

    //Calls all sub GUI Methods
    public void buildGUI(){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        
        jpMain.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jpMain.setPreferredSize(new Dimension(800,600));
        jpMain.setLayout(new GridBagLayout());

        addGroup("General");
        c.fill = GridBagConstraints.BOTH;
        c3.fill = GridBagConstraints.BOTH;
        c4.fill = GridBagConstraints.BOTH;

        buildGroupPanel();
        
        buildTitleBar();
        
        buildIconPanel();

        buildUsersPanel();
        
        buildUserInputPanel();
        
        buildChatBox();

        window.add(jpMain);
        window.pack();
    }

    //Builds the left bar of the GUI
    public void buildGroupPanel(){
        c.weighty = .5;
        c.weightx = 2;
        c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        jpLeft.setBackground(g3);
        jpLeft.setLayout(new GridBagLayout());
            c3.insets = new Insets(0, 5, 0, 5);
            c3.weighty = 1;
            c3.weightx = 1;
            c3.anchor = GridBagConstraints.CENTER;
            c3.gridx = 0;
            c3.gridy = 0;
            c3.gridheight = 1;
            c3.gridwidth = 1;
        Grouplist.setSelectionInterval(0, 0);
        Grouplist.setBackground(g3);
        Grouplist.setForeground(g7);
        Grouplist.setSelectionBackground(g6);
        Grouplist.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jpLeft.add(Grouplist,c3);
        jpMain.add(jpLeft, c);
    }

    //Builds the right bar of the GUI
    public void buildUsersPanel(){
        c.weighty = .5;
        c.weightx = 2;
        c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        jpRight.setBackground(g3);
        jpRight.setLayout(new GridBagLayout());
            c4.insets = new Insets(0, 5, 0, 5);
            c4.weighty = 1;
            c4.weightx = 1;
            c4.anchor = GridBagConstraints.CENTER;
            c4.gridx = 0;
            c4.gridy = 0;
            c4.gridheight = 1;
            c4.gridwidth = 1;
        Userlist.setBackground(g3);
        Userlist.setForeground(g7);
        Userlist.setSelectionBackground(g6);
        Userlist.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jpRight.add(Userlist,c4);
        jpMain.add(jpRight, c);
    }

    //Builds the bottom bar of the GUI
    public void buildUserInputPanel(){
        c.weighty = 1;
        c.weightx = 4;
        c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 1;
        c.gridy = 2;
        c.gridheight = 1;
        c.gridwidth = 1;
        jpBottom.setLayout(new GridBagLayout());
        jpBottom.setBackground(g1);
            c2.weighty = 1;
            c2.weightx = 1;
            c2.anchor = GridBagConstraints.CENTER;
            c2.gridx = 0;
            c2.gridy = 0;
            c2.gridheight = 1;
            c2.gridwidth = 10;
            jpbTop.setBackground(g1);
        jpBottom.add(jpbTop,c2);
            c2.weighty = 1;
            c2.weightx = 1;
            c2.anchor = GridBagConstraints.SOUTH;
            c2.gridx = 0;
            c2.gridy = 3;
            c2.gridheight = 1;
            c2.gridwidth = 10;
            jpbBottom.setBackground(g1);
        jpBottom.add(jpbBottom,c2);
            c2.weighty = 4;
            c2.weightx = 10;
            c2.anchor = GridBagConstraints.SOUTH;
            c2.gridx = 1;
            c2.gridy = 1;
            c2.gridheight = 2;
            c2.gridwidth = 8;
            c2.fill = GridBagConstraints.HORIZONTAL;
            c2.insets = new Insets(0, 5, 0, 5);
            jpbCenter.setLayout(new BorderLayout());
            jpbCenter.add(tField);
        jpBottom.add(jpbCenter, c2);
        tField.setSize(75, 30);
        tField.setBackground(g5);
        tField.setForeground(g7);
        tField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        jpMain.add(jpBottom, c);
    }

    //Builds the top left corner of the GUI
    public void buildIconPanel(){
        c.weighty = .5;
        c.weightx = 2;
        c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        jpTopLeft.setBackground(g4);

        jpTopLeft.add(lightB);
        jpTopLeft.add(darkB);

        jpMain.add(jpTopLeft, c);
    }
    //Builds the top bar of the GUI
    public void buildTitleBar(){
        c.weighty = .5;
        c.weightx = 4;
        c.anchor = GridBagConstraints.NORTH;
		c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 3;
        jpTop.setBackground(g1);
        jpTop.setLayout(new GridBagLayout());
            c5.insets = new Insets(0, 5, 0, 5);
            c5.weighty = 1;
            c5.weightx = 1;
            c5.anchor = GridBagConstraints.SOUTHWEST;
            c5.gridx = 0;
            c5.gridy = 0;
            c5.gridheight = 1;
            c5.gridwidth = 1;
        chatLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        jpTop.add(chatLabel, c5);
        jpMain.add(jpTop, c);
    }

    //Builds the Central Chat Box
    public void buildChatBox(){
        c.weighty = 8;
        c.weightx = 8;
        c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        jpCenter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jpCenter.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jpCenter.getVerticalScrollBar().setForeground(g4);
        jpCenter.getVerticalScrollBar().setBackground(g3);
        tArea.setBackground(g2);
        tArea.setForeground(g7);
        jpCenter.setBackground(g2);
        Border tbBorder = BorderFactory.createMatteBorder(2, 0, 2, 0, g3);
        jpCenter.setBorder(tbBorder);
        jpMain.add(jpCenter, c);
    }

    //Creates the Action Listeners 
    public void setupAL(){
        //Dark Button Action Listener
        darkB.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                g1 = Color.decode("#353940"); //Top & Message Bar BG
                g2 = Color.decode("#35393f"); //Center
                g3 = Color.decode("#303136"); //Left & Right & Center Scroll
                g4 = Color.decode("#303136"); //Scroll Bar
                g5 = Color.decode("#484b53"); //Message Bar
                g6 = Color.decode("#42464d"); //Highlight & Font
                g7 = Color.decode("#0c0c0c");
                jpTopLeft.removeAll();
                window.getContentPane().removeAll();
                window.revalidate();
                window.repaint();
                buildGUI();
            } 
          } );
        //Light Button Action Listener
        lightB.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                g1 = Color.decode("#FDFFFF"); //Top & Message Bar BG
                g2 = Color.decode("#DDD1C7"); //Center
                g3 = Color.decode("#E8E8E8"); //Left & Right & Center Scroll
                g4 = Color.decode("#FDFFFF"); //Scroll Bar
                g5 = Color.decode("#DDD1C7"); //Message Bar
                g6 = Color.decode("#42464d"); //Highlight & Font
                g7 = Color.decode("#1C0F13");
                jpTopLeft.removeAll();
                window.getContentPane().removeAll();
                window.revalidate();
                window.repaint();
                buildGUI();
            } 
          } );
          //User Entry Action Listener
          tField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

 
                     
                if(tField.getText().startsWith("/w "))
                    out.println(username.toString() + "+" + tField.getText());
                else
                    out.println(tField.getText());
                tField.setText("");
                //Userlist.clearSelection(); TODO: Fix this
            }
        });


        //Groups or Channels Action Listener
        Grouplist.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()){
                    JList source = (JList)event.getSource();
                    String selected = source.getSelectedValue().toString();
                    chatLabel.setText(selected);
                }
            }
        });

        //Users Acgtion Listener
        Userlist.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()){
                    JList source = (JList)event.getSource();
                    String selected = source.getSelectedValue().toString();
                    tField.setText("/w " + selected);
                }
            }
        });
    }

    //Adds a group once the server sends the creation msg
    public void addGroup(String s){
        if(!listModel2.contains(s))
            listModel2.addElement(s);
        
    }

    //Adds a user once the server sends the connection msg
    public void addUser(String s){
        if(!listModel1.contains(s))
            listModel1.addElement(s);

    }

    //Removes a group onces the server says it doesn't exist
    public void removeUser(String s){
        System.out.println("Ran:"+ s);
        if(listModel1.contains(s)){
            listModel1.removeElement(s);
        }

    }

    //Removes a group onces the server says it doesn't exist
    public void removeGroup(String s){
        if(listModel2.contains(s))
            listModel2.removeElement(s);

    }

    //Method to handle all incoming messages and route them
    public void messageManager(String s){
        ArrayList<String> tempS = new ArrayList<String>();
        System.out.println(s);

        //Normal Message to All
        if(s.startsWith("000")){
            tempS = splitNormalMessage(s);
            tArea.append(tempS.get(0) + tempS.get(2) + "\n");
            tArea.setCaretPosition(tArea.getDocument().getLength());
        }

        //Private Message to User
        if(s.startsWith("001")){
            tempS = splitPrivateMessage(s);
            tArea.append(tempS.get(2) + "\n");
            tArea.setCaretPosition(tArea.getDocument().getLength());
        }

        //Group Update Message
        if(s.startsWith("002")){
            tempS = splitGroupUpdateMessage(s);
            for(String ug: tempS){
                addGroup(ug);
            }
        }

        //User Update Message
        if(s.startsWith("003")){
            tempS = splitUserUpdateMessage(s);
            for(String us: tempS){
                addUser(us);
            }
        }

        //User Login Request
        if(s.startsWith("004")){
            out.println(getUserName());
        }
        //User Login Response
        if(s.startsWith("005")){
            tField.setEditable(true);
        }

        //Remove User
        if(s.startsWith("006")){
            removeUser(s.substring(3));
        }

        //Remove Group
        if(s.startsWith("007")){
            removeGroup(s.substring(3));
        }        
        
    }

    //Normal Message Helper method of messageManager
    public ArrayList<String> splitNormalMessage(String s){
        ArrayList<String> tempAL = new ArrayList<String>();
        String tempS =s.substring(s.indexOf(",")+1);
        String user = tempS.substring(0, tempS.indexOf(","));
        tempS = tempS.substring(tempS.indexOf(",")+1);
        String channel = tempS.substring(0, tempS.indexOf(","));
        tempS = tempS.substring(tempS.indexOf(",")+1);
        String message = tempS;
        tempAL.add(user);
        tempAL.add(channel);
        tempAL.add(": " + message);
        return tempAL;
    }

    //Private Message Helper method of messageManager
    public ArrayList<String> splitPrivateMessage(String s){
        ArrayList<String> tempAL = new ArrayList<String>();
        String tempS =s.substring(s.indexOf(",")+1);
        String sender = tempS.substring(0, tempS.indexOf(","));
        tempS = tempS.substring(tempS.indexOf(",")+1);
        String reciever = tempS.substring(0, tempS.indexOf(","));
        tempS = tempS.substring(tempS.indexOf(",")+1);
        String message = tempS;
        tempAL.add(sender);
        tempAL.add(reciever);
        tempAL.add(sender + "(" + sender + "-->" + reciever + "):" + message);

        return tempAL;
    }

    //Group Message Helper method of messageManager
    public ArrayList<String> splitGroupUpdateMessage(String s){
        ArrayList<String> tempAL = new ArrayList<String>();
        String tempS = s.substring(s.indexOf(",")+1);
        String tempI = "";
        int count = tempS.length() - tempS.replace(",", "").length();
        System.out.println(count);
        for(int i = 0; i< count; i++){
            tempI = tempS.substring(0, tempS.indexOf(","));
            tempS = tempS.substring(tempS.indexOf(",")+1);
            tempAL.add(tempI);
        }
        tempI = tempS;
        tempAL.add(tempI);

        return tempAL;
    }

    //User Update Message Helper method of messageManager
    public ArrayList<String> splitUserUpdateMessage(String s){
        ArrayList<String> tempAL = new ArrayList<String>();
        String tempS = s.substring(s.indexOf(",")+1);
        String tempI = "";
        int count = tempS.length() - tempS.replace(",", "").length();
        System.out.println(count);
        for(int i = 0; i< count; i++){
            tempI = tempS.substring(0, tempS.indexOf(","));
            tempS = tempS.substring(tempS.indexOf(",")+1);
            tempAL.add(tempI);
        }
        tempI = tempS;
        tempAL.add(tempI);

        return tempAL;
    }
    

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.run();
    }
}