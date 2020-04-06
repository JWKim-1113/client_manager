import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//채팅클라이언트
public class Client extends JFrame implements ActionListener {

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
    private String ip = "127.0.0.1"; // 자기자신
    private int port = 12345;

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
    }
    private void Main_init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,516,450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("전체접속자");
        lblNewLabel.setBounds(12,10,86,15);
        contentPane.add(lblNewLabel);

        User_list.setBounds(12,32,109,117);
        contentPane.add(User_list);

        access_btn.setBounds(417,3,63,23);
        contentPane.add(access_btn);

        notesend_btn.setBounds(12,159,109,23);
        contentPane.add(notesend_btn);

        JLabel lblNewLabel_1 = new JLabel("채팅방목록");
        lblNewLabel_1.setBounds(12,192,97,15);
        contentPane.add(lblNewLabel_1);


        Room_list.setBounds(12,213,109,135);
        contentPane.add(Room_list);


        joinroom_btn.setBounds(12,358,109,23);
        contentPane.add(joinroom_btn);


        createroom_btn.setBounds(12,386,109,23);
        contentPane.add(createroom_btn);


        Chat_area.setBounds(133,29,344,347);
        contentPane.add(Chat_area);

        message_tf = new JTextField();
        message_tf.setBounds(133,387,279,21);
        contentPane.add(message_tf);
        message_tf.setColumns(10);

        send_btn.setBounds(414,386,63,23);
        contentPane.add(send_btn);

        this.setVisible(true);
    }

    private void Network(){
        try {
            socket = new Socket(ip,port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==access_btn)
        {
            System.out.println("접속 버튼 클릭");
            Network();
        }
        if(e.getSource()==notesend_btn)
        {
            System.out.println("쪽지 보내기 버튼 클릭");
        }
        else if(e.getSource()==joinroom_btn)
        {
            System.out.println("방 참여 버튼 클릭");
        }
        else if(e.getSource()==createroom_btn)
        {
            System.out.println("방 만들기 버튼 클릭");
        }
        else if(e.getSource()==send_btn)
        {
            System.out.println("채팅 전송 버튼 클릭");
        }
    }
}
