package Winder.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Winder.communicaton.MesgRecv;

public class MainFrame extends Application {
    private Stage stage;
    private String address;
    
    public static int id;
    public static MainController mainController;
    
    private Socket socket = null;
    public static MesgRecv client = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage; //ステージを保存
        stage.setTitle("Winder");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/Main.fxml"));
        Parent root = loader.load();
        mainController = (MainController)loader.getController();
        mainController.setStage(stage);

        Scene scene = new Scene(root);

        stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == true && newValue == false) {
                client.out.println("DISCONNECT");
                client.Disconnect();
            }
        });

        stage.setScene(scene);
        stage.show();

        try {
            File file = new File("dat/userdata.dat");
            BufferedReader br = new BufferedReader(new FileReader(file));

            id = Integer.parseInt(br.readLine());

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File file = new File("dat/configure.dat");
            BufferedReader br = new BufferedReader(new FileReader(file));

            address = br.readLine();

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            socket = new Socket(address, 10000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client = new MesgRecv(socket);
        client.start();
    }

    public static void main (String[] args) {
        launch(args);
    }
}