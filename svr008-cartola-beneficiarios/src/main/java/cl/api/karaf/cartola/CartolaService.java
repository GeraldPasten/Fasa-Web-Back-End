package cl.api.karaf.cartola;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.cartola.model.response;
import cl.api.karaf.cartola.provider.conexionServicio;

@Service
@Path("/services/")
public class CartolaService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();
	private static final Logger LOG = LoggerFactory.getLogger(CartolaService.class);

	@GET
	@Produces({ MediaType.APPLICATION_JSON, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
	@Path("/ver/cartola")
	public ArrayList<response> cartola(String body) throws Exception {

		JSONObject js = new JSONObject(body);
		String Tipo = js.getString("Tipo");
		String codigoConvenio = js.getString("codigoConvenio");
		String rut = js.getString("rut");
		String fechaIni = js.getString("fechaIni");
		String fechaFin = js.getString("fechaFin");
		String token = js.getString("token");

		boolean validate = seguridad.ApiSeguridadWeb(token);

		if (validate) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Cartola");

			ArrayList<response> datos = service.GetCartola(Tipo, codigoConvenio, rut, fechaIni, fechaFin);
			ArrayList<response> excel = new ArrayList<response>();

			if (datos.size() > 5000) {
				// Create the header row
				HSSFRow headerRow = sheet.createRow(0);
				Field[] fields = response.class.getDeclaredFields();

				for (int i = 0; i < fields.length; i++) {

					if (fields[i].getName().equals("Tipo") || fields[i].getName().equals("codigo")
							|| fields[i].getName().equals("detalle") || fields[i].getName().equals("b64")) {
						LOG.info("ENTRO AL IF");
						LOG.info(fields[i].getName());
						continue;

					}
					LOG.info("NO ENTRO AL IF");
					LOG.info(fields[i].getName());
					HSSFCell cell = headerRow.createCell(i);
					cell.setCellValue(fields[i].getName());
				}

				// Add the response data to the data array
				// Add the data rows
				for (int i = 0; i < datos.size(); i++) {
					response r = datos.get(i);
					HSSFRow dataRow = sheet.createRow(i + 1);
					dataRow.createCell(0).setCellValue(r.getEstado());
					dataRow.createCell(1).setCellValue(r.getFecha());
					dataRow.createCell(2).setCellValue(r.getFarmacia());
					dataRow.createCell(3).setCellValue(r.getId_receta());
					dataRow.createCell(4).setCellValue(r.getDireccion());
					dataRow.createCell(5).setCellValue(r.getComuna());
					dataRow.createCell(6).setCellValue(r.getBoleta());
					dataRow.createCell(7).setCellValue(r.getGuia());
					dataRow.createCell(8).setCellValue(r.getSAP());
					dataRow.createCell(9).setCellValue(r.getDecripcion_producto());
					dataRow.createCell(10).setCellValue(r.getCantidad());
					dataRow.createCell(11).setCellValue(r.getPrecio());
					dataRow.createCell(12).setCellValue(r.getDescto());
					dataRow.createCell(13).setCellValue(r.getBonificado());
					dataRow.createCell(14).setCellValue(r.getCopago());
					dataRow.createCell(15).setCellValue(r.getTotal());
					dataRow.createCell(16).setCellValue(r.getTipo());
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
			} else {
				workbook.close();
				return service.GetCartola(Tipo, codigoConvenio, rut, fechaIni, fechaFin);
			}
		} else {
			JSONObject errorJson = new JSONObject();
			errorJson.put("error", "Token Invalido, por favor vuelva generarlo");
			errorJson.put("codigo", 403);
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorJson.toString())
					.type(MediaType.APPLICATION_JSON).build());
		}

	}

}