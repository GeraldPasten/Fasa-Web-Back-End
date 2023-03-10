package cl.api.karaf.login.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.api.karaf.login.model.*;

public class conexionServicio {

	Connection con;

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

	public void getconexionServicio() {
		try {
			String url = String.format("jdbc:oracle:thin:@150.100.255.25:1521:qacl10r2");
			String username = "ADMACCESOS_OWN";
			String passwd = "ADMACCESOS_OWN";

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, username, passwd);
		} catch (Exception e) {
			System.out.println("error");
		}

	}

	public ArrayList<Login> Login(String user, String passwd) throws SQLException, IOException {

		ArrayList<Login> datos = new ArrayList<Login>();
		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS.P_VALIDAR_ACCESO (?,?,?,?,?)}";
		try {

			CallableStatement cst = con.prepareCall(Sql);

			cst.setString(1, user);
			cst.setString(2, passwd);
			cst.registerOutParameter(3, java.sql.Types.VARCHAR);
			cst.registerOutParameter(4, java.sql.Types.VARCHAR);
			cst.registerOutParameter(5, java.sql.Types.VARCHAR);

			cst.execute();

			Login model = new Login();
			model.setCodigoResultadoLogin(cst.getString(3));
			model.setDetalleResultado(cst.getString(4));
			model.setTipo(cst.getString(5));

			datos.add(model);

			cst.close();

		} catch (Exception e) {
			LOG.info("Error en login: "+e);
			Login model = new Login();
			model.setCodigoResultadoLogin("3");
			model.setDetalleResultado("Error al loguearse: "+e);

			datos.add(model);

		} finally {
			con.close();
			LOG.info("Cierro");
		}

		return datos;

	}

}
