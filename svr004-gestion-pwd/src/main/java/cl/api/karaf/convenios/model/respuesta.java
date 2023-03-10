package cl.api.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class respuesta {

	private String codigorespuesta;
	private String DetalleResultado;
	
	public String getCodigorespuesta() {
		return codigorespuesta;
	}
	public void setCodigorespuesta(String codigorespuesta) {
		this.codigorespuesta = codigorespuesta;
	}
	public String getDetalleResultado() {
		return DetalleResultado;
	}
	public void setDetalleResultado(String detalleResultado) {
		DetalleResultado = detalleResultado;
	}
	
	

	

	
	
}
