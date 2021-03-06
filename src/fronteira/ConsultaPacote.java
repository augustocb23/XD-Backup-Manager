package fronteira;

import entidade.Pacote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.ArrayList;

class ConsultaPacote extends Consultas {
	private DefaultTableModel modeloTabela = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private ArrayList<Pacote> lista;

	private ConsultaPacote() {
		setTitulo("Pacotes");

		//cria o cabeçalho da tabela
		tabelaConsulta.setModel(modeloTabela);
		modeloTabela.addColumn("Código");
		modeloTabela.addColumn("Nome");
		modeloTabela.addColumn("Tamanho");
		modeloTabela.addColumn("Backup");
		tabelaConsulta.getColumn("Código").setMaxWidth(60);

		onAtualizar();
	}

	static ArrayList<Pacote> seleciona() {
		ConsultaPacote janela = new ConsultaPacote();
		ArrayList<Pacote> pacotes = new ArrayList<>();

		//limpa o painel central e adiciona o botão Selecionar
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
				linha = janela.tabelaConsulta.convertRowIndexToModel(linha);
				Pacote pacote = null; //busca os dados do pacote selecionado no banco
				try {
					pacote = Pacote.buscaPacote((Integer) janela.modeloTabela.getValueAt(linha, 0));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				pacotes.add(pacote); //adiciona à lista
			}

			janela.setVisible(false);
		});
		janela.pnlCentral.add(btnSelecionar);

		//exibe a janela
		janela.onAtualizar();
		janela.setVisible(true);

		return pacotes;
	}

	@Override
	void filtraTabela() {
//declara a lista
		ArrayList<Pacote> listaFiltrada = new ArrayList<>();

		//busca no nome
		String termo = txtBusca.getText();
		int termoExt = termo.length(); // numero de caracteres a serem buscados
		for (Pacote pacote : lista) { // busca todos os itens
			int buscaExt = pacote.getNome().length(); // numero de caracteres
			for (int i = 0; i <= buscaExt - termoExt; i++) // busca pela string
				if (pacote.getNome().regionMatches(true, i, termo, 0, termoExt))
					if (!listaFiltrada.contains(pacote)) //se encontrou, adiciona na lista
						listaFiltrada.add(pacote);
		}

		//imprime os resultados na tabela
		modeloTabela.setNumRows(0);
		for (Pacote pacote : listaFiltrada)
			modeloTabela.addRow(new Object[]{pacote.getCodigo(), pacote.getNome(), new DecimalFormat("##.### MB")
					.format(pacote.getTamanho())});
	}

	@Override
	void onAtualizar() {
		try {
			lista = Pacote.listaPacotes();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao buscar dados", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//preenche a tabela
		modeloTabela.setNumRows(0);
		for (Pacote pacote : lista)
			modeloTabela.addRow(new Object[]{pacote.getCodigo(), pacote.getNome(), new DecimalFormat("##.### MB")
					.format(pacote.getTamanho()), pacote.getCodigoBackup()});
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
			Pacote.excluirPacote((int) modeloTabela.getValueAt(linhaSelecionada, 0));
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
		CadastraPacote.cadastra();
		onAtualizar();
	}
}
