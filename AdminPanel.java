import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;

/**
 * The Admin Panel is the main part of the UI and frontend, as well as the connection for using the backend classes for users,
 * groups, and the visitor. The admin panel is implemented with a Singleton Pattern, since it only needs to be instantiated once in 
 * the program. Admin Panel includes three more panels that include the User tree, buttons for adding users, groups and opening a user view, 
 * as well as buttons for showing the analysis.
 */
public class AdminPanel
{
	private static AdminPanel instance;
	private JPanel adminPanel;
	private UserTreeModel userTreeModel;
	private UserGroup root;
	private UserTreeNode rootNode;
	private UserTreeNode lastSelected;
	private AnalysisVisitor visitor;

	/** public getInstance() to reference the same instance of the Admin panel */
	public static AdminPanel getInstance() {
		if (instance == null) {
			instance = new AdminPanel();
		}
		return instance;
	}

	/** private Constructor for Singleton pattern
	 * initializes the AnalysisVisitor, and blank panel
	 * adds all of the other panels to the main admin panel
	 */
	private AdminPanel() {
		adminPanel = new JPanel();
		JFrame frame = new JFrame("Mini Twitter");

		visitor = new AnalysisVisitor();

		frame.setSize(800, 500);
		adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.X_AXIS));

		adminPanel.add(getTreePanel());
		adminPanel.add(getUserButtonPanel());
		adminPanel.add(getAnalysisPanel());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(adminPanel);
		frame.setVisible(true);
	}

	/** Returns a panel for the User tree
	 * initializes a root UserGroup, a rootNode TreeNode,
	 * and a TreeModel that is put into a JTree
	 */
	private JTree getTreePanel() {

		UserGroup root = new UserGroup("Root");
		this.root = root;
		UserTreeNode rootNode = new UserTreeNode(root);
		this.rootNode = rootNode;

		UserTreeModel userTreeModel = new UserTreeModel(rootNode);
		this.userTreeModel = userTreeModel;

		JTree userTree = new JTree(userTreeModel);
		userTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				lastSelected = (UserTreeNode)userTree.getLastSelectedPathComponent();
				System.out.println(lastSelected);
			}
			
		});

		userTree.setBounds(0, 0, 200, 200);
		return userTree;
	}

	/** Returns a panel for adding Users and adding UserGroups */
	private JPanel getUserButtonPanel() {
		JPanel userButtonPanel = new JPanel();
		userButtonPanel.setLayout(new GridLayout(3, 2));

		JTextField addUserInput = new JTextField();
		JButton addUserButton = new JButton("Add User");
		addUserButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String input = addUserInput.getText();
					addUserInput.setText("");

					User newUser = new User(input);
					UserTreeNode newUserNode = new UserTreeNode(newUser);
					if (lastSelected != rootNode) {
						lastSelected.add(newUserNode);
					}
					else {
						rootNode.add(newUserNode);
					}
					UserGroup selectedGroup = lastSelected.getUserGroup();
					selectedGroup.addUser(newUser);
					userTreeModel.reload();
					System.out.println("Total Users: " + visitor.getUserCount());
				}
			}
		);

		JTextField addUserGroupInput = new JTextField();
		JButton addUserGroupButton = new JButton("Add User Group");
		addUserGroupButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String input = addUserGroupInput.getText();
					addUserGroupInput.setText("");
					UserGroup newUserGroup = new UserGroup(input);
					UserTreeNode newUserGroupNode = new UserTreeNode(newUserGroup);
					if (lastSelected != rootNode) {
						lastSelected.add(newUserGroupNode);
					}
					else {
						rootNode.add(newUserGroupNode);
					}
					UserGroup selectedGroup = lastSelected.getUserGroup();
					selectedGroup.addGroup(newUserGroup);
					userTreeModel.reload();
					System.out.println("Total Groups: " + visitor.getUserGroupCount());
				}
				
			}
		);

		JButton userViewButton = new JButton("Open User View");
		userViewButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new UserView(lastSelected.getUser());
				}
				
			}
		);

		userButtonPanel.setBounds(250, 250, 100, 100);
		userButtonPanel.add(addUserInput);
		userButtonPanel.add(addUserButton);
		userButtonPanel.add(addUserGroupInput);
		userButtonPanel.add(addUserGroupButton);
		userButtonPanel.add(userViewButton);

		return userButtonPanel;
	}

	/** Returns a panel for adding the analysis buttons. Here, the visitor is used every time
	 * a button is clicked, so the visiting process refreshes and collects recursively every time.
	 */
	private JPanel getAnalysisPanel() {
		JPanel analysisPanel = new JPanel();
		analysisPanel.setLayout(new GridLayout(3, 2));

		JButton showUserTotalButton = new JButton("Show User Total");
		showUserTotalButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					prepareVisitor();
					JOptionPane.showMessageDialog(showUserTotalButton, "Total Users: " + visitor.getUserCount());
				}
				
			}
		);

		JButton showUserGroupTotalButton = new JButton("Show User Group Total");
		showUserGroupTotalButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					prepareVisitor();
					JOptionPane.showMessageDialog(showUserGroupTotalButton, "Total User Groups: " + visitor.getUserGroupCount());
				}
				
			}
		);

		JButton showMessagesTotalButton = new JButton("Show Message Total");
		showMessagesTotalButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					prepareVisitor();
					JOptionPane.showMessageDialog(showMessagesTotalButton, "Total Messages: " + visitor.getNewsFeedCount());
				}
	
			}
		);

		JButton showPositivePercentButton = new JButton("Show Positive Percentage");
		showPositivePercentButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					prepareVisitor();
					JOptionPane.showMessageDialog(showPositivePercentButton, "Positive Message Percentage: " + visitor.getPositivePercentage() + "%");
				}
				
			}
		);

		JButton verifyIDButton = new JButton("Verify ID");
		verifyIDButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String message = "All Users and Groups are valid";
					if (!verifyIDs()) {
						message = "Not all Users and Groups are valid";
					}

					JOptionPane.showMessageDialog(verifyIDButton, message);
				}

			}
		);

		JButton lastUpdatedUserButton = new JButton("Get Last Updated User");
		lastUpdatedUserButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(lastUpdatedUserButton, "Last Updated User: " + findLastUpdatedUser());
				}

			}
		);

		analysisPanel.add(showUserTotalButton);
		analysisPanel.add(showUserGroupTotalButton);
		analysisPanel.add(showMessagesTotalButton);
		analysisPanel.add(showPositivePercentButton);
		analysisPanel.add(verifyIDButton);
		analysisPanel.add(lastUpdatedUserButton);

		return analysisPanel;
	}

	/** 
	 * private helper for initializing the visiting process,
	 * by resetting the counts, and starting from the root.
	 */
	private void prepareVisitor() {
		visitor.setCounts();
		root.accept(visitor);
	}

	/**
	 * private helper for verification of IDs for Users and UserGroups, 
	 * returns a boolean if all are valid, false otherwise
	 */
	private boolean verifyIDs() {
		boolean validUsers = true;
		boolean validGroups = true;

		// UUID is already unique and contains no space, so Users are already all unique.

		// Checking validity of UserGroups
		int copyCount = 0;
		for (UserGroup group : UserGroup.getCreatedGroups()) {
			String currentID = group.getName();
			for (UserGroup otherGroup : UserGroup.getCreatedGroups()) {
				if (currentID == otherGroup.getName()) {
					copyCount++;
				}
			}
			if (copyCount > 1) {
				validGroups = false;
			}
			if (currentID.contains(" ")) {
				validGroups = false;
			}
		}

		return (validUsers && validGroups);
	}

	/** private helper for finding the last updated user,
	 * by finding the largest time in milliseconds.
	 */
	private String findLastUpdatedUser() {
		long lastTime = 0;
		User lastUpdatedUser = null;

		for (User user : User.getCreatedUsers()) {
			if (user.getLastUpdateTime() > lastTime) {
				lastUpdatedUser = user;
				lastTime = user.getLastUpdateTime();
			}
		}

		if (lastUpdatedUser == null) {
			return "No Users Found";
		}

		return lastUpdatedUser.getName() + " - " + lastUpdatedUser.getID();
	}
}
