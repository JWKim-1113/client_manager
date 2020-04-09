import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

//채팅클라이언트
public class Client extends JFrame implements ActionListener, KeyListener {

    private JPanel contentPane;

    private JTextField textField_1;
    private JTextField textField_2;

    private JTextField message_tf;
    private JButton access_btn = new JButton("접속");
    private JButton notesend_btn = new JButton("쪽지보내기");
    private JButton joinroom_btn = new JButton("채팅방참여");
    private JButton createroom_btn = new JButton("방만들기");
    private JButton send_btn = new JButton("전송");


    private JList User_list = new JList(); // 전체 접속자 list
    private JList Room_list = new JList(); // 전체 방목록 list
    private JTextArea Chat_area = new JTextArea(); // 채팅창 변수

    //네트워크를 위한 자원변수
    private Socket socket;
    private String ip = "127.0.0.1"; //"127.0.0.1" 는 자기자신
    private int port = 80;
//    private String id = "";
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;

    //그 외 변수
    Vector user_list = new Vector();
    Vector room_list = new Vector();
    StringTokenizer st;
    Boolean isConnect = false;
    private String My_Room; //내가 현재 있는 방 이름

    Client()
    {
        Main_init();
        start();
    }

    private void start(){
        access_btn.addActionListener(this);
        notesend_btn.addActionListener(this);
        joinroom_btn.addActionListener(this);
        createroom_btn.addActionListener(this);
        send_btn.addActionListener(this);
        message_tf.addKeyListener(this);
    }
    private void Main_init(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (socket != null) socket.close();
                }catch (IOException e1){}
                isConnect = false;
                dispose();

            }
        });
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,516,450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("전체접속자");
        lblNewLabel.setBounds(12,10,86,15);
        contentPane.add(lblNewLabel);
        JScrollPane scrollList = new JScrollPane(User_list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollList.setBounds(12,32,109,117);
        contentPane.add(scrollList);
        User_list.setListData(user_list);

        access_btn.setBounds(417,3,63,23);
        contentPane.add(access_btn);

        notesend_btn.setBounds(12,159,109,23);
        contentPane.add(notesend_btn);

        JLabel lblNewLabel_1 = new JLabel("채팅방목록");
        lblNewLabel_1.setBounds(12,192,97,15);
        contentPane.add(lblNewLabel_1);

        scrollList = new JScrollPane(Room_list,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollList.setBounds(12,213,109,135);
        contentPane.add(scrollList);
        Room_list.setListData(room_list);

        joinroom_btn.setBounds(12,358,109,23);
        contentPane.add(joinroom_btn);


        createroom_btn.setBounds(12,386,109,23);
        contentPane.add(createroom_btn);

        JScrollPane scrollPane = new JScrollPane(Chat_area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(133,29,344,347);
        contentPane.add(scrollPane);
        Chat_area.setEditable(false);

        message_tf = new JTextField();
        message_tf.setBounds(133,387,279,21);
        contentPane.add(message_tf);
        message_tf.setColumns(10);
        message_tf.setEnabled(false);

        send_btn.setBounds(414,386,63,23);
        contentPane.add(send_btn);
        send_btn.setEnabled(false);

        this.setVisible(true);
    }

    private void Network() {
        try {
            socket = new Socket(ip,port);

            if(socket != null) // 정상적으로 소켓이 연결되었을경우
            {
                Connection();

            }
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "연결 실패","알림",JOptionPane.ERROR_MESSAGE);

        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "연결 실패","알림",JOptionPane.ERROR_MESSAGE);

        }
    }
    private void Connection() {
        try {
            is = socket.getInputStream();
            dis = new DataInputStream(is);

            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
            isConnect=true;
            access_btn.setEnabled(false);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "연결 실패","알림",JOptionPane.ERROR_MESSAGE);
        } // Stream 설정 끝

        //처음 접속시에 ID 전송
        send_message(Client_App.current_id);

        //User_list에 사용자 추가
        user_list.add(Client_App.current_id);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        String msg = dis.readUTF(); //메세지 수신
                        System.out.println("서버로부터 수신된 메세지 : "+msg);

                        inMessage(msg);
                    } catch (IOException e) {
                        try {
                            os.close();
                            is.close();
                            dos.close();
                            dis.close();
                            socket.close();
                            JOptionPane.showMessageDialog(null, "서버와 접속 끊어짐","알림",JOptionPane.ERROR_MESSAGE);
                        }catch (IOException e1){}
                        break;

                    }
                }
            }
        });

        th.start();
    }
    private void inMessage(String str){ //서버로부터 들어오는 모든 메세지
        st = new StringTokenizer(str,"/");
        String protocol = st.nextToken();
        String message = st.nextToken();

        System.out.println("프로토콜 : "+protocol);
        System.out.println("내용 : "+message);

        if(protocol.equals("NewUser")) //새로운 접속자
        {
            user_list.add(message);
        }
        //올드유저가 날라오면
        else if(protocol.equals("OldUser")) {
            user_list.add(message);
        }
        else if(protocol.equals("Note")){

            String note = st.nextToken();

            System.out.println(message+" 사용자로부터 온 쪽지 : "+note);
            JOptionPane.showMessageDialog(null,note,message+"님으로 부터 쪽지 : ",JOptionPane.CLOSED_OPTION);

        }
        else if(protocol.equals("user_list_update")){
            // User_list.updateUI(); // 안좋음
            User_list.setListData(user_list);
        }
        else if(protocol.equals("CreateRoom")) //방 만들었을때
        {
            My_Room = message;
            message_tf.setEnabled(true);
            send_btn.setEnabled(true);
            joinroom_btn.setEnabled(false);
            createroom_btn.setEnabled(false);
        }
        else if(protocol.equals("CreateRoomFail")) //방만들기 실패했을때
        {
            JOptionPane.showMessageDialog(null, "방만들기 실패","알림",JOptionPane.ERROR_MESSAGE);
        }
        else if(protocol.equals("New_Room")) //새로운 방을 만들었을때
        {
            room_list.add(message);
            Room_list.setListData(room_list);
        }
        else if(protocol.equals("Chatting")){
            String msg = st.nextToken();

            Chat_area.append(message+" : "+msg+"\n");
        }
        else if(protocol.equals("OldRoom")){
            room_list.add(message);
        }
        else if(protocol.equals("room_list_update")){
            Room_list.setListData(room_list);
        }
        else if(protocol.equals("JoinRoom")){
            My_Room = message;
            message_tf.setEnabled(true);
            send_btn.setEnabled(true);
            joinroom_btn.setEnabled(false);
            createroom_btn.setEnabled(false);
            JOptionPane.showMessageDialog(null, "채팅방에 입장했습니다.","알림",JOptionPane.INFORMATION_MESSAGE);
        }
        else if(protocol.equals("User_out")){
            user_list.remove(message);

        }
    }
    private void send_message(String str){ //서버에게 메세지를 보내는 부분
        try {
            dos.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==access_btn)
        {
            if(!isConnect) {
                System.out.println("접속 버튼 클릭");
                Network();

            }
            else JOptionPane.showMessageDialog(null,"이미 접속 되어있습니다","알림",JOptionPane.ERROR_MESSAGE);
        }
        if(e.getSource()==notesend_btn)
        {
            if(isConnect) {
                System.out.println("쪽지 보내기 버튼 클릭");
                String user = (String) User_list.getSelectedValue();

                String note = JOptionPane.showInputDialog("보낼 메세지");
                if (note != null) {
                    send_message("Note/" + user + "/" + note);
                    //ex = Note/User2@나는 User1이야
                }
                System.out.println("받는 사람 : " + user + "| 보낼 내용 : " + note);
            }
            else JOptionPane.showMessageDialog(null,"서버 미접속 상태입니다.","알림",JOptionPane.ERROR_MESSAGE);
        }
        else if(e.getSource()==joinroom_btn)
        {
            if(isConnect) {
                String JoinRoom = (String) Room_list.getSelectedValue();
                send_message("JoinRoom/" + JoinRoom);
            }
            else JOptionPane.showMessageDialog(null,"서버 미접속 상태입니다.","알림",JOptionPane.ERROR_MESSAGE);
            System.out.println("방 참여 버튼 클릭");
        }
        else if(e.getSource()==createroom_btn)
        {
            if(isConnect) {
                String roomname = JOptionPane.showInputDialog("방 이름");
                if (roomname != null) {
                    send_message("CreateRoom/" + roomname);
                }
            }
            else JOptionPane.showMessageDialog(null,"서버 미접속 상태입니다.","알림",JOptionPane.ERROR_MESSAGE);
            System.out.println("방 만들기 버튼 클릭");

        }
        else if(e.getSource()==send_btn)
        {
            if(!message_tf.getText().equals("")) {
                send_message("Chatting/" + My_Room + "/" + message_tf.getText().trim());
                message_tf.setText("");
                message_tf.requestFocus();
            }
            //프로토콜 -> Chatting + 방이름 + 내용
            System.out.println("채팅 전송 버튼 클릭");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==10){
            if(!message_tf.getText().equals("")) {
                send_message("Chatting/" + My_Room + "/" + message_tf.getText().trim());
                message_tf.setText("");
                message_tf.requestFocus();
            }
        }
    }
}
