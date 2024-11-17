import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
public class ChromeDinosaur extends JPanel implements ActionListener,KeyListener{
    int boardWidth=750;
    int boardHeight=250;
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image cactus4Img;
    Image cactus5Img;
    Image cactus6Img;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            if(dinosaur.y==dinosaurY){
                velocityY=-17;
                dinosaur.img=dinosaurJumpImg;
            }

            if(gameOver){
                dinosaur.y=dinosaurY;
                dinosaur.img=dinosaurImg;
                velocityY=0;
                cactusArray.clear();
                score=0;
                gameOver=false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;
        Block(int x,int y,int width,int height,Image img){
            this.img=img;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
        }
    }
    int dinosaurWidth =85;
    int dinosaurHeight=85;
    int dinosaurX=50;
    int dinosaurY=boardHeight-dinosaurHeight;
    Block dinosaur;

    //cactus
    int cactus1Width=34;
    int cactus2Width=69;
    int cactus3Width=102;

    int cactusHeight=60;
    int cactusX=700;
    int cactusY=boardHeight-cactusHeight;
    ArrayList<Block> cactusArray;

    //physics
    int velocityX=-12;
    int velocityY=0;//Dinosaur jump speed
    int gravity=1;

    boolean gameOver=false;
    int score=0;


    Timer gameLoop;
    Timer placeCactusTimer;

    public ChromeDinosaur(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg=new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg=new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg=new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        cactus1Img=new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img=new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img=new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();
        cactus4Img=new ImageIcon(getClass().getResource("./img/big-cactus1.png")).getImage();
        cactus5Img=new ImageIcon(getClass().getResource("./img/big-cactus2.png")).getImage();
        cactus6Img=new ImageIcon(getClass().getResource("./img/big-cactus3.png")).getImage();

        dinosaur=new Block(dinosaurX,dinosaurY,dinosaurWidth,dinosaurHeight,dinosaurImg);
        //cactus
        cactusArray=new ArrayList<Block>();
        gameLoop =new Timer(1000/60,this);
        gameLoop.start();

        placeCactusTimer=new Timer(1500,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                placeCactus();

            }

        });
        placeCactusTimer.start();
    }
    void placeCactus(){
        if(gameOver){
            return;
        }
        double placeCactusChance=Math.random();
        if(placeCactusChance>.90){
            Block cactus=new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus3Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance>.70){
            Block cactus=new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus2Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance>.50){
            Block cactus=new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus1Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance>.35){
            Block cactus=new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus4Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance>.20){
            Block cactus=new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus5Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance>.10){
            Block cactus=new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus6Img);
            cactusArray.add(cactus);
        }
        if(cactusArray.size()>10){
            cactusArray.remove(0);

        }
        


    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(dinosaur.img,dinosaur.x,dinosaur.y,dinosaur.width,dinosaur.height,null);
        for(int i=0;i<cactusArray.size();i++){
            Block cactus=cactusArray.get(i);
            g.drawImage(cactus.img,cactus.x,cactus.y,cactus.width,cactus.height,null);
        }
        g.setColor(Color.black);
        g.setFont(new Font("Courier",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: "+String.valueOf(score),10,35);
        }
        else{
            g.drawString(String.valueOf(score),10,35);
        }

    }
    boolean collision(Block a,Block b){
        return a.x<b.x+b.width &&
        a.x+a.width>b.x &&
        a.y<b.y+b.height &&
        a.y+a.height>b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver){
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }
    public void move(){
        velocityY+=gravity;
        dinosaur.y+=velocityY;
        if(dinosaur.y>dinosaurY){//stop dinosaur from falling below ground
            dinosaur.y=dinosaurY;
            velocityY=0;
            dinosaur.img=dinosaurImg;
        }
        //cactus
        for(int i=0;i<cactusArray.size();i++){
            Block cactus=cactusArray.get(i);
            cactus.x+=velocityX;
            if(collision(dinosaur,cactus)){
                gameOver=true;
                dinosaur.img=dinosaurDeadImg;
            }
        }

        //score
        score++;

    }
}