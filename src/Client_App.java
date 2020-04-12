import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
 //test
public class Client_App {
    private JFrame frame;
    private JPanel welcomePanel;
    private JPanel profilePanel;
    private JPanel tablePanel;
    private JPanel homePanel;
    static String current_id = "";
    private int loginResult=2;
    private String firstCheck ="1";
    private JTextField textID = new JTextField(10);
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
//        customer.deleteTable();
//        customer.createTable();
        frame = new JFrame();
        frame.setBounds(100, 100, 1077, 706);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		ImagePanel welcomePanel = new ImagePanel(new ImageIcon("./img/background.jpg").getImage());
        
//		JPanel profilePanel, tablePanel, homePanel;

        //로그인화면(첫화면) panel
        
        frame.getContentPane().add(welcomePanel);

        //************************************* profile 등록 화면 ******************************************
        profilePanel = new JPanel();
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBounds(0,0, welcomePanel.getWidth(),welcomePanel.getHeight());
        frame.getContentPane().add(profilePanel);
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
//=================================================================================================
        
//=================================================================================================        
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
        
        //************************************* profile 등록 성공시 첫 로그인 메인 화면 ******************************************

        homePanel = new JPanel();
		homePanel.setBackground(Color.WHITE);
		homePanel.setBounds(0, 0, 1059, 659);
		frame.getContentPane().add(homePanel);
		homePanel.setLayout(null);
		homePanel.setVisible(false);
		
		JLabel main = new JLabel("Welcome main ");
		main.setVerticalAlignment(SwingConstants.TOP);
        main.setBounds(484,36,167,29);
        main.setFont(new Font("Lato",Font.BOLD,20));
        homePanel.add(main);
        
        JButton btnNewButton = new JButton("채팅");
        btnNewButton.setBounds(46, 157, 297, 67);
        homePanel.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Client();
                new Server();
            }
        });
        
        JButton btnNewButton_1 = new JButton("게시판");
        btnNewButton_1.setBounds(46, 272, 297, 67);
        homePanel.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("");
        btnNewButton_2.setBounds(46, 389, 297, 67);
        homePanel.add(btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton("");
        btnNewButton_3.setBounds(46, 506, 297, 67);
        homePanel.add(btnNewButton_3); 
       
        //************************************* 관리자용 TABLE 화면 ******************************************
        tablePanel = new JPanel();
        tablePanel.setBounds(0,0,welcomePanel.getWidth(),welcomePanel.getHeight());
        String[][] data = customer.getCustomers();
        String[] headers = new String[]{"ID","UserName","Password","Name","Phone","Gender","Age","Note"};
        JTable table = new JTable(data,headers);
//        table.setModel(new DefaultTableModel(data,headers));
        table.setBounds(0,300,800,400);
        table.setRowHeight(30);
        table.setFont(new Font("Sanserif",Font.BOLD,15));
        table.setAlignmentX(0);
        table.setSize(800,400);
        //사이즈를 정했지만 안정해지는경우도있으므로 setPreferredScrollableViewportSize 로 두번크기설정
        table.setPreferredScrollableViewportSize(new Dimension(800,400));
        //JScrollPane < 스크롤이가능한 컴포넌트로 추가한다.
        tablePanel.add(new JScrollPane(table));
        // ************************************* create 패널 ******************************************
        JButton createBtn = new JButton("Create");
        createBtn.setBounds(500,600,150,40);
        createBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel createPanel = new JPanel();
                createPanel.setBounds(0,0,welcomePanel.getWidth(),welcomePanel.getHeight());
                createPanel.setBackground(Color.WHITE);
                createPanel.setLayout(null);


                JLabel createMain = new JLabel("Welcome createPanel");
                createMain.setBounds(360,50,300,40);
                createMain.setFont(new Font("Lato",Font.BOLD,20));
                createPanel.add(createMain);	
                //관리자가 처음 만들어줄 username
                JLabel username = new JLabel("userName");
                username.setFont(new Font("Lato",Font.BOLD,20));
                username.setBounds(100,150,110,40);

                JTextField textUserName = new JTextField(10);
                textUserName.setBounds(220,150,140,40);
                createPanel.add(username);
                createPanel.add(textUserName);
                //관리자가 처음 만들어줄 password
                JLabel password = new JLabel("password");
                password.setFont(new Font("Late",Font.BOLD,20));
                password.setBounds(100,250,110,40);
                
                JPasswordField textPassword = new JPasswordField(10);
                textPassword.setBounds(220,250,140,40);
                createPanel.add(password);
                createPanel.add(textPassword);
                
                //username 과 password 저장 버튼 생성 
                JButton saveBtn =new JButton("SAVE");
                saveBtn.setBounds(350,400,150,40);
                saveBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String Username = textUserName.getText();
						String password="";
//						String name="";
//						String phone="";
//						String gender="";
//						String age="";
//						String note="";
						char[] secret_pw = textPassword.getPassword();
						for (char cha : secret_pw) {
								Character.toString(cha);  //cha에 저장된 값 String으로 변환
								
								//pw 에 저장하기, pw 에 값이 비어있으면 저장, 값이 있으면 이어서 저장하는 삼항연산자
								password += (password.equals("")) ? ""+cha+"" : ""+cha+"";			
						}
						Boolean flag = customer.createCustomer(Username,password);
//						Boolean flag = customer.createCustomer(Username,password,name, phone, gender, age, note);
						if(flag==true){
							
							//실행시 바로 테이블이 실시간으로 보이게끔
							table.setModel(new DefaultTableModel(customer.getCustomers(),headers));

			                TableColumnModel columnModels = table.getColumnModel();
			                columnModels.getColumn(0).setPreferredWidth(10);
			                columnModels.getColumn(1).setPreferredWidth(100);
			                columnModels.getColumn(3).setPreferredWidth(50);
			                columnModels.getColumn(4).setPreferredWidth(10);

			                tablePanel.repaint();
			                frame.repaint();
			                frame.validate();
			                
		                    createPanel.setVisible(false);
		                    tablePanel.setVisible(true);
		                }
		                else{
		                }
		            }
					
				});
                
                createPanel.add(saveBtn);
                
                tablePanel.setVisible(false);
                createPanel.setVisible(true);
                
                frame.getContentPane().add(createPanel);
			}
		});
        
        
        // ************************************* update 패널 ******************************************
        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(500,700,150,40);
        updateBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row1=table.getSelectedRow();
                Object id= table.getValueAt(row1, 0);
                Object userName= table.getValueAt(row1, 1);
                Object Name =table.getValueAt(row1, 3);
                Object Phone =table.getValueAt(row1, 4);
                Object Gender =table.getValueAt(row1, 5);
                Object Age =table.getValueAt(row1, 6);
                Object Note =table.getValueAt(row1, 7);

                JPanel updatePanel = new JPanel();
                updatePanel.setBounds(0,0,welcomePanel.getWidth(),welcomePanel.getHeight());
                updatePanel.setBackground(Color.WHITE);
                updatePanel.setLayout(null);


                JLabel updateMain = new JLabel("Welcome updatePanel");
                updateMain.setBounds(360,50,300,40);
                updateMain.setFont(new Font("Lato",Font.BOLD,20));
                updatePanel.add(updateMain);

                JLabel name = new JLabel("Name");
                name.setFont(new Font("Lato",Font.BOLD,20));
                name.setBounds(100,150,85,40);

                JTextField textName = new JTextField(10);
                textName.setBounds(200,150,140,40);
                textName.setText((String) Name);
                updatePanel.add(name);
                updatePanel.add(textName);

                JLabel phone = new JLabel("Phone");
                phone.setFont(new Font("Lato",Font.BOLD,20));
                phone.setBounds(100,250,85,40);

                JTextField textPhone = new JTextField(10);
                textPhone.setBounds(200,250,140,40);
                textPhone.setText((String)Phone);
                updatePanel.add(phone);
                updatePanel.add(textPhone);

                JLabel age = new JLabel("Age");
                age.setFont(new Font("Lato",Font.BOLD,20));
                age.setBounds(100,350,85,40);

                JTextField textAge = new JTextField(2);
                textAge.setBounds(200,350,140,40);
                textAge.setText((String)Age);
                updatePanel.add(age);
                updatePanel.add(textAge);

                JLabel gender = new JLabel("Gender");
                gender.setFont(new Font("Lato",Font.BOLD,20));
                gender.setBounds(100,550,85,40);

                JComboBox comboBoxGender = new JComboBox(new String[]{"Male","Female"});
                comboBoxGender.setBounds(200,550,140,40);
                updatePanel.add(gender);
                updatePanel.add(comboBoxGender);

                JLabel note = new JLabel("Note");
                note.setFont(new Font("Lato",Font.BOLD,20));
                note.setBounds(400,150,85,40);

                JTextArea textNote = new JTextArea();
                textNote.setBounds(500,150,160,160);
                textNote.setBorder(BorderFactory.createLineBorder(Color.black,1));
                textNote.setText((String)Note);
                updatePanel.add(note);
                updatePanel.add(textNote);

                JButton updateSubmitBtn =new JButton("UpdateComplete");
                updateSubmitBtn.setBounds(500,400,150,40);
                updateSubmitBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String nameText = textName.getText();
                        String ageText = textAge.getText();
                        String phoneText = textPhone.getText();
                        String genderText = comboBoxGender.getSelectedItem().toString();
                        String noteText = textNote.getText();
                        Boolean flag = customer.updateCustomer(userName,nameText,phoneText,genderText,ageText,noteText,firstCheck);
                        if(flag==true){
                            table.setModel(new DefaultTableModel(customer.getCustomers(),headers));

                            TableColumnModel columnModels = table.getColumnModel();
                            columnModels.getColumn(0).setPreferredWidth(10);
                            columnModels.getColumn(1).setPreferredWidth(100);
                            columnModels.getColumn(3).setPreferredWidth(50);
                            columnModels.getColumn(4).setPreferredWidth(10);

                            tablePanel.repaint();
                            frame.repaint();
                            frame.validate();

                            updatePanel.setVisible(false);
                            tablePanel.setVisible(true);


                        }
                        else{
                        }

                    }
                });

                updatePanel.add(updateSubmitBtn);
                tablePanel.setVisible(false);
                updatePanel.setVisible(true);

                frame.getContentPane().add(updatePanel);

            }
        });
        //*************************************************************************************************
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(500,400,150,40);
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row1= table.getSelectedRow();
                Object deleteId=table.getValueAt(row1,0);
                Object deleteUsername=table.getValueAt(row1,1);
                customer.deleteCustomer(deleteId,deleteUsername);
                
                table.setModel(new DefaultTableModel(customer.getCustomers(),headers));

                TableColumnModel columnModels = table.getColumnModel();
                columnModels.getColumn(0).setPreferredWidth(10);
                columnModels.getColumn(1).setPreferredWidth(100);
                columnModels.getColumn(3).setPreferredWidth(50);
                columnModels.getColumn(4).setPreferredWidth(10);

                tablePanel.repaint();
                frame.repaint();
                frame.validate();
            }
        });
        tablePanel.add(createBtn);
        tablePanel.add(updateBtn);
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
        // 0번열 (name) 은 setPreferredWidth(100)은 100보다 사이즈가 더 커질경우 자동으로 테이블의 크기를 조절
        TableColumnModel columnModels = table.getColumnModel();
        columnModels.getColumn(0).setPreferredWidth(10);
        columnModels.getColumn(1).setPreferredWidth(100);
        columnModels.getColumn(3).setPreferredWidth(50);
        columnModels.getColumn(4).setPreferredWidth(10);
        tablePanel.setVisible(false);
//        ===============================================================================================================
        
        //submit(제출 ) 버튼 생성 및 action 
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
                String userName = textID.getText();
                Boolean flag = customer.updateCustomer(userName, nameText, phoneText,genderText,ageText,  noteText,firstCheck);
				if(flag==true) {
					profilePanel.setVisible(false);
					homePanel.setVisible(true);
					 table.setModel(new DefaultTableModel(customer.getCustomers(),headers));

		                TableColumnModel columnModels = table.getColumnModel();
		                columnModels.getColumn(0).setPreferredWidth(10);
		                columnModels.getColumn(1).setPreferredWidth(100);
		                columnModels.getColumn(3).setPreferredWidth(50);
		                columnModels.getColumn(4).setPreferredWidth(10);

		                tablePanel.repaint();
		                frame.repaint();
		                frame.validate();
				}else {
					
				}
			}
		});
        profilePanel.add(submitBtn);
/*
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
                    homePanel.setVisible(true);
                }
                else{
                }
            }
        });
        
        profilePanel.add(submitBtn);
*/
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
       
        textID.setBounds(400,418,160,25);
        
        //로그인화면 PW textField
        JPasswordField textPW = new JPasswordField(10);
        textPW.setBounds(400,463,160,25);
        //로그인화면 Login Button
        JButton logBtn =  new JButton("LogIn");
        logBtn.setIcon(new ImageIcon("./img/loginbtn.png"));
        logBtn.setPressedIcon(new ImageIcon("./img/loginbtn_click.png"));
        logBtn.setBounds(380,523,170,50);
        logBtn.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        	
        		//관리자로 접근
        		if(textID.getText().equals("admin")&&Arrays.equals(textPW.getPassword(),"admin".toCharArray())){
                    current_id = "admin";
                    System.out.println("administrator");
                    welcomePanel.setVisible(false);
                    tablePanel.setVisible(true);

                }
                
        		//유저 ID PW로 로그인 방법
        		String userName = textID.getText();
        		String password="";
        	
        		char [] pwd = textPW.getPassword();
        		for (char cha : pwd) {
					Character.toString(cha);  //cha에 저장된 값 String으로 변환
					
					//pw 에 저장하기, pw 에 값이 비어있으면 저장, 값이 있으면 이어서 저장하는 삼항연산자
					password += (password.equals("")) ? ""+cha+"" : ""+cha+"";			
        			}
        			loginResult= customer.LoginCustomer(userName, password,loginResult);
        		if(loginResult ==0) { //로그인 성공 
        			welcomePanel.setVisible(false);
        			profilePanel.setVisible(true);
        		}else if(loginResult ==1) {
        			welcomePanel.setVisible(false);
        			homePanel.setVisible(true);
        		}else if(textID.getText().equals("admin")&&Arrays.equals(textPW.getPassword(),"admin".toCharArray())){
                    current_id = "admin";
                    System.out.println("administrator");
                    welcomePanel.setVisible(false);
                    tablePanel.setVisible(true);

                }else{  //로그인 실패 
        			JOptionPane.showMessageDialog(null, "로그인 실패");
        		}
        	}
        	/*
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textID.getText().equals("1")&&Arrays.equals(textPW.getPassword(),"1".toCharArray())){
                    current_id = "1";
                    System.out.println("Account with no profile");
                    welcomePanel.setVisible(false);
                    profilePanel.setVisible(true);

                }

                else if(textID.getText().equals("2")&&Arrays.equals(textPW.getPassword(),"2".toCharArray())){
                    current_id = "2";
                    System.out.println("Account with profile");
                    welcomePanel.setVisible(false);
                    homePanel.setVisible(true);

                }
                else if(textID.getText().equals("3")&&Arrays.equals(textPW.getPassword(),"3".toCharArray())){
                    current_id = "3";
                    System.out.println("Account with profile");
                    welcomePanel.setVisible(false);
                    homePanel.setVisible(true);

                }
                else if(textID.getText().equals("admin")&&Arrays.equals(textPW.getPassword(),"admin".toCharArray())){
                    current_id = "admin";
                    System.out.println("administrator");
                    welcomePanel.setVisible(false);
                    tablePanel.setVisible(true);

                }
                else {
                    JOptionPane.showMessageDialog(null,"login fail");
                }
            }*/
        });
        welcomePanel.add(idLb);
        welcomePanel.add(textID);
        welcomePanel.add(pwLb);
        welcomePanel.add(textPW);
        welcomePanel.add(logBtn);
   //****************************************************************************************
        frame.setJMenuBar(menuBar(welcomePanel));
        frame.setSize(welcomePanel.getWidth(),welcomePanel.getHeight());
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    
    
    //************************************* 상단 메뉴바 ******************************************
    public JMenuBar menuBar(JPanel panel) {
    	

//		ImagePanel welcomePanel = new ImagePanel(new ImageIcon("./img/background.jpg").getImage());
//		frame.getContentPane().add(welcomePanel);

    	
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("About");
        
        bar.add(fileMenu);
        bar.add(aboutMenu);
    
        
        JMenuItem openFile = new JMenuItem("Open");
        JMenuItem exit = new JMenuItem("Exit"); 
        JMenuItem logout = new JMenuItem("logout");
        
        fileMenu.add(openFile);
        fileMenu.add(logout);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				profilePanel.setVisible(false);
				homePanel.setVisible(false);
				tablePanel.setVisible(false);
				panel.setVisible(true);
				
				
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
