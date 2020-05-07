import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Board extends JFrame {
	//Board Main 화면
	private static JPanel boardPane;
	
	private JTextField searchText;
	private JButton searchBoardBtn;
	private JButton createBoardBtn;
	
	public Board() {
		initialize();
	}
	
	
	private void initialize() {
		Board_list board_list = new Board_list();
//		board_list.deleteBoardTable();
//		board_list.createBoardTable();
		setBounds(300,100,600,330);
		setTitle("게시판");
		boardPane = new JPanel();
		
		boardPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(boardPane);
//		boardPane.setLayout(null);
		
//		JLabel searchLabel = new JLabel("검색조건");
//		searchLabel.setBounds(70,10,86,15);
//		boardPane.add(searchLabel);
//		
//		JComboBox comboBox = new JComboBox();
//		
//		comboBox.setModel(new DefaultComboBoxModel(new String[] {"title", "content","writer"}));
//		comboBox.setBounds(130,10,74,21);
//		boardPane.add(comboBox);
//		
		
		
		createBoardBtn =new JButton("글작성");
		createBoardBtn.setBounds(400,260,80,20);
		boardPane.add(createBoardBtn);
		
		createBoardBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new BoardInsert();
			}
		});
		
		
		String[][] data = Board_list.getBoards();
		String[] colNames = new String[] {"글번호","제목","내용...","작성자","조회수","작성일"};
		
		JTable boardTable = new JTable(data,colNames);
		boardTable.setBounds(0,0,600,200);
		boardTable.setRowHeight(30);
		boardTable.setFont(new Font("Sanserif",Font.BOLD,15));
        boardTable.setAlignmentX(0);
        boardTable.setSize(400,200);
        
        boardTable.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        		int rowNum = boardTable.getSelectedRow();
        		Object num =boardTable.getValueAt(rowNum, 0);
        		String title =(String) boardTable.getValueAt(rowNum, 1);
        		String content =(String) boardTable.getValueAt(rowNum, 2);
        		Object writer =boardTable.getValueAt(rowNum, 3);
        		
        		//상세조회 클릭시 조회수 오르게끔 구현하기! 
        		Board_list.hitsBoard(num,writer);
        		//Board 상세 정보 
        		new BoardDetail(num,title,content,writer);
        		
        		boardTable.setModel(new DefaultTableModel(board_list.getBoards(),colNames));

                TableColumnModel columnModels = boardTable.getColumnModel();
                columnModels.getColumn(0).setPreferredWidth(10);
                columnModels.getColumn(1).setPreferredWidth(100);
                columnModels.getColumn(3).setPreferredWidth(50);
                columnModels.getColumn(4).setPreferredWidth(10);

                boardPane.repaint();
                repaint();
                validate();
        	}
		});
        //사이즈를 정했지만 안정해지는경우도있으므로 setPreferredScrollableViewportSize 로 두번크기설정
        boardTable.setPreferredScrollableViewportSize(new Dimension(400,200));
        
        searchText=new JTextField();
		searchText.setBounds(220,10,400,20);
		boardPane.add(searchText);
		searchText.setColumns(10);
		
		searchText.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				String val = searchText.getText();
                TableRowSorter<TableModel> trs = new TableRowSorter<>(boardTable.getModel());
                boardTable.setRowSorter(trs);
                trs.setRowFilter(RowFilter.regexFilter(val)); // regular expression 을 통해 string 값이 정리가된다
			}
		});
		
		boardPane.add(new JScrollPane(boardTable));
		
		
		this.setVisible(true);
	}
	
//	
//	private static void refresh(JTable boardTable, Board_list board_list,String[] colNames) {
//		boardTable.setModel(new DefaultTableModel(board_list.getBoards(),colNames));
//
//        TableColumnModel columnModels = boardTable.getColumnModel();
//        columnModels.getColumn(0).setPreferredWidth(10);
//        columnModels.getColumn(1).setPreferredWidth(100);
//        columnModels.getColumn(3).setPreferredWidth(50);
//        columnModels.getColumn(4).setPreferredWidth(10);
//
//        boardPane.repaint();
//        repaint();
//        validate();
//	}
}
