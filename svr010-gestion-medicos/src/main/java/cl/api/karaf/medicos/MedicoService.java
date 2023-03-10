package cl.api.karaf.medicos;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.DELETE;
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

import cl.api.karaf.medicos.model.Codigolista;
import cl.api.karaf.medicos.model.medicos;
import cl.api.karaf.medicos.model.response;
import cl.api.karaf.medicos.provider.*;

@Service
@Path("/services/")
public class MedicoService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listar/listaCodigos")
	public ArrayList<Codigolista> ListarCodigos(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String codigoConvenio = js.getString("codigoConvenio");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.BuscarListas(codigoConvenio);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listar/listaMedicos")
	public ArrayList<medicos> ListarMedicos(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String codigoLista = js.getString("codigoLista");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.ListarMedicos(codigoLista);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/act/actualizarListaMedic")
	public ArrayList<response> ActualizarListaMedico(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String codigoLista = js.getString("codigoLista");
		String rut = js.getString("rut");
		String fecha = js.getString("fecha");
		String exc_inc = js.getString("exc_inc");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			service.EliminarRutListamedicos(codigoLista, rut, userRep);

			LOG.info("MEDICO SERVICE:::: ELIMINO RUT");

			return service.ActualizarListamedicos(codigoLista, rut, fecha, exc_inc, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/Ins/agregarRut")
	public ArrayList<response> AgregarRutListaMedico(String body) throws SQLException, IOException {

		ArrayList<response> datos = new ArrayList<>();

		JSONObject js = new JSONObject(body);
		String codigoLista = js.getString("codigoLista");
		String rut = js.getString("rut");
		String fecha = js.getString("fecha");
		String exc_inc = js.getString("exc_inc");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			ArrayList<response> respuesta = service.BuscarRutListamedicos(codigoLista, rut);

			LOG.info("ARRAY 0:::: " + respuesta.get(0).getCodigo());
			LOG.info("ARRAY 0:::: " + respuesta.get(0).getDetalle());

			if (respuesta.get(0).getCodigo() == 0) {
				service.InsertarListamedicos(codigoLista, rut, fecha, exc_inc, userRep);

			} else if (respuesta.get(0).getCodigo() == 2) {
				response model = new response();
				model.setCodigo(1);
				model.setDetalle("el rut no existe");
				datos.add(model);
				return datos;

			}

			response model = new response();
			model.setCodigo(0);
			model.setDetalle("Rut ingresado a la lista");
			datos.add(model);
			return datos;
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/del/eliminarRut")
	public ArrayList<response> EliminarRutListaMedico(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String codigoLista = js.getString("codigoLista");
		String rut = js.getString("rut");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.EliminarRutListamedicos(codigoLista, rut, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delAll/lista")
	public ArrayList<response> EliminarAllRutListaMedico(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String codigoLista = js.getString("codigoLista");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.EliminarAllRutListamedicos(codigoLista, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search/buscaRut")
	public ArrayList<response> BuscaRutListaMedico(String body) throws SQLException, IOException {

		JSONObject js = new JSONObject(body);
		String codigoLista = js.getString("codigoLista");
		String rut = js.getString("rut");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.BuscarRutListamedicos(codigoLista, rut);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(MedicoService.class);

}