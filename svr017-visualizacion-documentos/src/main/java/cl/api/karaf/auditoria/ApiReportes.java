package cl.api.karaf.auditoria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;


public class ApiReportes 

{
	
	
	public String ApiCall(String userRep,String accion,String detalle,String servicio ) throws IOException 
	{
		String body = null;
		URL url = new URL("http://localhost:8181/cxf/reporte/services/reporte/auditoria");
		String query = body;

		URLConnection urlc = url.openConnection();

		urlc.setRequestProperty("Content-Type", "application/json");
		urlc.setRequestProperty("user", userRep);
		urlc.setRequestProperty("accion", accion);
		urlc.setRequestProperty("detalle", detalle);
		urlc.setRequestProperty("servicio", servicio);
		
		urlc.setDoOutput(true);
		urlc.setAllowUserInteraction(false);

		PrintStream ps = new PrintStream(urlc.getOutputStream());
		ps.print(query);
		ps.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

		br.close();
		
		return "la llamada se realizo con exito";
				
	}

	
	
	
	
}
