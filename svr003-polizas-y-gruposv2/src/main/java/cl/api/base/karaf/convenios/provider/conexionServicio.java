package cl.api.base.karaf.convenios.provider;

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

import cl.api.base.karaf.convenios.model.*;
import cl.api.base.karaf.convenios.ApiReportes;

public class conexionServicio {

	Connection con;
	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);
	ApiReportes ApiCall = new ApiReportes();

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

	public ArrayList<response> buscarPolizas(String codigo, String userRep) throws SQLException, IOException {
		LOG.info(":::::::::::: Entrando en BUSCAR POLIZAS ::::::::: \n");
		String buscarPolizas = "{CALL dbo.PRD_MT_BUSCAR_POLIZA_WEB_CONVENIO (?,?,?)}";

		ArrayList<response> datos = new ArrayList<>();
		getconexionServicio();
		CallableStatement cst = con.prepareCall(buscarPolizas);

		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, codigo);
			cst.execute();
			ResultSet rs = cst.getResultSet();
			while (rs.next()) {

				response model = new response();
				model.setGrupoAhumada(rs.getString(1));
				model.setNombrePoliza(rs.getString(2));
				model.setCodigoPoliza(rs.getString(3));
				model.setPolizaAceptaBioequivalente(rs.getInt(4));
				model.setRutEmpresa(rs.getString(5));
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy");
					Date convertedCurrentDate = sdf.parse(rs.getString(6));
					String date = csdf.format(convertedCurrentDate);
					model.setTerminoBeneficio(date);
				} catch (Exception e) {
					LOG.info("Error en fecha Buscar Polizas");
				}

				model.setCuentaLiquidador(rs.getString(7));
				model.setEstadoPolizaAhumada(rs.getString(8));
				datos.add(model);

			}
			rs.close();
		} catch (SQLException e) {
			LOG.info("EXCEPTION :" + e);
		} finally {
			cst.close();
			con.close();
			LOG.info("Cerrando la conexion.....");
		}
		return datos;

	}

	public ArrayList<response1> ActualizarPoliza(String grupo, String nombre, String codigo, String rut, String fecha,
			int bio, String userRep) throws SQLException, IOException {

		LOG.info("::::::::::::: ENTRANDO A ACTUALIZAR POLIZA ::::::::::");

		SimpleDateFormat ddd = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");

		Timestamp timestamp;

		ArrayList<response1> datos = new ArrayList<>();

		String ActualizaPoliza = "{CALL dbo.PRD_MT_ACTUALIZA_POLIZA_WEB_CONVENIO (?,?,?,?,?,?,?,?)}";
		getconexionServicio();
		CallableStatement cst = con.prepareCall(ActualizaPoliza);
		try {
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
			if (bio == 0) {
				cst.setNull(8, java.sql.Types.NULL);
			} else {
				cst.setInt(8, bio);
			}

			cst.execute();

			response1 model = new response1();
			model.setCodigoRespuesta(cst.getInt(1));
			model.setDetalleRespuest(cst.getString(2));
			datos.add(model);

			Thread newThread = new Thread(() -> {
				try {
					ApiCall.ApiCall(userRep, "Actualizar", "El usuario inserto con los siguientes datos: |" + grupo
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
			cst.close();
			LOG.info("Cerrando la conexion.....");
		}

		return datos;
	}

}
