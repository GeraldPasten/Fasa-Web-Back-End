package cl.api.karaf.autorizaciones;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.autorizaciones.model.autorizacionPrevia;
import cl.api.karaf.autorizaciones.model.response;
import cl.api.karaf.autorizaciones.provider.*;

@Service
@Path("/services/")
public class AutorizacionService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insertar/autorizaciones")
	public ArrayList<response> InsertarAut(String body) throws SQLException, IOException {
		LOG.info("ENTRANDO A AUTORIZACIONES PREVIAS INSERT");
		JSONObject js = new JSONObject(body);

		String credenciales = js.getString("credenciales");
		String codigoPersona = js.getString("codigoPersona");
		String campo = js.getString("campo");
		String valorCampo = js.getString("valorCampo");
		String incluir_exc = js.getString("incluir_exc");
		String fecha_inicio = js.getString("fecha_inicio");
		String fecha_termino = js.getString("fecha_termino");
		String planId = js.getString("planId");
		String idMedico = js.getString("idMedico");
		String medicoIncExc = js.getString("medicoIncExc");
		String userRep = js.getString("userRep");
		int envases = js.getInt("envases");
		String token = js.getString("token");
		
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);
		
		if (validate) {
			return service.InsertarAutorizaciones(credenciales, codigoPersona, campo, valorCampo, incluir_exc, fecha_inicio,
					fecha_termino, envases, planId, idMedico, medicoIncExc, userRep);
		}else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}


	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listar/autorizaciones")
	public ArrayList<autorizacionPrevia> listarAut (String body) throws SQLException, IOException{
		JSONObject js = new JSONObject(body);
		String rut = js.getString("rut");
		String convenio = js.getString("convenio");
		String token = js.getString("token");
		
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);
		
		if (validate) {
			return service.listarAutorizacion(rut, convenio);
		}else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}
			
		
	}

	private static final Logger LOG = LoggerFactory.getLogger(AutorizacionService.class);

}