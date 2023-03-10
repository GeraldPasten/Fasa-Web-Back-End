package cl.api.karaf.auditoria.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response {

	
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
