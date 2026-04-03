package game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class GameGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private static JTextArea scene_description;
	private static JLabel image_lbl;
	private static JButton btn_choice_1;
	private static JButton btn_choice_2;
	private static Scene current_scene; 
	private AdventureGame game;
	private static JTextField user_inventory;
	private static JTextArea room_inventory;
	private JButton pickup_item_btn;
	
	/**
	 * Launch the application.
	 */
	
	
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameGUI frame = new GameGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameGUI() {
		
		game = new AdventureGame();
		current_scene = game.getCurrentScene();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 680, 420);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel title_label = new JLabel("Adventure Game");
		title_label.setFont(new Font("Serif", Font.BOLD, 32));
		title_label.setHorizontalAlignment(SwingConstants.CENTER);
		title_label.setBounds(197, 11, 269, 39);
		contentPane.add(title_label);
		
		
		//image label with icon for current room.
		image_lbl = new JLabel("");
		
		ImageIcon icon = new ImageIcon(GameGUI.class.getResource("/images2/lobby.png"));
		Image img = icon.getImage();
		
		Image scaledImg = img.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImg);
		image_lbl.setBounds(22, 61, 300, 300);
		contentPane.add(image_lbl);
		
		scene_description = new JTextArea();
		scene_description.setBounds(354, 61, 300, 60);
		contentPane.add(scene_description);
		
		btn_choice_1 = new JButton("New button");
		btn_choice_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nextId = current_scene.getChoices().get(0).getNextSceneId();
				current_scene = game.getScenes().findSceneById(nextId);
				
				updateSceneDisplay();
				
				//if its on the final exit scene then check if the player has one when they click the button.
				if (current_scene.getSceneId() == 5) {
					checkWinCondition();
				}
				
			}
		});
		btn_choice_1.setFont(new Font("Monospaced", Font.PLAIN, 12));
		btn_choice_1.setBounds(354, 261, 120, 100);
		contentPane.add(btn_choice_1);
		
		btn_choice_2 = new JButton("New button");
		btn_choice_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nextId = current_scene.getChoices().get(1).getNextSceneId();
				current_scene = game.getScenes().findSceneById(nextId);
				
				updateSceneDisplay();
				
				//if its on the final exit scene then check if the player has one when they click the button.
				if (current_scene.getSceneId() == 5) {
					checkWinCondition();
				}
				
			}
		});
		btn_choice_2.setFont(new Font("Monospaced", Font.PLAIN, 12));
		btn_choice_2.setBounds(534, 261, 120, 100);
		contentPane.add(btn_choice_2);
		
		user_inventory = new JTextField();
		user_inventory.setFont(new Font("Monospaced", Font.PLAIN, 12));
		user_inventory.setBounds(354, 135, 300, 50);
		contentPane.add(user_inventory);
		user_inventory.setColumns(10);
		
		room_inventory = new JTextArea();
		room_inventory.setFont(new Font("Monospaced", Font.PLAIN, 12));
		room_inventory.setBounds(354, 196, 160, 50);
		room_inventory.setLineWrap(true);
		room_inventory.setWrapStyleWord(true);
		room_inventory.setEditable(false);
		contentPane.add(room_inventory);
		room_inventory.setColumns(10);
		
		pickup_item_btn = new JButton("Pick up item");
		pickup_item_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item item_in_room = current_scene.getItem();
				
				if (item_in_room != null) {
					game.getPlayer().addItem(item_in_room);
					current_scene.removeItem();
					updateSceneDisplay();
				}
				
			}
		});
		pickup_item_btn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		pickup_item_btn.setBounds(534, 196, 120, 50);
		contentPane.add(pickup_item_btn);
		
		updateSceneDisplay();

	}
	
	public void checkWinCondition() {
		boolean can_win = game.canWinGame();
		
		if (can_win == true) {
			javax.swing.JOptionPane.showMessageDialog(this,
					"You used the Keycard and the Code Note to unlock the exit.\nYou escaped. You win!");
		}
		else {
			javax.swing.JOptionPane.showMessageDialog(this,
					"The exit will not open.\nYou are missing the required items.\nYou need to find the Keycard and the Code Note");
		}
		
		btn_choice_1.setEnabled(false);
		btn_choice_2.setEnabled(false);
		pickup_item_btn.setEnabled(false);
		
	}
	
	
	public void updateSceneDisplay() {
		
		scene_description.setText(current_scene.getDescription());
		
		
		Item item_in_room = current_scene.getItem();
		if (item_in_room == null) {
			room_inventory.setText("room has no items.");
		}
		else {
			room_inventory.setText(item_in_room.toString());
		}
		
		user_inventory.setText(game.getPlayer().getInventoryText());
		
		ImageIcon icon = new ImageIcon(current_scene.getImagePath());
		Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		image_lbl.setIcon(new ImageIcon(img));
		
		System.out.println(current_scene.getImagePath());
		
		btn_choice_1.setText("<html>" + current_scene.getChoices().get(0).getText() + "</html>");
		btn_choice_2.setText("<html>" + current_scene.getChoices().get(1).getText() + "</html>");
		
		
		
	}
}
