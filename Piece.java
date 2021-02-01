import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public abstract class Piece {
	int x, y;
	Image i;
	String symbol;
	boolean white;
	boolean hasMoved;
	int val;
	
	public Piece (int _x, int _y) {
		x = _x;
		y = _y;
		hasMoved = false;
	}
	
	public Piece (int _x, int _y, Image _i) {
		x = _x;
		y = _y;
		i = _i;
		hasMoved = false;
	}
	
	public Piece (Piece p) {
		if (p == null)
			return;
		x = p.x;
		y = p.y;
		symbol = p.symbol;
		white = p.white;
		hasMoved = p.hasMoved;
		val = p.val;
	}
	
	public int[] getLegalMoves () {
		return null;
	}
	
	public int[] getLegalMoves (Tiles[] t, int currentI) {
		return null;
	}
	
	public String toString () {
		return symbol;
	}
	
	public abstract Board[] allPossibleStates(int currentIndex, Tiles[] t);
}

class Knight extends Piece {
	public Knight (int _x, int _y, boolean white) {
		super(_x,_y);
		super.symbol = white? "n1":"n2";
		super.white = white;
		val = 3;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\knight.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_knight.png"));
		} catch (IOException e) {
			
		}
	}
	
	public Knight (Piece p) {
		super(p);
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\knight.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_knight.png"));
		} catch (IOException e) {
			
		}
	}
	
	public int[] getLegalMoves() {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = (y-10)/75;
		int c = (x-10)/75;
		
		int[] rmoves = {-1,-1,1,1,-2,-2,2,2};
		int[] cmoves = {-2,2,2,-2,1,-1,1,-1};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white)
						m.add(add);
				}
				else
					m.add(add);
			}
		}
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Tiles[] tempBoard = new Tiles[Main.c.tiles.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = Main.c.tiles[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	public int[] getLegalMoves(Tiles[] t, int currentI) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = currentI/8;
		int c = currentI%8;
		
		int[] rmoves = {-1,-1,1,1,-2,-2,2,2};
		int[] cmoves = {-2,2,2,-2,1,-1,1,-1};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white)
						m.add(add);
				}
				else
					m.add(add);
			}
		}
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = currentI;
			Tiles[] tempBoard = new Tiles[t.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = t[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	
	@Override
	public Board[] allPossibleStates (int currentIndex, Tiles[] c) {
		
		int[] indexes = getLegalMoves(c, currentIndex);
		Board[] b = new Board[indexes.length];
		
		for (int i=0; i<indexes.length; i++) {
			Board temp = new Board(c, white);
			temp.movePiece(currentIndex, indexes[i]);
			b[i] = temp;
		}
		return b;
	}
}

class Bishop extends Piece {
	public Bishop (int _x, int _y, boolean white) {
		super(_x,_y);
		super.symbol = white? "b1":"b2";
		super.white = white;
		val = 3;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\bishop.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_bishop.png"));
		} catch (IOException e) {
			
		}
	}
	
	public Bishop (Piece p) {
		super(p);
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\bishop.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_bishop.png"));
		} catch (IOException e) {
			
		}
	}
	
	public int[] getLegalMoves () {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = (y-10)/75;
		int c = (x-10)/75;
		
		int[] rmoves = {1,2,3,4,5,6,7};
		int[] cmoves = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves2 = {1,2,3,4,5,6,7};
		int[] cmoves2 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves2[i];
			int tc = c+cmoves2[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves3[i];
			int tc = c+cmoves3[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves4 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves4 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves4[i];
			int tc = c+cmoves4[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Tiles[] tempBoard = new Tiles[Main.c.tiles.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = Main.c.tiles[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	public int[] getLegalMoves (Tiles[] t, int currentI) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = currentI/8;
		int c = currentI%8;
		
		int[] rmoves = {1,2,3,4,5,6,7};
		int[] cmoves = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[i].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves2 = {1,2,3,4,5,6,7};
		int[] cmoves2 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves2[i];
			int tc = c+cmoves2[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves3[i];
			int tc = c+cmoves3[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves4 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves4 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves4[i];
			int tc = c+cmoves4[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = currentI;
			Tiles[] tempBoard = new Tiles[t.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = t[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	@Override
	public Board[] allPossibleStates (int currentIndex, Tiles[] c) {
		
		
		int[] indexes = getLegalMoves(c, currentIndex);
		Board[] b = new Board[indexes.length];
		
		for (int i=0; i<indexes.length; i++) {
			Board temp = new Board(c, white);
			temp.movePiece(currentIndex, indexes[i]);
			b[i] = temp;
		}
		return b;
	}
}

class Rook extends Piece {
	public Rook (int _x, int _y, boolean white) {
		super(_x,_y);
		super.symbol = white? "r1":"r2";
		super.white = white;
		val = 5;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\rook.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_rook.png"));
		} catch (IOException e) {
			
		}
	}
	
	public Rook (Piece p) {
		super(p);
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\rook.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_rook.png"));
		} catch (IOException e) {
			
		}
	}
	
	public int[] getLegalMoves () {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = (y-10)/75;
		int c = (x-10)/75;
		
		int[] rmoves = {1,2,3,4,5,6,7};
		int[] cmoves = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves2 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves2 = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves2[i];
			int tc = c+cmoves2[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves3 = {0,0,0,0,0,0,0};
		int[] cmoves3 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves3[i];
			int tc = c+cmoves3[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves4 = {0,0,0,0,0,0,0};
		int[] cmoves4 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves4[i];
			int tc = c+cmoves4[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Tiles[] tempBoard = new Tiles[Main.c.tiles.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = Main.c.tiles[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	public int[] getLegalMoves(Tiles[] t, int currentI) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = currentI/8;
		int c = currentI%8;
		
		int[] rmoves = {1,2,3,4,5,6,7};
		int[] cmoves = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves2 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves2 = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves2[i];
			int tc = c+cmoves2[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves3 = {0,0,0,0,0,0,0};
		int[] cmoves3 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves3[i];
			int tc = c+cmoves3[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves4 = {0,0,0,0,0,0,0};
		int[] cmoves4 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves4[i];
			int tc = c+cmoves4[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = currentI;
			Tiles[] tempBoard = new Tiles[t.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = t[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	@Override
	public Board[] allPossibleStates (int currentIndex, Tiles[] c) {
		
		
		int[] indexes = getLegalMoves(c, currentIndex);
		Board[] b = new Board[indexes.length];
		
		for (int i=0; i<indexes.length; i++) {
			Board temp = new Board(c, white);
			temp.movePiece(currentIndex, indexes[i]);
			b[i] = temp;
		}
		return b;
	}
}

class King extends Piece {
	public King (int _x, int _y, boolean white) {
		super(_x,_y);
		super.symbol = white? "k1":"k2";
		super.white = white;
		val = 0;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\king.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_king.png"));
		} catch (IOException e) {
			
		}
	}
	
	public King (Piece p) {
		super(p);
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\king.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_king.png"));
		} catch (IOException e) {
			
		}
	}
	
	public int[] getLegalMoves () {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = (y-10)/75;
		int c = (x-10)/75;
		
		int[] rmoves = {1,1,1,-1,-1,-1,0,0};
		int[] cmoves = {-1,0,1,-1,0,1,-1,1};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
					}
				}
				else
					m.add(add);
			}
		}
		
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Tiles[] tempBoard = new Tiles[Main.c.tiles.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = Main.c.tiles[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		//CASTLING
		if (!Chess.inCheck(Chess.createString(Main.c.tiles), white) && !hasMoved) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Piece temp = Main.c.tiles[currentIndex-4].p;
			if (m.contains(currentIndex-1) && temp != null && temp.symbol.contains("r") && !temp.hasMoved) {
				m.add(currentIndex-2);
			}
			temp = Main.c.tiles[currentIndex+3].p;
			if (m.contains(currentIndex+1) && temp != null && temp instanceof Rook && !temp.hasMoved) {
				m.add(currentIndex+2);
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}

	public int[] getLegalMoves (Tiles[] t, int currentI) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = currentI/8;
		int c = currentI%8;
		
		int[] rmoves = {1,1,1,-1,-1,-1,0,0};
		int[] cmoves = {-1,0,1,-1,0,1,-1,1};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
					}
				}
				else
					m.add(add);
			}
		}
		
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = currentI;
			Tiles[] tempBoard = new Tiles[t.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = t[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		//CASTLING
		if (!Chess.inCheck(Chess.createString(t), white) && !hasMoved) {
			int currentIndex = currentI;
			Piece temp = t[currentIndex-4].p;
			if (m.contains(currentIndex-1) && temp != null && temp.symbol.contains("r") && !temp.hasMoved) {
				m.add(currentIndex-2);
			}
			temp = t[currentIndex+3].p;
			if (m.contains(currentIndex+1) && temp != null && temp instanceof Rook && !temp.hasMoved) {
				m.add(currentIndex+2);
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	@Override
	public Board[] allPossibleStates (int currentIndex, Tiles[] c) {
		
		
		int[] indexes = getLegalMoves(c, currentIndex);
		Board[] b = new Board[indexes.length];
		
		for (int i=0; i<indexes.length; i++) {
			Board temp = new Board(c, white);
			temp.movePiece(currentIndex, indexes[i]);
			b[i] = temp;
		}
		return b;
	}
}

class Queen extends Piece {
	public Queen (int _x, int _y, boolean white) {
		super(_x,_y);
		super.symbol = white? "q1":"q2";
		super.white = white;
		val = 9;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\queen.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_queen.png"));
		} catch (IOException e) {
			
		}
	}
	
	public Queen (Piece p) {
		super(p);
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\queen.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_queen.png"));
		} catch (IOException e) {
			
		}
	}
	
	public int[] getLegalMoves () {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = (y-10)/75;
		int c = (x-10)/75;
		
		int[] rmoves = {1,2,3,4,5,6,7};
		int[] cmoves = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves2 = {1,2,3,4,5,6,7};
		int[] cmoves2 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves2[i];
			int tc = c+cmoves2[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves3[i];
			int tc = c+cmoves3[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves4 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves4 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves4[i];
			int tc = c+cmoves4[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves5 = {1,2,3,4,5,6,7};
		int[] cmoves5 = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves5[i];
			int tc = c+cmoves5[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves6 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves6 = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves6[i];
			int tc = c+cmoves6[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves7 = {0,0,0,0,0,0,0};
		int[] cmoves7 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves7[i];
			int tc = c+cmoves7[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves8 = {0,0,0,0,0,0,0};
		int[] cmoves8 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves8[i];
			int tc = c+cmoves8[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Tiles[] tempBoard = new Tiles[Main.c.tiles.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = Main.c.tiles[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	public int[] getLegalMoves (Tiles[] t, int currentI) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = currentI/8;
		int c = currentI%8;
		
		int[] rmoves = {1,2,3,4,5,6,7};
		int[] cmoves = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves[i];
			int tc = c+cmoves[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves2 = {1,2,3,4,5,6,7};
		int[] cmoves2 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves2[i];
			int tc = c+cmoves2[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves3 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves3[i];
			int tc = c+cmoves3[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves4 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves4 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves4[i];
			int tc = c+cmoves4[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves5 = {1,2,3,4,5,6,7};
		int[] cmoves5 = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves5[i];
			int tc = c+cmoves5[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves6 = {-1,-2,-3,-4,-5,-6,-7};
		int[] cmoves6 = {0,0,0,0,0,0,0};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves6[i];
			int tc = c+cmoves6[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves7 = {0,0,0,0,0,0,0};
		int[] cmoves7 = {1,2,3,4,5,6,7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves7[i];
			int tc = c+cmoves7[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		int[] rmoves8 = {0,0,0,0,0,0,0};
		int[] cmoves8 = {-1,-2,-3,-4,-5,-6,-7};
		
		for (int i=0; i<rmoves.length; i++) {
			int tr = r+rmoves8[i];
			int tc = c+cmoves8[i];
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white) {
						m.add(add);
						break;
					}
					break;
				}
				else
					m.add(add);
			}
		}
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = currentI;
			Tiles[] tempBoard = new Tiles[t.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = t[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	@Override
	public Board[] allPossibleStates (int currentIndex, Tiles[] c) {
		
		
		int[] indexes = getLegalMoves();
		Board[] b = new Board[indexes.length];
		
		for (int i=0; i<indexes.length; i++) {
			Board temp = new Board(c, white);
			temp.movePiece(currentIndex, indexes[i]);
			b[i] = temp;
		}
		return b;
	}
}

class Pawn extends Piece {
	public Pawn (int _x, int _y, boolean white) {
		super(_x,_y);
		super.symbol = white? "p1":"p2";
		super.white = white;
		val = 1;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\pawn.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_pawn.png"));
		} catch (IOException e) {
		}
	}
	
	public Pawn (Piece p) {
		super(p);
		val = 1;
		try {
			if (!white)
				super.i = ImageIO.read(new File("chess pieces\\pawn.png"));
			else
				super.i = ImageIO.read(new File("chess pieces\\white_pawn.png"));
		} catch (IOException e) {
			
		}
	}
	
	public int[] getLegalMoves() {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = (y-10)/75;
		int c = (x-10)/75;
		
		if (white) {
			boolean canDoTwo = false;
			int tr = r-1;
			int tc = c;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p == null) {
					m.add(add);
					canDoTwo = true;
				}
			}
			
			tc = c+1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white)
						m.add(add);
				}
			}
			
			tc = c-1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white)
						m.add(add);
				}
			}
			
			if (r==6 && canDoTwo) {
				tr = r-2;
				tc = c;
				if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
					int add = tr * 8 + tc;
					if(Main.c.tiles[add].p == null) {
						m.add(add);
					}
				}
			}
			
			// EN PASSANT
			
			if (r==3) {
				tr = r-2;
				tc = c-1;
				int add = tr * 8 + tc;
				
				if (Main.c.lastBoard.substring(add*2, add*2+2).equals("p2") && Main.c.tiles[(tr+2)*8 + tc].p != null && Main.c.tiles[(tr+2)*8 + tc].p instanceof Pawn) {
					m.add((tr+1) * 8 + tc);
				}
				tc = c+1;
				add = tr * 8 + tc;
				if (Main.c.lastBoard.substring(add*2, add*2+2).equals("p2") && Main.c.tiles[(tr+2)*8 + tc].p != null && Main.c.tiles[(tr+2)*8 + tc].p instanceof Pawn) {
					m.add((tr+1) * 8 + tc);
				}
			}
			
		}
		else {
			boolean canDoTwo = false;
			int tr = r+1;
			int tc = c;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p == null) {
					m.add(add);
					canDoTwo = true;
				}
			}
			
			tc = c+1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white)
						m.add(add);
				}
			}
			
			tc = c-1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(Main.c.tiles[add].p != null) {
					if (Main.c.tiles[add].p.white == !white)
						m.add(add);
				}
			}
			
			if (r==1 && canDoTwo) {
				tr = r+2;
				tc = c;
				if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
					int add = tr * 8 + tc;
					if(Main.c.tiles[add].p == null) {
						m.add(add);
					}
				}
			}
			
			// EN PASSANT
			if (r == 4) {
				tr = r+2;
				tc = c-1;
				int add = tr * 8 + tc;
				
				if (Main.c.lastBoard.substring(add*2, add*2+2).equals("p1") && Main.c.tiles[(tr-2)*8 + tc].p != null && Main.c.tiles[(tr-2)*8 + tc].p instanceof Pawn) {
					m.add((tr-1) * 8 + tc);
				}
				tc = c+1;
				add = tr * 8 + tc;
				if (Main.c.lastBoard.substring(add*2, add*2+2).equals("p1") && Main.c.tiles[(tr-2)*8 + tc].p != null && Main.c.tiles[(tr-2)*8 + tc].p instanceof Pawn) {
					m.add((tr-1) * 8 + tc);
				}
			}
			
		}
		
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = ((x-10)/75)+(y-10)/75*8;
			Tiles[] tempBoard = new Tiles[Main.c.tiles.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = Main.c.tiles[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	public int[] getLegalMoves(Tiles[] t, int currentI) {
		ArrayList<Integer> m = new ArrayList<Integer>();
		int r = currentI/8;
		int c = currentI%8;
		
		if (white) {
			boolean canDoTwo = false;
			int tr = r-1;
			int tc = c;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p == null) {
					m.add(add);
					canDoTwo = true;
				}
			}
			
			tc = c+1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white)
						m.add(add);
				}
			}
			
			tc = c-1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white)
						m.add(add);
				}
			}
			
			if (r==6 && canDoTwo) {
				tr = r-2;
				tc = c;
				if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
					int add = tr * 8 + tc;
					if(t[add].p == null) {
						m.add(add);
					}
				}
			}
			/*
			// EN PASSANT
			
			if (r==3) {
				tr = r-2;
				tc = c-1;
				int add = tr * 8 + tc;
				
				if (last.substring(add*2, add*2+2).equals("p2") && t[(tr+2)*8 + tc].p != null && t[(tr+2)*8 + tc].p instanceof Pawn) {
					m.add((tr+1) * 8 + tc);
				}
				tc = c+1;
				add = tr * 8 + tc;
				if (last.substring(add*2, add*2+2).equals("p2") && t[(tr+2)*8 + tc].p != null && t[(tr+2)*8 + tc].p instanceof Pawn) {
					m.add((tr+1) * 8 + tc);
				}
			}*/
			
		}
		else {
			boolean canDoTwo = false;
			int tr = r+1;
			int tc = c;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p == null) {
					m.add(add);
					canDoTwo = true;
				}
			}
			
			tc = c+1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white)
						m.add(add);
				}
			}
			
			tc = c-1;
			if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
				int add = tr * 8 + tc;
				if(t[add].p != null) {
					if (t[add].p.white == !white)
						m.add(add);
				}
			}
			
			if (r==1 && canDoTwo) {
				tr = r+2;
				tc = c;
				if (tr >= 0 && tr<= 7 && tc >= 0 && tc <= 7) {
					int add = tr * 8 + tc;
					if(t[add].p == null) {
						m.add(add);
					}
				}
			}
			/*
			// EN PASSANT
			if (r == 4) {
				tr = r+2;
				tc = c-1;
				int add = tr * 8 + tc;
				
				if (last.substring(add*2, add*2+2).equals("p1") && t[(tr-2)*8 + tc].p != null && t[(tr-2)*8 + tc].p instanceof Pawn) {
					m.add((tr-1) * 8 + tc);
				}
				tc = c+1;
				add = tr * 8 + tc;
				if (last.substring(add*2, add*2+2).equals("p1") && t[(tr-2)*8 + tc].p != null && t[(tr-2)*8 + tc].p instanceof Pawn) {
					m.add((tr-1) * 8 + tc);
				}
			}*/
			
		}
		
		//DONT FORGET TO MAKE SURE YOU ARE NOT IN CHECK
		for (int i=0; i<m.size(); i++) {
			int currentIndex = currentI;
			Tiles[] tempBoard = new Tiles[t.length];
			for(int j=0; j<tempBoard.length; j++)
				tempBoard[j] = t[j];
			//Chess.swap(tempBoard, currentIndex, m.get(i));
			String board = Chess.createString(tempBoard);
			String tempSmbl = board.substring(2*currentIndex, 2*currentIndex+2);
			board = board.substring(0, currentIndex*2) + "bb" + board.substring(currentIndex*2+2);
			board = board.substring(0, m.get(i)*2) + tempSmbl + board.substring(m.get(i)*2+2);
			if (Chess.inCheck(board, white)){
				m.remove(i);
				i--;
			}
		}
		
		int[] ret = new int[m.size()];
		for (int i=0; i<m.size(); i++) {
			ret[i] = m.get(i);
		}
		return ret;
	}
	
	@Override
	public Board[] allPossibleStates (int currentIndex, Tiles[] c) {
		
		
		int[] indexes = getLegalMoves(c, currentIndex);
		ArrayList<Board> b = new ArrayList<Board>();
		
		for (int i=0; i<indexes.length; i++) {
			if (indexes[i] >= 0 && indexes[i] <=7 && indexes[i] >= 56 && indexes[i] <= 63) {
				Board temp = new Board(c, white);
				Board temp2 = new Board(c, white);
				Board temp3 = new Board(c, white);
				Board temp4 = new Board(c, white);
				temp.movePiece(currentIndex, indexes[i], "Q");
				temp2.movePiece(currentIndex, indexes[i], "K");
				temp3.movePiece(currentIndex, indexes[i], "B");
				temp4.movePiece(currentIndex, indexes[i], "R");
				b.add(temp);
				b.add(temp2);
				b.add(temp3);
				b.add(temp4);
			}
			else {
				Board temp = new Board(c, white);
				temp.movePiece(currentIndex, indexes[i]);
				b.add(temp);
			}
		}
		
		Board[] ret = new Board[b.size()];
		b.toArray(ret);
		
		return ret;
	}
}