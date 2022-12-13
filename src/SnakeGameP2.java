import javax.swing.*;
import java.awt.*;


public class SnakeGameP2 extends JFrame {
    private GameBoard board;

    public SnakeGameP2(){

        setTitle("Snake Board Game");
        // Initialize a Game Board for Frame
        board = new GameBoard();
        // Add the Game Board to the Frame
        add(board);
        // Frame Size is not changeable
        setResizable(false);
        // Pack the frame to the size of the board
        pack();
        // Make Frame unhidden
        setBounds(500,150,600,600);
        setVisible(true);


    }

    public static void main(String[] args) {
        // Initialize the Snake Game with automate frame creation
        JFrame snakeGame = new SnakeGameP2();

    }
}
