import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Board {
    Tiles[] tiles;
    String currentBoard;
    int[] legalMoves;
    int lastIndex;
    boolean first = true;
    boolean player1 = true;
    boolean over = false;
    String lastBoard;
    
	public Board (Tiles[] t, boolean player) {
		tiles = new Tiles[t.length];
		for (int i=0; i<t.length; i++)
			tiles[i] = new Tiles(t[i]);
		lastBoard = Chess.createString(tiles);
		player1 = player;
	}
    
    public static boolean inCheck(String board, boolean white) {
    	//Find the king
    	int index = 0;
    	for (int i=0; i<board.length(); i+=2){
    		if (white) {
    			if (board.substring(i, i+2).equals("k1")){
    				index = i;
    				break;
    			}
    		}
    		else {
    			if (board.substring(i, i+2).equals("k2")){
    				index = i;
    				break;
    			}
    		}
    	}
    	
    	int c = (index/2)%8+1;
    	int r = (index/16)+1;
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("b2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("b1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		r++;
    		c++;
    	}
    	c = (index/2)%8-1;
    	r = (index/16)+1;
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("b2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("b1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		r++;
    		c--;
    	}
    	c = (index/2)%8+1;
    	r = (index/16)-1;
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("b2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("b1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		r--;
    		c++;
    	}
    	c = (index/2)%8-1;
    	r = (index/16)-1;
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("b2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("b1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		r--;
    		c--;
    	}
    	//	ROOK
    	c = (index/2)%8+1;
    	r = (index/16);
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("r2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("r1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		c++;
    	}
    	c = (index/2)%8-1;
    	r = (index/16);
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("r2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("r1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		c--;
    	}
    	c = (index/2)%8;
    	r = (index/16)+1;
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("r2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("r1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		r++;
    	}
    	c = (index/2)%8;
    	r = (index/16)-1;
    	while (r>=0 && r<=7 && c>=0 && c<=7){
    		int ti = r*16+2*c;
    		if (!board.substring(ti, ti+2).equals("bb")) {
    			if (white) {
    				if (board.substring(ti, ti+2).equals("r2") || board.substring(ti, ti+2).equals("q2"))
    					return true;
    			}
    			else {
    				if (board.substring(ti, ti+2).equals("r1") || board.substring(ti, ti+2).equals("q1"))
    					return true;
    			}
				break;
    		}
    		r--;
    	}
    	//KNIGHTS
    	int[] rmoves = {-1,-1,1,1,-2,-2,2,2};
		int[] cmoves = {-2,2,2,-2,1,-1,1,-1};
		c = (index/2)%8;
    	r = (index/16);
    	for (int i=0; i<rmoves.length; i++) {
    		int tc = c + cmoves[i];
    		int tr = r + rmoves[i];
    		if (tr>=0 && tr<=7 && tc>=0 && tc<=7) {
    			int ti = tr*16+2*tc;
    			if (!board.substring(ti, ti+2).equals("bb")) {
    				if (white) {
    					if(board.substring(ti, ti+2).equals("n2"))
    						return true;
    				}
    				else {
    					if(board.substring(ti, ti+2).equals("n1"))
    						return true;
    				}
    			}
    		}
    	}
    	//PANWS
    	c = (index/2)%8;
    	r = (index/16);
    	if(white) {
    		int tc = c-1;
    		int tr = r-1;
    		if (tr>=0 && tr<=7 && tc>=0 && tc<=7) {
    			int ti = tr*16+2*tc;
    			if (board.substring(ti, ti+2).equals("p2"))
    				return true;
    		}
    		tc = c+1;
    		if (tr>=0 && tr<=7 && tc>=0 && tc<=7) {
    			int ti = tr*16+2*tc;
    			if (board.substring(ti, ti+2).equals("p2"))
    				return true;
    		}
    	}
    	else {
    		int tc = c-1;
    		int tr = r+1;
    		if (tr>=0 && tr<=7 && tc>=0 && tc<=7) {
    			int ti = tr*16+2*tc;
    			if (board.substring(ti, ti+2).equals("p1"))
    				return true;
    		}
    		tc = c+1;
    		if (tr>=0 && tr<=7 && tc>=0 && tc<=7) {
    			int ti = tr*16+2*tc;
    			if (board.substring(ti, ti+2).equals("p1"))
    				return true;
    		}
    	}
    	return false;
    }
    
    public Piece getPiece(String n, int x, int y) {
    	switch(n) {
    		case "r1": return new Rook(x, y, true);
    		case "r2": return new Rook(x, y, false);
    		case "b1": return new Bishop(x, y, true);
    		case "b2": return new Bishop(x, y, false);
    		case "n1": return new Knight(x, y, true);
    		case "n2": return new Knight(x, y, false);
    		case "q1": return new Queen(x, y, true);
    		case "q2": return new Queen(x, y, false);
    		case "k1": return new King(x, y, true);
    		case "k2": return new King(x, y, false);
    		case "p1": return new Pawn(x, y, true);
    		case "p2": return new Pawn(x, y, false);
    		default: return null;
    	}
    }
    
    public static void swap (Tiles[] t, int i, int i2) {
    	Tiles temp = t[i];
    	t[i] = t[i2];
    	t[i2] = temp;
    }

    public boolean contains(int[] arr, int k) {
    	for (int i=0; i<arr.length; i++) {
    		if (arr[i] == k)
    			return true;
    	}
    	return false;
    }
    
    public void movePiece (int l, int i) {
		//CHANGE THE BOARD CONDITION
		lastBoard = Chess.createString(tiles);
		boolean hasTaken = tiles[i].p != null;
		Piece temp = tiles[l].p;
		tiles[l].p = null;
		tiles[i].p = temp;
		tiles[i].p.x = tiles[i].x;
		tiles[i].p.y = tiles[i].y;
		tiles[i].p.hasMoved = true;
		if (tiles[i].p instanceof King) {
			if (l - i == -2) {
				temp = tiles[l+3].p;
				tiles[l+3].p = tiles[i-1].p;
				tiles[i-1].p = temp;
				tiles[i-1].p.x = tiles[i-1].x;
				tiles[i-1].p.y = tiles[i-1].y;
				tiles[i-1].p.hasMoved = true;
			}
			else if (l - i == 2) {
				temp = tiles[l-4].p;
				tiles[l-4].p = tiles[i+1].p;
				tiles[i+1].p = temp;
				tiles[i+1].p.x = tiles[i+1].x;
				tiles[i+1].p.y = tiles[i+1].y;
				tiles[i+1].p.hasMoved = true;
			}
		}
		if (tiles[i].p != null && tiles[i].p.symbol.contains("p") && !hasTaken) {
			int lr = l/8;
			int lc = l%8;
			int r = i/8;
			int c = i%8;
			if (lr - r == 1 && lc-c == -1) {
				tiles[l+1].p = null;
			}
			else if (lr - r == 1 && lc-c == 1) {
				tiles[l-1].p = null;
			}
			else if (lr - r == -1 && lc-c == 1) {
				tiles[l-1].p = null;
			}
			else if (lr - r == -1 && lc-c == -1) {
				tiles[l+1].p = null;
			}
		}
		
		int sum = 0;
		for (int j=0; j<tiles.length; j++) {
			if (tiles[i].p != null && tiles[j].p != null && tiles[i].p.white == !tiles[j].p.white)
				sum += tiles[j].p.getLegalMoves().length;
		}
		if (sum == 0)
			over = true;
    }
    
    public void movePiece (int l, int i, String promoteTo) {
		//CHANGE THE BOARD CONDITION
		lastBoard = Chess.createString(tiles);
		Piece temp = tiles[l].p;
		tiles[l].p = null;
		tiles[i].p = temp;
		tiles[i].p.x = tiles[i].x;
		tiles[i].p.y = tiles[i].y;
		tiles[i].p.hasMoved = true;
		if (tiles[i].p instanceof King) {
			if (l - i == -2) {
				temp = tiles[l+3].p;
				tiles[l+3].p = tiles[i-1].p;
				tiles[i-1].p = temp;
				tiles[i-1].p.x = tiles[i-1].x;
				tiles[i-1].p.y = tiles[i-1].y;
				tiles[i-1].p.hasMoved = true;
			}
			else if (l - i == 2) {
				temp = tiles[l-4].p;
				tiles[l-4].p = tiles[i+1].p;
				tiles[i+1].p = temp;
				tiles[i+1].p.x = tiles[i+1].x;
				tiles[i+1].p.y = tiles[i+1].y;
				tiles[i+1].p.hasMoved = true;
			}
		}
		if (tiles[i].p != null && tiles[i].p.symbol.contains("p")) {
			int lr = l/8;
			int lc = l%8;
			int r = i/8;
			int c = i%8;
			if (lr - r == 1 && lc-c == -1) {
				tiles[l+1].p = null;
			}
			else if (lr - r == 1 && lc-c == 1) {
				tiles[l-1].p = null;
			}
			else if (lr - r == -1 && lc-c == 1) {
				tiles[l-1].p = null;
			}
			else if (lr - r == -1 && lc-c == -1) {
				tiles[l+1].p = null;
			}
		}
		
		if (tiles[i].p != null && tiles[i].p.symbol.contains("p") && ((i >= 0 && i <=7) || (i>=56 && i<=63))) {
    		String input = promoteTo;
    		if (input.toUpperCase().equals("Q"))
    			tiles[i].p = new Queen(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    		else if (input.toUpperCase().equals("K"))
    			tiles[i].p = new Knight(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    		else if (input.toUpperCase().equals("R"))
    			tiles[i].p = new Rook(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    		else if (input.toUpperCase().equals("B"))
    			tiles[i].p = new Bishop(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    	}
		
		int sum = 0;
		for (int j=0; j<tiles.length; j++) {
			if (tiles[i].p != null && tiles[j].p != null && tiles[i].p.white == !tiles[j].p.white)
				sum += tiles[j].p.getLegalMoves().length;
		}
		if (sum == 0)
			over = true;
    }
    
    public ArrayList<TreeNode> getAllBoardStates (TreeNode currentNode) {
    	ArrayList<TreeNode> ret = new ArrayList<TreeNode>();
    	for (int i=0; i<tiles.length; i++) {
    		if (tiles[i].p != null && tiles[i].p.white == player1) {
    			Board[] temp = tiles[i].p.allPossibleStates(i, tiles);
    			for (int j=0; j<temp.length; j++) {
    				ret.add(new TreeNode(new Board(temp[j].tiles, !temp[j].player1), currentNode));
    			}
    		}
    	}
    	
    	return ret;
    }
    
    public int eval () {
    	int sum = 0;
    	for (int i=0; i<tiles.length; i++) {
    		if (tiles[i].p != null) 
    			sum += player1? -tiles[i].p.val : tiles[i].p.val;
    	}
    	return sum;
    }
    
}
