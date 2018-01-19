package entidade;

import controle.TipoPacoteDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class TipoPacote {
	private int codigo;
	private String nome;

	public TipoPacote(int codigo, String nome) {
		setCodigo(codigo);
		setNome(nome);
	}

	public TipoPacote() {

	}

	public static void cadastrarTipo(String nome) throws Exception {
		try {
			if (nome.length() > 255)
				nome = nome.substring(0, 254);
			TipoPacoteDAO.cadastrarAtor(nome.toUpperCase());
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

	public static ArrayList<TipoPacote> listaTiposPacote() throws Exception {
		ArrayList<TipoPacote> lista;
		try {
			lista = TipoPacoteDAO.buscaTiposPacote();
			if (lista.isEmpty())
				throw new Exception("Nenhum item encontrado");
			return lista;
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void apagaTipoPacote(int codigo) throws Exception {
		try {
			TipoPacoteDAO.apagaTipoPacote(codigo);
		} catch (SQLException e) {
			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void renomearTipoPacote(int codigo, String nome) throws Exception {
		try {
			if (nome.length() > 255)
				nome = nome.substring(0, 254);
			TipoPacoteDAO.renomearTipoPacote(codigo, nome.toUpperCase());
		} catch (SQLException e) {
			//se o nome já está cadastrado
			if (e.getErrorCode() == 19)
				throw new Exception("Nome já está em uso");

			System.out.println("Código:" + e.getErrorCode());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
