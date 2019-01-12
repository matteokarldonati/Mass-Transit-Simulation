import core.model.Route;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import ui.EditWindow;
import ui.SimWindow;
import ui.AnalysisWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

public class Main extends Application{

    public void start(Stage primaryStage) throws Exception {
        // Initial stage setup
        primaryStage.setTitle("Mass Transit Simulation");


        // Sets the scene
        Scene simScene = new Scene(FXMLLoader.load(getClass().getResource("/layout/simulation.fxml")));
        Scene editScene = new Scene(FXMLLoader.load(getClass().getResource("/layout/editor.fxml")));
        Scene analysisScene = new Scene(FXMLLoader.load(getClass().getResource("/layout/analysis.fxml")));

        SimWindow.setEditScene(editScene);
        SimWindow.setAnalysisScene(analysisScene);
        EditWindow.setSimScene(simScene);
        EditWindow.setAnalysisScene(analysisScene);
        AnalysisWindow.setEditScene(editScene);
        AnalysisWindow.setSimScene(simScene);

        primaryStage.setScene(simScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}