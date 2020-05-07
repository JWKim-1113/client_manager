import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BoardUpdate extends JFrame{

	Board_list board_list = new Board_list();
	
	public BoardUpdate(Object id, String titles, String content, Object writer) {
		
		setBounds(new Rectangle(300,10,450,280));
		setTitle("게시글 수정");
		
		getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("글제목");
		lblNewLabel.setBounds(40,10,57,15);
		getContentPane().add(lblNewLabel);
		
		JTextField title = new JTextField(titles);
		title.setBounds(90,10,200,20);
		title.setColumns(10);
		title.setBackground(Color.white);
		getContentPane().add(title);
		
		JLabel lblNewLabel_1 = new JLabel("글내용");
		lblNewLabel_1.setBounds(40,50,57,15);
		getContentPane().add(lblNewLabel_1);
		
		JTextArea textArea = new JTextArea(content);
		textArea.setLineWrap(true);
        textArea.setRows(7);
        textArea.setBackground(Color.white);
        textArea.setBounds(90, 50, 300, 69);
        getContentPane().add(textArea);
        
        JButton btnWrite = new JButton("수정완료");
        btnWrite.setBounds(100,200,116,23);
        btnWrite.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String titles =title.getText();
				String txtarea =textArea.getText();
				
				board_list.updateBoard(id,titles,txtarea,writer);
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
