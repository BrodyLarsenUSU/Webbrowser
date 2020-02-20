package sample;


import javafx.application.Application;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        Browser br = new Browser("https://cs.usu.edu/", "data/favorites.txt");

        String filename = "data/favorites.txt";
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;

        while((line = in.readLine()) != null) {
            br.favorites.add(line);
        }

        br.display(primaryStage);
    }
}
