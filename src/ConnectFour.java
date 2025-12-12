import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ConnectFour extends JFrame {
    private final int ROWS = 6;
    private final int COLS = 7;
    private int turn = 0;

    private final JButton[][] board = new JButton[ROWS][COLS];
    private final char[][] colorGrid = new char[ROWS][COLS];
    private final int[] columns = new int[COLS];

    /**
     * Initialize the game: board, grid and buttons
     */
    public ConnectFour() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(100 * COLS, 100 * ROWS);
        setLocationRelativeTo(null);

        Color panelBg = new Color(176,186,195); // tinted blue color from the instruction
        Color btnBg = new Color(248,250,252);

        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 2, 2));
        panel.setBorder(new MatteBorder(2, 2, 2, 2, panelBg));
        panel.setBackground(panelBg);
        add(panel);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                JButton button = new JButton();

                button.setOpaque(true);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setBackground(btnBg); // tinted blue color from the instruction

                int col = c;
                button.addActionListener(_ -> addToColumn(col));

                panel.add(button);
                board[r][c] = button;
            }
        }

        // hides the flicker of window in the top left corner of the screen
        setVisible(true);
    }

    /**
     * Change the color of the topmost blank cell along the column the player has clicked on
     *
     * @param c index of the column where to change the color
     */
    public void addToColumn(int c) {
        int r = ROWS - columns[c] - 1;

        board[r][c].setBackground(getBtnColor());
        colorGrid[r][c] = getTurnColor();

        columns[c]++;

        if (checkWin(r, c)) {
            win();
            return;
        }

        if (columns[c] >= ROWS) {
            gameOver();
            return;
        }

        turn ^= 1;
    }

    /**
     * Check if the current color change is wining
     *
     * @param r row index of the cell of which the color was changed
     * @param c column index of the cell of which the color was changed
     * @return
     *      true: if the current player wins
     *      false: otherwise
     */
    private boolean checkWin(int r, int c) {
        // sequences in all four directions (| - \ /)
        int[][][] DIRECTIONS = {
                {{0, 1}, {0, -1}},  // HORIZONTAL       (|)
                {{1, 0}, {-1, 0}},  // VERTICAL         (-)
                {{-1, -1}, {1, 1}}, // MAJOR DIAGONAL   (\)
                {{-1, 1}, {1, -1}}, // MINOR DIAGONAL   (/)
        };

        for (int[][] dir : DIRECTIONS) {
            int count = -1; // account for overcount  by 1

            count += countSequence(r, c, dir[0][0], dir[0][1]);
            count += countSequence(r, c, dir[1][0], dir[1][1]);

            if (count >= 4) return true;
        }

        return false;
    }

    /**
     * Count the consecutive occurrences of the current player's color code in a given direction
     * the redirections are HORIZONTAL, VERTICAL, MAJOR DIAGONAL and MINOR DIAGONAL
     *
     * @param r the index of the row of the sequence
     * @param c the index of the column of the sequence
     * @param dr +1 or -1: in which direction along the row the next cell to consider
     * @param dc +1 or -1: in which direction along the column the next cell to consider
     * @return length of the sequence containing only current player's color code
     */
    private int countSequence(int r, int c, int dr, int dc) {
        int count = 0;
        char turnColor = getTurnColor();

        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && colorGrid[r][c] == turnColor) {
            count++;

            r += dr;
            c += dc;
        }
        return count;
    }

    /**
     * Get the appropriate color code for the current player
     * @return code for the current player's color: R or Y
     */
    private char getTurnColor() {
        return turn == 0 ? 'R' : 'Y';
    }

    /**
     * Get the appropriate button background color based on the player
     *
     * @return background color of the button for the current player
     */
    private Color getBtnColor() {
        return turn == 0 ? Color.RED : Color.YELLOW;
    }

    /**
     * Display message with winning color
     */
    private void win() {
        String player = turn == 0 ? "RED" : "YELLOW";
        JOptionPane.showMessageDialog(this, String.format("%s wins!", player));
        System.exit(0);
    }

    /**
     * Display message for game over
     */
    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over");
        System.exit(0);
    }

    /**
     * Initialize the class and start the game
     *
     * @param args arguments for the main function (unused)
     */
    public static void main(String[] args) {
        JFrame game = new ConnectFour();
    }
}
