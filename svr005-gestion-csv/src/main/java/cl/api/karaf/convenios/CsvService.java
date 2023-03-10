package cl.api.karaf.convenios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;

import cl.api.karaf.convenios.model.response1;
import cl.api.karaf.convenios.provider.conexionServicio;

@Service
@Path("/services/")
public class CsvService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();

	Connection con;

	@POST
	@Path("/csv")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, "multipart/mixed" })
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<response1> csv(MultipartBody body) throws SQLException, IOException {

		String csv = body.getAttachmentObject("csv", String.class);
		String userRep = body.getAttachmentObject("userRep", String.class);
		String token = body.getAttachmentObject("token", String.class);

        

		boolean validate = seguridad.ApiSeguridadWeb(token);
		if (validate) {
			return service.csv(csv, userRep);
		}else {
			ArrayList<response1> datos = new ArrayList<>();
	        response1 model = new response1();
	        model.setCodigoRespuesta(403);
	        model.setDetalleRespuest("Token Invalido, por favor vuelva generarlo");
	        datos.add(model);

	        throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
	            .entity(datos)
	            .type(MediaType.APPLICATION_JSON)
	            .build());


			
		}

		

	}

}