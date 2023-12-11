import java.util.List;
import java.util.ArrayList;

/**
 * UserGroup class that structures the Composite design pattern, by containing lists of
 * both Users (leaves), and UserGroups (composites), thus enabling the creation of the recursive, 
 * tree-like structure of the Composite pattern
 */
public class UserGroup implements UserInterface
{
	private String groupName;
	private List<UserInterface> userList;

	private static List<UserGroup> createdGroups = new ArrayList<>();
	private long creationTime;

	/** Constructor for the UserGroup, initializing the ID
	 * and list of UserInterfaces
	 */
	public UserGroup(String groupName) {
		setID(groupName);
		userList = new ArrayList<>();
		createdGroups.add(this);
	}

	/** Group name setter method */
	public void setID(String groupName) {
		this.groupName = groupName;
	}

	/** Group name getter method */
	public String getName() {
		return groupName;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	/** Adds a user to the UserInterface list */
	public void addUser(User user) {
		userList.add(user);
	}

	/** Adds a UserGroup to the UserInterface list */
	public void addGroup(UserGroup group) {
		userList.add(group);
	}

	/** Returns the UserInterface list */
	public List<UserInterface> getUserList() {
		return userList;
	}

	public static List<UserGroup> getCreatedGroups() {
		return createdGroups;
	}

	@Override
	public String toString() {
		return getName();
	}

	/** accept() method for UserGroup updates the group count,
	 * and recursively calls the function for the potential users and groups
	 * within this group.
	 * This function is key for the Visitor pattern implementation of the project.
	 * By recursively calling this function from the root node, we are able to visit all users
	 * and groups, and collect the stats needed for the analysis buttons.
	 */
	@Override
	public void accept(AnalysisVisitor visitor) {
		visitor.visitUserGroup(this);
		for (UserInterface user : userList) {
			user.accept(visitor);
		}
	}
}
