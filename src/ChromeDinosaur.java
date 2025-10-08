import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {
    int panelWidth = 750;
    int panelHeight = 250;

    // Images
    Image dinosaurStaticImage;
    Image dinosaurRunningImage;
    Image dinosaurJumpImage;
    Image dinosaurDeadImage;
    Image cactus1Image;
    Image cactus2Image;
    Image cactus3Image;

    // Dinosaur-specific variables
    int dinosaurWidth = 88;
    int dinosaurHeight = 94;
    int dinosaurX = 50;
    int dinosaurY = panelHeight - dinosaurHeight;

    GameObject dinosaur;

    // Cactus-specific variables
    int cactusHeight = 70;
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;
    int cactusX = 700;
    int cactusY = panelHeight - cactusHeight;

    ArrayList<GameObject> cactuses;
    GameObject cactus;

    // Timers
    Timer gameLoopTimer;
    Timer placeCactusTimer;

    // Game logic variables
    int velocityY = 0;
    int gravity = 1;
    int velocityX = -10;
    boolean gameOver = false;

    // General game objects class
    class GameObject {
        int x;
        int y;
        int width;
        int height;
        Image image;

        GameObject(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }
    }


    //
    // CONSTRUCTOR
    public ChromeDinosaur() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.WHITE);

        setFocusable(true);
        addKeyListener(this);

        // Load images
        dinosaurStaticImage = new ImageIcon(getClass().getResource("./image/dino.png")).getImage();
        dinosaurRunningImage = new ImageIcon(getClass().getResource("./image/dino-run.gif")).getImage();
        dinosaurJumpImage = new ImageIcon(getClass().getResource("./image/dino-jump.png")).getImage();
        dinosaurDeadImage = new ImageIcon(getClass().getResource("./image/dino-dead.png")).getImage();
        cactus1Image = new ImageIcon(getClass().getResource("./image/cactus1.png")).getImage();
        cactus2Image = new ImageIcon(getClass().getResource("./image/cactus2.png")).getImage();
        cactus3Image = new ImageIcon(getClass().getResource("./image/cactus3.png")).getImage();

        // dinosaur
        dinosaur = new GameObject(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurRunningImage);
        // cactuses
        cactuses = new ArrayList<GameObject>();

        // Place cactus timer
        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();

        // Game loop
        gameLoopTimer = new Timer(1000/60, this);
        gameLoopTimer.start();
    }
    // END OF CONSTRUCTOR
    //


    // Collision detection function
    private boolean collision(GameObject a, GameObject b) {
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    // Place cactus function
    private void placeCactus() {
        // Define probability of each cactus type being created
        double placeCactusChance = Math.random();

        if (placeCactusChance > 0.9) {
            cactus = new GameObject(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Image);
            cactuses.add(cactus);
        }
        else if (placeCactusChance > 0.7) {
            cactus = new GameObject(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Image);
            cactuses.add(cactus);
        }
            else if (placeCactusChance > 0.5) {
            cactus = new GameObject(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Image);
            cactuses.add(cactus);
        }
    }

    // Move function
    private void move() {
        // dinosaur
        velocityY += gravity;
        dinosaur.y += velocityY;
        
        if (dinosaur.y > dinosaurY) {
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.image = dinosaurRunningImage;
        }

        // cactuses
        for (int i=0; i<cactuses.size(); i++) {
            GameObject cactus = cactuses.get(i);
            cactus.x += velocityX;
            if (collision(dinosaur, cactus)) {
                gameOver = true;
            }
        }
    }

    // Custom drawing function
    private void draw(Graphics g) {
        // dinosaur
        g.drawImage(dinosaur.image, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        // cactuses
        for (int i=0; i<cactuses.size(); i++) {
            GameObject cactus = cactuses.get(i);
            g.drawImage(cactus.image, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
    }

    // Drawing function from JPanel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            dinosaur.image = dinosaurDeadImage;
            velocityX = 0;
            velocityY = 0;
            placeCactusTimer.stop();
            gameLoopTimer.stop();
        }
    }

    // KeyListener Methods
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (dinosaur.y == dinosaurY) {
                velocityY = -17;
                dinosaur.image = dinosaurJumpImage;
            }
            if (gameOver) {
                velocityX = -12;
                dinosaur.image = dinosaurRunningImage;
                gameOver = false;

                cactuses.clear();
                gameLoopTimer.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }
}
