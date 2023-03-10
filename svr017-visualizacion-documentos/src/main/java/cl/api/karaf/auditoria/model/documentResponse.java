package cl.api.karaf.auditoria.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class documentResponse {

	private String B64;
	private String status;
	private String Exception;
	public String getB64() {
		return B64;
	}
	public void setB64(String b64) {
		B64 = b64;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getException() {
		return Exception;
	}
	public void setException(String exception) {
		Exception = exception;
	}

	
	
	
	
}
