package cl.api.karaf.auditoria;

import org.json.JSONObject;

import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.auditoria.provider.*;

@Service
@Path("/services/")
public class ReporteService {

	conexionServicio service = new conexionServicio();
	
	@POST
	@Path("/reporte/auditoria")
	public String reporte(String body) throws SQLException {
		
		LOG.info("ENTRANDO A REPORTE Y AUDITORIA ");
		
		service.getconexionServicio();		
		JSONObject js = new JSONObject(body);
		
		String user = js.getString("user");
		String servicio = js.getString("servicio");
		String accion = js.getString("accion");
		String detalle = js.getString("detalle");
		
		return service.insertarReporte(user, servicio, accion, detalle);
	}
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ReporteService.class);

}