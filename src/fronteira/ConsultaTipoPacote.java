package fronteira;

import entidade.TipoPacote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

class ConsultaTipoPacote extends Consultas {
	private DefaultTableModel modeloTabela = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private ArrayList<TipoPacote> lista;

	private ConsultaTipoPacote() {
		setTitulo("Tipos de Pacotes");

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
		ConsultaTipoPacote janela = new ConsultaTipoPacote();
		janela.onAtualizar();
		janela.setVisible(true);
	}

	static TipoPacote seleciona() {
		ConsultaTipoPacote janela = new ConsultaTipoPacote();
		TipoPacote tipoPacote = new TipoPacote();

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

			tipoPacote.setCodigo((Integer) janela.modeloTabela.getValueAt(linhaSeleciona, 0));
			tipoPacote.setNome(String.valueOf(janela.modeloTabela.getValueAt(linhaSeleciona, 1)));

			janela.setVisible(false);
		});
		janela.pnlCentral.add(btnSelecionar);

		//exibe a janela
		janela.onAtualizar();
		janela.setVisible(true);

		return tipoPacote.getNome() == null ? null : tipoPacote;
	}

	@Override
	void filtraTabela() {
//declara a lista
		ArrayList<TipoPacote> listaFiltrada = new ArrayList<>();

		//busca no nome
		String termo = txtBusca.getText();
		int termoExt = termo.length(); // numero de caracteres a serem buscados
		for (TipoPacote tipoPacote : lista) { // busca todos os itens
			int buscaExt = tipoPacote.getNome().length(); // numero de caracteres
			for (int i = 0; i <= buscaExt - termoExt; i++) // busca pela string
				if (tipoPacote.getNome().regionMatches(true, i, termo, 0, termoExt))
					if (!listaFiltrada.contains(tipoPacote)) //se encontrou, adiciona na lista
						listaFiltrada.add(tipoPacote);
		}

		//imprime os resultados na tabela
		modeloTabela.setNumRows(0);
		for (TipoPacote tipoPacote : listaFiltrada)
			modeloTabela.addRow(new Object[]{tipoPacote.getCodigo(), tipoPacote.getNome()});
	}

	@Override
	void onAtualizar() {
		try {
			lista = TipoPacote.listaTiposPacote();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao buscar dados", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//preenche a tabela
		modeloTabela.setNumRows(0);
		for (TipoPacote tipoPacote : lista)
			modeloTabela.addRow(new Object[]{tipoPacote.getCodigo(), tipoPacote.getNome()});
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
			TipoPacote.apagaTipoPacote((int) modeloTabela.getValueAt(linhaSelecionada, 0));
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
		CadastraTipoPacote.cadastra();
		onAtualizar();
	}

	private void renomear() {
		int linhaSelecionada = tabelaConsulta.getSelectedRow();
		//verifica se não há nenhuma linha selecionada
		if (linhaSelecionada == -1) {
			JOptionPane.showMessageDialog(null, "Selecione um item da lista", "Alterar cadastro", JOptionPane
					.INFORMATION_MESSAGE);
			return;
		}

		//solicita o novo nome para alterar
		String nome = CadastraTipoPacote.renomear(String.valueOf(modeloTabela.getValueAt(linhaSelecionada, 1)));

		//altera o nome no banco de dados
		try {
			TipoPacote.renomearTipoPacote((int) modeloTabela.getValueAt(linhaSelecionada, 0), nome);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao alterar cadastro", JOptionPane.ERROR_MESSAGE);
		}

		onAtualizar();
	}
}
