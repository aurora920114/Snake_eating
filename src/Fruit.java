import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fruit {
    private int x;
    private int y;
    private ImageIcon png_straw;


    public Fruit(){
        //png_straw= new ImageIcon("strawberry.png");
        png_straw = new ImageIcon(getClass().getResource("strawberry.png"));
        this.x = (int)(Math.floor(Math.random()*Main.column)*Main.CELL_Size);
        this.y = (int)(Math.floor(Math.random()*Main.row)*Main.CELL_Size);
    }

    public  int getX(){
        return this.x;
    }
    public  int getY(){
        return this.y;
    }
    public void drawFruit(Graphics g){
        //g.setColor(Color.red);
        //g.fillOval(this.x,this.y,Main.CELL_Size,Main.CELL_Size);
        png_straw.paintIcon(null,g,this.x,this.y);
    }
    public void setNewLocation(Snake s){
        int newX;
        int newY;
        Boolean overLap;
        do {
            newX = (int)(Math.floor(Math.random()*Main.column)*Main.CELL_Size);
            newY = (int)(Math.floor(Math.random()*Main.row)*Main.CELL_Size);
            overLap = checkOverLap(newX,newY,s);
        }while (overLap);

        this.x = newX;
        this.y = newY;
    }
    private boolean checkOverLap(int new_x,int new_y,Snake s){
        ArrayList<Node>shake_Body = s.getSnakeBody();
        for (int j=0;j<s.getSnakeBody().size();j++){
            if(new_x ==shake_Body.get(j).x && new_y ==shake_Body.get(j).y){
                return true;
            }
        }
        return false;
    }

    public void setNewLocation() {
    }
}
