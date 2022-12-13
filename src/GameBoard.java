import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements ActionListener {

    // Board Dimensions
    int width = 600;
    int height = 600;
    int x[] = new int[width * height];
    int y[] = new int[width * height];
    // Apple Initial position on the Game Board
    int apple_x = 150;
    int apple_y = 150;
    int dots;
    int dot_size = 10;

    // Images for Snake and Apple
    Image apple;
    Image head;
    Image body;

    // Initial Snake Directions
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    // Action Listener Timer delay time
    int delay = 300;
    Timer time;
    int level = 1;
    int score =0;
    int range = 0;
    int fixed = 55;
    boolean inGame = true;
    boolean levInc = false;

    public GameBoard() {
        // Add a keyListener to Board for checking Key Direction
        addKeyListener(new TAdaptor());
        setFocusable(true);
        // Preferred Size of the Board or Panel
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        loadImage();
        // Start the game
        initGame();

    }

    // Start the Game with Default settings
    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 100 + i * dot_size;
            y[i] = 100;
        }
        // Add timer Action listener to the game during game initialization
        time = new Timer(delay, this);
        // Start the timer action listener after game starts
        time.start();
    }

    // Load the images for the snake and apple in the board
    public void loadImage() {
        // Create a Image Icon from the image Path and get the Image form the icon

        ImageIcon image_apple = new ImageIcon("src/resources/apple.png");
        apple = image_apple.getImage();

        ImageIcon image_head = new ImageIcon("src/resources/head.png");
        head = image_head.getImage();

        ImageIcon image_body = new ImageIcon("src/resources/dot.png");
        body = image_body.getImage();
    }

    // Paint components in the game board so that it sync with game progress
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(levInc){
            String levlup = "LEVEL "+level+" COMPLETED !!!";
            Font small = new Font("TimesNewRoman", Font.BOLD, 18);
            FontMetrics metrics = getFontMetrics(small);
            graphics.setColor(Color.YELLOW);
            graphics.setFont(small);
            graphics.drawString(levlup, (width - metrics.stringWidth(levlup)) / 2, height / 2);
            levInc = false;
            time.stop();
            time.start();
        }
        if (inGame) {
            String lev = "LEVEL "+level;
            String scor = "SCORE"+": "+score;
            Font miny = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics levmet = getFontMetrics(miny);
            graphics.setColor(Color.YELLOW);
            graphics.setFont(miny);
            graphics.drawString(lev,40,20);
            graphics.drawString(scor,width-levmet.stringWidth(scor)-20,20);
            range = levmet.getHeight();
            graphics.drawLine(0,range+10,width,range+10);

            // Snake and Apple Paint
            graphics.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    graphics.drawImage(head, x[0], y[0], this);
                } else {
                    graphics.drawImage(body, x[i], y[i], this);
                }
            }

           // Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(graphics);
        }


    }

    // Game Over message Graphics display on board
    private void gameOver(Graphics graphics) {
        String over = "GAME OVER";
        String fina = "LEVEL "+level+" SCORE : "+score;
        Font small = new Font("TimesNewRoman", Font.BOLD, 18);
        FontMetrics metrics = getFontMetrics(small);
        graphics.setColor(Color.YELLOW);
        graphics.setFont(small);
        graphics.drawString(fina, (width - metrics.stringWidth(fina)) / 2, height / 2+20);
        graphics.drawString(over, (width - metrics.stringWidth(over)) / 2, height / 2-20);
    }

    // Snake Movement in the game Board with respective to key pressed
    private void move() {
        for (int i = dots - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= dot_size;
        }
        if (rightDirection) {
            x[0] += dot_size;
        }
        if (upDirection) {
            y[0] -= dot_size;
        }
        if (downDirection) {
            y[0] += dot_size;
        }
    }
    public void checkGameUp(){

    }


    // Actions to be done with timer action Listener after delay time
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (inGame) {
            checkCollision();
            checkApple();
            move();
            checkScore();
        }
        // Repaint the Game Board with Game Progress
        repaint();
    }

    // Check if the apple eaten or not by snake and update Snake size and apple
    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            score+=10;
            relocateApple();
        }
    }

    // ReLocate the apple when eaten by snake
    private void relocateApple() {
        int pos = (int) (Math.random() * fixed);
        apple_x = pos * 10;

        pos = (int) (Math.random() * fixed);
        while(pos*10<range+20){
            pos = (int) (Math.random() * fixed);
        }
        apple_y = pos * 10;
        //System.out.println(apple_x+"  "+apple_y);
    }

    // Check collisions in the Game board
    private void checkCollision() {

        // Board Boundaries collision with snake
        if (x[0] < 0) {
            inGame = false;
        }
        if (x[0] > width) {
            inGame = false;
        }
        if (y[0] < range+10) {
            inGame = false;
        }
        if (y[0] > height) {
            inGame = false;
        }
        // Snake collision with itself
        for (int i = dots - 1; i >= 3; i--) {
            if (x[i] == x[0] && y[i] == y[0]) {
                inGame = false;
                break;
            }
        }
    }
    private void checkScore(){
        if(score>=50){
            score=0;
            level++;
            delay-=50;
            levInc = true;
        }
    }

    // KeyBoard sync with the board and update the game with the key pressed
    public class TAdaptor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key = keyEvent.getKeyCode();
            // Change direction value as per the key
            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

        }
    }
}
