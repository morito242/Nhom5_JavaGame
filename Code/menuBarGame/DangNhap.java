package menuBarGame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.w3c.dom.UserDataHandler;

import connection.JDBCConnection;

// import java.util.regex.*; no use of this

import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;

/**
 * Promp for login
 * @author ten nhom
 */
public class DangNhap {

	private JFrame frmLoginForm;
	private JTextField txtUserName;
	private JPasswordField txtPassword;


	public DangNhap() 
	{
		initialize();
	}

	/**
	 * Create the application.
	 */
	private void initialize() 
	{
		frmLoginForm = new JFrame();
		frmLoginForm.getContentPane().setBackground(new Color(255, 228, 181));
		frmLoginForm.getContentPane().setLayout(null);

		// render login content 
		// Login input field
		JLabel lblNewLabel = new JLabel("UserName");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		lblNewLabel.setBounds(20, 11, 104, 33);
		frmLoginForm.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setForeground(Color.BLACK);
		lblNewLabel_1.setBackground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(20, 58, 93, 28);
		frmLoginForm.getContentPane().add(lblNewLabel_1);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPassword.setBounds(123, 58, 183, 28);
		frmLoginForm.getContentPane().add(txtPassword);
		
		// Login button
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		btnLogin.setForeground(Color.BLACK);
		btnLogin.setBackground(new Color(0, 206, 209));
		btnLogin.setBounds(20, 105, 286, 32);
		
		btnLogin.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (txtUserName.getText().equals("") || txtPassword.getText().equals(""))
				{
					JOptionPane.showConfirmDialog(null, "Some Fields Are is Empty", "Error", JOptionPane.DEFAULT_OPTION);
				}
				else
				{
					PreparedStatement pst = null;
					Connection conn = null;
					try
					{
						conn = JDBCConnection.getConnection();
						
						String sql = "select * from users where username = ? and password = ?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, txtUserName.getText());
			            pst.setString(2, txtPassword.getText());
		            
						ResultSet rs = pst.executeQuery();
						
						if (rs.next())
						{
							Game game = new Game(txtUserName.getText());
						}
						else
						{
							JOptionPane.showConfirmDialog(null, "Fail", "Error", JOptionPane.DEFAULT_OPTION);
						}
					}
					catch (Exception e1) {}
				}
			}
		});
		frmLoginForm.getContentPane().add(btnLogin);

		// Register button
		JButton registerBtn = new JButton("Register");
		registerBtn.setBackground(new Color(255, 160, 122));
		registerBtn.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		registerBtn.setBounds(64, 143, 93, 32);
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DangKy form = new DangKy();
			}

		});
		frmLoginForm.getContentPane().add(registerBtn);
		
				// Reset button
				JButton btnReset = new JButton("Reset");
				btnReset.setBackground(new Color(255, 127, 80));
				btnReset.setFont(new Font("Century Gothic", Font.PLAIN, 15));
				btnReset.setBounds(184, 143, 93, 32);
				frmLoginForm.getContentPane().add(btnReset);
				
						txtUserName = new JTextField();
						txtUserName.setFont(new Font("Tahoma", Font.PLAIN, 15));
						txtUserName.setBounds(123, 14, 183, 28);
						frmLoginForm.getContentPane().add(txtUserName);
						txtUserName.setColumns(10);
				btnReset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						txtUserName.setText(null);
						txtPassword.setText(null);
					}

				});

		// render window
		frmLoginForm.setTitle("Login");
		frmLoginForm.setLocationRelativeTo(null);
		frmLoginForm.setBounds(200, 100, 347, 221);
		frmLoginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					DangNhap promp = new DangNhap();
					promp.frmLoginForm.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
