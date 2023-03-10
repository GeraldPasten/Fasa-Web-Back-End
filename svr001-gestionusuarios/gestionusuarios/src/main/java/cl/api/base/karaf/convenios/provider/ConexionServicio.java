package cl.api.base.karaf.convenios.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import cl.api.base.karaf.convenios.model.*;
import cl.api.base.karaf.convenios.ApiReportes;
import cl.api.base.karaf.convenios.ApiCorreo;

public class ConexionServicio {

	Connection con;

	private static final Logger LOG = LoggerFactory.getLogger(ConexionServicio.class);

	ApiReportes apiReporte = new ApiReportes();

	ApiCorreo apiCallCorreo = new ApiCorreo();

	public void getconexionServicio() {
		try {
			String url = ("jdbc:oracle:thin:@150.100.255.25:1521:qacl10r2");
			String username = "ADMACCESOS_OWN";
			String passwd = "ADMACCESOS_OWN";

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, username, passwd);
		} catch (Exception e) {
			LOG.info("error en la conexion a la base de datos: "+e);
		}

	}

	public ArrayList<Usuario> leerUsuario(String user) throws SQLException {
		LOG.info(":::::::::::: LEERPACIENTE:::::::::::::::::::\n");
		ArrayList<Usuario> datos = new ArrayList<>();
		
		String leer = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_leer (?,?,?)}";
		
		String id;
		getconexionServicio();
		OracleCallableStatement oracst = (OracleCallableStatement) con.prepareCall(leer);
		OracleResultSet depResulSet1 = null;
		try {
			 int CURSOR = OracleTypes.CURSOR;
			 
			
			oracst.setString(1, user);
			oracst.registerOutParameter(2, CURSOR);
			oracst.registerOutParameter(3, CURSOR);
			oracst.execute();

			depResulSet1 = (OracleResultSet) oracst.getCursor(2);
			
			
			if (depResulSet1.isBeforeFirst() == false) 
			
			{
				LOG.info("ENTRE AL IF:");
				Usuario model = new Usuario();
				model.setCodigo("1");
				model.setError("usuario no encontrado");
				datos.add(model);
			}else {
			while (depResulSet1.next()) {
				
				id = String.valueOf(depResulSet1.getInt(1));
				Usuario model = new Usuario();

				model.setNombre(depResulSet1.getString(4));
				model.setRut(depResulSet1.getString(21));
				model.setNdocumento(depResulSet1.getString(22));
				model.setCorreo(depResulSet1.getString(2));
				model.setApellido(depResulSet1.getString(19));
				model.setApellido2(depResulSet1.getString(20));
				model.setCelular(depResulSet1.getString(21));
				model.setCodigo("0");
				model.setId(id);
				datos.add(model);

			}
			}
			
		} catch (Exception e) {

			LOG.info(e.getMessage());
			Usuario model = new Usuario();
			model.setCodigo("1");
			model.setError("no se encontro al usuario: "+e);
			datos.add(model);

		} finally {
			oracst.close();
			depResulSet1.close();
			con.close();
			LOG.info("Cierro");
		}
		return datos;
	}

	public ArrayList<OutActualizar> InsertarPaciente(String user, String passwd, String nombre, String apellido,
			String apellido2, String rut, String ndocumento, String celular, String userRep)
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
			cst.setString(6, null);
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
			cst.setString(18, ndocumento);
			cst.setString(19, rut);
			cst.setString(20, celular);
			cst.setString(21, null);
			cst.setString(22, null);

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
					apiReporte.apiCall(userRep, "Insertar",
							"El usuario inserto con los siguientes datos: |" + user + " | " + nombre + " | "
									+ user + " | " + apellido + " | " + apellido2 + " | " + rut + " | " + ndocumento + " | "
									+ celular + " | ",
							"InsertarPaciente");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("No se pudo insertar el usuario :"+e);
			
			OutActualizar model = new OutActualizar();
			model.setCodigoResultado("1");
			model.setDetalleResultado("No se pudo insertar el usuario :"+e);
			
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro");
		}
		return datos;
	}

	public ArrayList<TokenModel> GeneraToken(String user, String userRep) throws SQLException {

		LOG.info("Entrando en GeneraTOKEN-ConexionServicio\n");
		ArrayList<TokenModel> datos = new ArrayList<TokenModel>();
		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS.p_solicitar_Token (?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Sql);
		try {
			
			cst.setString(1, user);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.execute();

			TokenModel model = new TokenModel();
			String token = cst.getString(2);

			model.setToken(cst.getString(2));
			model.setDetalle("Token generado");
			datos.add(model);

			Thread newThread2 = new Thread(() -> {
				try {
					apiCallCorreo.apiCorreoCall(user, token);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread2.start();

			Thread newThread = new Thread(() -> {
				try {
					apiReporte.apiCall(userRep, "Insertar",
							"se envio un correo de TokenModel con el usuario :" + user, "GenerarToken");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("No se pudo generar el TokenModel: "+e);
			TokenModel model = new TokenModel();
			model.setCodigoRespuesta("1");
			model.setDetalle("No se pudo generar el TokenModel: "+e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro");
		}

		return datos;
	}

	public ArrayList<ValidaToken> ValidarToken(String user, String token, String userRep)
			throws SQLException, IOException {
		LOG.info("Entrando en ValidaTOKEN-ConexionServicio\n");
		ArrayList<ValidaToken> datos = new ArrayList<ValidaToken>();
		String Sql = "{CALL PKG_TBL_USUARIOS_CONVENIOS. p_confirmar_Token (?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Sql);
		try {
			
			cst.setString(1, user);
			cst.setString(2, token);
			cst.registerOutParameter(3, java.sql.Types.INTEGER);
			cst.registerOutParameter(4, java.sql.Types.VARCHAR);
			cst.execute();

			ValidaToken model = new ValidaToken();
			model.setCodigoResultado(cst.getInt(3));
			model.setDetalleResultado(cst.getString(4));

			datos.add(model);
			cst.close();

		} catch (Exception e) {
			LOG.info("erro al generar TokenModel: "+e);
			ValidaToken model = new ValidaToken();
			model.setCodigoResultado(1);
			model.setDetalleResultado("Hubo un erro al validar TokenModel: "+e);

			datos.add(model);
		} finally {
			cst.close();
			con.close();
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
					apiReporte.apiCall(userRep, "Insertar", "El usuario " + userRep + "asocio al rol: " + id_rol
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
