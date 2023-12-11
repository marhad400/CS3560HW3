import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

/**
 * User class that structures an Observer pattern amongst other Users.
 * Users can follow (observe) another user, and be followed by a User or that same User back (observed), 
 * so Users are both Observers and Observable within the design pattern.
 */
public class User implements UserInterface
{
	private UUID userID;
	private String name;
	private List<User> followers;
	private List<User> following;
	private List<String> newsFeed;
	private String twitterPost;
	private static List<User> createdUsers = new ArrayList<>();

	private long creationTime;
	private long lastUpdateTime;

	/** Constructor that takes in a String for the name of the User.
	 * Here we initialize the unique ID, and lists containing Users followed, Users following, 
	 * and News Feed list.
	 * @param name
	*/
	public User(String name) {
		setID(UUID.randomUUID());
		followers = new ArrayList<>();
		following = new ArrayList<>();
		newsFeed = new ArrayList<>();
		this.name = name;
		createdUsers.add(this);
		creationTime = System.currentTimeMillis();
	}

	/** User ID setter method */
	public void setID(UUID userID) {
		this.userID = userID;
	}

	/** User ID getter method */
	public UUID getID() {
		return userID;
	}

	public String getName() {
		return name;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public long getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	/** followUser() implements the Observer behavior */
	public void followUser(User userToFollow) {
		following.add(userToFollow);
		userToFollow.addFollower(this);
	}

	/** addFollower() implements the Observable behavior */
	public void addFollower(User follower) {
		followers.add(follower);
	}

	/** Returns a List of Users that follow (observe) this User */
	public List<User> getFollowers() {
		return followers;
	}

	/** Returns a List of Users that this User is following (observing) */
	public List<User> getFollowing() {
		return following;
	}

	/** Updates the post message to all of the followers' news feeds */
	public String post(String post) {
		twitterPost = post;
		updateFollowers();
		return post;
	}

	private void updateFollowers() {
		for (User follower : followers) {
			follower.updateNewsFeed(this, twitterPost);
		}
	}

	public void updateNewsFeed(User poster, String update) {
		lastUpdateTime = System.currentTimeMillis();
		newsFeed.add(poster.getName() + ": " + update);
	}

	/** Returns list of news feed messages */
	public List<String> getNewsFeed() {
		return newsFeed;
	}

	/** Returns a static list of all created Users, utilized in the analysis section of the Admin Panel */
	public static List<User> getCreatedUsers() {
		return createdUsers;
	}

	@Override
	public String toString() {
		return getName();
	}

	/** 
	 * accept() function for the User updates the total User count, and news feed count.
	 */
	@Override
	public void accept(AnalysisVisitor visitor) {
		visitor.visitUser(this);
		visitor.visitNewsFeed(this.newsFeed);
	}
}