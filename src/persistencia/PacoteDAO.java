package persistencia;

import entidade.Ator;
import entidade.Pacote;
import entidade.Tag;
import entidade.TipoPacote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;

public class PacoteDAO {
	public static void cadastraPacote(Pacote pacote) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		//prepara os itens para serem cadastrados
		String nome = pacote.getNome();
		Integer ator = pacote.getAtor() == null ? null : pacote.getAtor().getCodigo();
		Long data = pacote.getData().getTime();
		Double tamanho = pacote.getTamanho();
		Integer tipo = pacote.getTipo() == null ? null : pacote.getTipo().getCodigo();

		s.execute("INSERT INTO pacote(nomePacote, codAtor, dataCriacao, tamPacote, codBackup, codTipoPacote) VALUES "
				+ "('" + nome + "', '" + ator + "'," + data + "," + tamanho + ", null,'" + tipo + "');");
		ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ROWID();");
		pacote.setCodigo(rs.getInt(1));
		rs.close();
		s.close();

		//atualiza a lista de tags
		s = c.createStatement();
		for (Tag tag : pacote.getTags())
			s.execute("INSERT INTO tags_pacote(codPacote, codTag) VALUES (" + pacote.getCodigo() + ", (SELECT codTag" +
					" " +
					"FROM tags WHERE nomeTag='" + tag.getNome() + "'));");

		s.close();
		c.close();
	}

	public static ArrayList<Pacote> listaPacotes() throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();
		ArrayList<Pacote> lista = new ArrayList<>();

		ResultSet rs = s.executeQuery("SELECT codPacote, nomePacote, codAtor, nomeAtor, dataCriacao, tamPacote, " +
				"codBackup, codTipoPacote, nomeTipoPacote FROM pacote LEFT JOIN ator USING (codAtor) LEFT JOIN " +
				"tipoPacote USING (codTipoPacote);");
		while (rs.next()) {
			Pacote pacote = new Pacote();
			pacote.setCodigo(rs.getInt("codPacote"));
			pacote.setNome(rs.getString("nomePacote"));
			pacote.setAtor(new Ator(rs.getInt("codAtor"), rs.getString("nomeAtor")));
			pacote.setData(rs.getDate("dataCriacao"));
			pacote.setTamanho((double) rs.getFloat("tamPacote"));
			pacote.setCodigoBackup(rs.getObject("codBackup") == null ? null : rs.getInt("codBackup"));
			pacote.setTipo(new TipoPacote(rs.getInt("codTipoPacote"), rs.getString("nomeTipoPacote")));
			pacote.setTags(buscaTags(c, pacote.getCodigo()));
			lista.add(pacote);
		}

		rs.close();
		s.close();
		c.close();
		return lista;
	}

	public static Pacote buscaPacote(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();
		Pacote pacote = new Pacote();

		ResultSet rs = s.executeQuery("SELECT codPacote, nomePacote, codAtor, nomeAtor, dataCriacao, tamPacote, " +
				"codBackup, codTipoPacote, nomeTipoPacote FROM pacote LEFT JOIN ator USING (codAtor) LEFT JOIN " +
				"tipoPacote USING (codTipoPacote) WHERE  codPacote=" + codigo + ";");

		if (rs.next()) {
			pacote.setCodigo(rs.getInt("codPacote"));
			pacote.setNome(rs.getString("nomePacote"));
			pacote.setAtor(new Ator(rs.getInt("codAtor"), rs.getString("nomeAtor")));
			pacote.setData(new Date(rs.getDate("dataCriacao").getTime()));
			pacote.setTamanho((double) rs.getFloat("tamPacote"));
			pacote.setCodigoBackup(rs.getObject("codBackup") == null ? null : rs.getInt("codBackup"));
			pacote.setTipo(new TipoPacote(rs.getInt("codTipoPacote"), rs.getString("nomeTipoPacote")));
			pacote.setTags(buscaTags(c, pacote.getCodigo()));
		} else {//nenhum pacote encontrado
			rs.close();
			s.close();
			c.close();
			return null;
		}

		rs.close();
		s.close();
		c.close();
		return pacote;
	}

	//busca as tags do codigo informado utilizando uma conexão já existente
	private static LinkedHashSet<Tag> buscaTags(Connection c, Integer codigo) throws SQLException {
		LinkedHashSet<Tag> lista = new LinkedHashSet<>();
		Statement s = c.createStatement();

		ResultSet rs = s.executeQuery("SELECT codTag, nomeTag FROM tags_pacote NATURAL JOIN tags WHERE " +
				"codPacote=" + codigo + ";");
		while (rs.next())
			lista.add(new Tag(rs.getInt("codTag"), rs.getString("nomeTag")));

		rs.close();
		s.close();
		return lista;
	}

	public static void alteraPacote(Pacote pacote) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		//prepara os itens para serem cadastrados
		int codigo = pacote.getCodigo();
		String nome = pacote.getNome();
		Integer ator = pacote.getAtor() == null ? null : pacote.getAtor().getCodigo();
		Long data = pacote.getData().getTime();
		Double tamanho = pacote.getTamanho();
		Integer tipo = pacote.getTipo() == null ? null : pacote.getTipo().getCodigo();

		s.execute("UPDATE pacote SET nomePacote='" + nome + "', codAtor=" + ator + ", dataCriacao=" + data + ", " +
				"tamPacote=" + tamanho + ", codTipoPacote=" + tipo + " WHERE codPacote=" + codigo + ";");

		//apaga todas as ocorrências em tags_pacote e re-adiciona
		s.execute("DELETE FROM tags_pacote WHERE codPacote=" + codigo + ";");
		for (Tag tag : pacote.getTags())
			s.execute("INSERT INTO tags_pacote(codPacote, codTag) VALUES (" + pacote.getCodigo() + ", (SELECT codTag" +
					" " +
					"FROM tags WHERE nomeTag='" + tag.getNome() + "'));");

		s.close();
		c.close();
	}

	public static void excluirPacote(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("DELETE FROM pacote WHERE codPacote=" + codigo + ";");
		s.execute("DELETE FROM tags_pacote WHERE codPacote=" + codigo + ";");

		s.close();
		c.close();
	}
}
