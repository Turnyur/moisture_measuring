/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moisture;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Turnyur Siy
 */
public class fxml_moistureController implements Initializable {
FadeTransition ft ;
    @FXML
    private String mstr = "";
    @FXML
    private byte[] buffer = new byte[1024];
    @FXML // fx:id="fruitCombo"
    private ComboBox<String> COMPortCombo; // Value injected by FXMLLoader
    @FXML
    Label con_status, myPort, real_data;
    @FXML
    private String selected_port = "";
    @FXML
    private Button connect_btn;
    public fxml_moistureController controller;
    public SerialPort serialPort;
    @FXML
    private final Button mstop;
    @FXML
    private HBox r_vbox; 

    public fxml_moistureController() {
        con_status = new Label();
        myPort = new Label();
        real_data = new Label();
        connect_btn = new Button();
        mstop = new Button();
        r_vbox=new HBox();

    }

    /* 
     @FXML
     private Label label;
    
     @FXML
     private void handleButtonAction(ActionEvent event) {
     System.out.println("You clicked me!");
     label.setText("Hello World!");
     }
     */
    @FXML
    private void handle_combo() {
        /*  COMPortCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

         @Override
         public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 
         }
            
         });
         */
        
        selected_port = COMPortCombo.getSelectionModel().getSelectedItem();
        ft= new FadeTransition(Duration.millis(400), COMPortCombo);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);

        ft.play();
        
        ft = new FadeTransition(Duration.millis(800), myPort);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);

        ft.play();
        myPort.setText("USB Port " + selected_port + " selected");
    }

    @FXML
    private void handle_mstop(ActionEvent event) {
        ft = new FadeTransition(Duration.millis(900), mstop);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(2);
        ft.setAutoReverse(false);

        ft.play();
        serialPort.close();
        connect_btn.setVisible(true);
        con_status.setTranslateX(-120);
        con_status.setTextFill(Color.RED);
       // r_vbox.setBackground(Background.EMPTY);
        con_status.setText("Disconnected!");
    }

    @FXML
    private void handle_connect_btn() {

        try {

            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(selected_port);
            if (portIdentifier.isCurrentlyOwned()) {
                System.out.println("Error: Port is currently in use");
            } else {
                CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                if (commPort instanceof SerialPort) {
                    serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    InputStream in = serialPort.getInputStream();
                    OutputStream out = serialPort.getOutputStream();

                  //  (new Thread(new SerialWriter(out))).start();

                    // serialPort.addEventListener(new SerialReader(in));
                    serialPort.notifyOnDataAvailable(true);
                    serialPort.addEventListener(new SerialPortEventListener() {
                        @Override
                        public void serialEvent(SerialPortEvent spe) {

                            //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                            int data;
                            try {

                                int len = 0;
                                while ((data = in.read()) > -1) {
                                    /*
                                     if ( data == '\n' ) {
                                     break;
                                     }
                                     */
                                    buffer[len++] = (byte) data;
                                }
                                //System.out.print(new String(buffer, 0, len));
                                mstr = (new String(buffer, 0, len));
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        TranslateTransition tt = new TranslateTransition(Duration.millis(200), real_data);
                                        tt.setByY(20f);
                                        tt.setCycleCount(2);
                                        tt.setAutoReverse(true);

                                        tt.play();
                                        real_data.setText(mstr);
                                        connect_btn.setVisible(false);
                                        con_status.setTranslateX(120);
                                        con_status.setTextFill(Color.GREEN);
                                        con_status.setText("Connected!");
                                        r_vbox.setTranslateY(58);
                                        
                                        
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.exit(-1);
                            }

                        }
                    });

                } else {
                    System.out.println("Error: Only serial ports are handled by this example.");
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.print("Selected Port not active");
            myPort.setText("Selected Port not active");
            real_data.setText("Selected Port not active");
        }
    }


   /*
    

    public static class SerialWriter implements Runnable {

        OutputStream out;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {
                int c = 0;
                while ((c = System.in.read()) > -1) {
                    //  this.out.write(c);

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
    */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        COMPortCombo.getItems().setAll("COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10",
                "COM11", "COM12", "COM13", "COM14", "COM15", "COM16", "COM17", "COM18", "COM19", "COM20", "COM21", "COM22", "COM23", "COM24",
                "COM25", "COM26", "COM27", "COM28", "COM29", "COM30", "COM31", "COM32", "COM33", "COM34", "COM35", "COM36", "COM37", "COM38", "COM39",
                "COM40", "COM41", "COM42", "COM43", "COM44", "COM45", "COM46", "COM47", "COM48", "COM49", "COM50", "COM51", "COM52", "COM53", "COM54",
                "COM55", "COM56", "COM56", "COM58", "COM59", "COM60", "COM61", "COM62", "COM63", "COM64", "COM65", "COM66", "COM67", "COM68", "COM69",
                "COM70", "COM71", "COM72", "COM73", "COM74", "COM75", "COM76", "COM77", "COM78", "COM79", "COM80", "COM81", "COM82", "COM83", "COM84",
                "COM85", "COM86", "COM87", "COM88", "COM89", "COM90", "COM91", "COM92", "COM93", "COM94", "COM95", "COM96", "COM97", "COM98", "COM99");
        mstop.setVisible(false);
    }

}
