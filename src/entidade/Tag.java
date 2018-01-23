package entidade;

import controle.TagDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Tag {
	private Integer codigo;
	private String nome;

	public Tag() {
	}

	public Tag(Integer codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	public static void cadastrarTag(String nome) throws Exception {
		nome = nome.toUpperCase();

		try {
			TagDAO.cadastrarTag(nome);
		} catch (SQLException e) {
			//se o nome já está cadastrado
			if (e.getErrorCode() == 19)
				throw new Exception("Nome já está em uso");

			//se for outro erro
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static ArrayList<Tag> buscaTags() throws Exception {
		ArrayList<Tag> lista;
		try {
			lista = TagDAO.buscaTags();
			if (lista.isEmpty())
				throw new Exception("Nenhum item encontrado");
			return lista;
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void apagaTag(int codigo) throws Exception {
		try {
			TagDAO.apagaTag(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Tag tag = (Tag) o;
		return Objects.equals(codigo, tag.codigo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
