import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/** 
 * The User View includes a frame and panel that opens from the admin panel when selecting a user.
 * In a User View, one is able to follow other users created from the admin, post messages to their followers,
 * and get message feed from users they follow.
 */
public class UserView
{
	private User user;
	private JPanel userViewPanel;
	private JTextPane followingText;
	private JTextPane newsFeed;
	private JTextPane lastUpdatedTime;
	private static List<UserView> createdUserViews = new ArrayList<>();
	
	/** 
	 * Constructor: initializes panel and frame, adds all of the inner panels to the main panel
	 */
	public UserView(User user) {
		userViewPanel = new JPanel();
		JFrame userFrame = new JFrame("User: " + user.getName() + " - Time Created: " + user.getCreationTime());

		this.user = user;

		userFrame.setSize(600, 500);
		userViewPanel.setLayout(new BoxLayout(userViewPanel, BoxLayout.Y_AXIS));

		userViewPanel.add(getLastUpdatedTimePanel());
		userViewPanel.add(getFollowUserPanel());
		userViewPanel.add(getFollowingPanel());
		userViewPanel.add(getPostButtonPanel());
		userViewPanel.add(getNewsFeedPanel());

		userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userFrame.getContentPane().add(userViewPanel);
		userFrame.setVisible(true);

		createdUserViews.add(this);
	}

	/** Private helper that iterates through all of a user's following, adding a continual string to a
	 * text pane in the user view
	 */
	private void updateFollowingText() {
		String followingTextString = "Currently Following:";
		for (User followedUser : user.getFollowing()) {
			followingTextString += "\n";
			followingTextString += followedUser.getName();
		}
		followingText.setText(followingTextString);
	}

	/** Private helper that updates a string of a user's news feed, posted to a text pane */
	private void updatePostText() {
		String postTextString = "News Feed";
		for (String post : user.getNewsFeed()) {
			postTextString += "\n";
			postTextString += post;
		}
		newsFeed.setText(postTextString);
	}

	/** Private helper updating the latest time pane */
	private void updateLastTime() {
		lastUpdatedTime.setText("");
		lastUpdatedTime.setText("Last Updated Time: " + user.getLastUpdateTime());
	}

	/** Returns a panel outputting the latest updated time for the current user view. */
	public JPanel getLastUpdatedTimePanel() {
		JPanel lastUpdatedTimePanel = new JPanel();
		lastUpdatedTimePanel.setLayout(new GridLayout(1, 1));

		lastUpdatedTime = new JTextPane();
		updateLastTime();

		lastUpdatedTimePanel.add(lastUpdatedTime);

		return lastUpdatedTimePanel;

	}

	/** Returns a panel including a TextField and Button, adding a User to this User's following list */
	public JPanel getFollowUserPanel() {
		JPanel followerUserPanel = new JPanel();
		followerUserPanel.setLayout(new GridLayout(1, 2));

		JTextField enterUserID = new JTextField();
		JButton followerUserButton = new JButton("Follow User");
		followerUserButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					for (User potentialUser : User.getCreatedUsers()) {
						if (potentialUser.getName().equals(enterUserID.getText())) {
							User addedUser = potentialUser;
							user.followUser(addedUser);
							updateFollowingText();
							enterUserID.setText("");
						}
					}
				}
			}
		);

		followerUserPanel.add(enterUserID);
		followerUserPanel.add(followerUserButton);

		return followerUserPanel;
	}

	/** Returns a panel with a text pane that displays the user's following list */
	public JPanel getFollowingPanel() {
		JPanel followingPanel = new JPanel();
		followingPanel.setLayout(new GridLayout(1, 1));

		followingText = new JTextPane();
		updateFollowingText();

		followingPanel.add(followingText);

		System.out.println(followingText.getText());

		return followingPanel;
	}

	/** Returns a panel that allows this user to post a message to it's followers' news feeds */
	public JPanel getPostButtonPanel() {
		JPanel postButtonPanel = new JPanel();
		postButtonPanel.setLayout(new GridLayout(1, 2));

		JTextField enterPost = new JTextField();
		JButton postTweetButton = new JButton("Post Tweet");
		postTweetButton.addActionListener(
			new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					user.post(enterPost.getText());
					enterPost.setText("");

					for (UserView uv : createdUserViews) {
						for (User follower : user.getFollowers()) {
							if (user.getFollowers().contains(follower)) {
								uv.updatePostText();
								uv.updateLastTime();
							}
						}
					}
				}
			}
		);
		postButtonPanel.add(enterPost);
		postButtonPanel.add(postTweetButton);

		return postButtonPanel;
	}

	/** Returns a panel that displays a message on the followers' user views */
	public JPanel getNewsFeedPanel() {
		JPanel newsFeedPanel = new JPanel();
		newsFeedPanel.setLayout(new GridLayout(1, 1));

		newsFeed = new JTextPane();
		updatePostText();

		newsFeedPanel.add(newsFeed);

		return newsFeedPanel;
	}
}
