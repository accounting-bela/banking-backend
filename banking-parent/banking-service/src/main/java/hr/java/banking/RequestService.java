package hr.java.banking;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import hr.java.banking.entities.Racun;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.request.QrRequest;
import net.sf.jasperreports.engine.JRException;

@Service
public interface RequestService {
	
	InputStreamResource getQrCode(Racun racun) throws BankingStatusException;
	
	String createJasper(QrRequest request, InputStream image) throws JRException, IOException;

}
