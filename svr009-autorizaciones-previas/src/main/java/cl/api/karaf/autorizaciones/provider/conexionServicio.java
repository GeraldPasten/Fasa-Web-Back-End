package cl.api.karaf.autorizaciones.provider;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.autorizaciones.ApiReportes;
import cl.api.karaf.autorizaciones.model.autorizacionPrevia;
import cl.api.karaf.autorizaciones.model.response;

public class conexionServicio {

	ApiReportes callApi = new ApiReportes();
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

	public ArrayList<response> InsertarAutorizaciones(String credenciales, String codigoPersona, String campo,
			String valorCampo, String incluir_exc, String fecha_inicio, String fecha_termino, int envases,
			String planId, String idMedico, String medicoIncExc, String userRep) throws SQLException {

		getconexionServicio();

		SimpleDateFormat ddd = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

		Timestamp inicio;
		Timestamp termino;

		ArrayList<response> datos = new ArrayList<>();

		String Autorizaciones = "{CALL [dbo].[PRD_INSERT_AUTORIZACIONES_PREVIAS_WEB_CONVENIO] (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement cst = con.prepareCall(Autorizaciones);
		try {
			

			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.registerOutParameter(3, java.sql.Types.INTEGER);

			cst.setInt(4, 0);
			cst.setString(5, credenciales.replaceAll("\\s", ""));
			cst.setString(6, codigoPersona);
			cst.setString(7, campo);
			cst.setString(8, valorCampo);
			cst.setString(9, incluir_exc);
			try {
				java.util.Date convertedCurrentDate = ddd.parse(fecha_inicio);
				String date = csdf.format(convertedCurrentDate);
				inicio = new Timestamp(ddd.parse(date).getTime());

				LOG.info("FECHA INICIO: " + inicio);

				cst.setTimestamp(10, inicio);
			} catch (Exception e) {
				LOG.info("Error en fecha inicio :" + e);
			}
			try {
				java.util.Date convertedCurrentDate = ddd.parse(fecha_termino);
				String date = csdf.format(convertedCurrentDate);
				termino = new Timestamp(ddd.parse(date).getTime());

				LOG.info("FECHA TERMINO: " + termino);

				cst.setTimestamp(11, termino);
			} catch (Exception e) {
				LOG.info("Error en fecha termino :" + e);
			}
			cst.setNull(12, java.sql.Types.INTEGER); // envase
			LOG.info("ENVASE SE PASO NULL");
			cst.setNull(13, java.sql.Types.INTEGER); // Descuento
			LOG.info("DESCUENTO SE PASO NULL");
			cst.setNull(14, java.sql.Types.INTEGER); // copago
			LOG.info("COPAGO SE PASO NULL");
			cst.setNull(15, java.sql.Types.INTEGER); // creditos
			LOG.info("CREDITOS SE PASO NULL");
			cst.setNull(16, java.sql.Types.INTEGER); // envases mensual
			LOG.info("ENVASES MENSUAL SE PASO NULL");
			cst.setShort(17, (short) 0); // control Id ApExterno
			cst.setByte(18, (byte) 1); // levantar todas las restricciones
			cst.setByte(19, (byte) 0); // levantar restriccion de medico
			cst.setByte(20, (byte) 0); // levantar restriccion de farmacia
			cst.setByte(21, (byte) 0); // levantar restriccion de genero
			cst.setByte(22, (byte) 0); // levantar restriccion de edad
			cst.setByte(23, (byte) 0); // levantar restriccion de envases
			cst.setByte(24, (byte) 0); // levantar restriccion dosis
			cst.setByte(25, (byte) 0); // levantar restriccion dia de tratamiento
			cst.setByte(26, (byte) 0); // levantar restriccion oportunidad dispensacion
			cst.setInt(27, 0); // dosis min
			cst.setInt(28, 0); // dosis max
			cst.setInt(29, 0); // dias tratamiento minimo
			cst.setInt(30, 0); // dias tratamiento maximo
			cst.setInt(31, envases); // acumulado envases
			cst.setInt(32, 0); // acumulado envases mensual

			if (planId.isEmpty() == true) {
				cst.setNull(33, java.sql.Types.VARCHAR);

			} else {
				cst.setString(33, planId);
			}

			cst.setNull(34, java.sql.Types.INTEGER);

			if (idMedico.isEmpty() == true) {

				cst.setNull(35, java.sql.Types.VARCHAR);
				LOG.info("EXCLUSION/INCLUSION MEDICO SE PASO NULL");
				cst.setNull(36, java.sql.Types.VARCHAR);
				LOG.info("MEDICO SE PASO NULL");

			} else {
				cst.setString(36, medicoIncExc);
				cst.setString(35, idMedico);
			}

			cst.execute();

			response model = new response();
			model.setCodigo(cst.getInt(1));
			model.setDescripcion(cst.getString(2));
			model.setId(cst.getInt(3));
			datos.add(model);


			Thread newThread = new Thread(() -> {
				try {
					callApi.ApiCall(userRep, "Insertar", "El usuario: |" + userRep
							+ "| inserto una atorizacion previa con los siguiente datos: | Credenciales: "
							+ credenciales + " | " + codigoPersona + " | Campo UPG/ASAP" + campo + " | " + valorCampo
							+ " | INC/EXC:" + incluir_exc + " | Fecha Inicio: " + fecha_inicio + " | Fecha Termino: "
							+ fecha_termino + " | Envases: " + envases + " |Protocolo: " + planId + " | Id Medico: "
							+ idMedico + " | Medico INC/EXC: " + medicoIncExc + " | ", "insertarAutorizacionPrevia");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			newThread.start();

		} catch (Exception e) {
			LOG.info("Error al Insertar autorizacion previa :" + e);
			response model = new response();
			model.setCodigo(1);
			model.setDescripcion("Error al Insertar autorizacion previa :" + e);
			datos.add(model);
		} finally {
			con.close();
			LOG.info("Cierro conexion");
		}
		return datos;

	}

	public ArrayList<autorizacionPrevia> listarAutorizacion(String rut, String convenio) throws SQLException {

		getconexionServicio();
		ArrayList<autorizacionPrevia> datos = new ArrayList<>();
		String sp = "{CALL PRD_REPORTE_AUTORIZACION_PREVIA_WEB_CONVENIO (?,?,?,?)}";
		CallableStatement cst = con.prepareCall(sp);
		String envaseMesual;
		String acumuladoEnvases;
		String idAPp;
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, rut);
			cst.setString(4, convenio);

			cst.execute();

			ResultSet rs = cst.getResultSet();
			if (rs.isBeforeFirst()== false) {
				autorizacionPrevia model = new autorizacionPrevia();
				model.setDetalle("No se encontraron datos");
				model.setCodigo("1");
				datos.add(model);
			} else {
				while (rs.next()) {
					envaseMesual = String.valueOf(rs.getInt(8));
					acumuladoEnvases = String.valueOf(rs.getInt(9));
					idAPp = String.valueOf(rs.getInt(10));

					autorizacionPrevia model = new autorizacionPrevia();
					model.setCredencial(rs.getString(1));
					model.setCodigoPersona(rs.getString(2));
					model.setCampo(rs.getString(3));
					model.setLAPB_VALOR_CAMPO(rs.getString(4));
					model.setLAPB_INCLUIR_EXCLUIR(rs.getString(5));
					model.setLAPB_FECHA_INICIO(rs.getString(6));
					model.setLAPB_FECHA_TERMINO(rs.getString(7));
					model.setLAPB_Q_ENVASES_MENSUAL(envaseMesual);
					model.setLAPB_ACUMULADO_ENVASES_MENSUAL(acumuladoEnvases);
					model.setLAPB_416_ID_AP(idAPp);
					model.setPLA_524_PLAN_ID(rs.getString(11));
					model.setLMA_ID_MEDICO(rs.getString(12));
					model.setLAPB_MEDICO_INCEXC(rs.getString(13));
					model.setCodigo("0");
					datos.add(model);

				}

			}

		} catch (Exception e) {
			LOG.info("Error al listar Autorizaciones" + e);
			autorizacionPrevia model = new autorizacionPrevia();
			model.setDetalle("Error al listar Autorizaciones: " + e);
			model.setCodigo("1");
			datos.add(model);
		}

		return datos;

	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
