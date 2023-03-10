package cl.api.karaf.convenios.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.api.karaf.convenios.model.*;
import cl.api.karaf.convenios.ApiReportes;

public class conexionServicio {

	Connection con;

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);
	ApiReportes CallApi = new ApiReportes();

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

	public ArrayList<respuesta> ActualizarPwd(String user, String passwd, String userRep)
			throws SQLException, IOException {

		ArrayList<respuesta> datos = new ArrayList<respuesta>();
		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_actualizar_pwd (?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Sql);
		try {

			cst.setString(1, user);
			cst.setString(2, passwd);
			cst.registerOutParameter(3, java.sql.Types.VARCHAR);
			cst.registerOutParameter(4, java.sql.Types.VARCHAR);
			cst.execute();

			LOG.info(cst.getString(3));

			respuesta model = new respuesta();
			model.setCodigorespuesta(cst.getString(3));
			model.setDetalleResultado(cst.getString(4));

			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					CallApi.ApiCall(userRep, "Actualizar", "El usuario: | " + user + " | Actualizo la contraseña",
							"actualizarPwd");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info(e.getMessage());

			respuesta model = new respuesta();
			model.setCodigorespuesta("2");
			model.setDetalleResultado("No se puedo actualizar la contraseña: " + e);
			datos.add(model);

		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro");
		}

		return datos;
	}

	public void cierraConexion() {

		try {
			con.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al cerrar conexion", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
