package cliente;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

public class VentanaClienteChat extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;	
	private JPanel textPanel, inputPanel;
	private JTextField textField;
	private String name, message;
	private Font meiryoFont = new Font("Meiryo", Font.PLAIN, 14);
	private Border blankBorder = BorderFactory.createEmptyBorder(10,10,20,10);//top,r,b,l
	private ChatClient chatClient;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    
    
    protected JTextArea textArea, userArea;
    protected JFrame frame;
    protected JButton startButton, sendButton;
    protected JPanel clientPanel, userPanel;
    protected JPanel clientPanel2, userPanel2;

	
    
    public static void main(String args[]){
		//set the look and feel to 'Nimbus'
		try{
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
				if("Nimbus".equals(info.getName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch(Exception e){
			
		}
		
		new VentanaClienteChat();
	}
    
    public VentanaClienteChat(){
		
		frame = new JFrame("Visualización del chat del cliente");	
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        
		    	if(chatClient != null){
			    	try {
			        	sendMessage("Hasta la proxima, Me tengo que despedir por ahora");
			        	chatClient.serverIF.leaveChat(name);
					} catch (RemoteException e) {
						e.printStackTrace();
					}		        	
		        }
		        System.exit(0);  
		    }   
		});
		
	
		Container c = getContentPane();
		JPanel outerPanel = new JPanel(new BorderLayout());
		
		outerPanel.add(getInputPanel(), BorderLayout.CENTER);
		outerPanel.add(getTextPanel(), BorderLayout.SOUTH);
		
		c.setLayout(new BorderLayout());
		c.add(outerPanel, BorderLayout.CENTER);
		c.add(getUsersPanel(), BorderLayout.WEST);
		//c.add(getUsersPanel2(), BorderLayout.NORTH);
		c.add(getUsersPanel3(), BorderLayout.EAST);



		frame.add(c);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocation(100, 100);
		textField.requestFocus();
	
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
    
    
    public JPanel getTextPanel(){
		String welcome = "Primero ingrese su nombre y click en 'Iniciar' para comenzar\n";
		textArea = new JTextArea(welcome, 14, 24);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setFont(meiryoFont);
		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textPanel = new JPanel();
		textPanel.add(scrollPane);
	
		textPanel.setFont(new Font("Meiryo", Font.PLAIN, 14));
		return textPanel;
	}
    
    public JPanel getInputPanel(){
 
    	
		inputPanel = new JPanel(new GridLayout(1, 1, 5, 5));
		inputPanel.setBorder(blankBorder);	
		textField= new JTextField();
		textField.setFont(meiryoFont);
		inputPanel.add(textField);
		return inputPanel;
	}
    
    //Panel de usuario conectados
    
    public JPanel getUsersPanel2(){
		
		userPanel = new JPanel(new BorderLayout());
		String  userStr = "Chat Cliente";
		
		JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
		userPanel.add(userLabel, BorderLayout.NORTH);	
		userLabel.setFont(new Font("Meiryo", Font.PLAIN, 16));



		clientPanel.setFont(meiryoFont);
		userPanel.setBorder(blankBorder);

		return userPanel;		
	}
    
    public JPanel getUsersPanel3(){
		
		userPanel2 = new JPanel(new BorderLayout());
		String  userStr = "Nombre & Chat";
		
		JLabel userLabel2 = new JLabel(userStr, JLabel.CENTER);
		userPanel2.add(userLabel2, BorderLayout.NORTH);	
		userLabel2.setFont(new Font("Meiryo", Font.PLAIN, 12));



		//clientPanel.setFont(meiryoFont);
		//userPanel.setBorder(blankBorder);

		return userPanel2;		
	}
    
    public JPanel getUsersPanel(){
		
		userPanel = new JPanel(new BorderLayout());
		String  userStr = " Usuarios En Linea:       ";
		
		JLabel userLabel = new JLabel(userStr, JLabel.CENTER);
		userPanel.add(userLabel, BorderLayout.NORTH);	
		userLabel.setFont(new Font("Meiryo", Font.PLAIN, 14));

		String[] noClientsYet = {"Sin usuarios"};
		setClientPanel(noClientsYet);

		clientPanel.setFont(meiryoFont);
		userPanel.add(makeButtonPanel(), BorderLayout.NORTH);		
		userPanel.setBorder(blankBorder);

		return userPanel;		
	}
    
    public void setClientPanel(String[] currClients) {  	
    	clientPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel<String>();
        
        for(String s : currClients){
        	listModel.addElement(s);
        }
        
        //Create the list and put it in a scroll pane.
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(8);
        list.setFont(meiryoFont);
        JScrollPane listScrollPane = new JScrollPane(list);

        clientPanel.add(listScrollPane, BorderLayout.CENTER);
        userPanel.add(clientPanel, BorderLayout.CENTER);
    }
    
    //Botones de inicio y envio
    public JPanel makeButtonPanel() {		
		sendButton = new JButton("Enviar ");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);


		startButton = new JButton("Iniciar ");
		startButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(startButton);
		buttonPanel.add(sendButton);
		
		return buttonPanel;
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			//get connected to chat service
			if(e.getSource() == startButton){
				name = textField.getText();				
				if(name.length() != 0){
					frame.setTitle("Ventana para chat de " + name);
					textField.setText("");
					textArea.append("Usuario: " + name + " conectando a chat...\n");							
					getConnected(name);
					if(!chatClient.connectionProblem){
						startButton.setEnabled(false);
						sendButton.setEnabled(true);
						}
				}
				else{
					JOptionPane.showMessageDialog(frame, "Ingresa tu nombre para empezar");
				}
			}

			//get text and clear textField
			if(e.getSource() == sendButton){
				message = textField.getText();
				textField.setText("");
				sendMessage(message);
				System.out.println("Enviando mensaje : " + message);
			}
			
			
		}
		catch (RemoteException remoteExc) {			
			remoteExc.printStackTrace();	
		}
	}
	
	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.serverIF.updateChat(name, chatMessage);
	}
	
	private void getConnected(String userName) throws RemoteException{
		//remove whitespace and non word characters to avoid malformed url
		String cleanedUserName = userName.replaceAll("\\s+","_");
		cleanedUserName = userName.replaceAll("\\W+","_");
		try {		
			chatClient = new ChatClient(this, cleanedUserName);
			chatClient.startClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
