package cl.api.base.karaf.convenios.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import cl.api.base.karaf.convenios.model.*;
import cl.api.base.karaf.convenios.ApiReportes;

public class conexionServicio {

	ApiReportes apiCall = new ApiReportes();

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

	public ArrayList<roles> leerRoles() throws SQLException {

		ArrayList<roles> datos = new ArrayList<>();
		String spLeer = "{CALL pkg_tbl_roles.p_leer (?,?,?,?,?)}";
		getconexionServicio();

		String aplicacion = null;
		String rol = null;
		OracleCallableStatement cst = (OracleCallableStatement) con.prepareCall(spLeer);
		OracleResultSet rs = null;
		try {
			cst.setNull(1, java.sql.Types.NULL);
			cst.setInt(2, 81);
			cst.setNull(3, java.sql.Types.NULL);
			cst.setNull(4, java.sql.Types.NULL);
			cst.registerOutParameter(5, OracleTypes.CURSOR);

			cst.execute();

			rs = (OracleResultSet) cst.getCursor(5);

			if (rs.isBeforeFirst() == false) {
				roles model = new roles();
				model.setCodigo("1");
				model.setDetalle("No hay datos");
				datos.add(model);
			} else {
				while (rs.next()) {
					aplicacion = String.valueOf(rs.getInt(1));
					rol = String.valueOf(rs.getInt(2));

					roles model = new roles();
					model.setId_aplicacion(aplicacion);
					model.setId_rol(rol);
					model.setMenu_xml(rs.getString(4));
					model.setNombre(rs.getString(3));
					model.setVigencia(rs.getString(5));
					datos.add(model);
				}
			}
			

		} catch (Exception e) {
			LOG.info("error al listar rol :" + e);
			roles model = new roles();
			model.setCodigo("1");
			model.setDetalle("No hay datos");
			datos.add(model);
		} finally {
			rs.close();
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public ArrayList<response> insertarRol(String nombre, String vigencia, String userRep) throws SQLException {

		ArrayList<response> datos = new ArrayList<>();
		String spInsertar = "{CALL pkg_tbl_roles.p_insertar (?,?,?,?)}";
		getconexionServicio();
		String id_rol = null;
		CallableStatement cst = con.prepareCall(spInsertar);
		try {
			cst.setInt(1, 81);
			cst.setString(2, nombre);
			cst.setString(3, vigencia);
			cst.registerOutParameter(4, java.sql.Types.INTEGER);

			cst.execute();

			id_rol = String.valueOf(cst.getInt(4));

			response model = new response();
			model.setCodigo("1");
			model.setDetalle("Se inserto el Rol: " + nombre);
			model.setId_rol(id_rol);
			datos.add(model);
			
		} catch (Exception e) {
			response model = new response();
			model.setCodigo("2");
			model.setDetalle("No se pudo insertar el rol " + nombre + ", " + e);
			datos.add(model);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		Thread newThread = new Thread(() -> {
			try {
				apiCall.ApiCall(userRep, "Insertar", "El usuario " + userRep + "creo el rol: " + nombre, "insertarRol");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		newThread.start();

		return datos;
	}

	public ArrayList<response1> actualizarRol(int id_rol, String nombre, String userRep, String vigencia)
			throws SQLException {

		ArrayList<response1> datos = new ArrayList<>();
		String spActualizar = "{CALL pkg_tbl_roles.p_actualizar (?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(spActualizar);
		try {
			cst.setInt(1, id_rol);
			cst.setInt(2, 81);
			cst.setString(3, nombre);
			cst.setString(4, vigencia);

			cst.execute();
			response1 model = new response1();
			model.setCodigo("0");
			model.setDetalle("El rol: " + nombre + " fue actualizado correctamente");
			datos.add(model);

		} catch (SQLException e) {
			LOG.info("Error al actualizar rol: " + e);
			response1 model = new response1();
			model.setCodigo("1");
			model.setDetalle("El rol no se pudo actualizar: " + e);
			datos.add(model);
		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro conexion");
		}

		Thread newThread = new Thread(() -> {
			try {
				apiCall.ApiCall(userRep, "Actualizar", "El usuario " + userRep + "actualizo el rol: " + nombre,
						"actualizarRol");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		newThread.start();

		return datos;

	}

	public ArrayList<response1> eliminarRol(int id_rol, String userRep) throws SQLException {

		ArrayList<response1> datos = new ArrayList<>();

		String spEliminar = "{CALL pkg_tbl_roles.p_eliminar(?,?)";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(spEliminar);
		try {	
			cst.setInt(1, id_rol);
			cst.setInt(2, 81);
			cst.execute();
			

			response1 model = new response1();
			model.setCodigo("0");
			model.setDetalle("El rol se elimino");
			datos.add(model);

		} catch (Exception e) {
			LOG.info("Error al eliminar rol: " + e);
			response1 model = new response1();
			model.setCodigo("1");
			model.setDetalle("El rol no se pudo eliminar: " + e);
			datos.add(model);

		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		Thread newThread = new Thread(() -> {
			try {
				apiCall.ApiCall(userRep, "Eliminar", "El usuario " + userRep + "elimino el rol: " + id_rol,
						"eliminarRol");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		newThread.start();

		return datos;

	}

	public ArrayList<recursos> leerRecursos(int id_recurso) throws SQLException {

		ArrayList<recursos> datos = new ArrayList<>();
		String sp_leerRecurso = "{CALL pkg_tbl_recursos.p_leer (?,?,?,?,?,?,?)}";

		String aplicacion;
		String recu;
		String pclase;

		getconexionServicio();
		OracleCallableStatement cst = (OracleCallableStatement) con.prepareCall(sp_leerRecurso);
		OracleResultSet rs = null;

		try {
			cst.setInt(1, id_recurso);
			cst.setInt(2, 81);
			cst.setNull(3, java.sql.Types.NULL);
			cst.setNull(4, java.sql.Types.NULL);
			cst.setNull(5, java.sql.Types.NULL);
			cst.setNull(6, java.sql.Types.NULL);
			cst.registerOutParameter(7, OracleTypes.CURSOR);
			cst.execute();

			rs = (OracleResultSet) cst.getCursor(7);

			if (rs.isBeforeFirst() == false) {
				recursos model = new recursos();
				model.setCodigo("1");
				model.setDetalle("No hay datos");
				datos.add(model);
			} else {
				while (rs.next()) {
					recu = String.valueOf(rs.getInt(1));
					aplicacion = String.valueOf(rs.getInt(2));
					pclase = String.valueOf(rs.getInt(5));

					recursos model = new recursos();
					model.setId_recursos(recu);
					model.setId_aplicacion(aplicacion);
					model.setNombre_logico(rs.getString(3));
					model.setNombre_fisico(rs.getString(4));
					model.setClase(pclase);
					model.setVigencia(rs.getString(6));
					datos.add(model);
				}
			}
			
		} catch (Exception e) {
			recursos model = new recursos();
			LOG.info("Error al leer recurso de rol: " + e);
			model.setCodigo("1");
			model.setDetalle("Error al leer recurso de rol: " + e);
			datos.add(model);
		} finally {
			rs.close();
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public ArrayList<recursos> listarRecursos() throws SQLException {

		ArrayList<recursos> datos = new ArrayList<>();
		String sp_leerRecurso = "{CALL pkg_tbl_recursos.p_listar (?,?,?,?,?)}";
		String aplicacion;
		String recu;
		String pclase;

		getconexionServicio();
		OracleCallableStatement cst = (OracleCallableStatement) con.prepareCall(sp_leerRecurso);
		OracleResultSet rs = null;
		try {
			cst.setInt(1, 81);
			cst.setNull(2, java.sql.Types.NULL);
			cst.setNull(3, java.sql.Types.NULL);
			cst.setNull(4, java.sql.Types.NULL);
			cst.registerOutParameter(5, OracleTypes.CURSOR);
			cst.execute();

			rs = (OracleResultSet) cst.getCursor(5);

			if (rs.isBeforeFirst() == false) {
				recursos model = new recursos();
				model.setCodigo("1");
				model.setDetalle("No hay datos");
				datos.add(model);
			} else {
				while (rs.next()) {
					recu = String.valueOf(rs.getInt(1));
					aplicacion = String.valueOf(rs.getInt(2));
					pclase = String.valueOf(rs.getInt(5));

					recursos model = new recursos();
					model.setId_recursos(recu);
					model.setId_aplicacion(aplicacion);
					model.setNombre_logico(rs.getString(3));
					model.setNombre_fisico(rs.getString(4));
					model.setClase(pclase);
					model.setVigencia(rs.getString(6));
					datos.add(model);
				}
			}
			
		} catch (Exception e) {
			recursos model = new recursos();
			LOG.info("Error al leer recurso de rol: " + e);
			model.setCodigo("1");
			model.setDetalle("Error al leer recurso de rol: " + e);
			datos.add(model);
		} finally {
			rs.close();
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public ArrayList<recursoRol> leerRecursosRol(int id_recurso) throws SQLException {

		ArrayList<recursoRol> datos = new ArrayList<>();
		String spLeerRecursos = "{CALL pkg_tbl_rol_recursos.p_leer (?,?,?,?,?)}";

		String rol = null;
		String recursoPadre = null;
		String recurso = null;
		String aplicacion = null;

		getconexionServicio();
		OracleCallableStatement cst = (OracleCallableStatement) con.prepareCall(spLeerRecursos);
		OracleResultSet rs = null;
		try {
			
			cst.setNull(1, java.sql.Types.NULL);
			cst.setInt(2, 81);
			cst.setInt(3, id_recurso);
			cst.setNull(4, java.sql.Types.NULL);
			cst.registerOutParameter(5, OracleTypes.CURSOR);
			cst.execute();

			rs = (OracleResultSet) cst.getCursor(5);

			if (rs.isBeforeFirst() == false) {
				recursoRol model = new recursoRol();
				model.setCodigo("1");
				model.setDetalle("No hay datos");
				datos.add(model);

			} else {
				while (rs.next()) {

					rol = String.valueOf(rs.getInt(2));
					aplicacion = String.valueOf(rs.getInt(1));
					recurso = String.valueOf(rs.getInt(3));
					recursoPadre = String.valueOf(rs.getInt(4));

					recursoRol model = new recursoRol();
					model.setId_rol(rol);
					model.setId_aplicacion(aplicacion);
					model.setId_recurso(recurso);
					model.setId_recursoPadre(recursoPadre);
					datos.add(model);

				}

			}

		} catch (Exception e) {
			LOG.info("error al listar informacion: " + e);
			recursoRol model = new recursoRol();
			model.setCodigo("1");
			model.setDetalle("error al listar informacion " + e);
			datos.add(model);
		} finally {
			cst.close();
			rs.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public ArrayList<recursoRol> listarRecursosRol(int id_rol) throws SQLException {

		ArrayList<recursoRol> datos = new ArrayList<>();
		String spLeerRecursos = "{CALL pkg_tbl_rol_recursos.p_listar (?,?,?,?)}";

		String rol = null;
		String recursoPadre = null;
		String recurso = null;
		String aplicacion = null;

		getconexionServicio();
		OracleCallableStatement cst = (OracleCallableStatement) con.prepareCall(spLeerRecursos);
		OracleResultSet rs = null;
		try {
			cst.setInt(1, id_rol);
			cst.setInt(2, 81);
			cst.setNull(3, java.sql.Types.NULL);
			cst.registerOutParameter(4, OracleTypes.CURSOR);
			cst.execute();

			rs = (OracleResultSet) cst.getCursor(4);

			if (rs.isBeforeFirst() == false) {
				recursoRol model = new recursoRol();
				model.setCodigo("1");
				model.setDetalle("No hay datos");
				datos.add(model);

			} else {
				while (rs.next()) {

					rol = String.valueOf(rs.getInt(2));
					aplicacion = String.valueOf(rs.getInt(1));
					recurso = String.valueOf(rs.getInt(3));
					recursoPadre = String.valueOf(rs.getInt(4));

					recursoRol model = new recursoRol();
					model.setId_rol(rol);
					model.setId_aplicacion(aplicacion);
					model.setId_recurso(recurso);
					model.setId_recursoPadre(recursoPadre);
					datos.add(model);

				}

			}

		} catch (Exception e) {
			LOG.info("error al listar informacion: " + e);
			recursoRol model = new recursoRol();
			model.setCodigo("1");
			model.setDetalle("error al listar informacion " + e);
			datos.add(model);
		} finally {
			cst.close();
			con.close();
			rs.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public ArrayList<response1> asociarRolRecurso(int id_rol, int id_recurso, int id_recursoPadre, String userRep)
			throws SQLException {
		ArrayList<response1> datos = new ArrayList<>();
		String spinsertar = "{CALL pkg_tbl_rol_recursos.p_insertar (?,?,?,?)}";

		getconexionServicio();
		CallableStatement cst = con.prepareCall(spinsertar);
		try {
			cst.setInt(1, 81);
			cst.setInt(2, id_rol);
			cst.setInt(3, id_recurso);
			cst.setInt(4, id_recursoPadre);
			cst.execute();

			response1 model = new response1();
			model.setCodigo("0");
			model.setDetalle("Se asocio el recurso de id: " + id_recurso + " al rol de id: " + id_rol);
			datos.add(model);

		} catch (Exception e) {
			LOG.info("Error al asociar recurso: " + e);
			response1 model = new response1();
			model.setCodigo("1");
			model.setDetalle("No se pudo asociar el recurso: " + e);
			datos.add(model);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;
	}

	public ArrayList<response1> QuitarRecursoRol(int id_recurso, String userRep, int id_rol, int id_recursoPadre)
			throws SQLException {
		ArrayList<response1> datos = new ArrayList<>();
		String speliminar = "{CALL pkg_tbl_rol_recursos.p_eliminar (?,?,?,?,?)}";

		getconexionServicio();
		CallableStatement cst = con.prepareCall(speliminar);
		try {
			cst.setInt(1, 81);
			cst.setInt(2, id_rol);
			cst.setInt(3, id_recurso);
			cst.setInt(4, id_recursoPadre);
			cst.setNull(5, java.sql.Types.NULL);
			cst.execute();

			response1 model = new response1();
			model.setCodigo("0");
			model.setDetalle("Se elimino el recurso de id: " + id_recurso);
			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "Eliminar",
							"El usuario " + userRep + "quito el recurso: " + id_recurso + " al rol de id: " + id_rol,
							"quitarRecurso");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("Error al eliminar recurso: " + e);
			response1 model = new response1();
			model.setCodigo("1");
			model.setDetalle("No se su pudo eliminar el recurso: " + e);
			datos.add(model);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro conexion");
		}

		return datos;
	}

	public ArrayList<response1> AsociarUsuarioRol(int id_usuario, int id_rol, String vigencia, String userRep)
			throws SQLException {
		LOG.info("ENTRANDO ASOCIAR ROL USUARIO");
		ArrayList<response1> datos = new ArrayList<>();

		String spinsertar = "{CALL pkg_tbl_apl_usu_rol.p_insertar (?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(spinsertar);

		try {
			cst.setInt(1, 81);
			cst.setInt(2, id_usuario);
			cst.setInt(3, id_rol);
			cst.setString(4, vigencia);
			cst.execute();

			response1 model = new response1();
			model.setCodigo("1");
			model.setDetalle("Se asocio el rol al usuario exitosamente");
			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "Insertar", "El usuario " + userRep + "asocio al rol: " + id_rol
							+ " con el usuario de id: " + id_usuario, "asociarUsuarioRol");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (SQLException e) {
			LOG.info("Error al asociar rol a usuario: " + e);
			response1 model = new response1();
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

	public List<RolUsuario> leerUsuarioRol(int id_usuario) throws SQLException {

		ArrayList<RolUsuario> datos = new ArrayList<>();
		String spLeer = "{CALL pkg_tbl_apl_usu_rol.p_leer (?,?,?,?)}";
		getconexionServicio();
		OracleCallableStatement cst = (OracleCallableStatement) con.prepareCall(spLeer);
		int CURSOR = OracleTypes.CURSOR;
		OracleResultSet rs = null;

		String aplicacion = null;
		String usuario = null;
		String rol = null;

		try {

			cst.setInt(1, 81);
			if (id_usuario == 0) {
				cst.setNull(2, java.sql.Types.NULL);
			} else {
				cst.setInt(2, id_usuario);
			}
			cst.setNull(3, java.sql.Types.NULL);
			cst.registerOutParameter(4, CURSOR);
			cst.execute();

			rs = (OracleResultSet) cst.getCursor(4);
			if (rs.isBeforeFirst() == false)

			{
				RolUsuario model = new RolUsuario();
				model.setCodigo("1");
				model.setDetalle("Este usuario no tiene asignaod un rol");
				datos.add(model);
			} else {

				while (rs.next()) {

					aplicacion = String.valueOf(rs.getInt(1));
					usuario = String.valueOf(rs.getInt(2));
					rol = String.valueOf(rs.getInt(3));

					RolUsuario model = new RolUsuario();
					model.setId_aplicacion(aplicacion);
					model.setId_usuario(usuario);
					model.setId_rol(rol);
					model.setVigencia(rs.getString(4));
					datos.add(model);

				}
			}

		} catch (Exception e) {
			LOG.info("error al listar usuario: " + e);
			RolUsuario model = new RolUsuario();
			model.setCodigo("1");
			model.setDetalle("error al listar usuario: " + e);
		} finally {
			con.close();
			cst.close();
			rs.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

	public List<response1> updateAsociarRol(int id_usuario, int id_rol, int id_rol_nuevo) throws SQLException {

		ArrayList<response1> datos = new ArrayList<>();
		String spauct = "{CALL pkg_tbl_apl_usu_rol.p_actualizar (?,?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(spauct);

		LOG.info("id_usuario: " + id_usuario);
		LOG.info("id_rol: " + id_rol);
		LOG.info("id_rol_nuevo: " + id_rol_nuevo);

		try {
			cst.setInt(1, 81);
			if (id_usuario == 0) {
				cst.setNull(2, java.sql.Types.NULL);
			} else {
				cst.setInt(2, id_usuario);
			}
			if (id_rol == 0) {
				cst.setNull(3, java.sql.Types.NULL);
			} else {
				cst.setInt(3, id_rol);
			}
			if (id_rol_nuevo == 0) {
				cst.setNull(4, java.sql.Types.NULL);
			} else {
				cst.setInt(4, id_rol_nuevo);
			}
			cst.setNull(5, java.sql.Types.NULL);
			cst.execute();

			response1 model = new response1();
			model.setCodigo("0");
			model.setDetalle("Se actualizo el rol del usuario de id: " + id_usuario);
			datos.add(model);

		} catch (Exception e) {
			LOG.info("No se actualizo el rol del usuario: " + e);
			response1 model = new response1();
			model.setCodigo("1");
			model.setDetalle("No se actualizo el rol del usuario: " + e);
			datos.add(model);
		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro conexion");
		}

		return datos;

	}

}
