package ui;

import analysis.service.CalcEngine;
import analysis.service.GraphEngine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class AnalysisWindow {

    @FXML public Button simButton;
    private static Scene simScene;
    public static void setSimScene(Scene scene) {
        simScene = scene;
    }

    public void openSimScene(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(simScene);
    }

    @FXML public Button editButton;
    private static Scene editScene;
    public static void setEditScene(Scene scene) {
        editScene = scene;
    }

    public void openEditScene(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(editScene);
    }

    @FXML public static Image png = new Image("file:main/resources/layout/output.png");
    public static void updatePNG () {
        png = new Image("file:main/resources/layout/output.png");
    }

    @FXML static public Text effscore = new Text();
    public static void updateEffScore() {
        double currentEff = CalcEngine.getCurrentEffectiveness();
        String score = valueOf(currentEff);
        effscore.setText(score);
    }

    @FXML static public Text numWaiting = new Text();
    public static void updateNumWaiting() {
        double currentEff = CalcEngine.getCurrentEffectiveness();
        String score = valueOf(currentEff);
        numWaiting.setText(score);
    }

    @FXML static public Button updateAnalysis;
    public void ulysis() {
        updateNumWaiting();
        updateEffScore();
        updatePNG();
    }
}