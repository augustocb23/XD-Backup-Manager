package entidade;

import controle.PacoteDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;

public class Pacote {
	private Integer codigo;
	private String nome;
	private Ator ator;
	private Date data;
	private Double tamanho;
	private TipoPacote tipo;
	private Integer codigoBackup;
	private LinkedHashSet<Tag> tags;

	public static void cadastraPacote(Pacote pacote) throws Exception {
		pacote.nome = pacote.nome.toUpperCase();

		try {
			PacoteDAO.cadastraPacote(pacote);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static ArrayList<Pacote> listaPacotes() throws Exception {
		ArrayList<Pacote> lista;

		try {
			lista = PacoteDAO.listaPacotes();
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return lista;
	}

	public static Pacote buscaPacote(int codigo) throws Exception {
		Pacote pacote;
		try {
			pacote = PacoteDAO.buscaPacote(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		if (pacote == null)
			throw new Exception("Pacote " + codigo + " não encontrado.");
		return pacote;
	}

	public static void alteraPacote(Pacote pacote) throws Exception {
		pacote.nome = pacote.nome.toUpperCase();

		try {
			PacoteDAO.alteraPacote(pacote);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void excluirPacote(int codigo) throws Exception {
		try {
			PacoteDAO.excluirPacote(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	//define que um pacote é igual a outro caso possuam o mesmo código
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pacote pacote = (Pacote) o;
		return Objects.equals(codigo, pacote.codigo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	public Ator getAtor() {
		return ator;
	}

	public void setAtor(Ator ator) {
		this.ator = ator;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigoBackup() {
		return codigoBackup;
	}

	public void setCodigoBackup(Integer codigoBackup) {
		this.codigoBackup = codigoBackup;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LinkedHashSet<Tag> getTags() {
		return tags;
	}

	public void setTags(LinkedHashSet<Tag> tags) {
		this.tags = tags;
	}

	public Double getTamanho() {
		return tamanho;
	}

	public void setTamanho(Double tamanho) {
		this.tamanho = tamanho;
	}

	public TipoPacote getTipo() {
		return tipo;
	}

	public void setTipo(TipoPacote tipo) {
		this.tipo = tipo;
	}
}
