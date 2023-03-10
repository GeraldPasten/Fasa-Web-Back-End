package cl.api.karaf.eliminar.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.api.karaf.eliminar.ApiReportes;
import cl.api.karaf.eliminar.model.*;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;

public class conexionServicio {

	Connection con;

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

	ApiReportes ApiCall = new ApiReportes();

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

	public ArrayList<eliminar> EliminarUsuario(String id, String userRep) throws SQLException, IOException {

		LOG.info("Entrando en EliminarUsuario\n");

		ArrayList<eliminar> datos = new ArrayList<eliminar>();

		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_eliminar (?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Sql);
		try {

			cst.setString(1, id);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.registerOutParameter(3, java.sql.Types.VARCHAR);
			cst.execute();

			eliminar model = new eliminar();
			model.setCodigoRespuesta(cst.getString(2));
			model.setDetalleRespuesta(cst.getString(3));
			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Eliminar",
							"El usuario |" + userRep + "| Elimino al siguiente usuario: | " + id + " | ",
							"eliminarUsuario");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info(e.getMessage());
		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro");
		}

		return datos;
	}

	public ArrayList<Usuario> ListarUsuarios(String user, String convenio) throws SQLException, IOException {

		LOG.info(":::::::::::: CONEXIONSERVICIO ::::::::::::::\n");
		LOG.info(":::::::::::: LISTAR :::::::::::::::::::\n");

		ArrayList<Usuario> datos = new ArrayList<>();

		String leer = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_listar (?,?,?)}";
		getconexionServicio();
		OracleCallableStatement oracst = (OracleCallableStatement) con.prepareCall(leer);
		OracleResultSet depResulSet1 = null;
		try {
			oracst.setString(1, user);
			oracst.setString(2, convenio);
			oracst.registerOutParameter(3, OracleTypes.CURSOR);
			oracst.execute();

			String id;
			depResulSet1 = (OracleResultSet) oracst.getCursor(3);

			while (depResulSet1.next()) {

				Usuario model = new Usuario();
				id = String.valueOf(depResulSet1.getInt(1));
				model.setNombre(depResulSet1.getString(4));
				model.setRut(depResulSet1.getString(21));
				model.setNdocumento(depResulSet1.getString(22));
				model.setCorreo(depResulSet1.getString(2));
				model.setApellido(depResulSet1.getString(19));
				model.setApellido2(depResulSet1.getString(20));
				model.setCelular(depResulSet1.getString(21));
				model.setCargo(depResulSet1.getString(6));
				model.setId(id);

				datos.add(model);

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			depResulSet1.close();
			oracst.close();
			con.close();
			LOG.info("Cierro");
		}
		return datos;
	}

	public ArrayList<Usuario> ListarUsuariosReporte() throws SQLException, IOException {

		LOG.info(":::::::::::: LISTAR USUARIOS PARA REPORTE Y AUDITORIA :::::::::::::::::::\n");

		ArrayList<Usuario> datos = new ArrayList<>();

		String leer = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_listar (?,?,?)}";
		getconexionServicio();
		OracleCallableStatement oracst = (OracleCallableStatement) con.prepareCall(leer);
		OracleResultSet depResulSet1 = null;
		try {

			oracst.setString(1, "usuarioParaListar");
			oracst.setString(2, "convenioParaListar");
			oracst.registerOutParameter(3, OracleTypes.CURSOR);
			oracst.execute();

			depResulSet1 = (OracleResultSet) oracst.getCursor(3);

			if (depResulSet1.isBeforeFirst() == false) {
				Usuario model = new Usuario();
				model.setDetalle("No se encontraron datos");
				model.setCodigo("1");
				datos.add(model);
			}
			while (depResulSet1.next()) {

				Usuario model = new Usuario();
				model.setCorreo(depResulSet1.getString(2));
				datos.add(model);

			}

		} catch (Exception e) {
			LOG.info("No se encontraron datos: " + e);
			Usuario model = new Usuario();
			model.setDetalle("No se encontraron datos: " + e);
			model.setCodigo("1");
			datos.add(model);
		} finally {
			con.close();
			oracst.close();
			depResulSet1.close();
			LOG.info("Cierro");
		}
		return datos;
	}

}
