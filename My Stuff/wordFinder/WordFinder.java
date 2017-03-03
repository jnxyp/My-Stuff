package wordFinder;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * <p>
 * <b>Word Finder<b> <br />
 * This program can find specific word from a character square
 * </p>
 * 
 * @author Xin <a href= "http://blog.jnxyp.tk/">(Jn_xyp)</a>
 * @version 2017-02-28
 */
public class WordFinder extends JFrame {
  JTextField[][] blocks;

  JTextField txtFldRow, txtFldCol, txtFldWord;

  Font f = new Font("Consolas", Font.PLAIN, 12);

  public WordFinder() {
    // Set frame properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Word Finder | Version 0.1 by Jn_xyp");

    // Set visible
    setSize(400, 300);
    setVisible(true);
  }

  private class WordPane {
    final static int BLOCK_WIDTH  = 15;
    final static int BLOCK_HEIGHT = 15;

    int row, col;

    public WordPane() {
      // Set frame properties
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      setTitle("Input Pane");
      // Get variables
      row = Integer.parseInt(txtFldRow.getText());
      col = Integer.parseInt(txtFldCol.getText());
      // Generate text fields
      blocks = new JTextField[row][col];
      for (int i = 0; i < blocks.length; i++) {
        for (int j = 0; j < blocks[0].length; j++) {
          blocks[i][j] = new JTextField(1);
          blocks[i][j].setFont(f);
          add(blocks[i][j]);
        }
      }
      // Set visible
      setLayout(new GridLayout(row, col));
      setSize(col * BLOCK_WIDTH, row * BLOCK_HEIGHT);
      setVisible(true);
    }
  }

  public static void main(String[] args) {
    new WordFinder();
  }
}
