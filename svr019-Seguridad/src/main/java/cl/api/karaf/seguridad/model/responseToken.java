package cl.api.karaf.seguridad.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class responseToken {
	private String message;
	private String validacion;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getValidacion() {
		return validacion;
	}
	public void setValidacion(String validacion) {
		this.validacion = validacion;
	}
	
	
	
}
