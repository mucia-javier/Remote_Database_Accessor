import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

	// SUBCLASS for the panel holding the Database-Login-Credentials
	class Credentials extends JPanel {
		// Text-Fields (and their labels) that are used to get
		// Database-login-credentials from the user
		JLabel dbURL_label = new JLabel("Database URL: ");
		JTextField dbURL_field = new JTextField("jdbc:mysql://127.0.0.1:3316/", 20);
		
		/*
		JLabel port_label = new JLabel("Port: ");
		JTextField port_field = new JTextField("3316", 12);

		JLabel dbName_label = new JLabel("Database Name: ");
		JTextField dbName_field = new JTextField("openmrs", 12);
		*/
		
		JLabel username_label = new JLabel("Username: ");
		JTextField username_field = new JTextField("openmrs", 12);

		JLabel password_label = new JLabel("Password: ");
		JTextField password_field = new JTextField("95AhrHd~3irk", 12);

		JLabel error_label = new JLabel("");

		public Credentials() {
			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();

			//// First column /////////////////////////
			gc.anchor = GridBagConstraints.LINE_END;
			gc.weightx = 0.5;
			gc.weighty = 0.5;

			gc.gridx = 0;
			gc.gridy = 0;
			add(dbURL_label, gc);

			/*
			gc.gridx = 0;
			gc.gridy = 1;
			add(port_label, gc);

			gc.gridx = 0;
			gc.gridy = 2;
			add(dbName_label, gc);
			*/
		
			gc.gridx = 0;
			gc.gridy = 1;
			add(username_label, gc);

			gc.gridx = 0;
			gc.gridy = 2;
			add(password_label, gc);

			//// Second column
			gc.anchor = GridBagConstraints.LINE_START;

			gc.gridx = 1;
			gc.gridy = 0;
			add(dbURL_field, gc);

			/*
			gc.gridx = 1;
			gc.gridy = 1;
			add(port_field, gc);

			gc.gridx = 1;
			gc.gridy = 2;
			add(dbName_field, gc);
			*/
			
			gc.gridx = 1;
			gc.gridy = 1;
			add(username_field, gc);

			gc.gridx = 1;
			gc.gridy = 2;
			add(password_field, gc);

			gc.gridwidth = 2;
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.gridx = 1;
			gc.gridy = 3;
			gc.ipady = 20; // make this component tall
			add(error_label, gc);
		}

		public void showErrorMessage() {
			this.error_label.setText("Connection Error!");
		}

		public void showSuccessMessage() {
			this.error_label.setText("Connection Successful!");
		}

		public void hideErrorMessage() {
			this.error_label.setVisible(false);
		}

		public void getCredentials() {
//			dbConnection.setServerID(serverID_field.getText());
//			dbConnection.setPort(port_field.getText());
//			dbConnection.setDbName(dbName_field.getText());
			dbConnection.setdbURL(dbURL_field.getText());
			dbConnection.setUsername(username_field.getText());
			dbConnection.setPassword(password_field.getText());
		}

	}

	class Query extends JPanel {

		public String[][] makeDataTable(){

			int rows = dbConnection.data.size();
			int cols = dbConnection.colNames.size();
			String data [][] = new String[rows][cols];
			
	        for(int i=0; i<rows; ++i){
	        	for(int j=0; j<cols; ++j){
	        		data[i][j]=dbConnection.data.get(i).get(j);
	        		}
	        	}
	       return data;
		}
		public String[] makeHeadersTable(){

			int cols = dbConnection.colNames.size();
			String headers [] = new String[cols];
			
	        for(int i=0; i<cols; ++i){
	        	headers[i] = dbConnection.colNames.get(i);
	        	}
	       return headers;
		}
		
		public void resizeColumnWidth(JTable table) {
		    final TableColumnModel columnModel = table.getColumnModel();
		    for (int column = 0; column < table.getColumnCount(); column++) {
		        int width = 50; // Min width
		        for (int row = 0; row < table.getRowCount(); row++) {
		            TableCellRenderer renderer = table.getCellRenderer(row, column);
		            Component comp = table.prepareRenderer(renderer, row, column);
		            width = Math.max(comp.getPreferredSize().width +1 , width);
		        }
		        columnModel.getColumn(column).setPreferredWidth(width);
		    }
		}
		
		public Query() {
			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();

			final JLabel query_label = new JLabel("Command: ");
			final JTextField query_field = new JTextField(37);

			// This is the Button to execute queries/updates
			final JButton executeButton;
			executeButton = new JButton("Excecute");
			// Add behaviour
			
			executeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {	
						c.removeAll();
						c.revalidate();
						dbConnection.setQuery(query_field.getText());
						dbConnection.execute();
						//textArea.setText(dbConnection.theResult);
						//executeButton_numberOfClicks++;
						
						

						// Create some data
						String columnNames[] = makeHeadersTable();
						String dataValues[][] = makeDataTable();
						
						// Create a new table instance
						JTable dataTable = new JTable( dataValues, columnNames );
						JScrollPane aScrollPane = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						resizeColumnWidth(dataTable);
						dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

						//aScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						//aScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
						

						c.add(QuerySection, BorderLayout.NORTH);
						c.add(aScrollPane,BorderLayout.CENTER );
						c.revalidate();
				}
			});

			//// First column /////////////////////////
			gc.anchor = GridBagConstraints.LINE_END;
			gc.weightx = 0.0;
			gc.weighty = 2.0;

			gc.gridx = 0;
			gc.gridy = 0;
			add(query_label, gc);

			gc.gridx = 1;
			gc.gridy = 0;
			add(query_field, gc);

			gc.gridx = 2;
			gc.gridy = 0;
			add(executeButton, gc);

		}
	}

	final Container c;
	final JButton connectButton;
	final JTextArea textArea;
	final JScrollPane areaScrollPane;
	private int connectButton_numberOfClicks = 0;
	private int executeButton_numberOfClicks = 0;
	final Credentials credentialsPanel;
	private Query QuerySection;
	DBconnection dbConnection;

	public String columnNames[];

	public void changeSize(int x, int y) {
		this.setSize(x, y); // (X, Y)
	}
	

	public MainFrame(String title) {
		// Set the initial Size of the Frame -Asking for login credentials
		super(title);
		this.setSize(450, 190); // (X_size, Y_size)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		setLayout(new BorderLayout()); // Set layout manager

		// Creates the Pannel to input the Database-Login-Credentials
		credentialsPanel = new Credentials();

		// Addd Two buttons to the screen
		connectButton = new JButton("Connect");

		// Add Swing components to content pane
		c = getContentPane();
		c.add(connectButton, BorderLayout.SOUTH);
		c.add(credentialsPanel, BorderLayout.CENTER);
		dbConnection = new DBconnection();

		// Non-Editable Text area that will be showing the results of a query
		textArea = new JTextArea();
		textArea.setEditable(false);
		

		areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		QuerySection = new Query();

		// ########### CONTROLLERS/(ACTION LISTENERS) FOR THE BUTTONS
		// ################################
		// Add behaviour
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				credentialsPanel.getCredentials();
				dbConnection.connect();
				if (dbConnection.isConnected()) {
					credentialsPanel.showSuccessMessage();
					c.remove(connectButton);
					c.remove(credentialsPanel);
					c.revalidate();
					c.add(QuerySection, BorderLayout.NORTH);
					//c.add(areaScrollPane,BorderLayout.CENTER );
					//textArea.setText("");
					changeSize(650, 450); // X_size, Y_size
					c.revalidate();
				} else {
					credentialsPanel.showErrorMessage();
				}
				connectButton_numberOfClicks++;
			}
		});

	}
}
