package controle;

import entidade.Backup;
import entidade.Pacote;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class BackupDAO {
	public static void cadastraBackup(Backup backup) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		//prepara os itens para serem cadastrados
		Long data = backup.getDataGravacao().getTime();

		//cadastra o backup e salva o codigo
		s.execute("INSERT INTO backup(dataGravacao) VALUES (" + data + ");");
		ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ROWID();");
		backup.setCodigo(rs.getInt(1));
		rs.close();
		s.close();

		//atualiza os pacotes
		s = c.createStatement();
		for (Pacote pacote : backup.getPacotes())
			if (pacote.getCodigoBackup() == null)
				s.execute("UPDATE pacote SET codBackup = " + backup.getCodigo() + " WHERE codPacote=" + pacote
						.getCodigo() + ";");
			else //se for -1 (usuário removeu do backup)
				s.execute("UPDATE pacote SET codBackup = NULL WHERE codPacote=" + pacote.getCodigo() + ";");

		s.close();
		c.close();
	}

	public static ArrayList<Backup> listaBackup() throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();
		ArrayList<Backup> lista = new ArrayList<>();

		ResultSet rs = s.executeQuery("SELECT codBackup, dataGravacao, count(codBackup) AS 'pacotes', SUM(tamPacote)" +
				" " +
				"AS 'tamanho' FROM pacote NATURAL JOIN backup GROUP BY codBackup;");

		while (rs.next()) {
			Backup backup = new Backup();
			backup.setCodigo(rs.getInt("codBackup"));
			backup.setDataGravacao(rs.getDate("dataGravacao"));
			backup.setTotPacotes(rs.getInt("pacotes"));
			backup.setTamanho((double) rs.getFloat("tamanho"));

			lista.add(backup);
		}

		s.close();
		c.close();

		return lista;
	}

	public static Backup buscaBackup(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();
		Backup backup = new Backup();
		backup.setPacotes(new LinkedHashSet<>());

		ResultSet rs = s.executeQuery("SELECT codPacote, nomePacote, tamPacote, dataGravacao FROM pacote p LEFT " +
				"NATURAL JOIN " +
				"backup WHERE p.codBackup=" + codigo + ";");
		while (rs.next()) {
			Pacote pacote = new Pacote();

			pacote.setCodigo(rs.getInt("codPacote"));
			pacote.setNome(rs.getString("nomePacote"));
			pacote.setTamanho((double) rs.getFloat("tamPacote"));

			if (backup.getDataGravacao() == null) {
				backup.setCodigo(codigo);
				backup.setDataGravacao(rs.getDate("dataGravacao"));
			}
			backup.getPacotes().add(pacote);
		}

		s.close();
		c.close();
		return backup;
	}

	public static void excluirBackup(int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		s.execute("DELETE FROM backup WHERE codBackup=" + codigo + ";");
		//limpa os pacotes
		s.execute("UPDATE pacote SET codBackup=NULL WHERE codBackup=" + codigo + ";");

		s.close();
		c.close();
	}

	public static void alteraBackup(Backup backup, int codigo) throws SQLException {
		Connection c = Conexao.getConnection();
		Statement s = c.createStatement();

		//prepara os itens para serem cadastrados
		Long data = backup.getDataGravacao().getTime();

		s.execute("UPDATE backup SET codBackup=" + backup.getCodigo() + ", dataGravacao=" + data + " WHERE codBackup="
				+ codigo + ";");
		//atualiza os pacotes
		for (Pacote pacote : backup.getPacotes())
			if (pacote.getCodigoBackup() == null)
				s.execute("UPDATE pacote SET codBackup = " + backup.getCodigo() + " WHERE codPacote=" + pacote
						.getCodigo() + ";");
			else //se for -1 (usuário removeu do backup)
				s.execute("UPDATE pacote SET codBackup = NULL WHERE codPacote=" + pacote.getCodigo() + ";");

		s.close();
		c.close();
	}
}
