float sensor1=A0;
int temp_value;
float temp;
int power_LED=10;
int transceive_LED=11;
void setup()
{
  Serial.begin(9600); //set up serial library baud rate to 9600
  pinMode(transceive_LED,OUTPUT);
   pinMode(power_LED,OUTPUT);
   digitalWrite(power_LED,HIGH);
   
}
void loop()
{
  temp_value=(int)analogRead(sensor1);
  //temp=temp_value* (5.0 / 1023.0); //convert analog reading to temperature in degree Celsious
  temp=temp_value;
  delay(200);
 //  Serial.println("Soil Sensor: ");
  Serial.println(temp);
  
  for(int i=0;i<=5;i++){
   digitalWrite(transceive_LED,HIGH);
   delay(20);
   digitalWrite(transceive_LED,LOW);
   delay(20);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
 }
  delay(500);
}
