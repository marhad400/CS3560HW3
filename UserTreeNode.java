import javax.swing.tree.DefaultMutableTreeNode;

/**
 * UserTreeNode is used for the UserTree in the admin panel. It is the class that constructs the tree itself,
 * but within a node can be accepted a User, or a UserGroup. If the object accept is a User, it is a leaf node of the tree,
 * where this logic helps construct the tree's display.
 */
public class UserTreeNode extends DefaultMutableTreeNode
{
	private User userObject;
	private UserGroup userGroupObject;

	public UserTreeNode(User userObject) {
		super(userObject);
		this.userObject = userObject;
	}

	public UserTreeNode(UserGroup userGroupObject) {
		super(userGroupObject);
		this.userGroupObject = userGroupObject;
	}

	public User getUser() {
		return userObject;
	}

	public UserGroup getUserGroup() {
		return userGroupObject;
	}

	@Override
	public boolean isLeaf() {
		return !(getUserObject() instanceof UserGroup);
	}
}