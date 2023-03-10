package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ValidaToken {

	private int codigoResultado;
	private String detalleResultado;
	public int getCodigoResultado() {
		return codigoResultado;
	}
	public void setCodigoResultado(int codigoResultado) {
		this.codigoResultado = codigoResultado;
	}
	public String getDetalleResultado() {
		return detalleResultado;
	}
	public void setDetalleResultado(String detalleResultado) {
		this.detalleResultado = detalleResultado;
	}
	

	
	



}
