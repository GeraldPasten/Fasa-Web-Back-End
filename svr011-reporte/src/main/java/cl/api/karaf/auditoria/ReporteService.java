package cl.api.karaf.auditoria;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.auditoria.model.auditoria;
import cl.api.karaf.auditoria.provider.*;

@Service
@Path("/services/")
public class ReporteService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reportar/auditoria")
	public ArrayList<auditoria> reporte(String body) throws SQLException, IOException {
		LOG.info(":::::::::::: Entrando a reporte Aditoria :::::::::::::::::");
		service.getconexionServicio();
		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String servicio = js.getString("servicio");
		String accion = js.getString("accion");
		String fechaDesde = js.getString("fechaDesde");
		String fechaHasta = js.getString("fechaHasta");
		String token = js.getString("token");
		
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);
		
		if (validate) {
			return service.MostrarAuditoria(user, servicio, accion, fechaDesde, fechaHasta);
		}else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}
	
	}
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ReporteService.class);

}