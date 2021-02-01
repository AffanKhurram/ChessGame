import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Chess extends JComponent implements MouseListener {
	JFrame f;
    Tiles[] tiles;
    String currentBoard;
    String[] names;
    boolean selected = false;
    int[] legalMoves;
    int lastIndex;
    boolean first = true;
    boolean player1 = true;
    boolean over = false;
    String lastBoard;
    AI ai;
    boolean withAi;
    
	public Chess (JFrame _f, AI a) {
		f = _f;
		tiles = new Tiles[64];
		addMouseListener(this);
		currentBoard = "r2n2b2q2k2b2n2r2p2p2p2p2p2p2p2p2";
		player1 = true;
		for (int i=0; i<32; i++)
			currentBoard += "bb";
		currentBoard += "p1p1p1p1p1p1p1p1r1n1b1q1k1b1n1r1";
		String[] n = {"bishop.png", "king.png", "knight.png", "queen.png", "rook.png", "white_bishop.png", "white_king.png", "white_knight.png", "white_queen.png", "white_rook.png"};
		first = true;
		ai = a;
		String input = JOptionPane.showInputDialog(null, "Do you want to play against the ai? (yes/no)");
		withAi = input.toUpperCase().equals("YES");
	}
	
	public void paintComponent(Graphics g) {
		drawBoard(g);
		if (over) {
			g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
			g.drawString(Chess.inCheck(Chess.createString(tiles), player1)? "Checkmate":"Stalemate", 100, 200);
		}
		if (!over && !player1 && withAi) {
			aiMove();
		}
	}
	
	public void drawBoard(Graphics g) {
    	int val = 0;
    	if (first) {
    		first = false;
    		for (int i=0, k=0; i<8; i++) {
    			for(int j=0; j<8; j++, k++) {
    				Color c;
    				if (val%2 == 1) {
    					c = new Color(0, 102, 0);
    				}
    				else {
    					c = Color.white;
    				}
    				tiles[k] = new Tiles(j*75+10, i*75+10, 75, c, getPiece(currentBoard.substring(k*2, k*2+2), 75*j+10, 75*i+10));
    				val++;
    			}
    			val++;
    		}
    		lastBoard = createString(tiles);
    	}
    	
    	for (int i=0; i<tiles.length; i++) {
    		tiles[i].draw(g);
    	}
    	
    	if (selected) {
    		for (int i=0; i<legalMoves.length; i++) {
    			int index = legalMoves[i];
    			int x = (index%8)*75+20;
    			int y = (((index/8)*75)+20);
    			g.setColor(Color.GRAY);
    			g.fillOval(x+20, y+20, 15, 15);
    		}
    	}
    	
    	g.setColor(Color.black);
    	g.drawRect(10, 10, 75*8, 75*8);
    }
    
    public void mouseClicked(MouseEvent e) {
    	int x=e.getX();
    	int y=e.getY();
    	if (over)
    		return;
    	for (int i=0; i<tiles.length; i++) {
    		if (tiles[i].isClicked(x, y)) {
    			if (tiles[i].p != null && !selected) {
    				if (player1 && !tiles[i].p.white)
    					break;
    				if (!player1 && tiles[i].p.white)
    					break;
    			}
    			
    			if (!selected && tiles[i].p != null) {
    				selected = true;
        			legalMoves = tiles[i].p.getLegalMoves();
        			lastIndex = i;
    			}
    			else if (selected && contains(legalMoves, i)) {
    				player1 = !player1;
    				selected = false;
    				//CHANGE THE BOARD CONDITION
    				lastBoard = createString(tiles);
    				boolean hasTaken = tiles[i].p != null;
    				Piece temp = tiles[lastIndex].p;
    				tiles[lastIndex].p = null;
    				tiles[i].p = temp;
    				tiles[i].p.x = tiles[i].x;
    				tiles[i].p.y = tiles[i].y;
    				tiles[i].p.hasMoved = true;
    				if (tiles[i].p instanceof King) {
    					if (lastIndex - i == -2) {
    						temp = tiles[lastIndex+3].p;
    	    				tiles[lastIndex+3].p = tiles[i-1].p;
    	    				tiles[i-1].p = temp;
    	    				tiles[i-1].p.x = tiles[i-1].x;
    	    				tiles[i-1].p.y = tiles[i-1].y;
    	    				tiles[i-1].p.hasMoved = true;
    					}
    					else if (lastIndex - i == 2) {
    						temp = tiles[lastIndex-4].p;
    	    				tiles[lastIndex-4].p = tiles[i+1].p;
    	    				tiles[i+1].p = temp;
    	    				tiles[i+1].p.x = tiles[i+1].x;
    	    				tiles[i+1].p.y = tiles[i+1].y;
    	    				tiles[i+1].p.hasMoved = true;
    					}
    				}
    				if (tiles[i].p != null && tiles[i].p.symbol.contains("p") && !hasTaken) {
    					int lr = lastIndex/8;
    					int lc = lastIndex%8;
    					int r = i/8;
    					int c = i%8;
    					if (lr - r == 1 && lc-c == -1) {
    						tiles[lastIndex+1].p = null;
    					}
    					else if (lr - r == 1 && lc-c == 1) {
    						tiles[lastIndex-1].p = null;
    					}
    					else if (lr - r == -1 && lc-c == 1) {
    						tiles[lastIndex-1].p = null;
    					}
    					else if (lr - r == -1 && lc-c == -1) {
    						tiles[lastIndex+1].p = null;
    					}
    				}
    				if (tiles[i].p != null && tiles[i].p.symbol.contains("p") && ((i >= 0 && i <=7) || (i>=56 && i<=63))) {
    					player1 = true;
    					String input = JOptionPane.showInputDialog(null, "Enter first letter of piece you want to promote it to");
    					while (true) {
    						if (input.toUpperCase().equals("Q")) {
        						tiles[i].p = new Queen(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    							aiMove();
        						break;
    						}
        					else if (input.toUpperCase().equals("K")) {
        						tiles[i].p = new Knight(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    							aiMove();
        						break;
        					}
        					else if (input.toUpperCase().equals("R")) {
        						tiles[i].p = new Rook(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    							aiMove();
        						break;
        					}
        					else if (input.toUpperCase().equals("B")) {
        						tiles[i].p = new Bishop(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
    							aiMove();
        						break;
        					}
        					else
        						input = JOptionPane.showInputDialog(null, "Error enter again");
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
        		else if (selected && !contains(legalMoves, i))
        			selected = false;
    			
    		}
    	}
    	String board = createString(tiles);
		if (!board.contains("n") && !board.contains("r") && !board.contains("p") && !board.contains("b1") && !board.contains("b2") && !board.contains("q"))
			over = true;
		repaint();
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
    	// KING
    	int[] rmoves1 = {-1,-1,-1,0,0,1,1,1};
		int[] cmoves1 = {-1,0,1,-1,1,-1,0,1};
		c = (index/2)%8;
    	r = (index/16);
    	for (int i=0; i<rmoves1.length; i++) {
    		int tc = c + cmoves1[i];
    		int tr = r + rmoves1[i];
    		if (tr>=0 && tr<=7 && tc>=0 && tc<=7) {
    			int ti = tr*16+2*tc;
    			if (!board.substring(ti, ti+2).equals("bb")) {
    				if (white) {
    					if(board.substring(ti, ti+2).equals("k2"))
    						return true;
    				}
    				else {
    					if(board.substring(ti, ti+2).equals("k1"))
    						return true;
    				}
    			}
    		}
    	}
    	
    	return false;
    }
    
    public static String changeBoard(int lastIndex, int i, String cb) {
    	String smbl = cb.substring(lastIndex*2, lastIndex*2+2);
    	cb = cb.substring(0, lastIndex*2) + "bb" + cb.substring(lastIndex*2+2);
    	cb = cb.substring(0, i*2) + smbl + cb.substring(i*2+2);
    	return cb;
    }
    
    public static String createString(Tiles[] t) {
    	String r = "";
    	for (int i=0; i<t.length; i++) {
    		if (t[i].p == null)
    			r+="bb";
    		else
    			r += t[i].p.symbol;
    	}
    	return r;
    }
    
    public void aiMove () {
    	/**
    	 * "Smart" way is very slow
    	 *
    	 * **/
    	/*
    	Board nextMove = ai.makeMove();
    	if (nextMove == null) {
    		ArrayList<Tiles> asdf = new ArrayList<Tiles>();
    		ArrayList<Integer> ogIndex = new ArrayList<Integer>();
    		for (int j=0; j<tiles.length; j++) {
    			if (tiles[j].p != null && !tiles[j].p.white && tiles[j].p.getLegalMoves().length != 0) {
    				asdf.add(tiles[j]);
    				ogIndex.add(j);
    			}
    		}
    		ai.randomMove(asdf, ogIndex);
    	}
    	else {
    		movePiece(nextMove);
    	}*/
		
    	ArrayList<Tiles> asdf = new ArrayList<Tiles>();
		ArrayList<Integer> ogIndex = new ArrayList<Integer>();
		for (int j=0; j<tiles.length; j++) {
			if (tiles[j].p != null && !tiles[j].p.white && tiles[j].p.getLegalMoves().length != 0) {
				asdf.add(tiles[j]);
				ogIndex.add(j);
			}
		}
		ai.randomMove(asdf, ogIndex);
    	player1 = true;
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
    	/*Piece p = t[i].p;
    	t[i].p = null;
    	t[i2].p = p;*/
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
		lastBoard = createString(tiles);
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
		if (tiles[i].p != null && tiles[i].p.symbol.contains("p") && ((i >= 0 && i <=7) || (i>=56 && i<=63))) {
    		tiles[i].p = new Queen(tiles[i].p.x, tiles[i].p.y, tiles[i].p.white);
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
		repaint();
    }
    
    public void movePiece(Board b) {
    	tiles = b.tiles;
    	lastBoard = b.lastBoard;
    	player1 = !player1;
    }
    
    public void mouseExited(MouseEvent e){
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
}
