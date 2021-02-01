import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;


public class Tiles {
	int x, y;
	int width;
	Color c;
	Piece p;
	
	public Tiles(int _x, int _y, int w, Color _c) {
		x = _x;
		y = _y;
		width = w;
		c = _c;
	}
	
	public Tiles(int _x, int _y, int w, Color _c, Piece _p) {
		x = _x;
		y = _y;
		width = w;
		c = _c;
		p = _p;
	}
	
	public Tiles (Tiles t) {
		x = t.x;
		y = t.y;
		width = t.width;
		c = t.c;
		if (t.p instanceof Knight) {
			p = new Knight(t.p);
		}
		if (t.p instanceof King) {
			p = new King(t.p);
		}
		if (t.p instanceof Queen) {
			p = new Queen(t.p);
		}
		if (t.p instanceof Bishop) {
			p = new Bishop(t.p);
		}
		if (t.p instanceof Rook) {
			p = new Rook(t.p);
		}
		if (t.p instanceof Pawn) {
			p = new Pawn(t.p);
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(c);
		g.fillRect(x, y, width, width);
		if (p!=null) {
			g.drawImage(p.i, x+15, y+10, width-30, width-20, null);
		}
	}
	
	public boolean isClicked(int _x, int _y) {
		return (_x >= x && _x <= x+width && _y >= y && _y <= y+width);
	}
	
	public String toString () {
		return p.toString();
	}
	
}