import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		
	
		setBounds(300,100,751,500);
		setTitle("게시판");
		setFont(new Font("나눔바른고딕 Light",Font.BOLD,15));
		
		
		boardPane = new JPanel();
		boardPane.setBackground(Color.WHITE);
//		boardPane.setBorder(new EmptyBorder(5,5,5,5));
		
		setContentPane(boardPane);
		
		
		String[][] data = Board_list.getBoards();
		String[] colNames = new String[] {"글번호","제목","작성자","조회수","작성일"};
		JTable boardTable = new JTable(data,colNames);
		boardTable.setFont(new Font("나눔바른고딕 Light",Font.BOLD,18));
        boardTable.setAlignmentX(0);
        boardTable.setSize(400,200);
        
        boardTable.setModel(new DefaultTableModel(data,colNames) {
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        });
        
        boardTable.getTableHeader().setReorderingAllowed(false);
        boardTable.getTableHeader().setResizingAllowed(false);
        
        boardTable.getTableHeader().setBackground(new Color(80, 188, 223));
        boardTable.getTableHeader().setForeground(new Color(255,255,255));
       
        boardTable.setRowHeight(35);
        
        boardTable.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        		int rowNum = boardTable.getSelectedRow();
        		Object num =boardTable.getValueAt(rowNum, 0);
        		String title =(String) boardTable.getValueAt(rowNum, 1);
//        		String content =(String) boardTable.getValueAt(rowNum, 2);
        		Object writer =boardTable.getValueAt(rowNum, 2);
        		
        		//상세조회 클릭시 조회수 오르게끔 구현하기! 
        		Board_list.hitsBoard(num,writer);
        		//Board 상세 정보 
//        		new BoardDetail(num,title,content,writer);
        		new BoardDetail(num, title, writer);
        		setVisible(false);
        		boardTable.setModel(new DefaultTableModel(board_list.getBoards(),colNames));
                
        		TableColumnModel columnModels = boardTable.getColumnModel();
                
        		columnModels.getColumn(0).setPreferredWidth(10);
                columnModels.getColumn(1).setPreferredWidth(50);
                columnModels.getColumn(2).setPreferredWidth(100);
                columnModels.getColumn(3).setPreferredWidth(20);
                columnModels.getColumn(4).setPreferredWidth(10);
                boardPane.repaint();
                repaint();
                validate();
        	}
		});
        JScrollPane scrollPane = new JScrollPane(boardTable);
        scrollPane.setBounds(41, 55, 600, 330);
        //사이즈를 정했지만 안정해지는경우도있으므로 setPreferredScrollableViewportSize 로 두번크기설정
        scrollPane.setPreferredSize(new Dimension(600,330));
        boardPane.setLayout(null);
        
        searchText=new JTextField();
		searchText.setBounds(261,397,153,24);
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
				createBoardBtn.setBounds(565,16,75,27);
				
				boardPane.add(createBoardBtn);
				
				createBoardBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						new BoardInsert();
					}
				});
		
		boardPane.add(scrollPane);
		
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
