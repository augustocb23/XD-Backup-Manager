package controle;

import entidade.TipoPacote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoPacoteDAO {
	public static void cadastrarAtor(String nome) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("INSERT INTO tipoPacote(nomeTipoPacote) VALUES ('" + nome + "');");

		s.close();
		c.close();
	}

	public static ArrayList<TipoPacote> buscaTiposPacote() throws SQLException {
		Connection c = Conexao.getConnection();
		ArrayList<TipoPacote> lista = new ArrayList<>();

		//buca os dados
		Statement s = c.createStatement();
		ResultSet rs = s.executeQuery("SELECT codTipoPacote,nomeTipoPacote FROM tipoPacote;");
		//salva tudo na lista
		while (rs.next()) {
			TipoPacote tipoPacote = new TipoPacote();
			tipoPacote.setCodigo(rs.getInt("codTipoPacote"));
			tipoPacote.setNome(rs.getString("nomeTipoPacote"));
			lista.add(tipoPacote);
		}

		s.close();
		c.close();

		return lista;
	}

	public static void apagaTipoPacote(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("DELETE FROM tipoPacote WHERE codTipoPacote='" + codigo + "';");

		s.close();
		c.close();
	}

	public static void renomearTipoPacote(int codigo, String nome) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("UPDATE tipoPacote SET nomeTipoPacote='" + nome + "' WHERE codTipoPacote='" + codigo + "';");

		s.close();
		c.close();
	}
}
