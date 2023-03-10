package cl.api.karaf.apisExternas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.api.karaf.apisExternas.model.enrolar;

public class ApiEnrolar {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExternasService.class);

	public ArrayList<enrolar> CallEnrolar(String body) throws MalformedURLException {
		
		URL url = new URL("https://esb.ahumada.cl:8443/ESB/Kiosko/Enrolamiento/realizar");
		String query = body;
		ArrayList<enrolar> datos = new ArrayList<>();

		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			final SSLContext sc = SSLContext.getInstance("SSL");
			try {
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
			} catch (KeyManagementException e) {

				e.printStackTrace();
			}

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URLConnection urlc = url.openConnection();
			urlc.setRequestProperty("Content-Type", "application/json");
			urlc.setRequestProperty("Authorization", "Basic dmVudGFzOmZhc2EyMDE4");
			urlc.setAllowUserInteraction(true);

			LOG.info("Conectando: " + url.toString());
			urlc.setDoOutput(true);
			urlc.setAllowUserInteraction(false);

			PrintStream ps = new PrintStream(urlc.getOutputStream());
			ps.print(query);
			LOG.info("Consulta: " + query);
			ps.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			String response = br.readLine().toString();

			enrolar model = new enrolar();

			model.setMassage(response.split(",")[1]);
			model.setCodigo(response.split(",")[2]);
			datos.add(model);

			br.close();

		} catch (Exception e) {
			enrolar model = new enrolar();
			model.setMassage("Business error: Correo electrónico no exclusivo del cliente");
			model.setCodigo("ERROR_VALIDACION");
			datos.add(model);
		}
		return datos;
	}

}
