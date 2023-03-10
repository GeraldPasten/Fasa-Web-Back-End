package cl.api.karaf.apisExternas;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.springframework.stereotype.Service;

import cl.api.karaf.apisExternas.model.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Service
@Path("/services/")

public class ExternasService {
	ApiValidaRut CallValidaRut = new ApiValidaRut();
	ApiEnrolar CallEnrolar = new ApiEnrolar();


	@POST
	@Path("/validaRut")
	@Produces(MediaType.APPLICATION_JSON)
	public String validarut(String body) throws IOException {

		return CallValidaRut.CallValidaRut(body);
	}

	@POST
	@Path("/Enrolar")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<enrolar> Enrolar(String body) throws IOException, NoSuchAlgorithmException {

		return CallEnrolar.CallEnrolar(body);

	}

	@Context
	public void setMessageContext(MessageContext messageContext) {
	}

}