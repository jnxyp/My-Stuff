package wordFinder;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * <p>
 * <b>Word Finder</b> <br />
 * This program can find specific word from a character square
 * </p>
 * 
 * @author Xin <a href= "http://blog.jnxyp.tk/">(Jn_xyp)</a>
 * @version 2017-03-02
 */
public class WordFinder extends JFrame implements ActionListener {
  private JTextField[][] blocks;

  private JTextField txtFldRow, txtFldCol, txtFldWord, txtFldRegex;
  private JLabel     lblRow, lblCol, lblWord, lblRegex;
  private JButton    btnGenerate, btnLoad, btnSearch;

  private static WordFinder wordFinder;
  private WordPane          wordPane;

  Font f = new Font("Consolas", Font.BOLD, 12);

  public WordFinder() {
    // Set frame properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Word Finder | Version 0.1 by Jn_xyp");
    setLayout(new FlowLayout());
    // Initialize components
    lblRow = new JLabel("Row:");
    lblCol = new JLabel("Column:");
    lblWord = new JLabel("Search word:");
    lblRegex = new JLabel("Split Regex:");

    txtFldRow = new JTextField("10", 3);
    txtFldCol = new JTextField("10", 3);
    txtFldWord = new JTextField(15);
    txtFldWord.addActionListener(this);
    txtFldRegex = new JTextField("\\t", 5);

    btnGenerate = new JButton("GENERATE");
    btnGenerate.addActionListener(this);
    btnLoad = new JButton("LOAD");
    btnLoad.addActionListener(this);
    btnSearch = new JButton("SEARCH");
    btnSearch.addActionListener(this);
    btnSearch.setEnabled(false);

    add(lblRow);
    add(txtFldRow);
    add(lblCol);
    add(txtFldCol);
    add(lblWord);
    add(txtFldWord);
    add(lblRegex);
    add(txtFldRegex);
    add(btnGenerate);
    add(btnLoad);
    add(btnSearch);

    // Set visible
    setSize(580, 100);
    setResizable(false);
    setVisible(true);
    btnGenerate.requestFocus();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnGenerate) {
      btnGenerate.setEnabled(false);
      btnLoad.setEnabled(false);
      wordPane = new WordPane(Integer.parseInt(txtFldRow.getText()), Integer.parseInt(txtFldCol.getText()));
      btnSearch.setEnabled(true);
      txtFldWord.requestFocus();
      txtFldWord.selectAll();
    }
    else if (e.getSource() == btnSearch) {
      wordPane.clear();
      wordPane.search(txtFldWord.getText());
    }
    else if (e.getSource() == btnLoad) {
      File source = selectFile(new File(""));
      try {
        loadText(getText(source), txtFldRegex.getText());
      }
      catch (IOException e1) {
        System.out.println("Error: Cannot load the file.");
        return;
      }
      catch (NullPointerException e2) {
        System.out.println("Error: Did not select a file.");
        return;
      }
      btnGenerate.setEnabled(false);
      btnLoad.setEnabled(false);
      btnSearch.setEnabled(true);
      txtFldCol.setText("" + wordPane.cols);
      txtFldRow.setText("" + wordPane.rows);
    }
    else if (e.getSource() == txtFldWord) {
      btnSearch.doClick();
      txtFldWord.selectAll();
    }
  }

  private class WordPane extends JFrame {
    final static int BLOCK_WIDTH  = 25;
    final static int BLOCK_HEIGHT = 25;

    int rows, cols;

    public WordPane(int rows, int cols) {
      // Set frame properties
      setTitle("Input Pane");
      addWindowListener(new CloseAction());
      this.rows = rows;
      this.cols = cols;
      // Generate text fields
      blocks = new JTextField[rows][cols];
      for (int i = 0; i < blocks.length; i++) {
        for (int j = 0; j < blocks[0].length; j++) {
          blocks[i][j] = new JTextField(1);
          blocks[i][j].setFont(f);
          blocks[i][j].setHorizontalAlignment(JTextField.CENTER);
          // blocks[i][j].setText(" ");
          // blocks[i][j].setActionCommand(i + " " + j);
          add(blocks[i][j]);
        }
      }
      // Set visible
      setLayout(new GridLayout(rows, cols));
      setSize(cols * BLOCK_WIDTH, rows * BLOCK_HEIGHT);
      setVisible(true);
    }

    protected void clear() {
      for (JTextField[] x : blocks) {
        for (JTextField y : x) {
          y.setBackground(Color.WHITE);
        }
      }
    }

    protected void search(String target) {
      String first = target.substring(0, 1);
      int len = target.length() - 1;
      String temp;
      for (int i = 0; i < blocks.length; i++) {
        for (int j = 0; j < blocks[0].length; j++) {
          if (blocks[i][j].getText().equals(first)) {
            // Up
            temp = "";
            if (i - len >= 0) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i - x][j].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i - x][j].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Down
            temp = "";
            if (i + len < rows) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i + x][j].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i + x][j].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Left
            temp = "";
            if (j - len >= 0) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i][j - x].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i][j - x].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Right
            temp = "";
            if (j + len < cols) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i][j + x].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i][j + x].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Left-up
            temp = "";
            if (i - len >= 0 && j - len >= 0) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i - x][j - x].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i - x][j - x].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Right-up
            temp = "";
            if (i - len >= 0 && j + len < cols) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i - x][j + x].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i - x][j + x].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Right-down
            temp = "";
            if (i + len < rows && j + len < cols) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i + x][j + x].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i + x][j + x].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
            // Left-down
            temp = "";
            if (i + len < rows && j - len >= 0) {
              for (int x = 0; x <= len; x++) {
                temp += blocks[i + x][j - x].getText();
              }
              if (target.equals(temp)) {
                for (int x = 0; x <= len; x++) {
                  blocks[i + x][j - x].setBackground(Color.LIGHT_GRAY);
                }
              }
            }
          }
        }
      }
    }
  }

  private class CloseAction extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      btnGenerate.setEnabled(true);
      btnLoad.setEnabled(true);
      btnSearch.setEnabled(false);
      btnGenerate.requestFocus();
    }
  }

  private void loadText(String[] txt, String regex) {
    int col = 0;
    for (String temp : txt) {
      int length = temp.split(regex).length;
      if (length > col) {
        col = length;
      }
    }
    wordPane = new WordPane(txt.length, col);
    String[] line;
    for (int i = 0; i < blocks.length; i++) {
      line = txt[i].split(regex);
      for (int j = 0; j < line.length; j++) {
        blocks[i][j].setText(line[j]);
      }
    }
  }

  private String[] getText(File txtFile) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile)));
    ArrayList<String> txt = new ArrayList<>();
    String line;
    do {
      line = reader.readLine();
      if (line != null) {
        txt.add(line);
      }
    } while (line != null);
    reader.close();
    String[] result = new String[txt.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = txt.get(i);
    }
    return result;
  }

  private File selectFile(File path) {
    JFileChooser chooser = new JFileChooser(path);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    chooser.showOpenDialog(this);
    return chooser.getSelectedFile();
  }

  public static void main(String[] args) {
    wordFinder = new WordFinder();
  }

}
