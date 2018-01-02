package com.company;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static javax.imageio.ImageIO.read;

public class Thomas extends JComponent implements ActionListener, Runnable, KeyListener
{
    public ThomasUtilities util = new ThomasUtilities(1);
    private Rectangle2D.Double upperTrackDetectionZone = new Rectangle2D.Double(0, 0, 200, 49);
    private AffineTransform tx;
    private URL thomasThemeAddress = getClass().getResource("Thomas The Tank Engine Theme Song.wav");
    private AudioClip thomasThemeSong = JApplet.newAudioClip(thomasThemeAddress);
    private BufferedImage img;
    private Image[] thomasSpriteImageArray = new Image[8];
    private Image gun = Toolkit.getDefaultToolkit().createImage(getClass().getResource("Minigun_SU.png"));
    private int widthOfScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    private int heightOfScreen = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
    private JFrame mainGameWindow = new JFrame("NewGame");// Makes window with title "NewGame"
    private AffineTransform identityTx = new AffineTransform();
    private Timer paintTicker = new Timer(20, this);
    private Timer animationTicker = new Timer(45, this);
    private ImageIcon thomasImageIcon = new ImageIcon();
    private Image thomasSpriteImage;
    private int pictureCounter;
    private int thomasSpriteImageCounter;
    private int thomasTotalXtranslation = widthOfScreen / 2;
    private int thomasYPos = (int) (heightOfScreen * 0.69);
    private Image roadImage;
    //roadWidth = roadImage.getWidth(mainGameWindow);
    private Image trackImage;
    private int roadXPos = 0;
    private int groundLevelTrackYPos = (int) (heightOfScreen * 0.809);
    private int level2TrackYPos = (int) (heightOfScreen * 0.2);
    private double trackScale = 1.7;
    public boolean isGoingRight = false;
    private boolean isGoingLeft = true;
    private boolean isNotMoving;
    private boolean isJumping;
    private boolean isFalling;
    private int position;
    private int thomasMaxSpeed = 22;
    private int initialJumpingVelocity = -37;
    public int jumpingVelocity = initialJumpingVelocity;
    private int movingVelocity;
    private int gravityAcceleration = 1;
    private Graphics2D g2;
    private int trackWidth;
    private int roadWidth;
    private int thomasXtranslate;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Thomas());
    }

    @Override
    public void run()
    {
        loadImages();
        setUpMainGameWindow();
        thomasThemeSong.play();
        paintTicker.start();
        animationTicker.start();
    }

    public void paint(Graphics g)
    {
        g2 = (Graphics2D) g;
        g2.setTransform(AffineTransform.getScaleInstance(-1, 1));//....................Flip image horizontally
        drawThomas(g2, isGoingRight);//.................... Draw Thomas
        drawRoad();//........................ Draw Road
        drawUpperTracks();//................. Draw Upper Tracks
        g2.setTransform(identityTx);
        drawLowerTracks();//................. Draw Lower Tracks
        g2.setTransform(identityTx);

    }

    /***********************************************************************************************
     * Draw Thomas with sprite files
     ***********************************************************************************************/
    private void drawThomas(Graphics g2, boolean isGoingRight)
    {
        try {
            thomasSpriteImageCounter = (thomasSpriteImageCounter + 1) % 8;
            thomasSpriteImage = thomasSpriteImageArray[thomasSpriteImageCounter];
        } catch (Exception ex) {
            System.out.println("error reading thomas thomasSpriteImage from thomas sprite thomasSpriteImage array");
        }
        if (isGoingRight) {
            g2.translate(thomasTotalXtranslation -= 10, 0);// move Thomas to right
            g2.drawImage(thomasSpriteImage, 0, 0, null);
        }else
        {
            g2.translate(-5000, 0);
            g2.translate(thomasTotalXtranslation += 10, 0);// move Thomas to right
            g2.drawImage(thomasSpriteImage, 0, 0, null);
        }
    }

    /***********************************************************************************************
     * Draw road
     ***********************************************************************************************/
    private void drawRoad()
    {
        g2.setTransform(identityTx);
        g2.translate(roadWidth, heightOfScreen - 200);
        g2.drawImage(roadImage, 0, 0, null);
        for (int i = 0; i < 5; i++) //for loop that condenses the drawing of the roads
        {
            g2.translate(roadImage.getWidth(null), 0);
            g2.drawImage(roadImage, 0, 0, null);
        }
    }

    /***********************************************************************************************
     * Draw upper tracks
     ***********************************************************************************************/
    private void drawUpperTracks()
    {
        g2.setTransform(identityTx);
        g2.translate(1000, 500);
        g2.drawImage(trackImage, 0, 0, null);
        g2.translate(trackImage.getWidth(null), 0);
        g2.drawImage(trackImage, 0, 0, null);
    }

    /***********************************************************************************************
     * Draw lower tracks
     ***********************************************************************************************/
    private void drawLowerTracks()
    {
        g2.setTransform(identityTx);
        g2.translate(0, heightOfScreen - 200);
        for (int i = 0; i < 5; i++) //for loop that condenses the drawing of the tracks
        {
            g2.drawImage(trackImage, 0, 0, null);
            g2.translate(trackImage.getWidth(null), 0);
        }
    }
    /***********************************************************************************************
     * Respond to animation ticker and paint ticker
     ***********************************************************************************************/
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == paintTicker) {
            repaint();
        }
        if (e.getSource() == animationTicker) {
            if (g2 != null) {
                drawThomas(g2, true);
            }
        }
    }
    /***********************************************************************************************
     * Get .png files, convert to Image and load sprite array
     ***********************************************************************************************/
    private void loadImages()
    {
        try {
            thomasSpriteImageArray[0] = read(getClass().getResource("Thomas1.png"));
            thomasSpriteImageArray[1] = read(getClass().getResource("Thomas2.png"));
            thomasSpriteImageArray[2] = read(getClass().getResource("Thomas3.png"));
            thomasSpriteImageArray[3] = read(getClass().getResource("Thomas4.png"));
            thomasSpriteImageArray[4] = read(getClass().getResource("Thomas5.png"));
            thomasSpriteImageArray[5] = read(getClass().getResource("Thomas6.png"));
            thomasSpriteImageArray[6] = read(getClass().getResource("Thomas7.png"));
            thomasSpriteImageArray[7] = read(getClass().getResource("Thomas8.png"));
        } catch (IOException e) {
            System.out.println("error reading from thomas sprite array");
        }
        roadImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("ground.png"));
        trackImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("Standard Gauge Train Track Sprite.png"));
        trackWidth = trackImage.getWidth(null);
        roadWidth = roadImage.getWidth(null);

    }
    /***********************************************************************************************
     * Set up main JFrame
     ***********************************************************************************************/
    private void setUpMainGameWindow()
    {
        mainGameWindow.setTitle("Thomas the tank");
        mainGameWindow.setSize(widthOfScreen, heightOfScreen);
        mainGameWindow.add(this);// Adds the paint method to the JFrame
        mainGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGameWindow.getContentPane().setBackground(new Color(200, 235, 255));
        mainGameWindow.setVisible(true);
        mainGameWindow.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isGoingRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.isGoingRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
