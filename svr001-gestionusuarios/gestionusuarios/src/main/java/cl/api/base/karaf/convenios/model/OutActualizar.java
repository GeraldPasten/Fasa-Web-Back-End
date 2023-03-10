package cl.api.base.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OutActualizar {

	private String outSeq;
	private String codigoResultado;
	private String detalleResultado;

	
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
		return detalleResultado;
	}
	public void setDetalleResultado(String detalleResultado) {
		this.detalleResultado = detalleResultado;
	}


}
