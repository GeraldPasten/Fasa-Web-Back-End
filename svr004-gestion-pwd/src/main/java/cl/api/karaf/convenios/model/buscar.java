package cl.api.karaf.convenios.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class buscar {

	private int row ;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	
}
