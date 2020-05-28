import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.JTextPane;

public class BoardInsert extends JFrame {
	
	
	Board_list board_list = new Board_list();

	
	public BoardInsert() {
		getContentPane().setBackground(new Color(245, 245, 245));
		getContentPane().setForeground(Color.BLACK);
		
		
		
		setBounds(new Rectangle(300,10,600,500));
		setTitle("게시글 등록");
		setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JTextField title = new JTextField("제목을 입력해 주세요.");
		title.setFont(new Font("나눔바른고딕 Light", Font.BOLD, 15));
		title.setBounds(76,25,200,34);
		getContentPane().add(title);
		title.setColumns(10);
		
		JTextArea textArea = new JTextArea("");
		textArea.setFont(new Font("나눔바른고딕 Light", Font.BOLD, 13));
		textArea.setLineWrap(true);
        textArea.setRows(7);
        textArea.setBounds(76, 71, 466, 205);
        getContentPane().add(textArea);

        JButton btnWrite = new JButton("등록");
        btnWrite.setFont(new Font("나눔바른고딕 Light", Font.PLAIN, 15));
        btnWrite.setBounds(467,288,75,30);
        btnWrite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String titles =title.getText();
				String txtarea =textArea.getText();
				String username =Client_App.current_id;
				System.out.println(username);
				board_list.createBoard(titles,txtarea,username);
				
				setVisible(false);
				Board board=new Board();
			}
		});
        
        getContentPane().add(btnWrite);
        
        
        JButton btnClose = new JButton("취소");
        btnClose.setFont(new Font("나눔바른고딕 Light", Font.PLAIN, 15));
        btnClose.setBounds(389,288,75,30);
        btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Board board=new Board();
			}
		});
        getContentPane().add(btnClose);
        
		setVisible(true);
		
		
	}
}
