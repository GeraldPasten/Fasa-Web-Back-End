package cl.api.karaf.apisExternas.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class enrolar {

	private String codigo;
	private String massage;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getMassage() {
		return massage;
	}
	public void setMassage(String massage) {
		this.massage = massage;
	}
	
	

	
	
	
	
}
