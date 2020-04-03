import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
//a
//testBranch Commit test 1
// testBranch Commit test 2
// testBranch Commit test 3
public class Client_App {
    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    Client_App window = new Client_App();
                    window.frame.setVisible(true);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public Client_App(){
        initialize();
    }

    private void initialize(){
        //DB와 GUI 연결하기위한 Customer 객체생성
        Customer customer = new Customer();

        frame = new JFrame();

        //로그인화면(첫화면) panel
        ImagePanel welcomePanel = new ImagePanel(new ImageIcon("./img/background.jpg").getImage());
        frame.add(welcomePanel);

        //************************************* profile 등록 화면 ******************************************
        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBounds(0,0, welcomePanel.getWidth(),welcomePanel.getHeight());
        profilePanel.setLayout(null);
        profilePanel.setVisible(false);

        //메인패널 상단 label
        JLabel welcomeMain = new JLabel("Welcome main panel");
        welcomeMain.setBounds(360,50,300,40);
        welcomeMain.setFont(new Font("Lato",Font.BOLD,20));
        profilePanel.add(welcomeMain);

        JLabel name = new JLabel("Name");
        name.setFont(new Font("Lato",Font.BOLD,20));
        name.setBounds(100,150,85,40);
        JTextField textName = new JTextField(10);
        textName.setBounds(200,150,140,40);
        profilePanel.add(name);
        profilePanel.add(textName);

        JLabel phone = new JLabel("Phone");
        phone.setFont(new Font("Lato",Font.BOLD,20));
        phone.setBounds(100,250,85,40);
        JTextField textPhone = new JTextField(10);
        textPhone.setBounds(200,250,140,40);
        profilePanel.add(phone);
        profilePanel.add(textPhone);

        JLabel age = new JLabel("Age");
        age.setFont(new Font("Lato",Font.BOLD,20));
        age.setBounds(100,350,85,40);
        JTextField textAge = new JTextField(2);
        textAge.setBounds(200,350,140,40);
        profilePanel.add(age);
        profilePanel.add(textAge);

        JLabel birthDay = new JLabel("Birthday");
        birthDay.setFont(new Font("Lato",Font.BOLD,20));
        birthDay.setBounds(100,450,85,40);
        JTextField textBirthDay = new JTextField(8);
        textBirthDay.setBounds(200,450,140,40);
        profilePanel.add(birthDay);
        profilePanel.add(textBirthDay);

        JLabel gender = new JLabel("Gender");
        gender.setFont(new Font("Lato",Font.BOLD,20));
        gender.setBounds(100,550,85,40);
        JComboBox comboBoxGender = new JComboBox(new String[]{"Male","Female"});
        comboBoxGender.setBounds(200,550,140,40);
        profilePanel.add(gender);
        profilePanel.add(comboBoxGender);

        JLabel note = new JLabel("Note");
        note.setFont(new Font("Lato",Font.BOLD,20));
        note.setBounds(400,150,85,40);
        JTextArea textNote = new JTextArea();
        textNote.setBounds(500,150,160,160);
        textNote.setBorder(BorderFactory.createLineBorder(Color.black,1));
        profilePanel.add(note);
        profilePanel.add(textNote);


        //************************************* 관리자용 TABLE 화면 ******************************************
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(0,0,welcomePanel.getWidth(),welcomePanel.getHeight());
        String[][] data = customer.getCustomers();
        String[] headers = new String[]{"ID","Name","Phone","Gender","Age","Note"};
        JTable table = new JTable(data,headers);
        table.setBounds(0,300,800,400);
        table.setRowHeight(30);
        table.setFont(new Font("Sanserif",Font.BOLD,15));
        table.setAlignmentX(0);
        table.setSize(800,400);
        //사이즈를 정했지만 안정해지는경우도있으므로 setPreferredScrollableViewportSize 로 두번크기설정
        table.setPreferredScrollableViewportSize(new Dimension(800,400));
        //JScrollPane < 스크롤이가능한 컴포넌트로 추가한다.
        tablePanel.add(new JScrollPane(table));

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(500,400,150,40);
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row1= table.getSelectedRow();
                Object deleteName=table.getValueAt(row1,1);
                Object deletePhone=table.getValueAt(row1,2);
                customer.deleteCustomer(deleteName,deletePhone);
            }
        });
        tablePanel.add(deleteBtn);
        frame.getContentPane().add(tablePanel);

        //테이블 필터만들기 JTextField search 적는순간 적은부분있는것만남게
        JTextField search = new JTextField();
        search.setFont(new Font("Tahoma",Font.PLAIN,17));
        search.setBounds(76,13,1202,36);
        tablePanel.add(search);
        search.setColumns(10);
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String val = search.getText();
                TableRowSorter<TableModel> trs = new TableRowSorter<>(table.getModel());
                table.setRowSorter(trs);
                trs.setRowFilter(RowFilter.regexFilter(val)); // regular expression 을 통해 string 값이 정리가된다
            }
        });

        TableColumnModel columnModels = table.getColumnModel();
        columnModels.getColumn(0).setPreferredWidth(10);
        columnModels.getColumn(1).setPreferredWidth(100); // 0번열 (name) 은 setPreferredWidth(100)은 100보다 사이즈가 더 커질경우 자동으로 테이블의 크기를 조절
        columnModels.getColumn(3).setPreferredWidth(50); // 2번열 (gender) 은 50으로
        columnModels.getColumn(4).setPreferredWidth(10); // 3번열 (age) 는 10으로
        tablePanel.setVisible(false);

        // submit(제출)버튼 생성 및 action
        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(500,400,150,40);
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameText = textName.getText();
                String ageText = textAge.getText();
                String phoneText = textPhone.getText();
                String genderText = comboBoxGender.getSelectedItem().toString();
                String noteText = textNote.getText();
                Boolean flag = customer.createCustomer(nameText,phoneText,genderText,ageText,noteText);
                if(flag==true){
                    profilePanel.setVisible(false);
                }
                else{

                }
            }
        });
        profilePanel.add(submitBtn);

        frame.getContentPane().add(profilePanel);

        //************************************* LOGIN 화면 ******************************************

        //로그인화면 ID label
        JLabel idLb = new JLabel("ID :");
        idLb.setBounds(334,407,85,40);
        idLb.setFont(new Font("Lato",Font.BOLD,20));
        //로그인화면 PW label
        JLabel pwLb = new JLabel("PW :");
        pwLb.setBounds(322,455,85,40);
        pwLb.setFont(new Font("Lato",Font.BOLD,20));
        //로그인화면 ID textField
        JTextField textID = new JTextField(10);
        textID.setBounds(400,418,160,25);
        //로그인화면 PW textField
        JPasswordField textPW = new JPasswordField(10);
        textPW.setBounds(400,463,160,25);
        //로그인화면 Login Button
        JButton logBtn =  new JButton("LogIn");
        logBtn.setIcon(new ImageIcon("./img/loginbtn.jpg"));
        logBtn.setPressedIcon(new ImageIcon("./img/loginbtn_click.jpg"));
        logBtn.setBounds(380,523,175,57);
        logBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textID.getText().equals("1")&&Arrays.equals(textPW.getPassword(),"1".toCharArray())){
                    System.out.println("Login Successfully");
                    welcomePanel.setVisible(false);
                    profilePanel.setVisible(true);
                }
                else if(textID.getText().equals("admin")&&Arrays.equals(textPW.getPassword(),"admin".toCharArray())){
                    System.out.println("administrator");
                    welcomePanel.setVisible(false);
                    tablePanel.setVisible(true);

                }
                else {
                    JOptionPane.showMessageDialog(null,"login fail");
                }
            }
        });
        welcomePanel.add(idLb);
        welcomePanel.add(textID);
        welcomePanel.add(pwLb);
        welcomePanel.add(textPW);
        welcomePanel.add(logBtn);

        frame.setJMenuBar(menuBar());
        frame.setSize(welcomePanel.getWidth(),welcomePanel.getHeight());
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //************************************* 상단 메뉴바 ******************************************
    public JMenuBar menuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("About");

        bar.add(fileMenu);
        bar.add(aboutMenu);

        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        return bar;
    }
}

// 패널에 Image를 쉽게 넣기위해 생성한 ImagePanel
class ImagePanel extends JPanel{
    private Image img;
    public ImagePanel(Image img){
        this.img=img;
        setSize(new Dimension(img.getWidth(null),img.getHeight(null)));
        setPreferredSize(new Dimension(img.getWidth(null),img.getHeight(null)));
        setLayout(null);
    }
    public int getWidth(){ //이미지의 가로넓이 리턴
        return img.getWidth(null);
    }
    public int getHeight(){
        return img.getHeight(null);
    }
    public void paintComponent(Graphics g){
        g.drawImage(img,0,0,null);
    }
}
