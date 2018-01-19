package controle;

import entidade.Ator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AtorDAO {

	public static void cadastrarAtor(String nome) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("INSERT INTO ator(nomeAtor) VALUES ('" + nome + "');");

		s.close();
		c.close();
	}

	public static ArrayList<Ator> buscaAtores() throws SQLException {
		Connection c = Conexao.getConnection();
		ArrayList<Ator> lista = new ArrayList<>();

		//buca os dados
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT codAtor,nomeAtor FROM ator;");
		//salva tudo na lista
		while (rs.next()) {
			Ator ator = new Ator();
			ator.setCodigo(rs.getInt("codAtor"));
			ator.setNome(rs.getString("nomeAtor"));
			lista.add(ator);
		}

		s.close();
		c.close();

		return lista;
	}

	public static void apagaAtor(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("DELETE FROM ator WHERE codAtor='" + codigo + "';");

		s.close();
		c.close();
	}

	public static void renomearAtor(int codigo, String nome) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("UPDATE ator SET nomeAtor='" + nome + "' WHERE codAtor='" + codigo + "';");

		s.close();
		c.close();
	}
}
