package hr.java.banking.exceptions;

import lombok.Data;

@Data
public class BankingStatusException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	
	private String message;

	public BankingStatusException(Integer code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	

}
