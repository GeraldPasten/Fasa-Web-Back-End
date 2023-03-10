package cl.api.karaf.convenios;

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

import cl.api.karaf.convenios.model.*;
import cl.api.karaf.convenios.provider.*;

@Service
@Path("/services/")
public class ApiOracleService {

	conexionServicio service = new conexionServicio();
	ApiSeguridad seguridad = new ApiSeguridad();
	private static final Logger LOG = LoggerFactory.getLogger(ApiOracleService.class);

	@PUT
	@Path("/actualizarPwd")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<respuesta> actualizaPwd(String body) throws SQLException, IOException {
		LOG.info(":::::::: ENTRANDO A ACTUALIZAR PWD ::::::::::");

		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String passwd = js.getString("passwd");
		String userRep = js.getString("userRep");

		return service.ActualizarPwd(user, passwd, userRep);

	}

}