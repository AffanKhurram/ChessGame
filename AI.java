/**
 * @(#)AI.java
 *
 *
 * @author 
 * @version 1.00 2018/5/18
 */
import java.util.*;

public class AI {
	boolean white;
	int plyChecks = 2;
	
    public AI (boolean w) {
    	white = w;
    }
    
    public void randomMove (ArrayList<Tiles> t, ArrayList<Integer> og) {
    	Random random = new Random();
    	int rand = random.nextInt(t.size());
    	int index = random.nextInt(t.get(rand).p.getLegalMoves().length);
        Main.c.movePiece(og.get(rand), t.get(rand).p.getLegalMoves()[index]);
    }
    
    public Board makeMove () {
    	Tiles[] ogTiles = Main.c.tiles;
        TreeNode root = new TreeNode(new Board(ogTiles, false), null);
        root.nextNodes = root.b.getAllBoardStates(root);
        for (int i=0; i<root.nextNodes.size(); i++) {
        	TreeNode temp = root.nextNodes.get(i);
        	temp.nextNodes = temp.b.getAllBoardStates(temp);
        }
        int largest = -1111;
       	int index = 0;
        	
        for (int i=0; i<root.nextNodes.size(); i++) {
        	int t = alphabeta(root.nextNodes.get(i), plyChecks-1, -1000, 1000, true);
        	if (t > largest) {
        		largest = t;
        		index = i;
        	}
       	}
        	
        if (largest == -1111) {
        	return null;
       	}
        else {
       		return root.nextNodes.get(index).b;
       	}
    	
    }
    
    public int alphabeta(TreeNode node, int depth, int a, int b, boolean maximizingPlayer) {
    	if (depth == 0 || node.nextNodes == null) {
    		return node.b.eval();
    	}
    	
    	if (maximizingPlayer) {
    		int v = -1000;
    		for (int i=0; i<node.nextNodes.size(); i++) {
    			v = Math.max(v, alphabeta(node.nextNodes.get(i), depth-1, a, b, false));
    			a = Math.max(a, v);
    			if (b <= a) {
    				break;
    			}
    		}
    		return v;
    	}
    	else {
    		int v = 1000;
    		for (int i=0; i<node.nextNodes.size(); i++) {
    			v = Math.max(v, alphabeta(node.nextNodes.get(i), depth-1, a, b, true));
    			b = Math.max(a, v);
    			if (b <= a) {
    				break;
    			}
    		}
    		return v;
    	}
    }
    
    // figure this out later, right now hardcoded
    public TreeNode createTree(Tiles[] t, boolean white) {
    	TreeNode root = new TreeNode(new Board(t, white));
    	
    	
    	return root;
    }
}