import java.util.*;


public class TreeNode {
	Board b;
	ArrayList<TreeNode> nextNodes;
	TreeNode lastNode;
	
	public TreeNode (Board _b) {
		b = _b;
		nextNodes = null;
	}
	
	public TreeNode (Board _b, TreeNode last) {
		b = _b;
		nextNodes = null;
		lastNode = last;
	}
}