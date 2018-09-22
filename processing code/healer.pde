import processing.serial.*; 
Serial myPort = null;


final static String ICON  = "MyIcon.jpg";
final static String TITLE = "Healer";


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
float joystickX = 0; float joystickY = 0;
float select = 0;

Button btn_serial_up;              // move up through the serial port list
Button btn_serial_dn;              // move down through the serial port list
Button btn_serial_connect;         // connect to the selected serial port
Button btn_serial_disconnect;      // disconnect from the serial port
Button btn_serial_list_refresh;

Button send;
Button stop;

void setup(){
 size(displayWidth,600);
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

void draw(){
  background(120,100,90);
  
  
  fill(60,100,90);noStroke();rect(0,0,140,height);
  fill(0);stroke(217,179,16); rect(140,70,width- 550,40);
  
   send.Draw();stop.Draw();
  //==========================================
  
  fill(217,179,16);
  textSize(22);
  text(joystickX ,40,300);
  text(joystickY ,40,300); 
  text(select ,40,300); 
  
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
void mousePressed(){
   if(mouseX > 600 && mouseX < 720){ column = 2;}
   if(mouseX > 720 && mouseX < 840){ column = 3;}
   if(mouseX > 840 && mouseX < 940){ column = 4;}
  
    if (send.MouseIsOver()){
   // int a= Integer.parseInt(choose);
    float b= float(choose); 
    
    float dicimel = (b % 1)*10;
    float third = b % 100;
    float first = b/100;
    
    myPort.write((byte)first);
    myPort.write((byte)third);
    myPort.write((byte)dicimel);  

}
    
    if (stop.MouseIsOver()){
   myPort.write(byte(0));
   myPort.write(byte(0));
   myPort.write(byte(0));println("stop");}
   
   
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

void DrawTextBox(String title, String str, int x, int y, int w, int h)
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
void mouseWheel(MouseEvent event) {
  wheel = event.getCount();
  


  if(wheel== -1){pos++;}else pos--;
  
 if(pos > -1){pos = -1;}
}

void serialEvent(Serial myPort) { 
  
    
 // get the ASCII string:
 String inString = myPort.readStringUntil('\n');
 
 if (inString != null) {
 // trim off any whitespace:
 inString = trim(inString);
 // split the string on the commas and convert the 
 // resulting substrings into an integer array:
 float[] Data = float(split(inString, ","));
 // if the array has at least three elements, you know
 // you got the whole thing.  Put the numbers in the
 // color variables:
 
 
 
  if (Data.length >=3) {
 // map them to the range 0-255:
 in1 = Data[0];
 joystickX = Data[1];
 joystickY = Data[2]; 
 select = Data[3];
 
 
  }
 
 
 }
 }
 
 void changeAppIcon(PImage img) {
  final PGraphics pg = createGraphics(16, 16, JAVA2D);
img = loadImage("icon.jpg");
  pg.beginDraw();
  pg.image(img, 0, 0, 16, 16);
  pg.endDraw();

  frame.setIconImage(pg.image);
}

void changeAppTitle(String title) {
  frame.setTitle(title);
}
