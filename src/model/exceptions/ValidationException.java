package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	//cole��o map com chave:name e valor:error
	public Map<String, String> getErrors(){
		return errors;
	}
	
	//adiciona erros na cole��o Map
	public void addErros(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
	
	
}
