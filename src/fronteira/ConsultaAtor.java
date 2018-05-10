package fronteira;

import entidade.Ator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

class ConsultaAtor extends Consultas {
	private DefaultTableModel modeloTabela = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private ArrayList<Ator> lista;

	private ConsultaAtor() {
		setTitulo("Atores");

		//cria a tabela
		tabelaConsulta.setModel(modeloTabela);
		modeloTabela.addColumn("Código");
		modeloTabela.addColumn("Nome");
		tabelaConsulta.getColumn("Código").setMaxWidth(60);

		//cria o botão Renomear
		JButton btnRenomear = new JButton("Renomear");
		btnRenomear.addActionListener((ActionEvent event) -> renomear());
		btnRenomear.setMnemonic('R');
		pnlCentral.add(btnRenomear);
	}

	static void consulta() {
		ConsultaAtor janela = new ConsultaAtor();
		janela.onAtualizar();
		janela.setVisible(true);
	}

	static Ator seleciona() {
		ConsultaAtor janela = new ConsultaAtor();
		Ator ator = new Ator();

		//limpa o painel central e adiciona o botão Selecionar
		janela.pnlCentral.removeAll();
		JButton btnSelecionar = new JButton("Selecionar");
		btnSelecionar.setMnemonic('S');
		btnSelecionar.addActionListener(e -> {
			//tenta obter a linha selecionada
			int linhaSeleciona = janela.tabelaConsulta.getSelectedRow();
			if (linhaSeleciona == -1) {
				JOptionPane.showMessageDialog(null, "Selecione um item da lista.", "Selecionar item", JOptionPane
						.ERROR_MESSAGE);
				return;
			}
			linhaSeleciona = janela.tabelaConsulta.convertRowIndexToModel(linhaSeleciona);

			ator.setCodigo((Integer) janela.modeloTabela.getValueAt(linhaSeleciona, 0));
			ator.setNome(String.valueOf(janela.modeloTabela.getValueAt(linhaSeleciona, 1)));

			janela.setVisible(false);
		});
		janela.pnlCentral.add(btnSelecionar);

		//exibe a janela
		janela.onAtualizar();
		janela.setVisible(true);

		//retorna o item seleciona ou null
		return ator.getNome() == null ? null : ator;
	}

	//renomear ator
	private void renomear() {
		int linhaSelecionada = tabelaConsulta.getSelectedRow();
		//verifica se não há nenhuma linha selecionada
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(null, "Selecione um item da lista", "Alterar cadastro", JOptionPane
					.INFORMATION_MESSAGE);
			return;
		}
		linhaSelecionada = tabelaConsulta.convertRowIndexToModel(linhaSelecionada);

		//solicita o novo nome para alterar
		String nome = CadastraAtor.renomear(String.valueOf(modeloTabela.getValueAt(linhaSelecionada, 1)));

		//altera o nome no banco de dados
		try {
			Ator.renomearAtor((int) modeloTabela.getValueAt(linhaSelecionada, 0), nome);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao alterar cadastro", JOptionPane.ERROR_MESSAGE);
		}

		onAtualizar();
	}

	@Override
	void filtraTabela() {
		//declara a lista
		ArrayList<Ator> listaFiltrada = new ArrayList<>();

		//busca no nome
		String termo = txtBusca.getText();
		int termoExt = termo.length(); // numero de caracteres a serem buscados
		for (Ator ator : lista) { // busca todos os itens
			int buscaExt = ator.getNome().length(); // numero de caracteres
			for (int i = 0; i <= buscaExt - termoExt; i++) // busca pela string
				if (ator.getNome().regionMatches(true, i, termo, 0, termoExt))
					if (!listaFiltrada.contains(ator)) //se encontrou, adiciona na lista
						listaFiltrada.add(ator);
		}

		//imprime os resultados na tabela
		modeloTabela.setNumRows(0);
		for (Ator ator : listaFiltrada)
			modeloTabela.addRow(new Object[]{ator.getCodigo(), ator.getNome()});
	}

	@Override
	void onAtualizar() {
		try {
			lista = Ator.buscaAtores();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao buscar dados", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//preenche a tabela
		modeloTabela.setNumRows(0);
		for (Ator ator : lista)
			modeloTabela.addRow(new Object[]{ator.getCodigo(), ator.getNome()});
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
		linhaSelecionada = tabelaConsulta.convertRowIndexToModel(linhaSelecionada);
		//confirma a exclusão do cadastro
		String[] opcoes = {"Sim", "Não"};
		if (JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir " + modeloTabela.getValueAt
				(linhaSelecionada, 1) + "?", "Excluir cadastro", JOptionPane.YES_NO_OPTION, JOptionPane
				.QUESTION_MESSAGE, null, opcoes, opcoes[1]) != JOptionPane.YES_OPTION)
			return;
		try {
			Ator.apagaAtor((int) modeloTabela.getValueAt(linhaSelecionada, 0));
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
		CadastraAtor.cadastra();
		onAtualizar();
	}
}
