package cl.api.karaf.empresa;

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

import cl.api.karaf.empresa.model.OutActualizar;
import cl.api.karaf.empresa.model.UsuarioEmpresa;
import cl.api.karaf.empresa.model.*;
import cl.api.karaf.empresa.provider.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@Service
@Path("/services/")

public class EmpresaService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	private static final Logger LOG = LoggerFactory.getLogger(EmpresaService.class);

	@GET
	@Path("/leerEmpresa")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<UsuarioEmpresa> getUsuarioEmpresa(String body) throws SQLException, IOException {

		LOG.info("ENTRANDO A LEER EMPRESA APIORACLESERVICE :::::::: ");
		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadCall(token);
		LOG.info("respuesta api: " + validate);

		return service.leerUsuarioEmpresa(user);

	}

	@PUT
	@Path("/actualizarEmpresa")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<OutActualizar> PutUsuarioEmpresa(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A ACTUALIZAR EMPRESA APIORACLESERVICE :::::::: ");

		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String passwd = js.getString("passwd");
		String nombre = js.getString("nombre");
		String apellido = js.getString("apellido");
		String apellido2 = js.getString("apellido2");
		String kamConvenios = js.getString("kamConvenios");
		String kamCorreo = js.getString("kamCorreo");
		String cargo = js.getString("cargo");
		String rut = js.getString("rut");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadCall(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			int id;
			ArrayList<OutActualizar> datos = new ArrayList<>();
			ArrayList<UsuarioEmpresa> respuesta = service.leerUsuarioEmpresa(user);
			LOG.info("a : " + respuesta);

			if (respuesta.get(0).getCodigo().equals("1")) {

				ArrayList<OutActualizar> respuestaEmpresa = service.InsertarEmpresa(user, passwd, nombre, apellido,
						apellido2, kamConvenios, kamCorreo, cargo, rut, userRep);
				id = Integer.parseInt(respuestaEmpresa.get(0).getOutSeq());
				LOG.info("id: " + id);
				service.AsociarUsuarioRol(id, 53, "S", userRep);

				OutActualizar model = new OutActualizar();
				model.setCodigoResultado(respuestaEmpresa.get(0).getCodigoResultado());
				model.setDetalleResultado(respuestaEmpresa.get(0).getDetalleResultado());
				datos.add(model);

			} else {
				OutActualizar model = new OutActualizar();
				model.setCodigoResultado("1");
				model.setDetalleResultado("El usuario ya existe");
				datos.add(model);
			}

			return datos;
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@PUT
	@Path("/actualizarConvenio")
	@Produces(MediaType.APPLICATION_JSON)
	public String actualizarConvenio(String body) throws SQLException, IOException {

		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String codigo = js.getString("codigo");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadCall(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			for (int i = 0; i < codigo.split(",").length; i++) {
				service.ActualizarConvenio(user, codigo.split(",")[i], userRep);
			}

			JSONObject respuesta = new JSONObject();
			respuesta.put("detalle", "Se asociaron correctamente los convenios");
			return respuesta.toString();
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}
	}

	@GET
	@Path("/listarConvenios")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<empresa> getConvenios(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LISTAR APIORACLESERVICE :::::::: ");
		JSONObject js = new JSONObject(body);
		String token = js.getString("token");
		boolean validate = seguridad.ApiSeguridadCall(token);

		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.ListarConvenio();
		} else {
			ArrayList<empresa> datos = new ArrayList<>();
			empresa model = new empresa();
			model.setCodigo("403");
			model.setDetalle("Token Invalido, por favor vuelva generarlo");
			datos.add(model);

			throw new WebApplicationException(
					Response.status(Response.Status.FORBIDDEN).entity(datos).type(MediaType.APPLICATION_JSON).build());
		}

	}

}