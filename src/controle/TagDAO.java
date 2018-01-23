package controle;

import entidade.Tag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TagDAO {

	public static void cadastrarTag(String nome) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("INSERT INTO tags(nomeTag) VALUES ('" + nome + "');");

		s.close();
		c.close();
	}

	public static ArrayList<Tag> buscaTags() throws SQLException {
		Connection c = Conexao.getConnection();
		ArrayList<Tag> lista = new ArrayList<>();

		//buca os dados
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT codTag, nomeTag FROM tags;");
		//salva tudo na lista
		while (rs.next()) {
			Tag tag = new Tag(rs.getInt("codTag"), rs.getString("nomeTag"));
			lista.add(tag);
		}

		s.close();
		c.close();

		return lista;
	}

	public static void apagaTag(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("DELETE FROM tags WHERE codTag='" + codigo + "';");

		s.close();
		c.close();
	}
}
