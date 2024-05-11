package BDImagenes;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class ejercicio {

	private static final String URL = "jdbc:mysql://localhost/";
	private static final String USER = "root";
	private static final String PWD = "carlosjr5";
	private static Connection conex;
	private static PreparedStatement ps;

	public static void main(String[] args) {
		try {
			conex = conectar("agenda");
			insertarDatos(conex);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cerrar(conex);
		}
	}

	public static void insertarDatos(Connection conex) {
		try {
			conex.setAutoCommit(false);

			ps = conex.prepareStatement(
					"INSERT INTO contacto (nombre, telefono, imagen, binarioimagen) VALUES (?, ?, ?, ?)");
			ps.setString(1, randomNames());
			ps.setInt(2, Integer.parseInt(randomTlf()));
			ps.setString(3, rutaRelativaImg());
			ps.setBlob(4, new FileInputStream(foto()));

			ps.executeUpdate();

			conex.commit();
			conex.setAutoCommit(true);
		} catch (Exception e) {
			doRollback();
			e.printStackTrace();
		}
	}

	public static String randomNames() {
		String[] nombres = { "Juan", "Alberto", "Javier", "Daniel", "Marcos", "Roberto", "Antonio", "Darío", "Andrés" };
		return nombres[new Random().nextInt(nombres.length)];
	}

	public static String randomTlf() {
		StringBuilder numero = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 9; i++) {
			numero.append(random.nextInt(10)); // Genera un dígito aleatorio
		}
		return numero.toString();
	}

	public static File foto() {
		return new File("img/andres.jpg");
	}

	public static String rutaRelativaImg() {
		return foto().getPath();
	}

	public static Connection conectar(String nombreBD) throws SQLException {
		return DriverManager.getConnection(URL + nombreBD, USER, PWD);
	}

	public static void cerrar(Connection conex) {
		if (conex != null) {
			try {
				conex.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void doRollback() {
		try {
			conex.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
