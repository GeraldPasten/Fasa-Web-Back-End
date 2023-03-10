package cl.api.karaf.convenios.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.convenios.model.response1;
import cl.api.karaf.convenios.ApiReportes;
import cl.api.karaf.convenios.model.response;

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

	public ArrayList<response1> EliminarAllRutListamedicos(String codigoLista) throws SQLException {
		LOG.info(":::::::ETRANDO A Eliminar Lista de Medicos ::::::::");
		getconexionServicio();
		ArrayList<response1> datos = new ArrayList<>();
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

			response1 model = new response1();
			model.setCodigoRespuesta(cst.getInt(1));
			model.setDetalleRespuest(cst.getString(2));
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

	public ArrayList<response> ActualizarListamedicos(String codigoLista, String rut, String fecha, String exc_inc,
			String userRep) throws SQLException {

		LOG.info(":::::::ETRANDO A Actualizar Lista de Medicos ::::::::");

		int cont = 0;
		String Actu = "{CALL [dbo].[PRD_MT_ACTUALIZA_LISTA_MEDICO_WEB_CONVENIO] (?,?,?,?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(Actu);

		ArrayList<response> datos = new ArrayList<>();

		SimpleDateFormat ddd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat csdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp timestamp;

		try {
			LOG.info("ENTRE AL TRY");

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

			cst.close();

			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "Insertar", "El usuario inserto con los siguientes datos: " + codigoLista
							+ " | " + rut + " | " + fecha + " | " + exc_inc, " insertarListaMedicos");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("error: " + e);
			response model = new response();
			model.setCodigo(1);
			model.setDetalle("No se pudo actualizar");
			datos.add(model);
			cont = 1;
		} finally {
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

	public ArrayList<response1> csv(String csv, String userRep, String codigoLista) throws SQLException

	{

		getconexionServicio();

		LOG.info(codigoLista);
		LOG.info(csv);
		LOG.info(userRep);

		String error = "";
		String codigo = "";
		String descripcion = "";
		boolean PasoValidacion = true;

		ArrayList<response1> datos = new ArrayList<>();
		response1 model = new response1();
		Scanner scanner = new Scanner(csv);
		Scanner scanner1 = new Scanner(csv);
		int cont = 0;

		LOG.info("CONT:::::" + cont);

		try {

			while (scanner.hasNextLine()) {

				String line = scanner.nextLine();

				if (cont > 0 && line != null && !line.trim().isEmpty()) {
					LOG.info("Line :" + line);

					LOG.info("::::::::::::: Pasando CST :::::::::::::");

					LOG.info("LENGTH: " + line.split(",").length);
					cont++;
					String[] fields = line.split(",", -1);

					LOG.info("Largo de los fields: " + fields.length);
					for (int i = 0; i < fields.length; i++) {
						if (fields[i] == null || fields[i].trim().isEmpty()) {
							fields[i] = "NULL";
						}
					}

					String rutMedico = fields[4];
					String fecha = fields[2];
					String incExc = fields[1];
					String ValidacodigoLista = fields[0];

					LOG.info("QUE ES ESTO: " + fields[0]);
					LOG.info("COMPARACION: " + codigoLista.equals(ValidacodigoLista));

					try {
						try {
							if (codigoLista.equals(ValidacodigoLista) == false) {
								PasoValidacion = false;
								error = "Linea : " + cont + ": El Codigo lista no es el mismo ";
							}
							if (ValidacodigoLista.isEmpty()) {
								PasoValidacion = false;
								error = "Linea : " + cont + ": El Codigo lista esta vacio" + error;
							}
						} catch (Exception e) {
							PasoValidacion = false;
							LOG.info("Error en codigoLista");
							error = "Linea : " + cont + " Falta el codigoLista  " + error;
						}

						try {
							if (rutMedico.length() > 10) {
								PasoValidacion = false;
								error = "Linea : " + cont + ": El dato rut no puede tener mas de 10 " + error;
							}

							if (rutMedico.equals("NULL")) {

								PasoValidacion = false;
								error = "Linea : " + cont + ": El dato rut esta vacio  " + error;

							} else {
								ArrayList<response> respuesta = BuscarRutListamedicos(codigoLista, rutMedico);
								if (respuesta.get(0).getCodigo() == 0) {

									LOG.info("RUT PASO CST :" + rutMedico);

								} else {
									PasoValidacion = false;
									LOG.info("Error en rut NO ENCONTRADO");
									error = "Linea : " + cont + " El rut no existe en la base de datos";
								}

							}

						} catch (Exception e) {
							PasoValidacion = false;
							LOG.info("Error en rut");
							error = "Linea : " + cont + " Falta el dato rut  " + error;
						}

						try {
							if (fecha.equals("NULL")) {
								PasoValidacion = false;
								error = "Linea : " + cont + ": El dato fecha esta vacio \n  " + error;

							} else {
								LOG.info("FECHA PASO CST");
							}

						} catch (Exception e) {
							PasoValidacion = false;
							LOG.info("Error en fecha ");
							error = "Linea : " + cont + " Falta el dato fecha  " + error;
						}

						try {
							if (incExc.length() > 1) {
								PasoValidacion = false;
								error = "Linea : " + cont
										+ ": El dato Exclusion/inclusion no puede tener mas de 1 caracter " + error;
							}

							if (incExc.equals("NULL")) {
								PasoValidacion = false;
								error = "Linea : " + cont + ": El dato Exclusion/inclusion vacio \n  " + error;
							} else {

								LOG.info("EXCLUSION PASO CST:" + incExc);
							}

						} catch (Exception e) {
							PasoValidacion = false;
							LOG.info("Error en el dato Exclusion/inclusion");
							error = "Linea : " + cont + " Falta el dato Exclusion/inclusion  " + error;
						}
					} catch (Exception e) {
						PasoValidacion = false;
						LOG.info("EXCEPTION CST :" + e);

					}

				}

				cont++;
			}

			LOG.info("PASO VALIDACION: " + PasoValidacion);

			if (PasoValidacion == true) {
				EliminarAllRutListamedicos(codigoLista);
				LOG.info("se eliminan los datos");

			} else {
				model.setCodigoRespuesta(2);
				model.setDetalleRespuest(error);
				model.setDescripcion(codigo + " , " + descripcion);
				datos.add(model);
			}

			while (scanner1.hasNextLine() && PasoValidacion == true) {
				String line = scanner1.nextLine();

				if (cont > 0 && line != null) {
					String[] fields = line.split(",", -1);

					LOG.info("Largo de los fields: " + fields.length);
					for (int i = 0; i < fields.length; i++) {
						if (fields[i] == null || fields[i].trim().isEmpty()) {
							fields[i] = "NULL";
						}
					}

					String rutMedico = fields[4];
					String fecha = fields[2];
					String incExc = fields[1];

					ActualizarListamedicos(codigoLista, rutMedico, fecha, incExc, userRep);

					model.setCodigoRespuesta(1);
					model.setDetalleRespuest("La carga masiva fue realizada con exito");
					model.setDescripcion(codigo + " , " + descripcion);
					datos.add(model);

				} else {
					model.setCodigoRespuesta(2);
					model.setDetalleRespuest(error);
					model.setDescripcion(codigo + " , " + descripcion);
					datos.add(model);

				}
			}

			scanner.close();

			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "cargaMasiva",
							"El usuario: |" + userRep + "| cargos masivamente a la lista de medicos |",
							"cargaMasivaMedicos");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("CONECTION EXCEPTION:" + e);
			model.setCodigoRespuesta(2);
			model.setDetalleRespuest("Error de conexion " + e);
			datos.add(model);

		} finally {
			con.close();
			LOG.info("CIERRO");
		}

		return datos;

	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
