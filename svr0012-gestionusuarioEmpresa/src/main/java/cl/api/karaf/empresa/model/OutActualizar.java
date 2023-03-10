package cl.api.karaf.empresa.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OutActualizar {
	
	private String outSeq;
	private String codigoResultado;
	private String DetalleResultado;
	public String getOutSeq() {
		return outSeq;
	}
	public void setOutSeq(String outSeq) {
		this.outSeq = outSeq;
	}
	public String getCodigoResultado() {
		return codigoResultado;
	}
	public void setCodigoResultado(String codigoResultado) {
		this.codigoResultado = codigoResultado;
	}
	public String getDetalleResultado() {
		return DetalleResultado;
	}
	public void setDetalleResultado(String detalleResultado) {
		DetalleResultado = detalleResultado;
	}

	
	
	

	
	


	
	

	


	

	
}
