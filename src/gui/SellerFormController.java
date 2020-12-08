package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;

	private SellerService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	// outros objetos que implementar essa classe, recebem o evento
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			// pega os dados das caixinhas e instancia um departament
			entity = getFormData();
			// salvando no banco de dados
			service.saveOrUpdate(entity);
			// notifica os listeners
			notifyDataChangeListeners();
			// pegando referencia da tela atual e fechando.
			Utils.currentStage(event).close();

		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	// método que notifica as mudanças na lista(Classe subject - emite o evento)
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		//campo nome não pode estar vazio
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addErros("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	// se a injeção da classe Seller estiver nula, retorna um erro, caso
	// contrário, monta o objeto nos campos txt.
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

	// percorrendo a coleção e preenchendo as caixas de texto com os erros
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		// se na coleção de chaves Set, conter o name, ele seta a mensagem de erro no
		// label
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}

	}
}
