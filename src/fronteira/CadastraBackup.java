package fronteira;

import entidade.Backup;
import entidade.Pacote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;

class CadastraBackup extends Cadastros {
	private Backup backup = new Backup();
	private int codBackup;

	private JTextField txtCodigo;
	private Date data = new Date(System.currentTimeMillis());
	private JTextField txtData;
	private LinkedHashSet<Pacote> listaPacotes = new LinkedHashSet<>();
	private DefaultTableModel mdlPacotes = new DefaultTableModel() {
		//torna as células não editáveis
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private JTable tblPacotes;
	private float tamanho;
	private JTextField txtTamanho;
	private JTextField txtItens;

	private CadastraBackup() {
		setTitulo("Cadastrar pacote");

		//campo código
		JPanel pnlCodigo = new JPanel(new GridBagLayout());
		JLabel lblCodigo = new JLabel("Código:");
		adicionaCampo(pnlCodigo, lblCodigo, false);
		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		pnlCodigo.setToolTipText("Código do backup (gerado automaticamente)");
		txtCodigo.setToolTipText("Código do backup (gerado automaticamente)");
		adicionaCampo(pnlCodigo, txtCodigo, true);
		adicionaCampo(pnlCampos, pnlCodigo, true);
		//campo data
		JPanel pnlData = new JPanel(new GridBagLayout());
		JLabel lblData = new JLabel("Data:");
		adicionaCampo(pnlData, lblData, false);
		//cria a máscara do campo de data
		javax.swing.text.MaskFormatter mskData;
		try {
			mskData = new javax.swing.text.MaskFormatter("##/##/####");
			txtData = new javax.swing.JFormattedTextField(mskData);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//preenche com a data atual
		txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
		//seleciona tudo ao clicar
		txtData.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				txtData.selectAll();
			}
		});
		txtData.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				super.focusLost(e);
				//testa se é uma data válida
				try {
					data = new SimpleDateFormat("dd/MM/yyyy").parse(txtData.getText());
					txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(data));
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(null, "Digite a data no formato DD/MM/AAAA", "Data inválida",
							JOptionPane.ERROR_MESSAGE);
					//preenche com a data atual
					txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())));
					data = new Date(System.currentTimeMillis());
				}
				testaCampos();
			}
		});
		adicionaCampo(pnlData, txtData, true);
		pnlData.setToolTipText("Insira a data que o backup foi gravado");
		txtData.setToolTipText("Insira a data que o backup foi gravado");
		adicionaCampo(pnlCampos, pnlData, true);
		//campo pacotes (botões)
		JPanel pnlPacotesBtn = new JPanel(new GridBagLayout());
		JButton btnAdicPacote = new JButton("Adicionar pacote");
		btnAdicPacote.addActionListener(e -> adicionaPacote());
		btnAdicPacote.setToolTipText("Adiciona um pacote ao backup");
		adicionaCampo(pnlPacotesBtn, btnAdicPacote, true);
		JButton btnRemovPacote = new JButton("Remover");
		btnRemovPacote.addActionListener(e -> removePacote());
		btnRemovPacote.setToolTipText("Remove o pacote selecionado");
		adicionaCampo(pnlPacotesBtn, btnRemovPacote, true);
		adicionaCampo(pnlCampos, pnlPacotesBtn, true);
		//campo pacotes (tabela)
		JPanel pnlPacotesTbl = new JPanel(new GridBagLayout());
		tblPacotes = new JTable(mdlPacotes);
		JScrollPane sclPacotes = new JScrollPane(tblPacotes);
		adicionaCampo(pnlPacotesTbl, sclPacotes, true);
		adicionaCampo(pnlCampos, pnlPacotesTbl, true);
		//dados do backup
		JPanel pnlDados = new JPanel(new GridBagLayout());
		JLabel lblTamanho = new JLabel("Tamanho:");
		adicionaCampo(pnlDados, lblTamanho, false);
		txtTamanho = new JTextField("00,000 GB");
		txtTamanho.setEditable(false);
		adicionaCampo(pnlDados, txtTamanho, true);
		JLabel lblItens = new JLabel("Itens:");
		adicionaCampo(pnlDados, lblItens, false);
		txtItens = new JTextField("00");
		txtItens.setEditable(false);
		adicionaCampo(pnlDados, txtItens, true);
		adicionaCampo(pnlCampos, pnlDados, true);

		//cria o cabeçalho da tabela
		mdlPacotes.addColumn("Código");
		mdlPacotes.addColumn("Nome");
		mdlPacotes.addColumn("Tamanho");
		tblPacotes.getColumn("Código").setMaxWidth(60);

		//centraliza a janela
		pack();
		setLocationRelativeTo(null);
	}

	private CadastraBackup(int codigo) {
		this();
		txtCodigo.setEditable(true);
		codBackup = codigo;

		try {
			backup = Backup.buscaBackup(codigo);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao carregar dados", JOptionPane.ERROR_MESSAGE);
			return;
		}

		txtCodigo.setText(String.valueOf(codigo));
		data = backup.getDataGravacao();
		txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(data));
		listaPacotes = backup.getPacotes();

		//preenche a tabela de pacotes
		for (Pacote pacote : listaPacotes) {
			mdlPacotes.addRow(new Object[]{pacote.getCodigo(), pacote.getNome(), new DecimalFormat("##.### MB").format
					(pacote.getTamanho())});
			//atualiza os dados
			if (pacote.getTamanho() != null)
				tamanho += pacote.getTamanho() / 1024;
		}

		testaCampos();
	}

	static void cadastra() {
		CadastraBackup janela = new CadastraBackup();
		janela.setVisible(true);
	}

	static void altera(int codigo) {
		CadastraBackup janela = new CadastraBackup(codigo);
		janela.setVisible(true);
	}

	private void adicionaPacote() {
		ArrayList<Pacote> pacotes = ConsultaPacote.seleciona();
		if (pacotes.isEmpty())
			return;

		//verifica se o pacote já pertence a outro backup
		Iterator<Pacote> i = pacotes.iterator();
		while (i.hasNext()) {
			Pacote pacote = i.next();
			if (pacote.getCodigoBackup() != null && !listaPacotes.contains(pacote)) {
				String[] opcoes = {"Sim", "Não"};
				if (JOptionPane.showOptionDialog(null, "O pacote " + pacote.getNome() + " já pertence ao " +
								"backup " + pacote
								.getCodigoBackup() + ". Adicionar a este\nbackup irá removê-lo do backup antigo.\n" +
								"Continuar?",
						"Adicionar " +
								"pacote", JOptionPane.YES_NO_OPTION, JOptionPane
								.QUESTION_MESSAGE, null, opcoes, opcoes[1]) != JOptionPane.YES_OPTION)
					i.remove();
				pacote.setCodigoBackup(null); //limpa o campo backup
			}
		}

		//adiciona os pacotes
		boolean repetido = true;
		for (Pacote pacote : pacotes)
			if (listaPacotes.add(pacote)) { //tenta adicionar
				mdlPacotes.addRow(new Object[]{pacote.getCodigo(), pacote.getNome(), /*pacote.getTamanho() == null ?
						null :*/ new DecimalFormat("##.### MB").format(pacote.getTamanho())}); //adiciona na tabela
				//atualiza os dados
				if (pacote.getTamanho() != null)
					tamanho += pacote.getTamanho() / 1024;
			} else if (repetido) { //avisa se houver um pacote repetido
				JOptionPane.showMessageDialog(null, "Um ou mais pacotes já foram adicionados a este backup", "Pacote" +
						" " +
						"já " +
						"adicionado", JOptionPane.INFORMATION_MESSAGE);
				repetido = false; //evita que o aviso seja exibido mais de uma vez
			}

		testaCampos();
	}

	private void removePacote() {
		int linhaSelecionada = tblPacotes.getSelectedRow();
		if (linhaSelecionada < 0) {
			JOptionPane.showMessageDialog(null, "Nenhum item selecionado", "Selecione o pacote que deseja remover",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int codigo = (int) mdlPacotes.getValueAt(linhaSelecionada, 0);


		for (Pacote pacote : listaPacotes) {
			//remove do backup (seta como -1)
			if (pacote.getCodigo() == codigo) {
				pacote.setCodigoBackup(-1);
				//atualiza os dados
				if (pacote.getTamanho() != null)
					tamanho -= pacote.getTamanho() / 1024;
			}
		}
		//remove da tabela
		mdlPacotes.removeRow(linhaSelecionada);

		testaCampos();
	}

	@Override
	void testaCampos() {
		if (mdlPacotes.getRowCount() > 0)
			btnSalvar.setEnabled(true);
		else
			btnSalvar.setEnabled(false);
		txtTamanho.setText(new DecimalFormat("##.### GB").format(tamanho));
		txtItens.setText(String.valueOf(mdlPacotes.getRowCount()));
	}

	@Override
	void onOK() {
		backup.setDataGravacao(data);
		backup.setPacotes(listaPacotes);

		try {
			if (backup.getCodigo() == null) //se for novo
				Backup.cadastraBackup(backup);
			else {
				backup.setCodigo(Integer.valueOf(txtCodigo.getText()));
				Backup.alteraBackup(backup, codBackup);
			}

			JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso", "Cadastrar backup", JOptionPane
					.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
			return;
		}

		dispose();
	}
}
