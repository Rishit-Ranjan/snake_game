import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class App extends JPanel implements ActionListener, KeyListener {
    private final int BOARD_WIDTH = 20;
    private final int BOARD_HEIGHT = 20;
    private final int CELL_SIZE = 20;
    private final int GAME_SPEED = 100;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private boolean isGameOver;
    private Timer timer;
    private Random random;

    public App() {
        setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        
        initGame();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(BOARD_WIDTH/2, BOARD_HEIGHT/2));
        direction = 'R';
        random = new Random();
        spawnFood();
        isGameOver = false;
        
        timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    private void spawnFood() {
        int x, y;
        do {
            x = random.nextInt(BOARD_WIDTH);
            y = random.nextInt(BOARD_HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void move() {
        if (isGameOver) return;

        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U': newHead.y--; break;
            case 'D': newHead.y++; break;
            case 'L': newHead.x--; break;
            case 'R': newHead.x++; break;
        }

        // Check collision with walls
        if (newHead.x < 0 || newHead.x >= BOARD_WIDTH ||
            newHead.y < 0 || newHead.y >= BOARD_HEIGHT ||
            snake.contains(newHead)) {
            isGameOver = true;
            timer.stop();
            return;
        }

        snake.add(0, newHead);

        // Check if food is eaten
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw game over message
        if (isGameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String msg = "Game Over! Score: " + (snake.size() - 1);
            g.drawString(msg, 
                (BOARD_WIDTH * CELL_SIZE - metrics.stringWidth(msg)) / 2, 
                BOARD_HEIGHT * CELL_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_SPACE:
                if (isGameOver) {
                    initGame();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new App());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
