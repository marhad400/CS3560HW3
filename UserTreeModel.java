import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/** 
 * The UserTreeModel takes in root TreeNode, in this project a UserTreeNode.
 * The Model helps construct the entire user tree, where we initialize the Model and tree from
 * our root in the admin panel.
 */
public class UserTreeModel extends DefaultTreeModel
{
	public UserTreeModel(TreeNode root) {
		super(root);
	}
}
