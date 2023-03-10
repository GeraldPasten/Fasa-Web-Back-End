package cl.api.karaf.auditoria.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response1 {

	private int codigo;
	private String detalle;
	private int codigoDocumento;
	private String nombreDocumento;
	private String repositorio;
	private String convenio;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public int getCodigoDocumento() {
		return codigoDocumento;
	}
	public void setCodigoDocumento(int codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	public String getRepositorio() {
		return repositorio;
	}
	public void setRepositorio(String repositorio) {
		this.repositorio = repositorio;
	}
	public String getConvenio() {
		return convenio;
	}
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	
	
	
	

	
	



}
