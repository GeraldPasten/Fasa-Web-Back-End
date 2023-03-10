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

public class conexionServicio {

	ApiReportes CallApi = new ApiReportes();

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

	public ArrayList<response1> ActualizarPoliza(String grupo, String nombre, String codigo, String rut, String fecha,
			String bio, String userRep) throws SQLException, IOException {

		getconexionServicio();
		LOG.info("::::::::::::: ENTRANDO A ACTUALIZAR POLIZA ::::::::::");

		int aceptaBio = Integer.parseInt(bio);

		SimpleDateFormat ddd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat csdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		Timestamp timestamp;

		ArrayList<response1> datos = new ArrayList<>();

		String ActualizaPoliza = "{CALL dbo.PRD_MT_ACTUALIZA_POLIZA_WEB_CONVENIO (?,?,?,?,?,?,?,?)}";

		try {
			CallableStatement cst = con.prepareCall(ActualizaPoliza);

			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, grupo);
			cst.setString(4, nombre);
			cst.setString(5, codigo);
			cst.setString(6, rut);
			try {

				java.util.Date convertedCurrentDate = ddd.parse(fecha);

				String date = csdf.format(convertedCurrentDate);

				timestamp = new Timestamp(ddd.parse(date).getTime());

				LOG.info("value of TIMESTAMP : " + timestamp);

				cst.setTimestamp(7, timestamp);
			} catch (Exception e) {
				LOG.info("Error en fecha" + e);

			}
			if (aceptaBio == 0) {
				cst.setNull(8, java.sql.Types.NULL);
			} else {
				cst.setInt(8, aceptaBio);
			}

			cst.execute();

			response1 model = new response1();
			model.setCodigoRespuesta(cst.getInt(1));
			model.setDetalleRespuest(cst.getString(2));
			datos.add(model);
			cst.close();

			Thread newThread = new Thread(() -> {
				try {
					CallApi.ApiCall(userRep, "Actualizar", "El usuario inserto con los siguientes datos: |" + grupo
							+ " | " + codigo + " | " + nombre + " | " + rut + " | ", "actualizarPoliza");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (SQLException e) {
			LOG.info("EXCEPTION :" + e);
		} finally {
			con.close();
			LOG.info("Cerrando la conexion.....");
		}

		return datos;
	}

	public ArrayList<response1> csv(String csv, String userRep) throws SQLException {

		LOG.info(userRep);
		LOG.info(csv);

		String error = "";
		int control = 0;
		boolean PasoValidacion = true;
		ArrayList<response1> datos = new ArrayList<>();

		response1 model = new response1();

		Scanner scanner = new Scanner(csv);
		int cont = 0;
		try {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				LOG.info("CONT:::::" + cont);

				if (cont > 0 && line != null && !line.trim().isEmpty()) {

					LOG.info("Line :" + line);

					try {
						LOG.info("::::::::::::: Pasando CST :::::::::::::");
						
						LOG.info("LENGTH: " + line.split(",").length);

						String[] fields = line.split(",", -1);

						LOG.info("Largo de los fields: " + fields.length);
						for (int i = 0; i < fields.length; i++) {
							if (fields[i] == null || fields[i].trim().isEmpty()) {
								fields[i] = "NULL";
							}
						}

						String codigoPoliza = fields[0];
						String grupoAhumada = fields[2];
						String nombrePoliza = fields[3];
						String polizaAceptaBioequivalente = fields[4];
						String rutEmpresa = fields[5];
						String terminoBeneficio = fields[6];

						try {
							if (grupoAhumada.length() > 10) {
								control = 2;
								error = "Linea " + cont + ": El dato Grupo ahumada no puede tener mas de 10 caracteres" + error;
								PasoValidacion = false;
								LOG.info("Fallo el grupo ahumada");
							}
							if (grupoAhumada.equals("NULL")) {
								control = 2;
								error = "Linea " + cont + ": El dato Grupo ahumada esta vacio\n" + error;
								PasoValidacion = false;
								LOG.info("Fallo el grupo ahumada vacio");
							} else {
								LOG.info("split 2 :" + grupoAhumada);
							}

						} catch (Exception e) {
							LOG.info("Error en GRUPO poliza");
							control = 1;
							error = "Falta el dato GRUPO poliza";
							PasoValidacion = false;

						}

						try {
							if (nombrePoliza.length() > 60) {
								control = 2;
								error = "Linea " + cont + ": El dato nombre poliza no puede tener mas de 60 caracteres" + error;
								PasoValidacion = false;
								LOG.info("Fallo el nombrePoliza");
							}
							if (nombrePoliza.equals("NULL")) {

								control = 2;
								error = "Linea " + cont + ": El dato nombre poliza esta vacio \n" + error;
								PasoValidacion = false;
								LOG.info("Fallo el nombrePoliza vacio"); 

							} else {
								LOG.info("split 3 :" + nombrePoliza);

							}

						} catch (Exception e) {
							LOG.info("Error en NOMBRE poliza");
							control = 1;
							error = "Falta el dato NOMBRE poliza";
							PasoValidacion = false;
						}

						try {
							if(codigoPoliza.length() > 15) {
								control = 2;
								error = "Linea " + cont + ": El dato codigo poliza no puede tener mas de 15 caracteres" + error;
								PasoValidacion = false;
								LOG.info("Fallo el codigoPoliza"); 
								
							}
							if (codigoPoliza.equals("NULL")) {

								control = 2;
								error = "Linea " + cont + ": El dato codigo poliza esta vacio \n" + error;
								PasoValidacion = false;
								LOG.info("Fallo el codigoPoliza vacio"); 

							} else {
								LOG.info("split 0 :" + codigoPoliza);
							}

						} catch (Exception e) {
							LOG.info("Error en CODIGO POLIZA poliza");
							control = 1;
							error = "Falta el dato CODIGO POLIZA poliza";
							PasoValidacion = false;
						}

						try {
							if (rutEmpresa.length() > 10) {
								control = 2;
								error = "Linea " + cont + ": El dato Rut Empresa no puede superar los 10 caracteres" + error;
								PasoValidacion = false;
								LOG.info("Fallo el rut"); 
							}
							if (rutEmpresa.equals("NULL")) {
								control = 2;
								error = "Linea " + cont + ": El dato Rut Empresa esta vacio \n" + error;
								PasoValidacion = false;
								LOG.info("Fallo el rut Medico"); 
							} else {
								LOG.info("split 5 :" + rutEmpresa);
							}

						} catch (Exception e) {
							LOG.info("Error en RUT empresa");
							control = 1;
							error = "Falta el dato RUT empresa";
							PasoValidacion = false;
						}

						try {

							if (terminoBeneficio.equals("NULL")) {
								control = 2;
								error = "Linea " + cont + " :El dato Fecha termino esta vacio \n" + error;
								PasoValidacion = false;
								LOG.info("Fallo el terminoBeneficio"); 
							} else {
								LOG.info("paso fecha");
							}

						} catch (Exception e1) {
							LOG.info("Error en Fecha termino");
							control = 1;
							error = "Falta el dato Fecha termino";
							PasoValidacion = false;
							
						}
						try {
							if (polizaAceptaBioequivalente.length() > 1) {
								control = 2;
								error = "Fila " + cont + ": El dato Bioequivalente no puede tener mas de 1 caracter" + error;
								PasoValidacion = false;
								LOG.info("Fallo el polizaAceptaBioequivalente"); 
							}
							if (polizaAceptaBioequivalente.equals("NULL")) {
								control = 2;
								error = "Fila " + cont + ": El dato Bioequivalente esta vacio \n" + error;
								PasoValidacion = false;
								LOG.info("Fallo el polizaAceptaBioequivalente vacio");
							} else {
								LOG.info("split 4 :" + polizaAceptaBioequivalente);
							}

						} catch (Exception e) {
							LOG.info("Error en Bioequivalente");
							control = 1;
							error = "Falta el dato Bioequivalente";
							PasoValidacion = false;
						}
						
						LOG.info("Validacion: "+PasoValidacion);
						if (PasoValidacion == true) {
							LOG.info("se ejecuto el SP");
							ActualizarPoliza(grupoAhumada, nombrePoliza, codigoPoliza, rutEmpresa, terminoBeneficio,
									polizaAceptaBioequivalente, userRep);
						}
						
						LOG.info("No paso por el SP");
	

					} catch (Exception e) {
						LOG.info("EXCEPTION CST :" + e);
					}

				}
				cont++;

			}
			scanner.close();

			Thread newThread = new Thread(() -> {
				try {
					CallApi.ApiCall(userRep, "cargaMasiva",
							"El usuario " + userRep + " actualizo masivamente los datos", "cargaMasivaPolizas");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("CONECTION EXCEPTION:" + e);
			model.setCodigoRespuesta(2);
			model.setDetalleRespuest("Error de conexion: " + e);
			datos.add(model);

		} finally {
			LOG.info("Termino metodo de carga masiva");
		}
		LOG.info("Control :" + control);

		if (control == 1 || control == 2) {
			model.setCodigoRespuesta(2);
			model.setDetalleRespuest(error);
			datos.add(model);

		} else {
			model.setCodigoRespuesta(1);
			model.setDetalleRespuest("La carga masiva fue realizada con exito");
			datos.add(model);
		}

		return datos;
	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
