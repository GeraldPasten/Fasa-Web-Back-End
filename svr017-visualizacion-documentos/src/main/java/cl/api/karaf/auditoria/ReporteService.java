package cl.api.karaf.auditoria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.auditoria.model.Response1;
import cl.api.karaf.auditoria.model.documentResponse;
import cl.api.karaf.auditoria.provider.conexionServicio;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.json.JSONObject;

@Service
@Path("/services/")
public class ReporteService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/visualizar/documento")
	public ArrayList<documentResponse> reporte(MultipartBody body) throws IOException {

		List<Attachment> atts = body.getAllAttachments();
		String filename = atts.get(0).getContentDisposition().getFilename();
		String convenio = body.getAttachmentObject("convenio", String.class);
		String userRep = body.getAttachmentObject("userRep", String.class);
		String token = body.getAttachmentObject("token", String.class);

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			
			Properties prop = new Properties();
			FileInputStream input = new FileInputStream("/opt/fuse-karaf-7.6.0.fuse-760025-redhat-00001/config.properties");
			prop.load(input);

			// Obtenemos el valor de la variable mi_variable
			String miVariable = prop.getProperty("rutas");

			LOG.info("El valor de mi_variable es: " + miVariable);
			
			LOG.info("USERREP:" + userRep);
			LOG.info("CONVENIO:" + convenio);

			File carpeta1 = new File(miVariable + "/" + convenio);

			if (carpeta1.exists() == false) {

				carpeta1.mkdir();
				LOG.info("se crea la carpeta");
			}

			ArrayList<documentResponse> datos = new ArrayList<>();

			File carpeta = new File(miVariable + convenio + "/" + filename);

			try {

				InputStream is = atts.get(0).getObject(InputStream.class);

				OutputStream os = new FileOutputStream(carpeta);
				IOUtils.copy(is, os);
				IOUtils.closeQuietly(os);
				IOUtils.closeQuietly(is);

				documentResponse model = new documentResponse();
				model.setStatus("0");
				datos.add(model);

				service.insertarDocumento(1, filename, carpeta.getAbsolutePath().toString(), convenio, userRep);

			} catch (Exception ex) {
				LOG.info("uploadFile.error():", ex);
				documentResponse model = new documentResponse();
				model.setException("Ah ocurrido un error" + ex);
				model.setStatus("1");
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
	@Path("/upload/file")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<documentResponse> upload(String body) throws IOException {
		JSONObject js = new JSONObject(body);
		String archivo = js.getString("archivo");
		String convenio = js.getString("convenio");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			Properties prop = new Properties();
			FileInputStream input = new FileInputStream(
					"/opt/fuse-karaf-7.6.0.fuse-760025-redhat-00001/config.properties");
			prop.load(input);

			String miVariable = prop.getProperty("rutas");

			LOG.info("El valor de mi_variable es: " + miVariable);

			ArrayList<documentResponse> datos = new ArrayList<>();

			File dir = new File(miVariable + convenio + "/" + archivo);

			try {

				byte[] fileContent = Files.readAllBytes(dir.toPath());

				documentResponse model = new documentResponse();
				model.setB64(Base64.getEncoder().encodeToString(fileContent));
				model.setStatus("0");
				datos.add(model);

			} catch (Exception e) {
				LOG.info("algo salio mal:" + e);
				documentResponse model = new documentResponse();
				model.setException("error al descargar archivo:" + e);
				model.setStatus("1");
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

	@GET
	@Path("/get/document")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Response1> listarDocumentos(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String convenio = js.getString("convenio");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.listarDocumentos(convenio);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@PUT
	@Path("/put/document")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Response1> InsertarDocumento(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		LOG.info(body);
		int idDocumento = js.getInt("idDocumento");
		String convenio = js.getString("convenio");
		String repositorio = js.getString("repositorio");
		String nombreDoumento = js.getString("nombreDocumento");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.insertarDocumento(idDocumento, nombreDoumento, repositorio, convenio, null);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@DELETE
	@Path("/del/document")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Response1> EliminarDocumento(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		int idDocumento = js.getInt("idDocumento");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);
		LOG.info("respuesta api: " + validate);

		if (validate) {
			return service.eliminarDocumento(idDocumento, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(ReporteService.class);

}