import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

//채팅서버
public class Server extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField port_tf;
    private JTextArea textArea = new JTextArea();
    private JButton start_btn = new JButton("서버 실행");
    private JButton stop_btn = new JButton("서버 중지");

    //network 자원
    private ServerSocket server_socket;
    private Socket socket;
    private int port;
    private Vector user_vc = new Vector();
    private Vector room_vc = new Vector();
    private boolean RoomCh = true;
    private StringTokenizer st;

    private void Server_start(){

        try {
            server_socket = new ServerSocket(80); // 27048포트 사용

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "이미 사용중인 포트","알림",JOptionPane.ERROR_MESSAGE);
        }

        if(server_socket!=null){
            Connection();
        }
    }

    private void Connection() {


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        textArea.append("사용자 접속 대기중\n");
                        socket = server_socket.accept(); // 사용자 접속 무한 대기
                        textArea.append("사용자 접속!\n");

                        UserInfo user = new UserInfo(socket); //사용자가 접속할때마다 Socket 객체 생성
                        user.start(); // 객체의 스레드 실행

                    } catch (IOException e) {
                        break;
                    }
                }//while
            }
            });
    th.start();
    }

    Server(){
        init(); //화면 생성 메소드
        start(); //리스너 설정 메소드
    }
    private void start(){
        start_btn.addActionListener(this);
        stop_btn.addActionListener(this);
    }

    private void init(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if(server_socket!=null)server_socket.close();
                    user_vc.removeAllElements();
                    room_vc.removeAllElements();
                }catch(IOException e1){}
                dispose();
            }
        });
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,319,370);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12,10,279,205);
        contentPane.add(scrollPane);


        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);

        JLabel lblNewLabel = new JLabel("포트 번호");
        lblNewLabel.setBounds(12,238,57,15);
        contentPane.add(lblNewLabel);

        port_tf = new JTextField();
        port_tf.setBounds(81,235,210,21);
        contentPane.add(port_tf);
        port_tf.setColumns(10);


        start_btn.setBounds(12,280,140,23);
        contentPane.add(start_btn);


        stop_btn.setBounds(151,280,140,23);
        contentPane.add(stop_btn);
        stop_btn.setEnabled(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start_btn)
        {
            System.out.println("스타트 버튼 클릭");
            Server_start(); //소켓 생성 및 사용자 대기
            start_btn.setEnabled(false);
            port_tf.setEditable(false);
            stop_btn.setEnabled(true);
        }
        else if(e.getSource() == stop_btn) {

            stop_btn.setEnabled(false);
            start_btn.setEnabled(true);
            port_tf.setEditable(true);

            try {
                server_socket.close();
                user_vc.removeAllElements();
                room_vc.removeAllElements();
            }catch(IOException e1){}
            System.out.println("서버 스탑 버튼 클릭");
        }
    }

    class UserInfo extends Thread{
        private OutputStream os;
        private InputStream is;
        private DataOutputStream dos;
        private DataInputStream dis;

        private Socket user_socket;
        private String nickName = "";

        UserInfo(Socket socket){ // 생성자 메소드
            this.user_socket = socket;

            UserNetwork();
        }
        private void UserNetwork(){
            try {
                is = user_socket.getInputStream();
                dis = new DataInputStream(is);

                os = user_socket.getOutputStream();
                dos = new DataOutputStream(os);

                nickName = dis.readUTF();
                textArea.append(nickName+" : 사용자 접속!\n");

                //기존 사용자들에게 새로운 사용자 알림
                System.out.println("현재 접속된 사용자 수 : "+user_vc.size());

                BroadCast("NewUser/"+nickName); // 기존사용자에게 자신을 알림

                //자신에게 기존 사용자를 알림
                for(int i = 0; i < user_vc.size(); i++){
                    UserInfo u = (UserInfo)user_vc.elementAt(i);
                    send_Message("OldUser/"+u.nickName); //나에게 보내는것이므로 send_Message 사용
                }

                //자신에게 기존 방 목록을 받아옴
                for(int i=0;i<room_vc.size();i++){
                    RoomInfo r = (RoomInfo)room_vc.elementAt(i);
                    send_Message("OldRoom/"+r.Room_name);
                }

                send_Message("room_list_update/ ");

                user_vc.add(this); // 사용자에게 알린 후 Vector에 자신을 추가한다.
                BroadCast("user_list_update/ ");

            }catch(IOException e){
                JOptionPane.showMessageDialog(null, "stream 설정 에러 발생","알림",JOptionPane.ERROR_MESSAGE);
            }

        } // run 메소드 끝




        //서버쪽에서 사용자에게 전송할수있는 메소드
        private void send_Message(String str) {
            try {
                dos.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){ //쓰레드에서 실행할 내용
            while(true){
                try {
                    String msg = dis.readUTF(); //메세지 수신
                    textArea.append(nickName+" : 사용자로부터 들어온 메세지 :"+msg+"\n");
                    InMessage(msg);
                } catch (IOException e) {
                    textArea.append(nickName+" : 사용자 접속 끊어짐\n");
                    try {
                        dos.close();
                        dis.close();
                        user_socket.close();
                        user_vc.remove(this);
                        BroadCast("User_out/" + nickName);
                        BroadCast("user_list_update/ ");
                    }catch(IOException e1){}
                    break;
                }

            }
        } //run

        private void InMessage(String str){ // 클라이언트로부터 들어오는 메세지 처리
            st = new StringTokenizer(str,"/");
            String protocol = st.nextToken();
            String message = st.nextToken();

            System.out.println("프로토콜 : "+protocol);
            System.out.println("메세지 : "+message);

            if(protocol.equals("Note")){

                //protocol = Note
                //message = user

                String note = st.nextToken();

                System.out.println("수신자 : "+message);
                System.out.println("메세지 : "+note);

                //벡터에서 해당 사용자를 찾아서 메세지 전송
                for(int i=0; i<user_vc.size();i++){
                    UserInfo u = (UserInfo)user_vc.elementAt(i);
                    //만약 닉네임이 사용자가 찾는 수신자와 같으면
                    if(u.nickName.equals(message)){
                        u.send_Message("Note/"+nickName+"/"+note);
                        // ex) Note/User1@~~~
                    }
                }
            }
            else if(protocol.equals("CreateRoom")){
                //1. 현재 같은 방이 존재 하는지 확인한다.
                for(int i=0;i<room_vc.size();i++){
                    RoomInfo r = (RoomInfo)room_vc.elementAt(i);

                    if(r.Room_name.equals(message)) { //만들고자 하는 방이 이미 존재할때
                        send_Message("CreateRoomFail/ok");
                        RoomCh=false;
                        break;
                    }

                } //for

                if(RoomCh) { //방을 만들수 있을때
                    RoomInfo new_room = new RoomInfo(message,this);
                    room_vc.add(new_room); // 전체 방 벡터에 방을 추가
                    send_Message("CreateRoom/"+message);

                    BroadCast("New_Room/"+message);
                }
                RoomCh=true;
            }//else if

            else if(protocol.equals("Chatting")){
                String msg = st.nextToken();

                for(int i=0;i<room_vc.size();i++){
                    RoomInfo r = (RoomInfo)room_vc.elementAt(i);

                    if(r.Room_name.equals(message)){ //해당 방을 찾았을 때
                        r.BroadCast_Room("Chatting/"+nickName+"/"+msg);
                    }
                }
            }
            else if(protocol.equals("JoinRoom")){
                for(int i=0;i<room_vc.size();i++){
                    RoomInfo r = (RoomInfo)room_vc.elementAt(i);
                    if(r.Room_name.equals(message)){
                        //새로운 사용자를 알린다
                        r.BroadCast_Room("Chatting/알림/******  "+nickName+"님이 입장하셨습니다  ******");
                        //사용자 추가
                        r.Add_User(this);
                        send_Message("JoinRoom/"+message);
                    }
                }
            }

        }

        private void BroadCast(String str){
            //벡터에 사용자들이 객체로 들어있으면 벡터에서 오브젝트형태를 하나 꺼내서 UserInfo 형태로 형변환시키고
            //그렇게되면 u라는 객체를 통해서 send_Message 메소드 호출가능함
            for(int i=0; i<user_vc.size();i++){
                // i 번째에 있는것을 꺼내와서 메세지를 전송
                UserInfo u = (UserInfo)user_vc.elementAt(i);
                u.send_Message(str); // 프로토콜부분 -> NewUser/
            }
        }
    } //UserInfo class

    class RoomInfo
    {
        private String Room_name;
        private Vector Room_user_vc = new Vector();

        RoomInfo(String str, UserInfo u){
            this.Room_name = str;
            this.Room_user_vc.add(u);
        }

        public void BroadCast_Room(String str){ //현재 방의 모든 사람에게 알린다
            for(int i=0; i<Room_user_vc.size();i++){
                UserInfo u = (UserInfo)Room_user_vc.elementAt(i);

                u.send_Message(str);
            }
        }
        private void Add_User(UserInfo u){
            this.Room_user_vc.add(u);
        }
    }
}
