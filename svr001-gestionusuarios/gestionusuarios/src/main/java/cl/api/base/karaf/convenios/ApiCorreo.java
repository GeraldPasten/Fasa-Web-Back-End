package cl.api.base.karaf.convenios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiCorreo

{
	private static final Logger LOG = LoggerFactory.getLogger(ApiOracleService.class);

	public String apiCorreoCall(String user, String token) throws IOException {
		String body = null;

		URL url = new URL("http://150.100.253.61:8181/cxf/correo/services/enviar");
		String query = body;

		// make connection
		URLConnection urlc = url.openConnection();
		// It Content Type is so important to support JSON call
		urlc.setRequestProperty("Content-Type", "application/json");
		urlc.setRequestProperty("toEmailUser", user);
		urlc.setRequestProperty("subject", "Token de validacion");
		urlc.setRequestProperty("bodyMail", "<h2>USUARIO: " + user + "</h2>" + "<h2>TOKEN: " + token + "</h2>");
		urlc.setRequestProperty("smtpFriendlyDisplayName", "FASA-TOKEN-FROM");
		urlc.setRequestProperty("smtpFriendlyMailName", "no_reply_mail@ahumada.cl");
		urlc.setRequestProperty("smtpReplyDisplayName", "no_reply@ahumada.cl");

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
