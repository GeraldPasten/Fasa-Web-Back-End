package cl.api.karaf.apisExternas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiValidaRut {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExternasService.class);
	
	public String CallValidaRut(String body) throws IOException {
		
		URL url = new URL("http://150.100.253.61:8181/cxf/id-checker/validate");
		String query = body;

		// make connection
		URLConnection urlc = url.openConnection();
		// It Content Type is so important to support JSON call
		urlc.setRequestProperty("Content-Type", "application/json");
		LOG.info("Conectando: " + url.toString());
		// use post mode
		urlc.setDoOutput(true);
		urlc.setAllowUserInteraction(false);

		// send query
		PrintStream ps = new PrintStream(urlc.getOutputStream());
		ps.print(query);
		LOG.info("Consulta: " + query);
		ps.close();

		// get result
		BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

		String response = br.readLine().toString();
		br.close();
		return response;
		
	}

}
