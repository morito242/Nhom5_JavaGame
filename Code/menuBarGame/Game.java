package menuBarGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import connection.JDBCConnection;

public class Game extends JFrame implements ActionListener {

	score sc = new score();
    private static final long serialVersionUID = 1L;
    private int point;
    private int question_num = 0;

    // Swing components
    private Container container;
    private JPanel questionPane, answerPane, nextChallengePane;
    private static JPanel FilePane,DatabasePane;
    private JTextField answer;
    private JLabel checker, question, score;
    private BufferedReader reader_Ans;
    private BufferedReader reader_Ques;
    private File questionFile, answerFile;
    private JMenuBar menuBar;
    final JFileChooser fileDialog = new JFileChooser();
    static ArrayList<String> ques = new ArrayList<String>();
    static ArrayList<String> ans = new ArrayList<String>();
    // Queue and Stack
    private Queue<File> queue = new LinkedList<> ();
    private Stack<File> stack = new Stack<File> ();
    private int isStack;
    ArrayList<String> cate = new ArrayList<String>();
    String[] questionType;
    ArrayList<Integer> cateid = new ArrayList<Integer>();
    private String username;
   
    public Game(String user) throws IOException 
    {
        super("Funny Question Game"); // use this instead of setTitle for JFrame
        this.username = user;
        init();
        try 
        {
			Music music = new Music();
			
		} 
        catch (LineUnavailableException e) {} 
        catch (IOException e) {} 
        catch (UnsupportedAudioFileException e) {}
        
      
    }

    private void init() throws IOException {
        // readFile(); // add this line to fix bug

        this.point = sc.getPoint(username);
        container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS)); // arrange vertialy
        
        renderFileChooser();
        // container.add(FilePane);

        renderQuestionPane();
        renderAnswerPane();
        renderDatabasePane();
        renderNextChallengePane();
        container.add(questionPane);
        container.add(answerPane);
        container.add(nextChallengePane);

        menuBar = new Menu(FilePane,DatabasePane);

        this.setJMenuBar(menuBar);
        renderWindow();
        System.out.print(username);
    }

    /**
     * Add this funciton to fix bug
     * 
     * @throws IOException
     */
    private void readFile() throws IOException 
    {
        String question_name = questionFile.getName();
        
        answerFile = new File(questionFile.getParentFile().getAbsolutePath() + "\\" + question_name.substring(0, question_name.lastIndexOf("_")) + "_answer.txt");
        System.out.println(questionFile.getParentFile().getAbsolutePath() + "\\" + question_name.substring(0, question_name.lastIndexOf("_")) + "_answer.txt");
        InputStream inputStream_Ques = new FileInputStream(questionFile);
        
        InputStream inputStream_Ans = new FileInputStream(answerFile);
        InputStreamReader inputStreamReader_Ques = new InputStreamReader(inputStream_Ques);
        InputStreamReader inputStreamReader_Ans = new InputStreamReader(inputStream_Ans);
        
        reader_Ques = new BufferedReader(inputStreamReader_Ques);
        reader_Ans = new BufferedReader(inputStreamReader_Ans);

        // clear list
        ques.clear();
        ans.clear();
        String line_Ques = "";
        String line_Ans = "";
        while ((line_Ques = reader_Ques.readLine()) != null && (line_Ans = reader_Ans.readLine()) != null) {
            ques.add(line_Ques);
            ans.add(line_Ans);
        }

        // close stream
        inputStream_Ques.close();
        inputStreamReader_Ans.close();
    }

    private void renderWindow() {
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    private void renderFileChooser() 
    {
        FilePane = new JPanel();
        FilePane.setPreferredSize(new Dimension(400, 100));
        FilePane.setBackground(new Color(255, 228, 181));

        // question
        JPanel questionFilePane = new JPanel(new FlowLayout(1, 10, 0));
        JButton ques_choose_file = new JButton("Choose File");
        ques_choose_file.setBackground(new Color(0, 206, 209));
        ques_choose_file.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		ques_choose_file.setBounds(64, 143, 93, 32);
        JLabel ques_file_choosed = new JLabel("");

        // JFileChooser config
        fileDialog.setDialogTitle("File");
        fileDialog.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        fileDialog.setCurrentDirectory(new java.io.File(".")); // get current directory
        fileDialog.setMultiSelectionEnabled(true);

        ques_choose_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int res = fileDialog.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                	String[] options = {"A->Z", "Z->A"};
                	 isStack = JOptionPane.showOptionDialog(null, "Choose play type",
                             "Select play type",
                             JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
             		File[] files = fileDialog.getSelectedFiles();
                	for (File file : files) { 
                		if (isStack == 0) { // Means choose queue
                			queue.add(file);
                		} else if (isStack == 1) { // Means choose stack
                			stack.add(file);
                		}	
                	}}
            }

        });

        questionFilePane.add(new JLabel("File: "));
        questionFilePane.add(ques_choose_file);
        questionFilePane.add(ques_file_choosed);
        // Confirm button
        JButton confirmBtn = new JButton("Let's go");
        confirmBtn.setBackground(new Color(255, 160, 122));
        confirmBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		confirmBtn.setBounds(64, 143, 93, 32);
        confirmBtn.setPreferredSize(new Dimension(160, 30));
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // read file
                    if (isStack == 0) { // Means choose Queue	
                    	handleQueue();
                    } else if (isStack == 1) { // Means choose Stack
                    	handleStack();
                    }
                    makeNewGame();
                } catch (Exception ecep) {
                    JOptionPane.showMessageDialog(container, "Cannot Open file", "Error", JOptionPane.CANCEL_OPTION);
                }
            }
        });

        FilePane.add(questionFilePane);
        
        FilePane.add(confirmBtn);
    }
    
    private void handleQueue() throws IOException {
    	questionFile = queue.remove();
        readFile();
    }
    private void handleStack() throws IOException {
    	questionFile = stack.pop();
        readFile();
    }
    private void makeNewGame() 
    {
        // config new game
        question_num = 0;
        point = sc.getPoint(username);
        question.setText("Quesiton: " + ques.get(question_num));
        question.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        score.setText("Total: " + point);
        score.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        answer.setEnabled(true);
        answer.setFont(new Font("Century Gothic", Font.PLAIN, 15));
    }

    private void renderQuestionPane() {
        questionPane = new JPanel(new FlowLayout(1, 10, 10));
        setBackground(new Color(255, 228, 181));
        questionPane.setBackground(new Color(88, 214, 141));

        question = new JLabel("Quesiton: ");
        question.setFont(new Font("Century Gothic", Font.PLAIN, 15));

        questionPane.add(question);
    }

    private void renderAnswerPane() {
        answerPane = new JPanel(new FlowLayout(1, 5, 5));
        answerPane.setBackground(new Color(0, 206, 209));

        answer = new JTextField(20);
        answer.addActionListener(this); // add event listener
        checker = new JLabel("");
        score = new JLabel("Total: " + this.point); 
        answer.setEnabled(false);

        answerPane.add(new JLabel("Your answer: "));
        answerPane.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        answerPane.add(answer);
        answerPane.add(score);
        answerPane.add(checker);
    }
    public void getCate()
    {
    	PreparedStatement pst = null;
		Connection conn = null;
		try
		{
			conn = JDBCConnection.getConnection();
			
			String sql = "select * from category";
			pst = conn.prepareStatement(sql);
			
        
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				cate.add(rs.getString(2));
				cateid.add(rs.getInt(1));
			}
		}
		catch (Exception e1) {}
		questionType = new String[cate.size()];
		for (int i = 0; i < cate.size(); i++)
			questionType[i] = cate.get(i);
    }
    public void getQuesandAns(int id)   
    {
    	PreparedStatement pst = null;
		Connection conn = null;
		try
		{
			conn = JDBCConnection.getConnection();
			
			String sql = "select * from quesandans where idcate = ?";
			pst = conn.prepareStatement(sql);
			pst.setLong(1, id);
        
			ResultSet rs = pst.executeQuery();
			
			while(rs.next())
			{
				ques.add(rs.getString(2));
				ans.add(rs.getString(3));
			}
		}
		catch (Exception e1) {}
    }
    private void renderDatabasePane()
    {
    	DatabasePane =new JPanel();
    	DatabasePane.setPreferredSize(new Dimension(400, 100));
    	DatabasePane.setBackground(new Color(255, 228, 181));
         // Dropdown list
         // get list of question type from datbase and put to array questionType
         // SELECT UNIQUE questionType FROM challenge_table
//         String[] questionType = { "Math", "English", "History" };
    	 getCate();
         JComboBox<String> list = new JComboBox<String>(questionType);
 	     list.setFont(new Font("Century Gothic", Font.PLAIN, 15));

         // Confirm button
         JButton confirmBtn = new JButton("Play");
         confirmBtn.setBackground(new Color(255, 160, 122));
         confirmBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
 		 confirmBtn.setBounds(64, 143, 93, 32);
         confirmBtn.setPreferredSize(new Dimension(160, 30));
         confirmBtn.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 String type = (String) list.getSelectedItem();
//                 System.out.println(type);
                 //clear
                 ques.clear();
                 ans.clear();
                 // Get question and answer from database to 2 array ques and ans
                 // SELECT question, answer FROM challenge_table WHERE questionType = "";
                 
                 // Make a for loop to add answer and question to 2 array ques and ans
//                 ques.add("aaaaa");
//                 ans.add("aaaa");
                
                 int getcate = 0;
                 for(int i=0;i<cate.size();i++)
                 {
                	 if(cate.get(i)==type)
                	 {
                		 getcate=i;
                	 }
                 }
//                 System.out.println(cateid.get(getcate));
                 
                 getQuesandAns(cateid.get(getcate));
                 
                 makeNewGame();
             }
         });
         
         DatabasePane.add(new JLabel("Choose question type: ") );
         DatabasePane.add(list);
         DatabasePane.add(confirmBtn);
         
    }
    


	private void renderNextChallengePane() {
    	nextChallengePane = new JPanel();
    	nextChallengePane.setBackground(new Color(88, 214, 141));
    	JButton next = new JButton("NEXT");
    	next.setBackground(new Color(255, 160, 122));
		next.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		next.setBounds(64, 143, 93, 32);
    	next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // read file
                    if (isStack == 0) { // Means choose Queue	
                    	handleQueue();
                    } else if (isStack == 1) { // Means choose Stack
                    	handleStack();
                    }
                    makeNewGame();
                } catch (Exception ecep) {
                    JOptionPane.showMessageDialog(container, "Cannot Open file", "Error", JOptionPane.CANCEL_OPTION);
                }
            }
        });
    	
    	nextChallengePane.add(next);
    }

//    public static void main(String[] args) throws IOException 
//    {
//        new Game();
//        
//    }

    // Catch event
    @Override
    public void actionPerformed(ActionEvent e) {
        String res = answer.getText();

        if (res.equalsIgnoreCase(ans.get(question_num))) 
        {
        	try
        	{
				WinMusic wnMusic = new WinMusic();
				wnMusic.start();
			}
        	catch (LineUnavailableException e1) {}
        	catch (IOException e1) {} 
        	catch (UnsupportedAudioFileException e1) {} 
            question_num++;
            point++;
            sc.savePoint(username, point);
            checker.setText("");
            answer.setText("");
            score.setText("Score: " + point);

            // try and catch error when out of quesion ==> end game
            try {
                   question.setText("Question: " + ques.get(question_num));
            } 
            catch (IndexOutOfBoundsException exception) {
            
                question.setText("You win!!!");
                answer.setEnabled(false); // disable answer when game end
            }
        } else 
        {
            checker.setText("Wrong aswer!!!");
   
    		checker.setFont(new Font("Century Gothic", Font.PLAIN, 15));
    		
            try
        	{
				LoseMusic wnMusic = new LoseMusic();
				wnMusic.start();
			}
        	catch (LineUnavailableException e1) {}
        	catch (IOException e1) {} 
        	catch (UnsupportedAudioFileException e1) {} 
        }
    }

}