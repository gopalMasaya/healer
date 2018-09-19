float data[10];
int currentValue = 0;
int values[10] ;
float x = 220.22;

float hz = 0;

unsigned long  Send;
unsigned long  Last_Send;


void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
 pinMode(LED_BUILTIN, OUTPUT);

}

void loop() {

  Send = millis();

   if(Serial.available()>0){
    //  LastSerial = SerialTime;
    int incomingValue = Serial.read();
   digitalWrite(LED_BUILTIN, HIGH);
    
    values[currentValue] = incomingValue;
data[0] = (int) values[0];
data[1] = (int) values[1];
data[2] = (int) values[2];

    currentValue++;
    if(currentValue > 2){   
        currentValue = 0; 
      }}

      hz = (data[0]*100)+ data[1]+(data[2]/10);

  if(data[0] == 0){  noTone(11);hz = 0;}
  if(data[0] > 0){ tone(11, hz,180000); } else noTone(11);
// analogWrite(11,data[0]/4);

 if(Send > Last_Send+3000){

  Serial.print(hz);Serial.print(",");
  Serial.println(x);

  Last_Send = Send;
 }
 
}

//SerialLCD Functions

