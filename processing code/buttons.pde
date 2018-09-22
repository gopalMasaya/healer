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
  
  void Draw() {
    
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
  
  boolean MouseIsOver() {
    if (mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h)) {
      return true;
    }
    return false;
  }
}
