package cl.api.karaf.empresa.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsuarioEmpresa {
	private String nombre;
	private String apellido;
	private String apellido2;
	private String correo;
	private String kamConvenios;
	private String kamCorreo;
	private String cargo;
	private String rut;
	private String convenio;
	private String detalle;
	private String codigo;
	private String id;
	private String celular;
	private String ndocumento;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getKamConvenios() {
		return kamConvenios;
	}
	public void setKamConvenios(String kamConvenios) {
		this.kamConvenios = kamConvenios;
	}
	public String getKamCorreo() {
		return kamCorreo;
	}
	public void setKamCorreo(String kamCorreo) {
		this.kamCorreo = kamCorreo;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getConvenio() {
		return convenio;
	}
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getNdocumento() {
		return ndocumento;
	}
	public void setNdocumento(String ndocumento) {
		this.ndocumento = ndocumento;
	}
	

	
	
	
	
	

	

}
