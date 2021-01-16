package menuBarGame;

import javax.swing.*;

import com.sun.tools.sjavac.comp.dependencies.PublicApiCollector;

import connection.JDBCConnection;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.awt.*;

public class DangKy extends JFrame {
    private final static String login = "Login.txt";
    // Swing component
    private Container container = new Container();
    JTextField username;
    JPasswordField password;
    
    public DangKy() {
        super("Register Form");
        setBackground(new Color(244, 164, 96));
        init();
    }
    public void setAccount()
    {
    	PreparedStatement pst = null;
		Connection conn = null;
		try
		{
			conn = JDBCConnection.getConnection();
			
			String sql = "insert into users (username,password) values(?,?)";
			//insert into login (user, pass) values (?,?)
			//pst.setString(1, username)
			//pst.setString(2, password)+
			pst = conn.prepareStatement(sql);
			pst.setString(1, username.getText());
			pst.setString(2, password.getText());
			
			int row = pst.executeUpdate();
		}
		catch (Exception e1) {}
    }
    
    public void setScore()
    {
    	PreparedStatement pst = null;
		Connection conn = null;
		try
		{
			conn = JDBCConnection.getConnection();
			
			String sql = "insert into userhistory (id,name) values(?,?)";
			//insert into login (user, pass) values (?,?)
			//pst.setString(1, username)
			//pst.setString(2, password)+
			pst = conn.prepareStatement(sql);
			pst.setInt(1, 3);
			pst.setString(2, username.getText());
			
			int row = pst.executeUpdate();
		}
		catch (Exception e1) {}
    }

    private void init() {
        container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        //===============render input section===============;
        JPanel inputSection = new JPanel();
        inputSection.setBackground(new Color(255, 228, 181));
        // userName
        JPanel userField = new JPanel();
        userField.setBounds(112, 8, 186, 28);

        // password     
        JPanel passField = new JPanel();
        passField.setBounds(112, 49, 186, 28);
        passField.setLayout(null);
        inputSection.setLayout(null);
        JLabel label = new JLabel("User name:");
        label.setBounds(20, 16, 82, 20);
        label.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        label.setForeground(new Color(0, 0, 0));
        inputSection.add(label);

        inputSection.add(userField);
        userField.setLayout(null);
        username = new JTextField(20);
        username.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        username.setBounds(0, 0, 186, 28);
        userField.add(username);
        inputSection.add(passField);
        password = new JPasswordField(20);
        password.setBounds(0, 0, 186, 28);
        passField.add(password);
        
        
        //===============render button section===============;
        JPanel buttonField = new JPanel();
        buttonField.setBackground(new Color(255, 228, 181));
        // confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBackground(new Color(0, 206, 209));
        confirmButton.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        confirmButton.setBounds(46, 6, 102, 29);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
//                String credential = username.getText() + "," + new String(password.getPassword());
//                if (!verifyUsername(credential))
//                    JOptionPane.showMessageDialog(container, "Duplicate username", "Error",
//                            JOptionPane.CANCEL_OPTION);
//                else if (writeFile(credential)) 
//                    JOptionPane.showMessageDialog(container, "Successfully create new user\nPlease Login again ", "Successful",
//                            JOptionPane.INFORMATION_MESSAGE);
//                else
//                    JOptionPane.showMessageDialog(container, "Cannot create new user", "Error",
//                            JOptionPane.CANCEL_OPTION);
            	setAccount();
            	setScore();
        		setVisible(false);
            }
        });
        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(255, 127, 80));
        cancelButton.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        cancelButton.setBounds(177, 6, 102, 29);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonField.setLayout(null);

        buttonField.add(confirmButton);
        buttonField.add(cancelButton);

        // Add to container
        container.add(inputSection);
        JLabel label_1 = new JLabel("Password:");
        label_1.setForeground(new Color(0, 0, 0));
        label_1.setBounds(20, 49, 82, 28);
        label_1.setFont(new Font("Century Gothic", Font.PLAIN, 15));
        inputSection.add(label_1);
        container.add(buttonField);

        renderWindow();
    }

    private void renderWindow() {
        setSize(345, 196);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    // Use to verify username exist or not
    private boolean verifyUsername(String cred){
        File inFile = new File(login);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(inFile);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        BufferedReader bufReader = new BufferedReader(fileReader);
        String line = ",";
        try {
            while ((line = bufReader.readLine()) != null)
                if (cred.split(",")[0].equals(line.split(",")[0])) {
                    return false;
                }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return true;
    }

    private boolean writeFile(String credential) {
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter(login, true));
            outputStream.println(credential);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (outputStream != null)
                outputStream.close();
        }
        
    }
}
