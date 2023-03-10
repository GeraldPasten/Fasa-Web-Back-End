package cl.api.karaf.beneficiario.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import cl.api.karaf.beneficiario.model.actualizaResponse;
import cl.api.karaf.beneficiario.model.response;
import cl.api.karaf.beneficiario.ApiReportes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

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

	public ArrayList<response> GetAllBeneficiario(String codigoCliente, String rut, int activos) throws SQLException {
		LOG.info(":::::::::::: Entrando a Listar Beneficiarios :::::::::");
		String listarBeneficiarios = "{CALL dbo.PRD_REPORTE_BENEFICIARIOS_WEB_CONVENIO (?,?,?,?,?)}";

		ArrayList<response> datos = new ArrayList<>();
		getconexionServicio();
		CallableStatement cst = con.prepareCall(listarBeneficiarios);
		try {

			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigoCliente);
			cst.setString(4, rut);
			cst.setInt(5, activos);
			cst.execute();

			ResultSet rs = cst.getResultSet();

			if (rs.isBeforeFirst() == false) {
				response model = new response();
				model.setCodigoError("1");
				model.setDetalle("No se encontraron datos");
				datos.add(model);

			} else {

				while (rs.next()) {
					response model = new response();
					model.setId(rs.getString(1));
					model.setCredenciales(rs.getString(2));
					model.setCodigoConvenio(rs.getString(3));
					model.setRutTitular(rs.getString(4));
					model.setRutBeneficiario(rs.getString(5));
					model.setCodigoCarga(rs.getString(6));
					model.setGrupo(rs.getString(7));
					model.setPoliza(rs.getInt(9));
					model.setCodigoRelacion(rs.getInt(10));
					model.setNombre(rs.getString(11));
					model.setApellido1(rs.getString(12));
					model.setApellido2(rs.getString(13));
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy");
						Date convertedCurrentDate = sdf.parse(rs.getString(14));
						String nacimiento = csdf.format(convertedCurrentDate);
						model.setFechaNacimiento(nacimiento);
					} catch (Exception e) {
						LOG.info("problemas al parsear la fecha " + e);
					}
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy");
						Date convertedCurrentDate = sdf.parse(rs.getString(15));
						String vigencia = csdf.format(convertedCurrentDate);
						model.setVigencia(vigencia);

					} catch (Exception e) {
						LOG.info("problemas al parsear la fecha " + e);
					}
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy");
						Date convertedCurrentDate = sdf.parse(rs.getString(16));
						String termino = csdf.format(convertedCurrentDate);
						model.setTermino(termino);

					} catch (Exception e) {
						LOG.info("problemas al parsear la fecha " + e);
					}

					if (rs.getInt(18) == 1) {
						model.setGenero("Masculino");
					} else if (rs.getInt(18) == 2) {
						model.setGenero("Femenino");
					}
					model.setMail(rs.getString(19));
					model.setDireccion(rs.getString(20));
					model.setComuna(rs.getString(22));
					model.setCiudad(rs.getString(23));
					datos.add(model);
				}

			}

			rs.close();

		} catch (Exception e) {
			LOG.info("error: " + e);
			response model = new response();
			model.setCodigoError("1");
			model.setDetalle("No se encontraron datos: " + e);
			datos.add(model);

		} finally {
			cst.close();
			con.close();
		}

		return datos;
	}

	public ArrayList<actualizaResponse> ActualizaBeneficiario(String rutEmpresa, String codigoCliente,
			String RutTitular, String rutBeneficiario, String codigoCarga, String codigoGrupo, String codigoPlan,
			int poliza, int codigoRelacion, String primeroNombre, String paterno, String materno, String nacimiento,
			String vigencia, String termino, String bloqueo, int sexo, String mail, String direccion,
			String codigoPostal, String comuna, String ciudad, String userRep) throws SQLException {

		String ActualizarBeneficiarios = "{CALL dbo.PRD_WS_ELEGIBILIDAD_PROCESO_API (?,?,?,?)}";

		getconexionServicio();
		ArrayList<actualizaResponse> datos = new ArrayList<>();
		SQLServerCallableStatement cst = (SQLServerCallableStatement) con.prepareCall(ActualizarBeneficiarios);

		try {

			SQLServerDataTable sourceDataTable = new SQLServerDataTable();

			LOG.info("Columnas");
			sourceDataTable.addColumnMetadata("RUT_BENEFICIARIO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("CODIGO_CARGA", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("CODIGO_GRUPO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("CODIGO_PLAN", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("POLIZA", java.sql.Types.NUMERIC);
			sourceDataTable.addColumnMetadata("CODIGO_RELACION", java.sql.Types.NUMERIC);
			sourceDataTable.addColumnMetadata("PRIMER_NOMBRE", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("PATERNO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("MATERNO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("FECHA_NACIMIENTO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("FECHA_VIGENCIA", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("FECHA_TERMINO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("FECHA_BLOQUEO", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("GENERO", java.sql.Types.NUMERIC);
			sourceDataTable.addColumnMetadata("MAIL", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("DIRECCION", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("CODIGO_POSTAL", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("COMUNA", java.sql.Types.VARCHAR);
			sourceDataTable.addColumnMetadata("CIUDAD", java.sql.Types.VARCHAR);

			LOG.info("fecha termino:" + termino);
			sourceDataTable.addRow(rutBeneficiario, codigoCarga, codigoGrupo, codigoPlan, (poliza == 0) ? null : poliza,
					codigoRelacion, primeroNombre, paterno, materno, nacimiento, vigencia, termino, bloqueo, sexo, mail,
					direccion, codigoPostal, comuna, ciudad);

			cst.setString(1, rutEmpresa);
			cst.setString(2, codigoCliente);
			cst.setString(3, RutTitular);
			cst.setStructured(4, "BENEFICIARIO_ACTUALIZACION", sourceDataTable);
			cst.execute();

			actualizaResponse model = new actualizaResponse();
			model.setDetalle("Se actualizaron los datos");
			model.setCodigoError("0");
			datos.add(model);

			LOG.info("USER REP " + userRep);

			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "Actualizar",
							"El usuario " + userRep + " actualizo los siguientes datos: | " + rutEmpresa + " | "
									+ RutTitular + " | " + " | " + rutBeneficiario + " | " + codigoCliente + " | "
									+ codigoGrupo + " | " + codigoPlan + " | " + poliza + " | " + codigoRelacion + " | "
									+ primeroNombre + " | " + paterno + " | " + materno + " | " + nacimiento + " | "
									+ vigencia + " | " + termino + " | " + bloqueo + " | " + sexo + " | " + mail + " | "
									+ direccion + " | " + codigoPostal + " | " + comuna + " | " + ciudad + " | ",
							"actualizarBeneficiario");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("Error en Actualizar beneficiarios: " + e);
			actualizaResponse model = new actualizaResponse();
			model.setCodigoError("1");
			model.setDetalle("ERROR: " + e);
			datos.add(model);
			e.printStackTrace();
		} finally {
			con.close();
			cst.close();
			LOG.info("Cierro conexion");
		}

		return datos;
	}

	public ArrayList<actualizaResponse> cargaMasiva(String csv, String userRep, String convenio) throws SQLException {

		LOG.info("Usuario: " + userRep);
		LOG.info("CSV: " + csv);
		LOG.info(convenio);

		ArrayList<actualizaResponse> datos = new ArrayList<>();
		Scanner scanner = new Scanner(csv);
		String error = "";
		int control = 0;
		boolean PasoValidacion = true;
		int cont = 0;
		int codigoRelacionNumeric = 0;
		int polizaNumeric = 0;
		int generoNumeric = 0;

		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				LOG.info("CONT:::::" + cont);

				if (cont > 0 && line != null && !line.trim().isEmpty()) {

					LOG.info("Line :" + line);
					LOG.info("::::::::::::: Pasando CST :::::::::::::");
					LOG.info("LENGTH: " + line.split(",").length);

					String[] fields = line.split(",", -1);

					LOG.info("Largo de los fields: " + fields.length);
					for (int i = 0; i < fields.length; i++) {
						if (fields[i] == null || fields[i].trim().isEmpty()) {
							fields[i] = "NULL";
						}
					}
					String rutbeneficiario = fields[16];
					String CODIGO_CARGA = fields[3];
					String CODIGO_GRUPO = fields[11];
					String POLIZA = fields[15];
					String CODIGO_RELACION = fields[5];
					String PRIMER_NOMBRE = fields[14];
					String Apellido1 = fields[0];
					String Apellido2 = fields[1];
					String FECHA_NACIMIENTO = fields[9];
					String FECHA_VIGENCIA = fields[19];
					String FECHA_TERMINO = fields[18];
					String GENERO = fields[10];
					String MAIL = fields[13];
					String DIRECCION = fields[8];
					String COMUNA = fields[6];
					String CIUDAD = fields[2];
					String CONVENIO = fields[4];
					String rutTitular = fields[17];

					cont++;

					if (convenio.equals(CONVENIO) == false) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": El Convenio no el mismo " + error;

					}

					if (rutbeneficiario.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": El rut beneficiario esta vacio  " + error;
					}
					if (CODIGO_CARGA.length() > 3) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": El CODIGO_CARGA tiene que tener un maximo de 3 caracteres  "
								+ error;
					}
					if (CODIGO_CARGA.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": El CODIGO_CARGA esta vacio  " + error;
					}
					if (CODIGO_GRUPO.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": CODIGO_GRUPO esta vacio  " + error;
					}
					if (POLIZA.length() > 15) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": POLIZA tiene que tener un maximo de 15 caracteres  " + error;
					} else {
						if (POLIZA.equals("NULL")) {
							polizaNumeric = 0;
						} else {
							polizaNumeric = Integer.parseInt(POLIZA);
						}
					}

					if (CODIGO_RELACION.length() > 1) {

						control = 2;
						error = "Linea  " + cont + ": CODIGO_RELACION exede su tamaño " + error;
					} else {

						if (CODIGO_RELACION.equals("NULL")) {
							PasoValidacion = false;
							control = 2;
							error = "Linea  " + cont + ": CODIGO_RELACION esta vacio  " + error;
						} else {
							codigoRelacionNumeric = Integer.parseInt(CODIGO_RELACION);
						}

					}
					if (PRIMER_NOMBRE.length() > 15) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": PRIMER_NOMBRE no puede tener mas de 15 caracteres " + error;
					}

					if (PRIMER_NOMBRE.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": PRIMER_NOMBRE esta vacio  " + error;
					}
					if (Apellido1.length() > 20) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": Apellido 1 no puede tener mas de 20 caracteres " + error;
					}
					if (Apellido1.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": Apellido 1 esta vacio  " + error;
					}
					if (Apellido2.length() > 15) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": Apellido 2 no puede tener mas de 15 caracteres " + error;
					}
					if (Apellido2.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": Apellido 2 esta vacio  " + error;
					}
					if (FECHA_NACIMIENTO.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": FECHA_NACIMIENTO esta vacio  " + error;
					}
					if (FECHA_VIGENCIA.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": FECHA_VIGENCIA esta vacio  " + error;
					}
					if (FECHA_TERMINO.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": FECHA_TERMINO esta vacio  " + error;
					}
					if (GENERO.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": GENERO esta vacio  " + error;
					} else {
						generoNumeric = Integer.parseInt(GENERO);
					}
					if (MAIL.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": MAIL esta vacio  " + error;
					}
					if (DIRECCION.length() > 60) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": DIRECCION no puede tener mas de 60 caracteres " + error;
					}
					if (DIRECCION.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": DIRECCION esta vacio  " + error;
					}
					if (COMUNA.length() > 20) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": COMUNA no puede tener mas de 20 caracteres  " + error;
					}
					if (COMUNA.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": COMUNA esta vacio  " + error;
					}
					if (CIUDAD.length() > 20) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": CIUDAD no puede tener mas de 20 caracteres  " + error;
					}
					if (CIUDAD.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": CIUDAD esta vacio  " + error;
					}
					if (rutTitular.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": Rut titular esta vacio  " + error;
					}
					if (CONVENIO.equals("NULL")) {
						PasoValidacion = false;
						control = 2;
						error = "Linea  " + cont + ": Convenio esta vacio  " + error;
					}

					if (PasoValidacion = true) {
						ActualizaBeneficiario("1-8", CONVENIO, rutTitular, rutbeneficiario, CODIGO_CARGA, CODIGO_GRUPO,
								"00", polizaNumeric, codigoRelacionNumeric, PRIMER_NOMBRE, Apellido1, Apellido2,
								FECHA_NACIMIENTO, FECHA_VIGENCIA, FECHA_TERMINO, "NULL", generoNumeric, MAIL, DIRECCION,
								"NULL", COMUNA, CIUDAD, userRep);
					}

				}
				cont++;

			}

			scanner.close();
			Thread newThread = new Thread(() -> {
				try {
					apiCall.ApiCall(userRep, "cargaMasiva",
							"El usuario: " + userRep + " cargo masivamente beneficiarios.", "cargaMasivaBeneficiarios");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

			if (control == 2) {
				actualizaResponse model = new actualizaResponse();
				model.setDetalle(error);
				model.setCodigoError("1");
				datos.add(model);
			} else {
				actualizaResponse model = new actualizaResponse();
				model.setDetalle("Se actualizaron los datos");
				model.setCodigoError("0");
				datos.add(model);
			}

		} catch (Exception e) {
			LOG.info("Error en Actualizar beneficiarios: " + e);
			actualizaResponse model = new actualizaResponse();
			model.setCodigoError("1");
			model.setDetalle("ERROR: " + e);
			datos.add(model);
			e.printStackTrace();
		} finally {
			LOG.info("paso valdicaciones:" + PasoValidacion);
		}

		return datos;
	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
