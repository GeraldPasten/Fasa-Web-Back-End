package cl.api.karaf.cartola.provider;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import cl.api.karaf.cartola.model.response;

public class conexionServicio {

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

	public ArrayList<response> GetCartola(String Tipo, String codigoConvenio, String rut, String fechaIni,
			String fechaFin) throws SQLException {

		LOG.info("::::::::: ENTRANDO A CARTOLA ::::::::::");

		String cartola = "{CALL dbo.PRD_REPORTE_VENTA_WEB_CONVENIO (?,?,?,?,?,?,?)}";

		ArrayList<response> datos = new ArrayList<>();
		getconexionServicio();
		CallableStatement cst = con.prepareCall(cartola);
		try {
			cst.registerOutParameter(1, java.sql.Types.INTEGER);
			cst.registerOutParameter(2, java.sql.Types.VARCHAR);
			cst.setString(3, Tipo);
			cst.setString(4, codigoConvenio);
			if (rut.isEmpty()) {
				cst.setNull(5, java.sql.Types.NULL);
			} else {
				cst.setString(5, rut);
			}
			cst.setString(6, fechaIni);
			cst.setString(7, fechaFin);
			cst.execute();

			ResultSet rs = cst.getResultSet();

			if (rs.isBeforeFirst())
			{
				while (rs.next()) {
					response model = new response();

					model.setEstado(rs.getString(1));
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
						SimpleDateFormat csdf = new SimpleDateFormat("dd-MM-yyyy");
						Date convertedCurrentDate = sdf.parse(rs.getString(2));
						String date = csdf.format(convertedCurrentDate);
						model.setFecha(date);
					} catch (Exception e) {
						LOG.info("problemas al parsear la fecha " + e);
					}
					model.setFarmacia(rs.getInt(3));
					model.setId_receta(rs.getInt(4));
					model.setDireccion(rs.getString(5));
					model.setComuna(rs.getString(6));
					model.setBoleta(rs.getInt(7));
					model.setGuia(rs.getInt(8));
					model.setSAP(rs.getInt(9));
					model.setDecripcion_producto(rs.getString(10));
					model.setCantidad(rs.getInt(11));
					model.setPrecio(rs.getInt(12));
					model.setDescto(rs.getInt(13));
					model.setBonificado(rs.getInt(14));
					model.setCopago(rs.getInt(15));
					model.setTotal(rs.getInt(16));
					model.setCodigo("0");
					datos.add(model);

				}
			} else {
				response model = new response();
				model.setDetalle("No se encontraron datos");
				model.setCodigo("1");
				datos.add(model);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
			response model = new response();
			model.setDetalle("No se encontraron datos: "+e);
			model.setCodigo("1");
			datos.add(model);
			LOG.info("Error en listar cartola beneficiarios" + e);
		} finally {
			cst.close();
			con.close();
		}

		return datos;
	}

	private static final Logger LOG = LoggerFactory.getLogger(conexionServicio.class);

}
