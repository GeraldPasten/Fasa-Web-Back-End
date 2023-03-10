package cl.api.karaf.eliminar;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
//import io.swagger.annotations.Api;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.eliminar.model.*;
import cl.api.karaf.eliminar.provider.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@Service
@Path("/services/")
//@Api(value = "/services", description = "Service for SVR001-GESTION USUARIOS ")
public class EliminarService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();
	private static final Logger LOG = LoggerFactory.getLogger(EliminarService.class);

	@POST
	@Path("/eliminar")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<eliminar> eliminarUsuario(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A ELIMINAR USUARIO APIORACLESERVICE :::::::: ");
		JSONObject js = new JSONObject(body);
		String id = js.getString("id");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.EliminarUsuario(id, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Path("/listar")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Usuario> getAllUsuarios(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER APIORACLESERVICE :::::::: ");
		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String convenio = js.getString("convenio");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.ListarUsuarios(user, convenio);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Path("/listar/rep")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Usuario> getAllUsuariosRep(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.ListarUsuariosReporte();
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

}