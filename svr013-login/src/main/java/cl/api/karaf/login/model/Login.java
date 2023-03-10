package cl.api.karaf.login.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Login {

	private String codigoResultadoLogin;
	private String detalleResultado;
	private String tipo;

	public String getCodigoResultadoLogin() {
		return codigoResultadoLogin;
	}

	public void setCodigoResultadoLogin(String codigoResultadoLogin) {
		this.codigoResultadoLogin = codigoResultadoLogin;
	}

	public String getDetalleResultado() {
		return detalleResultado;
	}

	public void setDetalleResultado(String detalleResultado) {
		this.detalleResultado = detalleResultado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
