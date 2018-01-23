package entidade;

import controle.BackupDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;

public class Backup {
	private Integer codigo;
	private Date dataGravacao;
	private LinkedHashSet<Pacote> pacotes;
	private int totPacotes;
	private double tamanho;

	public static void cadastraBackup(Backup backup) throws Exception {
		try {
			BackupDAO.cadastraBackup(backup);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static ArrayList<Backup> listaBackups() throws Exception {
		ArrayList<Backup> lista;

		try {
			lista = BackupDAO.listaBackup();
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return lista;
	}

	public static void excluirBackup(int codigo) throws Exception {
		try {
			BackupDAO.excluirBackup(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static Backup buscaBackup(int codigo) throws Exception {
		Backup backup;
		try {
			backup = BackupDAO.buscaBackup(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return backup;
	}

	public static void alteraBackup(Backup backup, int codigo) throws Exception {
		try {
			BackupDAO.alteraBackup(backup, codigo);
		} catch (SQLException e) {
			//código já está em uso
			if (e.getErrorCode() == 19)
				throw new Exception("Código do backup já está em uso");

			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public LinkedHashSet<Pacote> getPacotes() {
		return pacotes;
	}

	public void setPacotes(LinkedHashSet<Pacote> pacotes) {
		this.pacotes = pacotes;
	}

	public double getTamanho() {
		return tamanho;
	}

	public void setTamanho(double tamanho) {
		this.tamanho = tamanho;
	}

	public int getTotPacotes() {
		return totPacotes;
	}

	public void setTotPacotes(int totPacotes) {
		this.totPacotes = totPacotes;
	}
}
