package cl.api.karaf.auditoria.provider;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class conexionServicio {

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

	public String insertarReporte(String user, String servicio, String accion, String detalle) throws SQLException {

		String Insert = "{CALL [dbo].[PRD_MT_LOG_WEB_CONVENIO] (?,?,?,?,?,?,?)}";

		LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	    String formattedDateTime = dateTimeFormatter.format(now);
	    Timestamp timestamp = Timestamp.valueOf(formattedDateTime);
	    CallableStatement cst = con.prepareCall(Insert);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, user);
			cst.setString(4, servicio);
			cst.setString(5, accion);
			cst.setTimestamp(6, timestamp);
			LOG.info("fecha: "+timestamp);
			cst.setString(7, detalle);
			cst.execute();
			

		} catch (SQLException e) {
			LOG.info("error: " + e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro conexion reportes");
		}

		return "paso todo";
	};

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);
}
