package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Conexao {
	//nome do arquivo que contém os dados
	private final static String DB = System.getProperty("user.home") + "\\backups.sqlite";

	static Connection getConnection() throws SQLException {
		Connection c;
		//conecta ao banco de dados
		String url = "jdbc:sqlite:" + DB;
		c = DriverManager.getConnection(url);
		return c;
	}

	static String getDB() {
		return DB;
	}
}
