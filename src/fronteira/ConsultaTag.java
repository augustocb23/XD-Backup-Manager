package fronteira;

import entidade.Tag;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ConsultaTag extends Consultas {
	private DefaultTableModel modeloTabela = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private ArrayList<Tag> lista;

	private ConsultaTag() {
		setTitulo("Tags");

		//cria a tabela
		tabelaConsulta.setModel(modeloTabela);
		modeloTabela.addColumn("Código");
		modeloTabela.addColumn("Nome");
		tabelaConsulta.getColumn("Código").setMaxWidth(60);
	}

	static void consulta() {
		ConsultaTag janela = new ConsultaTag();
		janela.onAtualizar();
		janela.setVisible(true);
	}

	static ArrayList<Tag> seleciona() {
		ConsultaTag janela = new ConsultaTag();
		ArrayList<Tag> tags = new ArrayList<>();

		//limpa o painel central e adiciona o botão Selecionar
		janela.pnlCentral.removeAll();
		JButton btnSelecionar = new JButton("Selecionar");
		btnSelecionar.setMnemonic('S');
		btnSelecionar.addActionListener(e -> {
			//tenta obter a linha selecionada
			int[] linhasSelecionadas = janela.tabelaConsulta.getSelectedRows();
			if (linhasSelecionadas.length == 0) {
				JOptionPane.showMessageDialog(null, "Selecione um item da lista.", "Selecionar item", JOptionPane
						.ERROR_MESSAGE);
				return;
			}

			for (int linha : linhasSelecionadas) {
				Tag tag = new Tag();
				tag.setCodigo((Integer) janela.modeloTabela.getValueAt(linha, 0));
				tag.setNome(String.valueOf(janela.modeloTabela.getValueAt(linha, 1)));

				tags.add(tag);
			}

			janela.setVisible(false);
		});
		janela.pnlCentral.add(btnSelecionar);

		//exibe a janela
		janela.onAtualizar();
		janela.setVisible(true);

		//retorna o item seleciona ou null
		return tags;
	}

	@Override
	void filtraTabela() {
//declara a lista
		ArrayList<Tag> listaFiltrada = new ArrayList<>();

		//busca no nome
		String termo = txtBusca.getText();
		int termoExt = termo.length(); // numero de caracteres a serem buscados
		for (Tag tag : lista) { // busca todos os itens
			int buscaExt = tag.getNome().length(); // numero de caracteres
			for (int i = 0; i <= buscaExt - termoExt; i++) // busca pela string
				if (tag.getNome().regionMatches(true, i, termo, 0, termoExt))
					if (!listaFiltrada.contains(tag)) //se encontrou, adiciona na lista
						listaFiltrada.add(tag);
		}

		//imprime os resultados na tabela
		modeloTabela.setNumRows(0);
		for (Tag tag : listaFiltrada)
			modeloTabela.addRow(new Object[]{tag.getCodigo(), tag.getNome()});
	}

	@Override
	void onAtualizar() {
		try {
			lista = Tag.buscaTags();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao buscar dados", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//preenche a tabela
		modeloTabela.setNumRows(0);
		for (Tag tag : lista)
			modeloTabela.addRow(new Object[]{tag.getCodigo(), tag.getNome()});
	}

	@Override
	void onExcluir() {
		int linhaSelecionada = tabelaConsulta.getSelectedRow();
		//verifica se não há nenhuma linha selecionada
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(null, "Selecione um item da lista", "Excluir cadastro", JOptionPane
					.INFORMATION_MESSAGE);
			return;
		}
		//confirma a exclusão do cadastro
		String[] opcoes = {"Sim", "Não"};
		if (JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir " + modeloTabela.getValueAt
				(linhaSelecionada, 1) + "?", "Excluir cadastro", JOptionPane.YES_NO_OPTION, JOptionPane
				.QUESTION_MESSAGE, null, opcoes, opcoes[1]) == JOptionPane.NO_OPTION)
			return;
		try {
			Tag.apagaTag((int) modeloTabela.getValueAt(linhaSelecionada, 0));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao excluir cadastro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//exibe a confirmação
		JOptionPane.showMessageDialog(null, modeloTabela.getValueAt(linhaSelecionada, 1) + " apagadado com sucesso.",
				"Cadastro apagado", JOptionPane.INFORMATION_MESSAGE);

		onAtualizar();
	}

	@Override
	void onNovo() {
		CadastraTag.cadastra();
		onAtualizar();
	}
}
