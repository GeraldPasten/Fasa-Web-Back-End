package cl.api.karaf.login;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.api.karaf.login.model.*;
import cl.api.karaf.login.provider.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@Service
@Path("/services/")
public class LoginService {

	conexionServicio service = new conexionServicio();
	private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Login> Login(String body) throws SQLException, IOException {
		LOG.info(":::::::::: ENTRANDO A LOGIN APIORACLESERVICE ::::::: ");
		service.getconexionServicio();
		JSONObject js = new JSONObject(body);
		String user = js.getString("user");
		String passwd = js.getString("passwd");

		return service.Login(user, passwd);
	}


	@Context
	public void setMessageContext(MessageContext messageContext) {
	}

}