package entidade;

import persistencia.AtorDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class Ator {
	private Integer codigo;
	private String nome;

	public Ator(Integer codigo, String nome) {
		setCodigo(codigo);
		setNome(nome);
	}

	public Ator() {

	}

	public static void cadastrarAtor(String nome) throws Exception {
		try {
			if (nome.length() > 255)
				nome = nome.substring(0, 254);
			AtorDAO.cadastrarAtor(nome.toUpperCase());
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

	public static ArrayList<Ator> buscaAtores() throws Exception {
		ArrayList<Ator> lista;
		try {
			lista = AtorDAO.buscaAtores();
			if (lista.isEmpty())
				throw new Exception("Nenhum item encontrado");
			return lista;
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void apagaAtor(int codigo) throws Exception {
		try {
			AtorDAO.apagaAtor(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void renomearAtor(int codigo, String nome) throws Exception {
		try {
			if (nome.length() > 255)
				nome = nome.substring(0, 254);
			AtorDAO.renomearAtor(codigo, nome.toUpperCase());
		} catch (SQLException e) {
			//se o nome já está cadastrado
			if (e.getErrorCode() == 19)
				throw new Exception("Nome já está em uso");

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
