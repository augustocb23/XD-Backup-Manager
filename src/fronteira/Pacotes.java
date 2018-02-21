package fronteira;

import controle.Conexao;
import entidade.Backup;
import entidade.Pacote;
import entidade.Tag;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Pacotes {
	private final static String titulo = "XD Backup Manager";
	private final static String icone = "backups.png";
	private static DefaultTableModel mdlPacotes = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private static DefaultTableModel mdlBackups = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private ArrayList<Pacote> listaPacotes = new ArrayList<>();
	private JTextField txtBusca;
	private JTable tblPacotes;
	private JButton atoresButton;
	private JComboBox<Integer> cmbBackups;
	private JPanel principal;
	private JButton tagsButton;
	private JTable tblBackups;
	private JButton detalhesButton;
	private JButton excluirBackupButton;
	private JButton atualizarButton;
	private JButton novoButton;
	private JButton alterarButton;
	private JButton novoBackupButton;
	private JButton tiposButton;
	private JButton excluirPacoteButton;

	private Pacotes() {
		//define os dados da janela
		JFrame janela = new JFrame();
		janela.setSize(800, 600);
		janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		janela.setContentPane(principal);
		janela.setLocationRelativeTo(null);
		janela.setTitle(getTitulo());
		janela.setIconImage(Toolkit.getDefaultToolkit().getImage(Pacotes.getIcone()));

		//cria o cabeçalho para a tabela de pacotes
		tblPacotes.setModel(mdlPacotes);
		mdlPacotes.addColumn("Código");
		mdlPacotes.addColumn("Nome");
		mdlPacotes.addColumn("Tipo");
		mdlPacotes.addColumn("Ator");
		mdlPacotes.addColumn("Data");
		mdlPacotes.addColumn("Tamanho");
		mdlPacotes.addColumn("Backup");
		mdlPacotes.addColumn("Tags");
		tblPacotes.getColumn("Código").setMaxWidth(60);

		//cria o cabeçalho para a tabela de backups
		tblBackups.setModel(mdlBackups);
		mdlBackups.addColumn("Código");
		mdlBackups.addColumn("Data de Gravação");
		mdlBackups.addColumn("Tamanho total");
		mdlBackups.addColumn("Pacotes");
		tblBackups.getColumn("Código").setMaxWidth(60);

		//cria os listeners dos botões
		atoresButton.addActionListener(e -> ConsultaAtor.consulta());

		janela.setVisible(true);
		txtBusca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				super.keyTyped(e);
				if (txtBusca.getText().isEmpty())
					onAtualizar();
				else
					filtraTabelas();
			}
		});
		atualizarButton.addActionListener(e -> onAtualizar());
		novoButton.addActionListener(e -> {
			CadastraPacote.cadastra();
			onAtualizar();
		});
		tiposButton.addActionListener(e -> ConsultaTipoPacote.consulta());
		alterarButton.addActionListener(e -> {
			onAlterarPacote();
			onAtualizar();
		});
		excluirPacoteButton.addActionListener(e -> {
			onExcluirPacote();
			onAtualizar();
		});
		novoBackupButton.addActionListener(e -> {
			CadastraBackup.cadastra();
			onAtualizar();
		});
		tagsButton.addActionListener(e -> onTags());
		excluirBackupButton.addActionListener(e -> {
			onExcluirBackup();
			onAtualizar();
		});
		cmbBackups.addActionListener(e -> {
			if (cmbBackups.getSelectedItem() == null)
				onAtualizar();
			else
				filtraPacotes();
		});
		detalhesButton.addActionListener(e -> {
			onDetalhesBackup();
			onAtualizar();
		});
	}

	public static void main(String[] args) {
		//seleciona o tema Nimbus
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
		//cria o banco caso este não exista
		try {
			Conexao.start();
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Inicializando banco de dados", JOptionPane
					.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage() + "\n\nO programa será fechado.", "Erro ao inicializar " +
					"o " +
					"banco de dados", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		Pacotes pacotes = new Pacotes();
		mdlPacotes.addRow(new Object[]{"", "Carregando..."});
		pacotes.onAtualizar();
	}

	//métodos para a aba pacotes
	private void onAlterarPacote() {
		if (nenhumItem("pacotes"))
			return;
		int linhaSelecionada = tblPacotes.getSelectedRow();

		CadastraPacote.altera((int) mdlPacotes.getValueAt(linhaSelecionada, 0));
	}

	private void filtraPacotes() {
		ArrayList<Pacote> listaFiltrada = listaPacotes;

		//filtra de acordo o backup selecionado
		listaFiltrada.removeIf((Pacote pacote) -> pacote.getCodigoBackup() != cmbBackups.getSelectedItem());

		//preenche a tabela de pacotes
		mdlPacotes.setNumRows(0);
		preencheTabela(listaFiltrada);
	}

	private void onExcluirPacote() {
		if (nenhumItem("pacotes"))
			return;
		int linhaSelecionada = tblPacotes.getSelectedRow();

		//confirma a exclusão
		String[] opcoes = {"Sim", "Não"};
		if (JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir " + mdlPacotes.getValueAt
				(linhaSelecionada, 1) + "?", "Excluir cadastro", JOptionPane.YES_NO_OPTION, JOptionPane
				.QUESTION_MESSAGE, null, opcoes, opcoes[1]) == JOptionPane.NO_OPTION)
			return;

		try {
			Pacote.excluirPacote((int) mdlPacotes.getValueAt(linhaSelecionada, 0));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao excluir cadastro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//exibe a confirmação
		JOptionPane.showMessageDialog(null, "Cadastro excluído com sucesso.",
				"Cadastro apagado", JOptionPane.INFORMATION_MESSAGE);
	}

	//métodos para a aba backups
	private void onExcluirBackup() {
		if (nenhumItem("backups"))
			return;
		int linhaSelecionada = tblBackups.getSelectedRow();

		//confirma a exclusão
		String[] opcoes = {"Sim", "Não"};
		if (JOptionPane.showOptionDialog(null, "Tem certeza que deseja excluir o backup " + mdlBackups.getValueAt
						(linhaSelecionada, 0) + " de " + mdlBackups.getValueAt
						(linhaSelecionada, 1) + "?\nIsto não apagará os pacotes associados a ele.", "Excluir cadastro",
				JOptionPane.YES_NO_OPTION, JOptionPane
						.QUESTION_MESSAGE, null, opcoes, opcoes[1]) == JOptionPane.NO_OPTION)
			return;

		try {
			Backup.excluirBackup((int) mdlBackups.getValueAt(linhaSelecionada, 0));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao excluir cadastro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//exibe a confirmação
		JOptionPane.showMessageDialog(null, "Cadastro excluído com sucesso.",
				"Cadastro apagado", JOptionPane.INFORMATION_MESSAGE);
	}

	private void onDetalhesBackup() {
		if (nenhumItem("backups"))
			return;
		int linhaSelecionada = tblBackups.getSelectedRow();

		CadastraBackup.altera((int) mdlBackups.getValueAt(linhaSelecionada, 0));
	}

	//métodos para ambas as abas
	private void onTags() {
		ConsultaTag.consulta();
		onAtualizar();
	}

	private void onAtualizar() {
		//busca as listas no banco
		ArrayList<Backup> listaBackups;
		try {
			listaPacotes = Pacote.listaPacotes();
			listaBackups = Backup.listaBackups();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//preenche a tabela de pacotes
		mdlPacotes.setNumRows(0);
		preencheTabela(listaPacotes);
		//preenche a ComboBox
		cmbBackups.removeAllItems();
		cmbBackups.addItem(null);
		//preenche a tabela de backups
		mdlBackups.setNumRows(0);
		for (Backup backup : listaBackups) {
			cmbBackups.addItem(backup.getCodigo());
			mdlBackups.addRow(new Object[]{backup.getCodigo(), new
					SimpleDateFormat("dd/MM/yyyy").format(backup
					.getDataGravacao()), new DecimalFormat("##.### GB").format(backup.getTamanho()), backup
					.getTotPacotes()});
		}
	}

	private void filtraTabelas() {
		LinkedHashSet<Pacote> listaFiltrada = new LinkedHashSet<>();

		String termo = txtBusca.getText();
		int termoExt = termo.length(); // numero de caracteres a serem buscados
		for (Pacote pacote : listaPacotes) { // busca todos os itens
			//busca no nome
			int nomePacExt = pacote.getNome().length(); // numero de caracteres
			for (int i = 0; i <= nomePacExt - termoExt; i++) // busca pela string
				if (pacote.getNome().regionMatches(true, i, termo, 0, termoExt))
					listaFiltrada.add(pacote);
			//busca no tipo
			if (pacote.getTipo().getNome() != null) {
				int nomeTipoExt = pacote.getTipo().getNome().length();
				for (int i = 0; i <= nomeTipoExt - termoExt; i++) // busca pela string
					if (pacote.getTipo().getNome().regionMatches(true, i, termo, 0, termoExt))
						listaFiltrada.add(pacote);
			}
			//busca no ator
			if (pacote.getAtor().getNome() != null) {
				int nomeAtorExt = pacote.getAtor().getNome().length();
				for (int i = 0; i <= nomeAtorExt - termoExt; i++) // busca pela string
					if (pacote.getAtor().getNome().regionMatches(true, i, termo, 0, termoExt))
						listaFiltrada.add(pacote);
			}
			//busca nas tags
			for (Tag tag : pacote.getTags()) {
				int nomeTagExt = tag.getNome().length();
				for (int i = 0; i <= nomeTagExt - termoExt; i++) // busca pela string
					if (tag.getNome().regionMatches(true, i, termo, 0, termoExt))
						listaFiltrada.add(pacote);
			}
		}

		mdlPacotes.setNumRows(0);
		preencheTabela(listaFiltrada);
	}

	private void preencheTabela(Collection<Pacote> lista) {
		for (Pacote pacote : lista) {
			StringBuilder tags = new StringBuilder();
			for (Tag tag : pacote.getTags())
				tags.append(tag.getNome()).append("; ");
			mdlPacotes.addRow(new Object[]{pacote.getCodigo(), pacote.getNome(), pacote.getTipo() == null ? null :
					pacote.getTipo().getNome(), pacote.getAtor() == null ? null : pacote.getAtor().getNome(), new
					SimpleDateFormat("dd/MM/yyyy").format(pacote
					.getData()),
					new DecimalFormat("##.### GB").format(pacote.getTamanho()), pacote.getCodigoBackup(), tags});
		}
	}

	//métodos secundários
	//verifica se há um item selecionado na tabela
	private boolean nenhumItem(String tabela) {
		boolean nenhum = false;
		switch (tabela) {
			case "pacotes":
				if (tblPacotes.getSelectedRow() == -1)
					nenhum = true;
				break;
			case "backups":
				if (tblBackups.getSelectedRow() == -1)
					nenhum = true;
				break;
		}

		if (nenhum) {
			JOptionPane.showMessageDialog(null, "Selecione um item da lista.", "Nenhum item selecionado", JOptionPane
					.INFORMATION_MESSAGE);
			return true;
		}
		return false;
	}

	static String getIcone() {
		return icone;
	}

	static String getTitulo() {
		return titulo;
	}
}
