package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	//guardando referência
	private static Scene mainScene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//instancia de um objeto fxml
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			//carregando a view
			ScrollPane scrollPane = loader.load();
			
			//ajustar scrollpane com a menubar
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			//minha cena principal recebe a tela carregada
			mainScene = new Scene(scrollPane);
			//setando na cena do palco com a mainScene
			primaryStage.setScene(mainScene);
			//titulo da tela
			primaryStage.setTitle("JavaFX application");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
