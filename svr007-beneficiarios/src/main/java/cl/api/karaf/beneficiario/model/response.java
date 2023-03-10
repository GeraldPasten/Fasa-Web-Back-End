package cl.api.karaf.beneficiario.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class response {

	private String codigoConvenio;
	private String grupo;
	private String credenciales;
	private String rutTitular;
	private String rutBeneficiario;
	private String codigoCarga;
	private int poliza;
	private int codigoRelacion;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String fechaNacimiento;
	private String genero;
	private String vigencia;
	private String termino;
	private String mail;
	private String direccion;
	private String comuna;
	private String ciudad;
	private String id;

	private String b64;
	private String detalle;
	private String codigoError;
	public String getCodigoConvenio() {
		return codigoConvenio;
	}
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getCredenciales() {
		return credenciales;
	}
	public void setCredenciales(String credenciales) {
		this.credenciales = credenciales;
	}
	public String getRutTitular() {
		return rutTitular;
	}
	public void setRutTitular(String rutTitular) {
		this.rutTitular = rutTitular;
	}
	public String getRutBeneficiario() {
		return rutBeneficiario;
	}
	public void setRutBeneficiario(String rutBeneficiario) {
		this.rutBeneficiario = rutBeneficiario;
	}
	public String getCodigoCarga() {
		return codigoCarga;
	}
	public void setCodigoCarga(String codigoCarga) {
		this.codigoCarga = codigoCarga;
	}
	public int getPoliza() {
		return poliza;
	}
	public void setPoliza(int poliza) {
		this.poliza = poliza;
	}
	public int getCodigoRelacion() {
		return codigoRelacion;
	}
	public void setCodigoRelacion(int codigoRelacion) {
		this.codigoRelacion = codigoRelacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getVigencia() {
		return vigencia;
	}
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
	public String getTermino() {
		return termino;
	}
	public void setTermino(String termino) {
		this.termino = termino;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getB64() {
		return b64;
	}
	public void setB64(String b64) {
		this.b64 = b64;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	

}
