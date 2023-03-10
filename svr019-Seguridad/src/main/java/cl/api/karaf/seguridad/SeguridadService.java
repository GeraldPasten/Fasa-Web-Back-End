package cl.api.karaf.seguridad;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cl.api.karaf.seguridad.model.response;
import cl.api.karaf.seguridad.model.responseToken;
import cl.api.karaf.seguridad.provider.*;

@Service
@Path("/webConvenios/")
public class SeguridadService {

	conexionServicio service = new conexionServicio();

	@GET
	@Path("/generacion/token")
	public Response TokenSeguridad(String body) throws JsonProcessingException, SQLException {
		
		//Aqui se utiliza el metodo que llama al proceso almacenado y valida su resultado
		//En este caso si la respuesta del sp es "EXITO" entonces retorna el token y un 200 ok 
		//En caso de que los datos proporcionados no sean validos se devuelve un codigo 403 y un mensaje de error
		
		JSONObject js = new JSONObject(body);
		String usuario = js.getString("usuario");
		String password = js.getString("password");

		ArrayList<response> datos = service.WebConveniosToken(usuario, password);
		String validacion = datos.get(0).getMessage();
		if (validacion.equals("EXITO")) {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData = objectMapper.writeValueAsString(datos);

			return Response.ok(jsonData, MediaType.APPLICATION_JSON).build();
		} else {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonData = objectMapper.writeValueAsString(datos.get(0).getMessage());
			return Response.status(403, "Datos invalidos").entity(jsonData).build();
		}

	}
	
	@POST
	@Path("/validacion/token")
	public Response ValidaTokenSeguridad(String body) throws SQLException, JsonProcessingException {
	 
		JSONObject js = new JSONObject(body);
		String token = js.getString("token");
		
		ArrayList<responseToken> datos = service.WebConveniosValidaToken(token);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonData = objectMapper.writeValueAsString(datos);
		
		return Response.ok(jsonData, MediaType.APPLICATION_JSON).build();
		
	}
	
	

}