package hr.java.banking.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import hr.java.banking.RequestService;
import hr.java.banking.entities.Racun;
import hr.java.banking.exceptions.BankingStatusException;
import hr.java.banking.request.QrRequest;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Service
public class RequestServiceImpl implements RequestService {
	
	RestTemplate restTemplate;
	
	@Autowired
	private ResourceLoader resourceLoader;
	

	public RequestServiceImpl() {
		super();
		this.restTemplate = new RestTemplate();
	}



	@Override
	public InputStreamResource getQrCode(Racun racun) throws BankingStatusException {
		String url = "https://hub3.bigfish.software/api/v1/barcode";
		QrRequest req = new QrRequest(racun);
		HttpEntity<QrRequest> request = new HttpEntity<>(req);
		byte[] qr = restTemplate.postForObject(url, request, byte[].class);
		ByteArrayInputStream byteStream = null;
		String out = "";
		try {
		    out = createJasper(req, new ByteArrayInputStream(qr));
		    File file = new File(out);
		    byteStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
		}
		catch(IOException | JRException e)
		{
			System.out.println(e);
			throw new BankingStatusException(400, e.getLocalizedMessage());
		}
	    
		return new InputStreamResource(byteStream);
	}



	@Override
	public String createJasper(QrRequest request, InputStream image) throws JRException, IOException {

		  JasperReport jasperReport = null;
		  
		  File f = ResourceUtils.getFile("classpath:racuni.jasper");
		  
		  jasperReport = (JasperReport) JRLoader.loadObject(f);
		  
		  jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");

		  // Parameters for report
		  Map<String, Object> parameters = new HashMap<String, Object>();
		  
		  parameters.put("racun", request.getData());
		  
		  parameters.put("kod", image);

		  JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource(1));
		  
		  File out = new File("report.pdf");
		  
		  OutputStream output = new FileOutputStream(out);
		  
		  JasperExportManager.exportReportToPdfStream(print, output);
		  
		  return out.getAbsolutePath();
	}

}
