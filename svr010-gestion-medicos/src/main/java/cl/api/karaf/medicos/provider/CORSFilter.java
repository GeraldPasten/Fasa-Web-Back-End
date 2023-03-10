package cl.api.karaf.medicos.provider;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CORSFilter implements ContainerResponseFilter {

	
	private static final Logger LOG = LoggerFactory.getLogger(CORSFilter.class);
	
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		LOG.info(":::::::::::::::: ENTRANDO A LOS HEADERS DE CORS ::::::::::::::::::::::");
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		LOG.info(":::::::::::::::: PASANDO ALLOW-ORIGIN  ::::::::::::::::::::::");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, user, passwd");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "*");
		LOG.info(":::::::::::::::: PASANDO ALLOW-HEADERS  ::::::::::::::::::::::");
		responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		LOG.info(":::::::::::::::: PASANDO ALLOW-CREDENTIALS  ::::::::::::::::::::::");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		LOG.info(":::::::::::::::: PASANDO ALLOW-METHODS  ::::::::::::::::::::::");
		responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
		LOG.info(":::::::::::::::: PASANDO MAX-AGE ::::::::::::::::::::::");
	}

}
