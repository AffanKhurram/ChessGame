import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class Main {
	static Chess c;
	public static void main (String[] args) {
		
		AI ai = new AI(false);
		
		JFrame frame = new JFrame("ChessBoard");
		c = new Chess(frame, ai);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(c);
		frame.setSize(800, 800);
		frame.setVisible(true);
	}
}
