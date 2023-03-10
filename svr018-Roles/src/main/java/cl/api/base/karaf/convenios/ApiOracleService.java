package cl.api.base.karaf.convenios;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cl.api.base.karaf.convenios.model.*;
import cl.api.base.karaf.convenios.provider.*;

@Service
@Path("/services/")
public class ApiOracleService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	private static final Logger LOG = LoggerFactory.getLogger(ApiOracleService.class);

	@GET
	@Path("/leer/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<roles> getRoles(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER ROLES :::::::: ");
		JSONObject js = new JSONObject(body);
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.leerRoles();
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@PUT
	@Path("/ins/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response> insRoles(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A INSERTAR ROLES :::::::: ");
		ArrayList<response> datos = new ArrayList<>();
		String id_rolRespuesta;
		int id_rol;
		int id_recurso;
		JSONObject js = new JSONObject(body);
		String nombre = js.getString("nombre");
		String vigencia = js.getString("vigencia");
		String userRep = js.getString("userRep");
		String recursos = js.getString("recursos");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			try {
				ArrayList<response> respuesta = service.insertarRol(nombre, vigencia, userRep);

				id_rol = Integer.parseInt((respuesta.get(0).getId_rol()));
				id_rolRespuesta = respuesta.get(0).getId_rol();

				LOG.info("ID_ROL: " + id_rol);

				for (int i = 0; i < recursos.split(",").length; i++) {
					id_recurso = Integer.parseInt(recursos.split(",")[i]);

					LOG.info(i + ": id recursos: " + id_recurso);

					service.asociarRolRecurso(id_rol, id_recurso, id_recurso, userRep);
				}

				response model = new response();
				model.setCodigo("1");
				model.setDetalle("Se inserto el Rol: " + nombre);
				model.setId_rol(id_rolRespuesta);
				datos.add(model);

			} catch (Exception e) {
				response model = new response();
				model.setCodigo("2");
				model.setDetalle("No se pudo insertar el Rol: " + nombre);
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

	@POST
	@Path("/act/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> actRoles(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A ACTUALIZAR ROLES :::::::: ");

		int id_recursos;
		JSONObject js = new JSONObject(body);
		int id_rol = js.getInt("id_rol");
		String nombre = js.getString("nombre");
		String vigencia = js.getString("vigencia");
		String userRep = js.getString("userRep");
		String recursos = js.getString("recursos");
		String nuevoRecurso = js.getString("nuevoRecurso");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			for (int i = 0; i < recursos.split(",").length; i++) {

				id_recursos = Integer.parseInt(recursos.split(",")[i]);

				LOG.info(i + ": id recursos: " + id_recursos);

				service.QuitarRecursoRol(id_recursos, userRep, id_rol, id_recursos);
			}

			for (int i = 0; i < nuevoRecurso.split(",").length; i++) {

				id_recursos = Integer.parseInt(nuevoRecurso.split(",")[i]);

				LOG.info(i + ": id nuevo recursos: " + id_recursos);

				service.asociarRolRecurso(id_rol, id_recursos, id_recursos, userRep);
			}

			return service.actualizarRol(id_rol, nombre, userRep, vigencia);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@DELETE
	@Path("/del/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> delRoles(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A DELETE ROLES :::::::: ");

		JSONObject js = new JSONObject(body);
		int id_rol = js.getInt("id_rol");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.eliminarRol(id_rol, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Path("/leer/recurso")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<recursos> getRecursos(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER Recursos :::::::: ");
		JSONObject js = new JSONObject(body);
		int id_recurso = js.getInt("id_recurso");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.leerRecursos(id_recurso);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Path("/listar/recurso")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<recursos> getAllRecursos(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER Recursos :::::::: ");
		JSONObject js = new JSONObject(body);
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.listarRecursos();
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Path("/leer/recursos/rol")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<recursoRol> getRecursosRol(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER Recursos asociado a Rol :::::::: ");
		JSONObject js = new JSONObject(body);
		int id_recurso = js.getInt("id_recurso");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.leerRecursosRol(id_recurso);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@GET
	@Path("/listar/recurso/rol")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<recursoRol> getAllRecursosRol(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER Recursos :::::::: ");
		JSONObject js = new JSONObject(body);
		int id_rol = js.getInt("id_rol");
		String token = js.getString("token");
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.listarRecursosRol(id_rol);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@PUT
	@Path("/asociar/recursos")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> asociarRecurso(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A INSERTAR ROLES :::::::: ");

		JSONObject js = new JSONObject(body);
		int id_rol = js.getInt("id_rol");
		int id_recurso = js.getInt("id_recurso");
		int id_recursoPadre = js.getInt("id_recursoPadre");
		String userRep = js.getString("userRep");
		String token = js.getString("token");
		
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);
		
		if (validate) {
			return service.asociarRolRecurso(id_rol, id_recurso, id_recursoPadre, userRep);
		}else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

		
	}

	@DELETE
	@Path("/del/recurso/rol")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> delRecursoRol(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A DELETE ROLES :::::::: ");

		JSONObject js = new JSONObject(body);
		int id_recurso = js.getInt("id_recurso");
		int id_rol = js.getInt("id_rol");
		int id_recursoPadre = js.getInt("id_recursoPadre");
		String userRep = js.getString("userRep");
		String token = js.getString("token");
		
		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);
		
		if (validate) {
			return service.QuitarRecursoRol(id_recurso, userRep, id_rol, id_recursoPadre);
		}else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}
		

		
	}

	@PUT
	@Path("/asociar/rol/usuario")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> asociarUsuarioRol(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A ASOCIAR USUARIO A ROL :::::::: ");

		JSONObject js = new JSONObject(body);
		int rol = 0;
		int usuario = 0;
		String id_rol = js.getString("id_rol");
		String id_usuario = js.getString("id_usuario");
		String userRep = js.getString("userRep");
		String vigencia = js.getString("vigencia");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			ArrayList<response1> datos = new ArrayList<>();

			if (id_rol.isEmpty()) {
				response1 model = new response1();
				model.setDetalle("El dato id_rol no puede estar vacio");
				model.setCodigo("2");
				datos.add(model);
			} else {
				rol = Integer.parseInt(id_rol);
			}
			if (id_usuario.isEmpty()) {
				response1 model = new response1();
				model.setDetalle("El dato id_usuario no puede estar vacio");
				model.setCodigo("2");
				datos.add(model);
			} else {
				usuario = Integer.parseInt(id_usuario);
			}

			if (usuario != 0 && rol != 0) {
				return service.AsociarUsuarioRol(usuario, rol, vigencia, userRep);
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

	@GET
	@Path("/listar/usuario/rol")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RolUsuario> getUsuarioRol(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LEER UsuarioRol:::::::: ");

		int usuario = 0;
		int rol = 0;

		JSONObject js = new JSONObject(body);
		String id_usuario = js.getString("id_usuario");

			if (id_usuario.isEmpty() != true) {
				usuario = Integer.parseInt(id_usuario);
			}

			ArrayList<RolUsuario> datos = (ArrayList<RolUsuario>) service.leerUsuarioRol(usuario);

			if (datos.get(0).getId_rol() != null) {
				LOG.info("ID_ROL: " + rol);
				rol = Integer.parseInt(datos.get(0).getId_rol());
				ArrayList<recursoRol> respuesta = service.listarRecursosRol(rol);

				Set<String> recursos = new HashSet<>();
				for (recursoRol respuestaItem : respuesta) {
					recursos.add(respuestaItem.getId_recurso());
				}

				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (String recurso : recursos) {
					sb.append(recurso);
					if (i < recursos.size() - 1) {
						sb.append(",");
					}
					i++;
				}

				RolUsuario model = new RolUsuario();
				model.setId_recurso(sb.toString());
				datos.add(model);

			} else {
				RolUsuario model = new RolUsuario();
				model.setDetalle("El usuario no tiene un rol asociado");
				model.setCodigo("1");
				datos.add(model);

			}

			return datos;

	}

	@POST
	@Path("/act/usuario/rol")
	@Produces(MediaType.APPLICATION_JSON)
	public List<response1> actRolUsuario(String body) throws SQLException, IOException {

		int idUsuario = 0;
		int idRol = 0;
		int idRolNuevo = 0;

		JSONObject js = new JSONObject(body);
		String id_usuario = js.getString("id_usuario");
		String id_rol = js.getString("id_rol");
		String id_rol_nuevo = js.getString("id_rolNuevo");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			if (id_usuario.isEmpty() != true) {
				idUsuario = Integer.valueOf(id_usuario);
			}
			if (id_rol.isEmpty() != true) {
				idRol = Integer.valueOf(id_rol);
			}
			if (id_rol_nuevo.isEmpty() != true) {
				idRolNuevo = Integer.valueOf(id_rol_nuevo);
			}

			LOG.info("id_usuario: " + idUsuario);
			LOG.info("id_rol: " + idRol);
			LOG.info("id_rol_nuevo: " + idRolNuevo);

			return service.updateAsociarRol(idUsuario, idRol, idRolNuevo);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

}