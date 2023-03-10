package cl.api.base.karaf.convenios;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cl.api.base.karaf.convenios.model.*;
import cl.api.base.karaf.convenios.provider.*;

@Service
@Path("/services/")
public class ApiOracleService {

	ConexionServicio service = new ConexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	private static final Logger LOG = LoggerFactory.getLogger(ApiOracleService.class);

	@GET
	@Path("/leer")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> getUsuario(String body) throws SQLException, IOException {
		service.getconexionServicio();
		LOG.info(":::::::::: ENTRANDO A LEER APIORACLESERVICE :::::::: ");
		JSONObject js = new JSONObject(body);
		String user = js.getString("user");

		return service.leerUsuario(user);

	}

	@PUT
	@Path("/actualizarPaciente")
	@Produces(MediaType.APPLICATION_JSON)
	public List<OutActualizar> putUsuarioPaciente(String body) throws SQLException, IOException {

		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String passwd = js.getString("passwd");
		String nombre = js.getString("nombre");
		String apellido = js.getString("apellido");
		String apellido2 = js.getString("apellido2");
		String ndocumento = js.getString("ndocumento");
		String rut = js.getString("rut");
		String celular = js.getString("celular");
		String userRepActualizar = js.getString("userRep");

		int id;
		ArrayList<OutActualizar> datos = new ArrayList<>();
		ArrayList<Usuario> respuesta = service.leerUsuario(user);

		if (respuesta.get(0).getCodigo().equals("1")) {

			ArrayList<OutActualizar> respuestaPaciente = service.InsertarPaciente(user, passwd, nombre, apellido,
					apellido2, rut, ndocumento, celular, userRepActualizar);
			id = Integer.parseInt(respuestaPaciente.get(0).getOutSeq());
			LOG.info("id: " + id);
			service.AsociarUsuarioRol(id, 52, "S", userRepActualizar);

			OutActualizar model = new OutActualizar();
			model.setCodigoResultado(respuestaPaciente.get(0).getCodigoResultado());
			model.setDetalleResultado(respuestaPaciente.get(0).getDetalleResultado());
			datos.add(model);

		} else {
			OutActualizar model = new OutActualizar();
			model.setCodigoResultado("1");
			model.setDetalleResultado("El usuario ya existe");
			datos.add(model);
		}

		return datos;
	}

	@POST
	@Path("/generatoken")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TokenModel> generarToken(String body) throws SQLException, IOException {
		LOG.info("Entrando a generar Token");
		ArrayList<TokenModel> datos = new ArrayList<>();
		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String userRepToken = js.getString("userRep");

		if (user.isEmpty()) {
			TokenModel model = new TokenModel();
			model.setCodigoRespuesta("2");
			model.setDetalle("El usuario no puede estar vacio");
			datos.add(model);
		} else {

			return service.GeneraToken(user, userRepToken);
		}

		return datos;

	}

	@POST
	@Path("/validartoken")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ValidaToken> validarToken(String body) throws SQLException, IOException {
		service.getconexionServicio();

		JSONObject js = new JSONObject(body);
		String login = js.getString("user");
		String token = js.getString("token");
		String userRepValidarToken = js.getString("userRep");

		return service.ValidarToken(login, token, userRepValidarToken);

	}

}