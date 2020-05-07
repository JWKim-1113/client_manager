import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BoardInsert extends JFrame {
	
	
	Board_list board_list = new Board_list();

	
	public BoardInsert() {
		
		
		setBounds(new Rectangle(300,10,450,280));
		setTitle("게시글 등록");
		
		getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("글제목");
		lblNewLabel.setBounds(40,10,57,15);
		getContentPane().add(lblNewLabel);
		
		JTextField title = new JTextField("글제목입니다.");
		title.setBounds(90,10,200,20);
		getContentPane().add(title);
		title.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("글내용");
		lblNewLabel_1.setBounds(40,50,57,15);
		getContentPane().add(lblNewLabel_1);
		
		JTextArea textArea = new JTextArea("글내용 글내용..");
		textArea.setLineWrap(true);
        textArea.setRows(7);
        textArea.setBounds(90, 50, 300, 69);
        getContentPane().add(textArea);

        JButton btnWrite = new JButton("작성완료");
        btnWrite.setBounds(100,200,116,23);
        btnWrite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String titles =title.getText();
				String txtarea =textArea.getText();
				String username =Client_App.current_id;
				System.out.println(username);
				board_list.createBoard(titles,txtarea,username);
				
				setVisible(false);
				
				
			}
		});
        
        getContentPane().add(btnWrite);
        
        
        JButton btnClose = new JButton("닫기");
        btnClose.setBounds(230,200,76,23);
        btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
		});
        getContentPane().add(btnClose);
        
		setVisible(true);
		
		
	}

}
