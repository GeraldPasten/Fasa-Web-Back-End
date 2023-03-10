package cl.api.karaf.medicos.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class medicos {

	private String CodigoLista;
	private String rutMedico;
	private String nombre;
	private String fechaDesde;
	private String Exc_Inc;
	public String getCodigoLista() {
		return CodigoLista;
	}
	public void setCodigoLista(String codigoLista) {
		CodigoLista = codigoLista;
	}
	public String getRutMedico() {
		return rutMedico;
	}
	public void setRutMedico(String rutMedico) {
		this.rutMedico = rutMedico;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getExc_Inc() {
		return Exc_Inc;
	}
	public void setExc_Inc(String exc_Inc) {
		Exc_Inc = exc_Inc;
	}
	
	

	

}
