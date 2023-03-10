package cl.api.karaf.beneficiario;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.stereotype.Service;

import cl.api.karaf.beneficiario.model.actualizaResponse;
import cl.api.karaf.beneficiario.model.response;
import cl.api.karaf.beneficiario.provider.*;

@Service
@Path("/services/")
public class BeneficiarioService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listar/beneficiario")
	public ArrayList<response> beneficiario(String body) throws SQLException, IOException {
		JSONObject js = new JSONObject(body);
		String codigoCliente = js.getString("codigoCliente");
		String rut = js.getString("rut");
		String token = js.getString("token");
		int activos = js.getInt("activos");
		int cantidad;
		File file = new File("");
		String path = file.getAbsolutePath();

		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(path + "/config.properties");
		prop.load(input);

		// Obtenemos el valor de la variable mi_variable
		String miVariable = prop.getProperty("registros");

		cantidad = Integer.parseInt(miVariable);

		boolean validate = seguridad.ApiSeguridad(token);

		if (validate) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Beneficiario");

			ArrayList<response> datos = service.GetAllBeneficiario(codigoCliente, rut, activos);
			ArrayList<response> excel = new ArrayList<response>();

			if (datos.size() > cantidad) {
				// Create the header row
				HSSFRow headerRow = sheet.createRow(0);
				Field[] fields = response.class.getDeclaredFields();

				for (int i = 0; i < fields.length; i++) {

					if (fields[i].getName().equals("b64") || fields[i].getName().equals("detalle")
							|| fields[i].getName().equals("codigoError")) {
						continue;

					}
					HSSFCell cell = headerRow.createCell(i);
					cell.setCellValue(fields[i].getName());

				}

				// Add the response data to the data array
				// Add the data rows

				for (int i = 0; i < datos.size(); i++) {
					response r = datos.get(i);
					HSSFRow dataRow = sheet.createRow(i + 1);
					dataRow.createCell(0).setCellValue(r.getCodigoConvenio());
					dataRow.createCell(1).setCellValue(r.getGrupo());
					dataRow.createCell(2).setCellValue(r.getCredenciales());
					dataRow.createCell(3).setCellValue(r.getRutTitular());
					dataRow.createCell(4).setCellValue(r.getRutBeneficiario());
					dataRow.createCell(5).setCellValue(r.getCodigoCarga());
					dataRow.createCell(6).setCellValue(r.getPoliza());
					dataRow.createCell(7).setCellValue(r.getCodigoRelacion());
					dataRow.createCell(8).setCellValue(r.getNombre());
					dataRow.createCell(9).setCellValue(r.getApellido1());
					dataRow.createCell(10).setCellValue(r.getApellido2());
					dataRow.createCell(11).setCellValue(r.getFechaNacimiento().replace("-", ""));
					dataRow.createCell(12).setCellValue(r.getGenero());
					dataRow.createCell(13).setCellValue(r.getVigencia().replace("-", ""));
					dataRow.createCell(14).setCellValue(r.getTermino().replace("-", ""));
					dataRow.createCell(15).setCellValue(r.getMail());
					dataRow.createCell(16).setCellValue(r.getDireccion());
					dataRow.createCell(17).setCellValue(r.getComuna());
					dataRow.createCell(18).setCellValue(r.getCiudad());
					dataRow.createCell(19).setCellValue(r.getId());

				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				workbook.write(baos);
				workbook.close();

				// Convert Excel file to base64 string
				String encodedExcel = Base64.getEncoder().encodeToString(baos.toByteArray());

				ResponseBuilder response = Response.ok(encodedExcel);
				response.header("Content-Disposition", "attachment; filename=Cartola.xls");
				response.header("X-Response-Type", "Excel"); // Add a custom header to identify the response as an Excel

				response model = new response();
				model.setB64(encodedExcel);
				excel.add(model);

				return excel;

			}
			workbook.close();
			return service.GetAllBeneficiario(codigoCliente, rut, activos);
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
	@Path("/actualizar/beneficiario")
	public ArrayList<actualizaResponse> UpdateBeneficiario(String body) throws SQLException, IOException {

		JSONObject js = new JSONObject(body);
		String rutEmpresa = js.getString("rutEmpresa");
		String codigoCliente = js.getString("codigoCliente");
		String RutTitular = js.getString("RutTitular");
		String rutBeneficiario = js.getString("rutBeneficiario");
		String codigoCarga = js.getString("codigoCarga");
		String codigoGrupo = js.getString("codigoGrupo");
		String codigoPlan = js.getString("codigoPlan");
		int poliza = js.getInt("poliza");
		int codigoRelacion = js.getInt("codigoRelacion");
		String primeroNombre = js.getString("primeroNombre");
		String paterno = js.getString("paterno");
		String materno = js.getString("materno");
		String nacimiento = js.getString("nacimiento");
		String vigencia = js.getString("vigencia");
		String termino = js.getString("termino");
		String bloqueo = js.getString("bloqueo");
		int sexo = js.getInt("sexo");
		String mail = js.getString("mail");
		String direccion = js.getString("direccion");
		String codigoPostal = js.getString("codigoPostal");
		String comuna = js.getString("comuna");
		String ciudad = js.getString("ciudad");
		String userRep = js.getString("userRep");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridad(token);

		if (validate) {
			return service.ActualizaBeneficiario(rutEmpresa, codigoCliente, RutTitular, rutBeneficiario, codigoCarga,
					codigoGrupo, codigoPlan, poliza, codigoRelacion, primeroNombre, paterno, materno, nacimiento,
					vigencia, termino, bloqueo, sexo, mail, direccion, codigoPostal, comuna, ciudad, userRep);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	@POST
	@Path("/csv/beneficiarios")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, "multipart/mixed" })
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<actualizaResponse> csv(MultipartBody body) throws SQLException, IOException {
		String csv = body.getAttachmentObject("csv", String.class);
		String userRep = body.getAttachmentObject("userRep", String.class);
		String convenio = body.getAttachmentObject("convenio", String.class);
		String token = body.getAttachmentObject("token", String.class);
		boolean validate = seguridad.ApiSeguridad(token);
		LOG.info("respuesta api: " + validate);

		LOG.info(csv);
		LOG.info(userRep);

		if (validate) {
			return service.cargaMasiva(csv, userRep, convenio);
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

	private static final Logger LOG = LoggerFactory.getLogger(BeneficiarioService.class);

}