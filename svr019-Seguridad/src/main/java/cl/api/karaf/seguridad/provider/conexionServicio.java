package cl.api.karaf.seguridad.provider;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.seguridad.model.response;
import cl.api.karaf.seguridad.model.responseToken;

public class conexionServicio {

	String sp;
	Connection con;

	public void getconexionServicio() {
		try {
			String url = String.format(
					"jdbc:sqlserver://;serverName=150.100.255.39;databaseName=abf_desa;user=abfchile;password=abfchile");

			DriverManager.registerDriver(new SQLServerDriver());

			con = DriverManager.getConnection(url);

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public ArrayList<response> WebConveniosToken(String usuario, String password) throws SQLException {

		// Este metodo llama al SP que genera un token
		// Retorna la respuesta del SP
		// Convierte la password a b64 para poder pasarselo al Sp

		String passwordB64 = Base64.getEncoder().encodeToString(password.getBytes());
		LOG.info("conexion Password en b64: " + passwordB64);
		sp = "{CALL PRD_GENERAR_TOKEN (?,?,?,?,?,?)}";
		ArrayList<response> datos = new ArrayList<>();
		getconexionServicio();
		CallableStatement cst = con.prepareCall(sp);

		try {
			cst.setString(1, "WEBCONVENIOS");
			cst.setString(2, usuario);
			cst.setString(3, passwordB64);
			cst.setString(4, "WEBCONVENIOS");
			cst.registerOutParameter(5, java.sql.Types.VARCHAR);
			cst.registerOutParameter(6, java.sql.Types.VARCHAR);
			cst.execute();

			response model = new response();
			model.setToken(cst.getString(5));
			model.setMessage(cst.getString(6));
			datos.add(model);

		} catch (Exception e) {
			LOG.info("Error al generar token de seguridad");
			response model = new response();
			model.setMessage("Error al generar token de seguridad: " + e);
			datos.add(model);
		} finally {
			con.close();
			cst.close();
		}

		return datos;
	}

	public ArrayList<responseToken> WebConveniosValidaToken(String token) throws SQLException {

		// Este metodo valida el token proporcioando mediante el sp PRD_VALIDA_TOKEN,
		// este controla una variable "validacion"
		// la cual sera true en caso de que el token sea valido.

		Boolean validacion = false;
		int respuesta;
		String mensaje;
		ArrayList<responseToken> datos = new ArrayList<>();
		sp = "{CALL PRD_VALIDA_TOKEN (?,?,?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(sp);
		try {
			cst.setString(1, "WEBCONVENIOS");
			cst.setString(2, "wsconvenios");
			cst.setString(3, "WEBCONVENIOS");
			cst.setString(4, token);
			cst.registerOutParameter(5, java.sql.Types.INTEGER);
			cst.registerOutParameter(6, java.sql.Types.VARCHAR);
			cst.execute();

			respuesta = cst.getInt(5);
			mensaje = cst.getString(6);
			
			// 1 Valido - 0 Invalido
			if (respuesta == 1) {
				validacion = true;
			}

			responseToken model = new responseToken();
			model.setMessage(mensaje);
			model.setValidacion(validacion.toString());
			datos.add(model);

		} catch (Exception e) {
			LOG.info("Ocurrio un error al validar el token: " + e);
			responseToken model = new responseToken();
			model.setMessage("Ocurrio un error al validar el token: " + e);
			model.setValidacion(validacion.toString());
			datos.add(model);

		} finally {
			con.close();
			cst.close();
		}

		return datos;
	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
