import java.net.*;
import java.io.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;




class MyServer extends JFrame{
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font  font = new Font("Roboto",Font.PLAIN,25);

    
   // constructor
    public MyServer(){
        try{
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accepet connection.");
            System.out.println("waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
           // startWriting();


        }catch(Exception e){
            e.printStackTrace();
        }
        

    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
                System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode() == 10){
                   // System.out.println("You have pressed enter button");
                   String contentToSend = messageInput.getText();
                   messageArea.append("Me : "+contentToSend+"\n");
                   out.println(contentToSend);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();
                }

            }

        });
    }

     // creating GUI
     private void createGUI(){
        this.setTitle("Server Messager");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);  // set location to the center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // after clicking cross button it will close.

        // coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false); // we cannot edit after sending mssg
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);


        // set the layout of frame
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);


        this.setVisible(true);

    }
    

    // start reading method
    public void startReading(){
        // thread -- Read karke deta rahega
        Runnable r1 = ()->{
            System.out.println("reader started..");

            try{

                while(true){
                
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat.");
                        JOptionPane.showMessageDialog(this,"Client Terminated the chat.");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    //System.out.println("Client : "+msg);
                     messageArea.append("Client : "+ msg+"\n");
                
                }
            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is closed.");
            }
        };
        new Thread(r1).start();
    }
    
    // start writing method
    public void startWriting(){
        // thread -- data user se lega and then send karega client ko
        Runnable r2 = ()->{
            System.out.println("Writer started...");

            try{
                while(true && !socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }


                }
                System.out.println("Connection is closed.");

            }catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection closed.");
            }
        };

        new Thread(r2).start();
    }

    // main class
    public static void main(String[] args) {
        System.out.println("This is server..");
        new MyServer();
    }
}