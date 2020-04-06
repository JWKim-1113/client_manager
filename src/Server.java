import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    private void Server_start(){

        try {
            server_socket = new ServerSocket(12345); // 12345포트 사용

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(server_socket!=null){
            Connection();
        }
    }

    private void Connection(){

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    textArea.append("사용자 접속 대기중\n");
                    socket = server_socket.accept(); // 사용자 접속 무한 대기
                    textArea.append("사용자 접속!!!\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,319,370);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12,10,279,205);
        contentPane.add(scrollPane);


        scrollPane.setViewportView(textArea);

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

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start_btn)
        {
            System.out.println("스타트 버튼 클릭");
            Server_start(); //소켓 생성 및 사용자 대기
        }
        else if(e.getSource() == stop_btn)
        {
            System.out.println("서버 스탑 버튼 클릭");
        }
    }
}
