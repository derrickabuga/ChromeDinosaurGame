import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int frameWidth = 750;
        int frameHeight = 250;

        JFrame frame = new JFrame("Chrome Dinosaur Game");
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        ChromeDinosaur chromeDinosaur = new ChromeDinosaur();
        frame.add(chromeDinosaur);
        frame.pack();
        chromeDinosaur.requestFocus();
        
        frame.setVisible(true);
    }
}
