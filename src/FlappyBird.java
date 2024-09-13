import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;

//https://www.youtube.com/watch?v=Xw2MEG-FBsE

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    //intiate board size dimentions
    int boardWideth = 360;
    int boardHeight = 640;

    // intiate images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird
    int birdX = boardWideth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;


    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }
    //Pipes
    int pipeX = boardWideth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }


    //game logic
    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();


    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;

    // constructor
    FlappyBird() {
        setPreferredSize(new Dimension(boardWideth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images - get class referes to flappyBird class - getResource refers to image location
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // place new pipe
                placePipes();
            }
        });

        // place pipe timer
        placePipesTimer.start();
        //game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }


    //Place Pipe Function
    public void placePipes() {
        // create new pipe
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;


        //generate random pipe positions for top pipes
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight+ openingSpace;
        pipes.add(bottomPipe);
    }

    // Paint Function
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draw Function
    public void draw(Graphics g) {
        //System.out.println("draw");

        // draw background
        g.drawImage(backgroundImg, 0, 0, this);

        // draw bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //draw pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over!" + String.valueOf((int)score), 10, 35);
        } else {
            g.drawString("Score: " + String.valueOf((int)score), 10, 35);
        }
    }

    //Move function
    public void move() {
        // move bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);
        // stop bird from going beyond top of frame
        bird.y = Math.max(bird.y,0);

        // move pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }


            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }
        // game over (falling in the bottom of the screen )
        if (bird.y > boardHeight) {
            gameOver = true;

        }
    }

    // hitting the pipes function and game over
    public boolean collision(Bird a,Pipe b ) {
        // check if bird hits pipe
        return  a.x < b.x + b.width &&   // a's top left corner desn'treach b's top right corner
                a.x + a.width > b.x &&  // a's top right corner passes b's top left corner
                a.y < b.y + b.height && // a's top left corner deesn't rech b's bottom left corner
                a.y + a.height > b.y;   // a's bottom left corner passes b's top left corner


    }



    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();

        }
    }

    // this is the only one of we used from the next 3 methods that were implemented
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                // restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }

        }
    }

    // unused
    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    // unused
    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }
}
