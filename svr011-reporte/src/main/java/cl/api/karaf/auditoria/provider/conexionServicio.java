package cl.api.karaf.auditoria.provider;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.auditoria.model.auditoria;

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

	public ArrayList<auditoria> MostrarAuditoria(String user, String servicio, String accion, String fechaDesde,
			String fechaHasta) throws SQLException {

		ArrayList<auditoria> datos = new ArrayList<>();

		String Aud = "{CALL [dbo].[PRD_REPORTE_LOG_WEB_CONVENIO] (?,?,?,?,?,?,?)}";

		try {

			CallableStatement cst = con.prepareCall(Aud);
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, user);
			cst.setString(4, servicio);
			cst.setString(5, accion);
			cst.setString(6, fechaDesde);
			cst.setString(7, fechaHasta);
			cst.execute();

			ResultSet rs = cst.getResultSet();
			
			if(rs.isBeforeFirst() == false)
			{
				auditoria model = new auditoria();
				model.setCodigo("1");
				model.setDetalle("No se encontraron datos");
				datos.add(model);
				
			}else {
				while (rs.next()) {

					auditoria model = new auditoria();
					model.setUsuario(rs.getString(1));
					model.setServicio(rs.getString(2));
					model.setAccion(rs.getString(4));
					model.setFecha(rs.getString(3));
					model.setDetalle(rs.getString(5));
					datos.add(model);
				}
			}
			
		} catch (Exception e) {
			LOG.info("error: " + e);
			auditoria model = new auditoria();
			model.setCodigo("1");
			model.setDetalle("Error en reporte y auditoria: "+e);
			datos.add(model);

		} finally {

			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;
	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
