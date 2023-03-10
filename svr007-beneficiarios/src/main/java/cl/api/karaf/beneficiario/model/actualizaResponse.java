package cl.api.karaf.beneficiario.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class actualizaResponse {
	
	private String detalle;
	private String codigoError;
	private String carga;
	private String rut;
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}
	public String getCarga() {
		return carga;
	}
	public void setCarga(String carga) {
		this.carga = carga;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	
	
	
	
	
	
	
	

	
	
}
