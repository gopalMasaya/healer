#if 1
#include <Adafruit_GFX.h>
#include <MCUFRIEND_kbv.h>
#include <Fonts/FreeSans9pt7b.h>
#include <Fonts/FreeSans12pt7b.h>
#include <Fonts/FreeSerif12pt7b.h>
#include <FreeDefaultFonts.h>
#include <TouchScreen.h>
#include "data.h"
#define MINPRESSURE 200
#define MAXPRESSURE 1000


#define BLACK   0x0000
#define BLUE    0x001F
#define RED     0xF800
#define GREEN   0x07E0
#define CYAN    0x07FF
#define MAGENTA 0xF81F
#define YELLOW  0xFFE0
#define WHITE   0xFFFF
#define GREY    0x8410


MCUFRIEND_kbv tft;

// copy-paste results from TouchScreen_Calibr_native.ino
const int XP = 6, XM = A2, YP = A1, YM = 7; //ID=0x9341
const int TS_LEFT = 907, TS_RT = 136, TS_TOP = 942, TS_BOT = 139;

// from serial monitor calibration 26.9.18 = NOT WORKING
//const int XP=7,XM=A1,YP=A2,YM=6; //240x320 ID=0x0154
//const int TS_LEFT=553,TS_RT=349,TS_TOP=613,TS_BOT=780;


TouchScreen ts = TouchScreen(XP, YP, XM, YM, 300);
Adafruit_GFX_Button on_btn, off_btn, enter_btn;

int pixel_x, pixel_y;     //Touch_getXY() updates global vars
bool Touch_getXY(void)
{
    TSPoint p = ts.getPoint();
    pinMode(YP, OUTPUT);      //restore shared pins
    pinMode(XM, OUTPUT);
    digitalWrite(YP, HIGH);   //because TFT control pins
    digitalWrite(XM, HIGH);
    bool pressed = (p.z > MINPRESSURE && p.z < MAXPRESSURE);
    if (pressed) {
        pixel_x = map(p.x, TS_LEFT, TS_RT, 0, tft.width()); //.kbv makes sense to me
        pixel_y = map(p.y, TS_TOP, TS_BOT, 0, tft.height());
    }
    return pressed;
}



void setup(void)
{

    Serial.begin(9600);
    uint16_t ID = tft.readID();
    Serial.print("TFT ID = 0x");
    Serial.println(ID, HEX);
    Serial.println("Calibrate for your Touch Panel");
    if (ID == 0xD3D3) ID = 0x9486; // write-only shield
    tft.begin(ID);
    tft.setRotation(1);            //Screen Rotation
    tft.fillScreen(BLACK);         // backgroung


    // ***************************      BUTTONS    **************************
    //                       R/L   U/D   WITH  HIGHT                        FONT SIZE
    on_btn.initButton(&tft,  275,  150,   90,    30, WHITE, CYAN, BLACK, "UP", 2);  //button1

    //                       R/L   U/D   WITH  LONG                        FONT SIZE
    off_btn.initButton(&tft, 275,  220,  90,    30, WHITE, CYAN, BLACK, "DOWN", 2);   //button2

    //                         R/L   U/D   WITH  LONG                        FONT SIZE
    enter_btn.initButton(&tft, 275,  185,  90,    30, WHITE, CYAN, BLACK, "ENTER", 2);   //button3


    on_btn.drawButton(false);
    off_btn.drawButton(false);
    enter_btn.drawButton(false);

//               R/L  U/D  WITH HIGHT COLOR
    tft.fillRect(10,  70,  320,  50,  YELLOW);   // button color before preesed
}


void loop(void)
{

 // Serial.println(data);
//*****************************   Text on screen  ******************************
//           R/L  U/D  WITH
    showmsgXY(5,   4,   3, NULL, "Healer V1");      //header

    showmsgXY(5,   30,   2, NULL, "Freiquency Healer Gold");      //header
    for(int i = 0; i< 4;i++){
    showmsgXY(5,   60+ (i*30),   2, NULL, data[i][i]); 
       
    }

    bool down = Touch_getXY();
    on_btn.press(down && on_btn.contains(pixel_x, pixel_y));
    off_btn.press(down && off_btn.contains(pixel_x, pixel_y));
    if (on_btn.justReleased())
        on_btn.drawButton();
    if (off_btn.justReleased())
        off_btn.drawButton();
    if (on_btn.justPressed()) {
        on_btn.drawButton(true);
  
   // ***************************   DINAMIC COLORS AND PLACE   ***********************
  //               R/L  U/D  WITH HIGHT COLOR
       tft.fillRect(10, 70,  320,  50,  GREEN);   // color after preesed
    }
    if (off_btn.justPressed()) {
        off_btn.drawButton(true);
 //               R/L  U/D  WITH HIGHT COLOR
      tft.fillRect(10, 70, 320, 50, MAGENTA);  // color after preesed
    }
}
#endif

void showmsgXY(int x, int y, int sz, const GFXfont *f,  String msg)//const char  *
{
    int16_t x1, y1;
    uint16_t wid, ht;
   // tft.drawFastHLine(0, y, tft.width(), WHITE);
      tft.setFont(f);
      tft.setCursor(x, y);
      tft.setTextColor(GREEN);
      tft.setTextSize(sz);
      tft.print(msg);
}

