import javax.swing.*;
// https://www.youtube.com/watch?v=Xw2MEG-FBsE

public class App {
    public static void main(String[] args) throws Exception {
        
        // create game window size
        int boardWidth = 360;
        int boardHeight = 640;

        // instantiate the Jframe for the game
        JFrame frame = new JFrame("Flappy Bird");
        // set window size taken from the variables above
        frame.setSize(boardWidth,boardHeight);
        //place window at the center of the screen
        frame.setLocationRelativeTo(null);
        // to stop user from resizing the window game size
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Instantiate an object "FlappyBird"
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        // pack will support frame size setting
        frame.pack();
        // following step 14 / connected to keyListener
        flappyBird.requestFocus();
        // must be set at the bottom
        frame.setVisible(true);

    }
}
