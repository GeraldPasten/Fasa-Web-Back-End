package cl.api.karaf.empresa.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.api.karaf.empresa.model.Response1;
import cl.api.karaf.empresa.ApiReportes;
import cl.api.karaf.empresa.model.*;
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

	public ArrayList<UsuarioEmpresa> leerUsuarioEmpresa(String user) throws SQLException, IOException {

		ArrayList<UsuarioEmpresa> datos = new ArrayList<UsuarioEmpresa>();
		String leer = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_leer (?,?,?)}";
		String id;
		getconexionServicio();
		OracleCallableStatement oracst = (OracleCallableStatement) con.prepareCall(leer);
		OracleResultSet depResulSet1 = null;
		OracleResultSet depResulSet2 = null;
		try {
			oracst.setString(1, user);
			oracst.registerOutParameter(2, OracleTypes.CURSOR);
			oracst.registerOutParameter(3, OracleTypes.CURSOR);
			oracst.execute();

			depResulSet1 = (OracleResultSet) oracst.getCursor(2);
			depResulSet2 = (OracleResultSet) oracst.getCursor(3);

			if (depResulSet1.isBeforeFirst() == false) {
				UsuarioEmpresa model = new UsuarioEmpresa();
				model.setCodigo("1");
				model.setDetalle("no se encontro el usuario");
				datos.add(model);
			} else {
				while (depResulSet1.next()) {

					id = String.valueOf(depResulSet1.getInt(1));
					UsuarioEmpresa model = new UsuarioEmpresa();

					model.setNombre(depResulSet1.getString(4));
					model.setApellido(depResulSet1.getString(19));
					model.setApellido2(depResulSet1.getString(20));
					model.setCorreo(depResulSet1.getString(2));
					model.setKamConvenios(depResulSet1.getString(24));
					model.setKamCorreo(depResulSet1.getString(25));
					model.setCargo(depResulSet1.getString(6));
					model.setRut(depResulSet1.getString(21));
					model.setCelular(depResulSet1.getString(23));
					model.setNdocumento(depResulSet1.getString(22));
					model.setCodigo("0");
					model.setId(id);

					datos.add(model);
				}

			}

			if (depResulSet2.isBeforeFirst() == false) {
				UsuarioEmpresa model = new UsuarioEmpresa();
				model.setCodigo("1");
				model.setConvenio("El usuario no tiene convenios");
				datos.add(model);
			} else {
				while (depResulSet2.next()) {
					UsuarioEmpresa model = new UsuarioEmpresa();
					model.setCodigo("0");
					model.setConvenio(depResulSet2.getString(1));
					datos.add(model);
				}

			}

		} catch (Exception e) {
			UsuarioEmpresa model = new UsuarioEmpresa();
			model.setCodigo("1");
			model.setConvenio("Error al listar usuario: " + e);
			datos.add(model);

		} finally {
			con.close();
			oracst.close();
			depResulSet1.close();
			depResulSet2.close();
			LOG.info("Cierro");
		}
		return datos;
	}

	public ArrayList<OutActualizar> InsertarEmpresa(String user, String passwd, String nombre, String apellido,
			String apellido2, String kamConvenios, String kamCorreo, String cargo, String rut, String userRep)
			throws SQLException, IOException {

		String Insertar = "{CALL PKG_TBL_USUARIOS_CONVENIOS.P_ACTUALIZAR (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String id;
		ArrayList<OutActualizar> datos = new ArrayList<OutActualizar>();
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Insertar);
		try {

			cst.setInt(1, 0);
			cst.setString(2, user);
			cst.setString(3, passwd);
			cst.setString(4, nombre);
			cst.setString(5, "S");
			cst.setString(6, cargo);
			cst.setString(7, null);
			cst.setString(8, "CL");
			cst.setString(9, user);
			cst.setDate(10, null);
			cst.setDate(11, null);
			cst.setInt(12, 0);
			cst.setInt(13, 0);
			cst.setInt(14, 0);
			cst.setString(15, "WebConvenios");
			cst.setString(16, apellido);
			cst.setString(17, apellido2);
			cst.setString(18, rut);
			cst.setString(19, null);
			cst.setString(20, null);
			cst.setString(21, kamConvenios);
			cst.setString(22, kamCorreo);

			cst.registerOutParameter(23, java.sql.Types.INTEGER);
			cst.registerOutParameter(24, java.sql.Types.VARCHAR);
			cst.registerOutParameter(25, java.sql.Types.VARCHAR);
			cst.execute();

			id = String.valueOf(cst.getInt(23));
			OutActualizar model = new OutActualizar();
			model.setOutSeq(id);
			model.setCodigoResultado(cst.getString(24));
			model.setDetalleResultado(cst.getString(25));

			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Insertar",
							"El usuario inserto con los siguientes datos: " + " | " + user + " | " + nombre + " |"
									+ user + " | " + apellido + " | " + apellido2 + " | " + rut + " | " + kamConvenios
									+ " | " + kamCorreo + " |",
							"InsertarEmpresa");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("Error al insertar usuario empresa: " + e);
			OutActualizar model = new OutActualizar();
			model.setCodigoResultado("1");
			model.setDetalleResultado("Error al insertar usuario empresa: " + e);

			datos.add(model);

		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro");
		}
		return datos;
	}

	public ArrayList<empresa> ListarConvenio() throws SQLException, IOException {
		LOG.info("Entrando en ListarEmpresa-conexionServicio\n");
		ArrayList<empresa> datos = new ArrayList<>();

		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_listar_convenio (?)}";
		getconexionServicio();
		OracleCallableStatement oracst = (OracleCallableStatement) con.prepareCall(Sql);
		OracleResultSet depResulSet1 = null;
		try {

			oracst.registerOutParameter(1, OracleTypes.CURSOR);
			oracst.execute();

			depResulSet1 = (OracleResultSet) oracst.getCursor(1);

			while (depResulSet1.next()) {
				empresa model = new empresa();
				model.setRutEmpresa(depResulSet1.getString(3));
				model.setNombreEmpresa(depResulSet1.getString(2));
				model.setIdEmpresa(depResulSet1.getString(1));
				datos.add(model);

			}

		} catch (Exception e) {
			LOG.info(e.getMessage());

		} finally {
			con.close();
			depResulSet1.close();
			oracst.close();
			LOG.info("Cierro");
		}

		return datos;

	}

	public ArrayList<respuesta> ActualizarConvenio(String user, String codigo, String userRep)
			throws SQLException, IOException {
		LOG.info("Entrando en ActualizarEmpresa-conexionServicio\n");
		ArrayList<respuesta> datos = new ArrayList<respuesta>();

		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_actualizar_convenio (?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Sql);

		try {
			cst.setString(1, user);
			cst.setString(2, codigo);
			cst.registerOutParameter(3, java.sql.Types.VARCHAR);
			cst.registerOutParameter(4, java.sql.Types.VARCHAR);
			cst.execute();

			respuesta model = new respuesta();
			model.setCodigorespuesta(cst.getString(3));
			model.setDetalleResultado(cst.getString(4));
			datos.add(model);
			
			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Actualizar",
							"Se Actualizar con los siguientes datos" + user + " " + codigo + " .",
							"ActualizarConvenio");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("Error al asociar convenio: "+e);
			respuesta model = new respuesta();
			model.setCodigorespuesta("1");
			model.setDetalleResultado("Error al asociar convenio: "+e);
			datos.add(model);
		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro");
		}

		return datos;

	}

	public ArrayList<Response1> AsociarUsuarioRol(int id_usuario, int id_rol, String vigencia, String userRep)
			throws SQLException {
		LOG.info("ENTRANDO ASOCIAR ROL USUARIO");
		ArrayList<Response1> datos = new ArrayList<>();

		String spinsertar = "{CALL pkg_tbl_apl_usu_rol.p_insertar (?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(spinsertar);

		try {
			cst.setInt(1, 81);
			cst.setInt(2, id_usuario);
			cst.setInt(3, id_rol);
			cst.setString(4, vigencia);
			cst.execute();

			Response1 model = new Response1();
			model.setCodigo("1");
			model.setDetalle("Se asocio el rol al usuario exitosamente");
			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Insertar", "El usuario " + userRep + "asocio al rol: " + id_rol
							+ " con el usuario de id: " + id_usuario, "asociarUsuarioRol");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (SQLException e) {
			LOG.info("Error al asociar rol a usuario: " + e);
			Response1 model = new Response1();
			model.setCodigo("2");
			model.setDetalle("Error al asociar rol a usuario: " + e);
			datos.add(model);
		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro conexion");
		}
		return datos;
	}

}
