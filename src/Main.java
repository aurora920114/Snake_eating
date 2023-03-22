import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel implements KeyListener {
    public static final  int CELL_Size = 20;
    public  static  int width = 400;
    public  static  int height = 400;
    public  static int row = height/CELL_Size;
    public static int column = width/CELL_Size;
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private int speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private  int score;
    private int highest_score;
    String myfile = "HS.txt";//將最高分紀錄在txt檔


    public Main(){
        readHS();
        reset();
        addKeyListener(this);
    }
    private void setTimer(){
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint(); //re-call paintComponent
            }
        }, 0, speed);
    }
    private void reset(){
        score = 0;
        if(snake != null){
            //如果蛇的身體不是空的，則就是有玩過，需要reset
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true;
        direction = "down";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }
    @Override
    public void paintComponent(Graphics g) {
        //System.out.println("Repaint component");
        //檢查蛇的頭是否碰到蛇的身體
        ArrayList<Node>snake_body = snake.getSnakeBody();
        Node head = snake_body.get(0);
        for(int i =1;i<snake_body.size();i++){
            //檢查頭以外其他身體
            if(snake_body.get(i).x == head.x&&snake_body.get(i).y == head.y){
                allowKeyPress = false;//不再允許按按鍵
                t.cancel();//終止timer
                t.purge();
                int response = JOptionPane.showOptionDialog(this,"Game over...Your score is:"+score+".the highest score was:"+highest_score+".Continue?","Game over",
                        JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,JOptionPane.YES_OPTION);
                writeNewScore(score);
                //按下yes後執行：
                switch (response){
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }
            g.fillRect(0,0,width,height);
        snake.drewSnake(g);
        fruit.drawFruit(g);
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;
        if (direction.equals("right")) {
            snakeX += CELL_Size;
        } else if (direction.equals("left")) {
            snakeX -= CELL_Size;
        } else if (direction.equals("up")) {
            snakeY -= CELL_Size;
        } else if (direction.equals("down")) {
            snakeY += CELL_Size;
        }

        //蛇吃水果
            Node newHead = new Node(snakeX, snakeY);
            if(snake.getSnakeBody().get(0).x==fruit.getX()&&snake.getSnakeBody().get(0).y==fruit.getY()){
                System.out.println("Eating");
                fruit.setNewLocation(snake);
                fruit.drawFruit(g);//重新繪製水果
                score++;
            }else{
                snake.getSnakeBody().remove(snake.getSnakeBody().size()- 1);
            }
            snake.getSnakeBody().add(0, newHead);

            allowKeyPress = true;
            requestFocusInWindow();
        }
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(width, height);
        }
        public static void main(String[] args){
            JFrame window = new JFrame("Snake Eating");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(new Main());
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.setResizable(false);
        }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        if (allowKeyPress){
            if(e.getKeyCode()==37 && !direction.equals("right")){
            direction = "left";
            }else if(e.getKeyCode()==38&& !direction.equals("down")){
            direction = "up";
            }else if(e.getKeyCode()==39 && !direction.equals("left")){
            direction = "right";
             }else if(e.getKeyCode()==40&& !direction.equals("up")){
                direction = "down";
            }
            allowKeyPress = false;
            //******
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public  void readHS(){
        try {
            File myobj = new File(myfile);
            Scanner myReader = new Scanner(myobj);
            highest_score = myReader.nextInt();
            myReader.close();
        }catch (FileNotFoundException e) {
          highest_score = 0;
          try {
              File myobj = new File(myfile);
              if(myobj.createNewFile()){
                  System.out.println("File created:"+myobj.getName());
              }
              FileWriter myWriter = new FileWriter(myobj.getName());
              myWriter.write(" "+0);//表示初始值自0分開起
              myWriter.close();
          }catch (IOException err){
              System.out.println("An Error occurred!");
              err.printStackTrace();
          }
        }
    }
    private  void writeNewScore(int score){
         try{
             if(score > highest_score){
                 FileWriter myWriter = new FileWriter(myfile);
                 System.out.println("Record Highest Score.");
                 myWriter.write(""+score);
                 highest_score = score;
                 myWriter.close();
             }
         }catch (IOException err){
             err.printStackTrace();
         }
    }
}