package cl.api.karaf.eliminar.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class eliminar {

	private String detalleRespuesta;
	private String codigoRespuesta;
		
	public String getDetalleRespuesta() {
		return detalleRespuesta;
	}
	public void setDetalleRespuesta(String detalleRespuesta) {
		this.detalleRespuesta = detalleRespuesta;
	}
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}
	
	
	

	
	
	
}
