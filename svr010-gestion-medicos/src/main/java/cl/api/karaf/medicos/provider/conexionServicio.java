package cl.api.karaf.medicos.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.medicos.model.Codigolista;
import cl.api.karaf.medicos.model.medicos;
import cl.api.karaf.medicos.model.response;
import cl.api.karaf.medicos.ApiReportes;

public class conexionServicio {
	ApiReportes ApiCall = new ApiReportes();

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

	public ArrayList<Codigolista> BuscarListas(String codigoConvenio) throws SQLException {
		LOG.info("::::::::::::::: Entrando a BuscarListas :::::::::::::");
		ArrayList<Codigolista> datos = new ArrayList<>();
		String BuscarListas = "{CALL [dbo].[PRD_MT_BUSCAR_LISTA_MEDICO_WEB_CONVENIO] (?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(BuscarListas);
		try {		
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoConvenio);
			cst.execute();
			ResultSet rs = cst.getResultSet();
			while (rs.next()) {
				Codigolista model = new Codigolista();
				model.setCodigoLista(rs.getString(1));
				datos.add(model);
			}

		} catch (Exception e) {
			LOG.info("error: " + e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cierro conexion... ");
		}
		return datos;
	}

	public ArrayList<medicos> ListarMedicos(String codigoLista) throws SQLException {
		LOG.info(":::::::ETRANDO A LISTAR MEDICOS ::::::::");
		ArrayList<medicos> datos = new ArrayList<>();
		String ListMedicos = "{CALL [dbo].[PRD_MT_MOSTRAR_LISTA_MEDICOS_WEB_CONVENIO] (?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(ListMedicos);
		
		try {
			
			
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoLista);
			cst.execute();
			
			ResultSet rs = cst.getResultSet();
			
			while (rs.next()) {
				medicos model = new medicos();
				model.setCodigoLista(rs.getString(1));
				model.setRutMedico(rs.getString(2));
				model.setNombre(rs.getString(3));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy");
					Date convertedCurrentDate = sdf.parse(rs.getString(4));
					String date = csdf.format(convertedCurrentDate);
					model.setFechaDesde(date);
				} catch (Exception e) {
				}
				model.setExc_Inc(rs.getString(5));
				datos.add(model);

			}
			rs.close();
			
		} catch (Exception e) {
			LOG.info("error : " + e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando conexion.....");
		}

		return datos;

	}

	// LISTA MEDICOS

	public ArrayList<response> InsertarListamedicos(String codigoLista, String rut, String fecha, String exc_inc,
			String userRep) throws SQLException {

		LOG.info(":::::::ETRANDO A Actualizar Lista de Medicos ::::::::");

		int cont = 0;
		getconexionServicio();

		ArrayList<response> datos = new ArrayList<>();
		String Actu = "{CALL [dbo].[PRD_MT_ACTUALIZA_LISTA_MEDICO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";

		SimpleDateFormat ddd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat csdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp timestamp;
		CallableStatement cst = con.prepareCall(Actu);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoLista);
			cst.setString(4, rut);

			try {
				java.util.Date convertedCurrentDate = ddd.parse(fecha);

				String date = csdf.format(convertedCurrentDate);

				timestamp = new Timestamp(ddd.parse(date).getTime());

				LOG.info("timestamp" + timestamp);

				cst.setTimestamp(5, timestamp);
			} catch (Exception e) {
				response model = new response();
				model.setCodigo(3);
				model.setDetalle("La fecha no tiene el formato correcto (YYYY-MM-DD 00:00:00.000)");
				datos.add(model);
				cont = 3;
			}

			if (exc_inc.isEmpty()) {
				response model = new response();
				model.setCodigo(4);
				model.setDetalle("El dato exclusion/Inclusion no puede estar vacio");
				datos.add(model);
				cont = 4;
			} else
				cst.setString(6, exc_inc);
			cst.setInt(7, 1);
			cst.execute();

			LOG.info("descripcion  " + cst.getString(2));

			

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Insertar", "El usuario inserto con los siguientes datos: " + codigoLista
							+ " | " + rut + " | " + fecha + " | " + exc_inc, " insertarListaMedicos");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			response model = new response();
			model.setCodigo(1);
			model.setDetalle("No se pudo actualizar");
			datos.add(model);
			cont = 1;
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando conexion.....");
		}

		if (cont == 0) {
			response model = new response();
			model.setCodigo(0);
			model.setDetalle("Rut ingresado a la lista");
			datos.add(model);
		}

		return datos;
	}
	
	public ArrayList<response> ActualizarListamedicos(String codigoLista, String rut, String fecha, String exc_inc,
			String userRep) throws SQLException {

		LOG.info(":::::::ETRANDO A Actualizar Lista de Medicos ::::::::");

		int cont = 0;
		getconexionServicio();

		ArrayList<response> datos = new ArrayList<>();
		String Actu = "{CALL [dbo].[PRD_MT_ACTUALIZA_LISTA_MEDICO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";

		SimpleDateFormat ddd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat csdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp timestamp;
		CallableStatement cst = con.prepareCall(Actu);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoLista);
			cst.setString(4, rut);

			try {
				java.util.Date convertedCurrentDate = ddd.parse(fecha);

				String date = csdf.format(convertedCurrentDate);

				timestamp = new Timestamp(ddd.parse(date).getTime());

				LOG.info("timestamp" + timestamp);

				cst.setTimestamp(5, timestamp);
			} catch (Exception e) {
				response model = new response();
				model.setCodigo(3);
				model.setDetalle("La fecha no tiene el formato correcto (YYYY-MM-DD 00:00:00.000)");
				datos.add(model);
				cont = 3;
			}

			if (exc_inc.isEmpty()) {
				response model = new response();
				model.setCodigo(4);
				model.setDetalle("El dato exclusion/Inclusion no puede estar vacio");
				datos.add(model);
				cont = 4;
			} else
				cst.setString(6, exc_inc);
			cst.setInt(7, 1);
			cst.execute();

			LOG.info("descripcion  " + cst.getString(2));

			

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Actualizar", "El usuario inserto con los siguientes datos: " + codigoLista
							+ " | " + rut + " | " + fecha + " | " + exc_inc, "actualizarListaMedicos");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			response model = new response();
			model.setCodigo(1);
			model.setDetalle("No se pudo actualizar");
			datos.add(model);
			cont = 1;
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando conexion.....");
		}

		if (cont == 0) {
			response model = new response();
			model.setCodigo(0);
			model.setDetalle("Rut ingresado a la lista");
			datos.add(model);
		}

		return datos;
	}
	
	
	
	

	public ArrayList<response> EliminarRutListamedicos(String codigoLista, String rut, String userRep)
			throws SQLException {
		LOG.info(":::::::ETRANDO A Actualizar Lista de Medicos ::::::::");
		getconexionServicio();
		LOG.info("abro conexion Eliminar");
		ArrayList<response> datos = new ArrayList<>();
		String Actu = "{CALL [dbo].[PRD_MT_ACTUALIZA_LISTA_MEDICO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";
		CallableStatement cst = con.prepareCall(Actu);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoLista);
			cst.setString(4, rut);
			cst.setTimestamp(5, null);
			cst.setString(6, null);
			cst.setInt(7, 2);
			cst.execute();

			response model = new response();
			model.setCodigo(cst.getInt(1));
			model.setDetalle(cst.getString(2));
			datos.add(model);

			

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Eliminar",
							"El usuario elimino con los siguientes datos: | " + codigoLista + " | " + rut,
							"eliminarRutListaMedicos");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("Error: " + e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando conexion.....");
		}

		return datos;
	}

	public ArrayList<response> BuscarRutListamedicos(String codigoLista, String rut) throws SQLException {
		LOG.info(":::::::ETRANDO A BUSCAR RUT EN Lista de Medicos ::::::::");
		getconexionServicio();

		ArrayList<response> datos = new ArrayList<>();
		String Actu = "{CALL [dbo].[PRD_MT_ACTUALIZA_LISTA_MEDICO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";
		CallableStatement cst = con.prepareCall(Actu);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoLista);
			cst.setString(4, rut);
			cst.setTimestamp(5, null);
			cst.setString(6, null);
			cst.setInt(7, 4);
			cst.execute();

			response model = new response();
			model.setCodigo(cst.getInt(1));
			model.setDetalle(cst.getString(2));
			datos.add(model);

		} catch (Exception e) {
			LOG.info("Error: " + e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando conexion.....");
		}

		return datos;
	}

	public ArrayList<response> EliminarAllRutListamedicos(String codigoLista, String userRep) throws SQLException {
		LOG.info(":::::::ETRANDO A Actualizar Lista de Medicos ::::::::");
		getconexionServicio();
		ArrayList<response> datos = new ArrayList<>();
		String Actu = "{CALL [dbo].[PRD_MT_ACTUALIZA_LISTA_MEDICO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";
		CallableStatement cst = con.prepareCall(Actu);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoLista);
			cst.setString(4, null);
			cst.setTimestamp(5, null);
			cst.setString(6, null);
			cst.setInt(7, 3);
			cst.execute();

			response model = new response();
			model.setCodigo(cst.getInt(1));
			model.setDetalle(cst.getString(2));
			datos.add(model);

		} catch (Exception e) {
			LOG.info("Error: " + e);
			response model = new response();
			model.setCodigo(2);
			model.setDetalle("Error al eliminar ruts de lista medicos: " + e);
			datos.add(model);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando conexion.....");
		}

		return datos;
	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
