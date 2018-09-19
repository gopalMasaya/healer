import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class healer extends PApplet {

 
Serial myPort = null;


final static String ICON  = "MyIcon.jpg";
final static String TITLE = "Got Personalized Icon!";


String serial_list;                // list of serial ports
int serial_list_index = 0;         // currently selected serial port 
int num_serial_ports = 0; 
int connect = 0;


Table data;
PImage buttons;

int rows = 0;
int cols = 0;
String incoming[][] = new String[10][30];

int wheel = 0;
int pos = -1;
String choose = "";
int column = 4;
float in1=0,in2=0;

Button btn_serial_up;              // move up through the serial port list
Button btn_serial_dn;              // move down through the serial port list
Button btn_serial_connect;         // connect to the selected serial port
Button btn_serial_disconnect;      // disconnect from the serial port
Button btn_serial_list_refresh;

Button send;
Button stop;

public void setup(){
 
  surface.setResizable(true);
  
  
   changeAppIcon( loadImage(ICON) );
  changeAppTitle(TITLE);
  
  serial_list = Serial.list()[serial_list_index];
 num_serial_ports = Serial.list().length; 
  
 data = loadTable("healer.csv","header");
 buttons = loadImage("button1.png");
  
  send = new Button("send",10,100,120,40,22);
  stop = new Button("stop",10,160,120,40,22); 
  
   btn_serial_up = new Button("^", width - 140, 100, 40, 20,16);
  btn_serial_dn = new Button("v",  width - 80, 100, 40, 20,16);
  btn_serial_connect = new Button("Connect", width - 275, 20, 100, 25,16);
  btn_serial_disconnect = new Button("Disconnect", width - 275, 60, 100, 25,16);
  btn_serial_list_refresh = new Button("Refresh", width - 275, 100, 100, 25,16);

  
  rows = data.getRowCount();
  cols = data.getColumnCount();
  println(rows);
  println(cols);
}

public void draw(){
  background(120,100,90);
  
  
  fill(60,100,90);noStroke();rect(0,0,140,height);
  fill(0);stroke(217,179,16); rect(140,70,width- 550,40);
  
   send.Draw();stop.Draw();
  //==========================================
  
  fill(217,179,16);
  textSize(22);
  //text(in1,40,400);
  
  //=============================================
  for(int i = 1;i< cols; i++){

 incoming[i] = data.getStringColumn(i);
 

  }
  
  textAlign(LEFT);
   for(int j = 1; j< rows; j++){  
     
     text(incoming[1][j],160,100+(j*40)+(pos*40));
     text(incoming[2][j],600,100+(j*40)+(pos*40));
     text(incoming[3][j],720,100+(j*40)+(pos*40));
     text(incoming[4][j],840,100+(j*40)+(pos*40));
   //  text(incoming[5][j],950,100+(j*40)+(pos*40)); 
  }

// text window
if(pos < 0){ choose = incoming[column][-pos];}
  fill(125);strokeWeight(2);stroke(120,120,150);
  rect(10,30,120,50);
  fill(100,255,90);textSize(28);
  text(choose,35,65);
  
  //header
  fill(120,150,110);noStroke();rect(140,0,width- 140,60);
  fill(120,30,10);text("LOW", 600,40);
  text("HIGH", 700,40);text("AV", 840,40);
  
  if(column == 2){noFill(); stroke(120,10,10); rect(590,70,100,600);}
  if(column == 3){noFill(); stroke(120,10,10); rect(700,70,100,600);} 
  if(column == 4){noFill(); stroke(120,10,10); rect(820,70,100,600);}
  
  
   btn_serial_up.Draw();
  btn_serial_dn.Draw();
  btn_serial_connect.Draw();
  btn_serial_disconnect.Draw();
  btn_serial_list_refresh.Draw();
   
  DrawTextBox("Select Port", serial_list, width - 150, 20, 120, 60);
if(connect ==1){fill(130,190,130); textSize(12); text("connected",width - 100,70);}

  
  
}
public void mousePressed(){
   if(mouseX > 600 && mouseX < 720){ column = 2;}
   if(mouseX > 720 && mouseX < 840){ column = 3;}
   if(mouseX > 840 && mouseX < 940){ column = 4;}
  
    if (send.MouseIsOver()){
  //  int a= Integer.parseInt(choose);
    float b= PApplet.parseFloat(trim(choose)); 
    myPort.write((byte)b);}
    if (stop.MouseIsOver()){myPort.write(PApplet.parseByte(0));println("stop");}
   
   
  // up button clicked
  if (btn_serial_up.MouseIsOver()) { if (serial_list_index > 0) { serial_list_index--; serial_list = Serial.list()[serial_list_index]; }}
      
  // down button clicked
  if (btn_serial_dn.MouseIsOver()) { if (serial_list_index < (num_serial_ports - 1)) { serial_list_index++; serial_list = Serial.list()[serial_list_index];}}
    
    
  // Connect button clicked
  if (btn_serial_connect.MouseIsOver()) { connect = 1;
    if (myPort == null) { myPort  = new Serial(this, Serial.list()[serial_list_index], 9600);
      println("connected");
         myPort.bufferUntil('\n'); }}
         
         
  // Disconnect button clicked
  if (btn_serial_disconnect.MouseIsOver()) {  connect = 0;
    if (myPort != null) { myPort.stop(); myPort = null;}}
    
  // Refresh button clicked
  if (btn_serial_list_refresh.MouseIsOver()) { serial_list = Serial.list()[serial_list_index]; num_serial_ports = Serial.list().length;}

  
}

public void DrawTextBox(String title, String str, int x, int y, int w, int h)
{
  fill(25,25,0);
  rect(x, y, w, h);
  fill(255);
  textAlign(LEFT);
  textSize(16);
  text(title, x + 10, y + 10, w - 20, 20);
  textSize(16);  
  text(str, x + 10, y + 40, w - 20, h - 10);
}
public void mouseWheel(MouseEvent event) {
  wheel = event.getCount();
  


  if(wheel== -1){pos++;}else pos--;
  
 if(pos > -1){pos = -1;}
}

public void serialEvent(Serial myPort) { 
  
    
 // get the ASCII string:
 String inString = myPort.readStringUntil('\n');
 
 if (inString != null) {
 // trim off any whitespace:
 inString = trim(inString);
 // split the string on the commas and convert the 
 // resulting substrings into an integer array:
 float[] Data = PApplet.parseFloat(split(inString, ","));
 // if the array has at least three elements, you know
 // you got the whole thing.  Put the numbers in the
 // color variables:
 
 
 
  if (Data.length >=1) {
 // map them to the range 0-255:
 in1 = Data[0];
 in2 = Data[1];

 
 
  }
 
 
 }
 }
 
 public void changeAppIcon(PImage img) {
  final PGraphics pg = createGraphics(16, 16, JAVA2D);
img = loadImage("icon.jpg");
  pg.beginDraw();
  pg.image(img, 0, 0, 16, 16);
  pg.endDraw();

  frame.setIconImage(pg.image);
}

public void changeAppTitle(String title) {
  frame.setTitle(title);
}
class Button {
  String label;
  float x;    // top left corner x position
  float y;    // top left corner y position
  float w;    // width of button
  float h;    // height of button
  int textS;
  
  Button(String labelB, float xpos, float ypos, float widthB, float heightB, int ts) {
    label = labelB;
    x = xpos;
    y = ypos;
    w = widthB;
    h = heightB;
    textS = ts;
  }
  
  public void Draw() {
    
    if(MouseIsOver()){strokeWeight(2);} else{strokeWeight(1);}
    noFill();
    stroke(217,179,16);
    image(buttons,x,y,w,h);
    rect(x-1,y-1,w+1,h+1,5);
 
    textSize(textS);
    textAlign(CENTER, CENTER);
    if(MouseIsOver()== true){fill(217,179,16);}
    else fill(255);
    text(label, x + (w / 2), y + (h / 2)-5);
    
    
   // println(MouseIsOver());
  }
  
  public boolean MouseIsOver() {
    if (mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h)) {
      return true;
    }
    return false;
  }
}
  public void settings() {  size(displayWidth,600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "healer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
