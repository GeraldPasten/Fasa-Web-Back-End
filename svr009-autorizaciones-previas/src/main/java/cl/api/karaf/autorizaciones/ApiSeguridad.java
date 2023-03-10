package cl.api.karaf.autorizaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiSeguridad  {
	
	
	public boolean ApiSeguridadWeb(String token) throws IOException {
	    String endpointUrl = "http://localhost:8181/cxf/securityToken/webConvenios/validacion/token";

	    URL url = new URL(endpointUrl);
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type", "application/json");
	    connection.setRequestProperty("token", token);

	    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String inputLine;
	    StringBuilder response = new StringBuilder();
	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }
	    in.close();

	    JSONArray jsonArray = new JSONArray(response.toString());
	    JSONObject jsonObject = jsonArray.getJSONObject(0);
	    return jsonObject.getBoolean("validacion");
	}

}
