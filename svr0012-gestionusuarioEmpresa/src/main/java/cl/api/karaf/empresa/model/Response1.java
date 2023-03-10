package cl.api.karaf.empresa.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response1 {
	private String codigo;
	private String detalle;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	

	
	
	
}