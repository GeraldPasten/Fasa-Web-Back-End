package cl.api.base.karaf.convenios;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import cl.api.base.karaf.convenios.model.*;
import cl.api.base.karaf.convenios.provider.*;

@Service
@Path("/services/")
public class ApiOracleService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	private static final Logger LOG = LoggerFactory.getLogger(ApiOracleService.class);

	@GET
	@Path("/buscarPoliza")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response> buscarPoliza(String body) throws SQLException, IOException {

		JSONObject js = new JSONObject(body);
		String codigo = js.getString("codigo");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.buscarPolizas(codigo, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());

		}

	}

	@PUT
	@Path("/actualizarP")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> act(String body) throws SQLException, IOException {

		int bi = 0;
		JSONObject js = new JSONObject(body);
		String grupo = js.getString("grupo");
		String nombre = js.getString("nombre");
		String codigo = js.getString("codigo");
		String rut = js.getString("rut");
		String fecha = js.getString("fecha");
		String bio = js.getString("bio");
		String userRep = js.getString("userRep");
		String token = js.getString("token");
		
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);
		
		if (validate) {
			if (bio.isEmpty() != true) {
				bi = Integer.parseInt(bio);
			}

			return service.ActualizarPoliza(grupo, nombre, codigo, rut, fecha, bi, userRep);
		}else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());

		}

		
	}

}