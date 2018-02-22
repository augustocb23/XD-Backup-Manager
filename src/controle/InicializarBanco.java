package controle;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static controle.Conexao.getConnection;

public class InicializarBanco {
	public static void start() throws Exception {
		File file = new File(Conexao.getDB());
		boolean novoArquivo = !file.exists(); //testa se o banco de dados existe

		try (Connection c = getConnection(); Statement s = c.createStatement()) {
			if (novoArquivo) {
				//cria um novo banco de dados
				s.executeUpdate("CREATE TABLE ator" +
						"(" +
						"  codAtor  INTEGER" +
						"    PRIMARY KEY" +
						"  AUTOINCREMENT," +
						"  nomeAtor VARCHAR(255) NOT NULL" +
						");" +
						"" +
						"CREATE UNIQUE INDEX Cliente_nomeCliente_uindex" +
						"  ON ator (nomeAtor);" +
						"" +
						"CREATE TABLE backup" +
						"(" +
						"  codBackup    INTEGER" +
						"    PRIMARY KEY" +
						"  AUTOINCREMENT," +
						"  dataGravacao DATE" +
						");" +
						"" +
						"CREATE TABLE pacote" +
						"(" +
						"  codPacote     INTEGER" +
						"    PRIMARY KEY" +
						"  AUTOINCREMENT," +
						"  nomePacote    VARCHAR(255) NOT NULL," +
						"  codAtor       INTEGER" +
						"    CONSTRAINT Pacote_Ator_codAtor_fk" +
						"    REFERENCES ator (codAtor)" +
						"      ON UPDATE CASCADE" +
						"      ON DELETE SET NULL," +
						"  dataCriacao   DATE         NOT NULL," +
						"  tamPacote     FLOAT," +
						"  codBackup     INTEGER" +
						"    CONSTRAINT Pacote_Backup_codBackup_fk" +
						"    REFERENCES backup (codBackup)" +
						"      ON UPDATE CASCADE" +
						"      ON DELETE SET NULL," +
						"  codTipoPacote INTEGER" +
						"    CONSTRAINT Pacote_TipoPacote_codTipoPacote_fk" +
						"    REFERENCES tipoPacote (codTipoPacote)" +
						");" +
						"" +
						"CREATE TABLE tags" +
						"(" +
						"  codTag  INTEGER" +
						"    PRIMARY KEY" +
						"  AUTOINCREMENT," +
						"  nomeTag VARCHAR(255) NOT NULL" +
						");\n" +
						"" +
						"CREATE UNIQUE INDEX Tags_nomeTag_uindex" +
						"  ON tags (nomeTag);" +
						"" +
						"CREATE TABLE tags_pacote" +
						"(" +
						"  codPacote INTEGER" +
						"    CONSTRAINT TagsXpacote_Pacote_codPacote_fk" +
						"    REFERENCES pacote (codPacote)" +
						"      ON UPDATE CASCADE" +
						"      ON DELETE CASCADE," +
						"  codTag    INTEGER" +
						"    CONSTRAINT TagsXpacote_Tags_codTag_fk" +
						"    REFERENCES tags (codTag)" +
						"      ON UPDATE CASCADE" +
						"      ON DELETE CASCADE" +
						");" +
						"" +
						"CREATE TABLE tipoPacote" +
						"(" +
						"  codTipoPacote  INTEGER" +
						"    PRIMARY KEY" +
						"  AUTOINCREMENT," +
						"  nomeTipoPacote VARCHAR(255) NOT NULL" +
						");" +
						"" +
						"CREATE UNIQUE INDEX TipoPacote_nomeTipoPacote_uindex" +
						"  ON tipoPacote (nomeTipoPacote);" +
						"");
				//informa que um novo arquivo foi gerado
				throw new RuntimeException("Não foi possível localizar o banco de dados.\nUm novo arquivo foi gerado" +
						".");
			}
			if (!file.canWrite())
				//verifica se o arquivo existente é somente leitura
				throw new Exception("O banco de dados é somente leitura.\n" + "Verifique se você tem permissão para " +
						"acessar\n" + Conexao.getDB());
		} catch (SQLException e) {
			//caso não consiga criar um novo banco de dados
			e.printStackTrace();
			throw new Exception("Não foi possível acessar o banco de dados:\n" + e.getMessage());
		}
	}
}
