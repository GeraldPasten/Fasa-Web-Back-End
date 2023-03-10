package cl.api.karaf.auditoria.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.auditoria.ApiReportes;
import cl.api.karaf.auditoria.model.Response1;

public class conexionServicio {

	ApiReportes apiCall = new ApiReportes();

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

	public ArrayList<Response1> listarDocumentos(String convenio) throws SQLException {

		getconexionServicio();
		ArrayList<Response1> datos = new ArrayList<Response1>();

		String ReporteDocumentos = "{CALL [dbo].[PRD_REPORTE_DOCUMENTO_WEB_CONVENIO] (?,?,?)}";
		try {
			CallableStatement cst = con.prepareCall(ReporteDocumentos);

			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, convenio);

			cst.execute();

			ResultSet rs = cst.getResultSet();

			while (rs.next()) {
				Response1 model = new Response1();
				model.setCodigoDocumento(rs.getInt(1));
				model.setNombreDocumento(rs.getString(2));
				model.setRepositorio(rs.getString(3));
				model.setConvenio(rs.getString(4));
				datos.add(model);
			}

			cst.close();

		} catch (Exception e) {
			Response1 model = new Response1();
			model.setCodigo(3);
			model.setDetalle("Hubo un error en la llamada: " + e);
			datos.add(model);
		} finally {
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public ArrayList<Response1> insertarDocumento(int idDocumento, String nombreDocumento, String repositorio,
			String convenio, String userRep) throws SQLException {

		getconexionServicio();
		ArrayList<Response1> datos = new ArrayList<>();

		String insert = "{CALL [dbo].[PRD_MT_DOCUMENTO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";

		try {

			CallableStatement cst = con.prepareCall(insert);

			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setInt(3, idDocumento);
			cst.setString(4, nombreDocumento);
			cst.setString(5, repositorio);
			cst.setString(6, convenio);
			cst.setString(7, "I");

			cst.execute();

			Response1 model = new Response1();
			model.setCodigo(cst.getInt(1));
			model.setDetalle(cst.getString(2));
			datos.add(model);

			cst.close();

			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "Insertar", "El usuario |" + userRep + "| cargo el archivo: | "
							+ nombreDocumento + " | en la carpeta : | " + convenio, "cargarDocumento");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			Response1 model = new Response1();
			model.setCodigo(2);
			model.setDetalle("No se insertaron los datos :" + e);
			datos.add(model);

			LOG.info("se arruino: " + e);
		} finally {

			con.close();
			LOG.info("cerro la conexion");
		}

		return datos;
	}

	public ArrayList<Response1> eliminarDocumento(int idDocumento, String userRep) throws SQLException {
		getconexionServicio();
		ArrayList<Response1> datos = new ArrayList<>();

		String sp = "{CALL [dbo].[PRD_MT_DOCUMENTO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";

		CallableStatement cst = con.prepareCall(sp);

		try {

			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setInt(3, idDocumento);
			cst.setString(4, null);
			cst.setString(5, null);
			cst.setString(6, null);
			cst.setString(7, "E");

			cst.execute();
			
			Response1 model = new Response1();
			model.setCodigo(cst.getInt(1));
			model.setDetalle(cst.getString(2));
			datos.add(model);
			
			
			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "Eliminar", "El usuario |" + userRep + "| elimino el archivo: | "
							+ idDocumento,"eliminarDocumento");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();
			
		} catch (Exception e) {
			Response1 model = new Response1();
			model.setCodigo(2);
			model.setDetalle("No se insertaron los datos :" + e);
			datos.add(model);
			LOG.info("Error al eliminar documento: "+e);
		}finally {
			con.close();
			cst.close();
		}
		return datos;

	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
