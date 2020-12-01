package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	// synchronized garante que a aplicação gráfica não seja interrompida
	// parâmetro Consumer para evitar repetições de loadView
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			// instanciando um FXML a partir de um caminho do projeto
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//carrega novo VBox da tela carregada
			VBox newVBox = loader.load();

			// mainScene recebe o ScrollPane da classe Main
			Scene mainScene = Main.getMainScene();

			// mainVBox pegando o VBox da tela principal
			// getRoot (primeiro elemento da MainView = ScrollPane)
			// getContent (VBox do MainView)
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// childrens do VBox da MainView(MenuBar)
			Node mainMenu = mainVBox.getChildren().get(0);

			// limpar todos os filhos do mainVBox
			mainVBox.getChildren().clear();

			// adiciona na tela principal a MenuBar
			mainVBox.getChildren().add(mainMenu);

			// adiciona na tela principal, todos os filhos do newVBox(tela carregada pelo loadView)
			mainVBox.getChildren().addAll(newVBox.getChildren());

			//inicializa o controller passado no método
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
