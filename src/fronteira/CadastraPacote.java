package fronteira;

import entidade.Ator;
import entidade.Pacote;
import entidade.Tag;
import entidade.TipoPacote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;

class CadastraPacote extends Cadastros {
	private Pacote pacote = new Pacote();

	private JTextField txtNome;
	private Ator ator;
	private JTextField txtAtor;
	private Date data = new Date(System.currentTimeMillis());
	private JTextField txtData;
	private JSpinner spnTamanho;
	private TipoPacote tipoPacote;
	private JTextField txtTipo;
	private JList<String> lstTags;
	private LinkedHashSet<Tag> listaTags = new LinkedHashSet<>();
	private DefaultListModel<String> lstModel = new DefaultListModel<>();
	private JTextField txtBackup;

	private CadastraPacote() {
		setTitulo("Cadastrar pacote");

		//campo nome
		JPanel pnlNome = new JPanel(new GridBagLayout());
		JLabel lblNome = new JLabel("*Nome:");
		adicionaCampo(pnlNome, lblNome, false);
		txtNome = new JTextField();
		adicionaCampo(pnlNome, txtNome, true);
		adicionaCampo(pnlCampos, pnlNome, true);
		pnlNome.setToolTipText("Nome para o pacote, com no máximo 255 caracteres");
		txtNome.setToolTipText("Nome para o pacote, com no máximo 255 caracteres");
		//campo ator
		JPanel pnlAtor = new JPanel(new GridBagLayout());
		JButton btnAtor = new JButton("Ator");
		adicionaCampo(pnlAtor, btnAtor, false);
		txtAtor = new JTextField();
		txtAtor.setEditable(false);
		adicionaCampo(pnlAtor, txtAtor, true);
		btnAtor.addActionListener(e -> {
			ator = ConsultaAtor.seleciona();
			if (ator != null) {
				txtAtor.setText(ator.getNome());
			}
		});
		JButton btnLimparAtor = new JButton("X");
		btnLimparAtor.addActionListener(e -> {
			ator = null;
			txtAtor.setText(null);
		});
		adicionaCampo(pnlAtor, btnLimparAtor, false);
		btnAtor.setToolTipText("Uma pessoa associada a este pacote, como um cliente ou autor");
		txtAtor.setToolTipText("Uma pessoa associada a este pacote, como um cliente ou autor");
		btnLimparAtor.setToolTipText("Deixar o campo em branco");
		adicionaCampo(pnlCampos, pnlAtor, true);
		//campo data
		JPanel pnlData = new JPanel(new GridBagLayout());
		JLabel lblData = new JLabel("*Data:");
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
		pnlData.setToolTipText("Defina a data que o conteúdo do pacote foi criado");
		txtData.setToolTipText("Defina a data que o conteúdo do pacote foi criado");
		adicionaCampo(pnlCampos, pnlData, true);
		//campo tamanho
		JPanel pnlTamanho = new JPanel(new GridBagLayout());
		JLabel lblTamanho = new JLabel("Tamanho:");
		adicionaCampo(pnlTamanho, lblTamanho, false);
		spnTamanho = new JSpinner(new SpinnerNumberModel(0, 0, 9999999, .05));
		adicionaCampo(pnlTamanho, spnTamanho, true);
		JLabel lblGigas = new JLabel("MB");
		adicionaCampo(pnlTamanho, lblGigas, false);
		pnlTamanho.setToolTipText("Insira o tamanho do pacote, em megabytes");
		spnTamanho.setToolTipText("Insira o tamanho do pacote, em megabytes");
		adicionaCampo(pnlCampos, pnlTamanho, true);
		//campo tipo
		JPanel pnlTipo = new JPanel(new GridBagLayout());
		JButton btnTipo = new JButton("Tipo");
		adicionaCampo(pnlTipo, btnTipo, false);
		txtTipo = new JTextField();
		txtTipo.setEditable(false);
		adicionaCampo(pnlTipo, txtTipo, true);
		btnTipo.addActionListener(e -> {
			tipoPacote = ConsultaTipoPacote.seleciona();
			if (tipoPacote != null) {
				txtTipo.setText(tipoPacote.getNome());
			}
		});
		JButton btnLimparTipo = new JButton("X");
		btnLimparTipo.addActionListener(e -> {
			tipoPacote = null;
			txtTipo.setText(null);
		});
		adicionaCampo(pnlTipo, btnLimparTipo, false);
		btnTipo.setToolTipText("Defina o tipo de conteúdo que o pacote contém");
		txtTipo.setToolTipText("Defina o tipo de conteúdo que o pacote contém");
		btnLimparTipo.setToolTipText("Deixar o campo em branco");
		adicionaCampo(pnlCampos, pnlTipo, true);
		//campo tags (botões)
		JPanel pnlTagsBtn = new JPanel(new GridBagLayout());
		JButton btnAdicTag = new JButton("Adicionar tag");
		btnAdicTag.addActionListener(e -> adicionarTag());
		btnAdicTag.setToolTipText("Adicionar uma tag ao pacote");
		adicionaCampo(pnlTagsBtn, btnAdicTag, true);
		JButton btnRemovTag = new JButton("Remover");
		btnRemovTag.addActionListener(e -> removerTag());
		btnRemovTag.setToolTipText("Remove a tag selecionada");
		adicionaCampo(pnlTagsBtn, btnRemovTag, true);
		adicionaCampo(pnlCampos, pnlTagsBtn, true);
		//campo tags (lista)
		JPanel pnlTagsLista = new JPanel(new GridBagLayout());
		lstTags = new JList<>(lstModel);
		lstTags.setLayoutOrientation(JList.VERTICAL);
		lstTags.setVisibleRowCount(5);
		JScrollPane scrLista = new JScrollPane(lstTags);
		adicionaCampo(pnlTagsLista, scrLista, true);
		adicionaCampo(pnlCampos, pnlTagsLista, true);
		//campo backup
		JPanel pnlBackup = new JPanel(new GridBagLayout());
		JLabel lblBackup = new JLabel("Backup:");
		adicionaCampo(pnlBackup, lblBackup, false);
		txtBackup = new JTextField();
		txtBackup.setEnabled(false);
		adicionaCampo(pnlBackup, txtBackup, true);
		pnlBackup.setToolTipText("Código do backup que contém o item");
		txtBackup.setToolTipText("Código do backup que contém o item");
		adicionaCampo(pnlCampos, pnlBackup, true);
		//observações
		JLabel lblObrigatorio = new JLabel("*campo obrigatório");
		adicionaCampo(pnlCampos, lblObrigatorio, true);
		JLabel lblDicas = new JLabel("Passe o mouse sobre um item para ajuda");
		adicionaCampo(pnlCampos, lblDicas, true);

		//centraliza a janela
		pack();
		setLocationRelativeTo(null);

		//ativa o botão Salvar
		txtNome.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				testaCampos();
			}
		});

	}

	private CadastraPacote(int codigo) {
		this();
		try {
			pacote = Pacote.buscaPacote(codigo);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao buscar pacote", JOptionPane.ERROR_MESSAGE);
			return;
		}

		ator = pacote.getAtor();
		tipoPacote = pacote.getTipo();
		txtNome.setText(pacote.getNome());
		txtAtor.setText(ator.getNome());
		txtData.setText(new SimpleDateFormat("dd/MM/yyyy").format(pacote.getData()));
		data = pacote.getData();
		spnTamanho.setValue(pacote.getTamanho());
		txtTipo.setText(tipoPacote.getNome());
		listaTags = pacote.getTags();
		lstTags.setVisibleRowCount(5);
		for (Tag tag : listaTags)
			lstModel.addElement((tag.getNome()));
		txtBackup.setText(pacote.getCodigoBackup() == null ? null : String.valueOf(pacote.getCodigoBackup()));
		btnSalvar.setEnabled(true);
	}

	static void cadastra() {
		CadastraPacote janela = new CadastraPacote();
		janela.setVisible(true);
	}

	static void altera(int codigo) {
		CadastraPacote janela = new CadastraPacote(codigo);
		janela.pack();
		janela.setVisible(true);
	}

	//adiciona uma tag ao pacote
	private void adicionarTag() {
		ArrayList<Tag> tags = ConsultaTag.seleciona();
		if (tags.isEmpty())
			return;

		//adiciona as tags
		boolean repetida = true;
		for (Tag tag : tags)
			if (listaTags.add(tag)) //tenta adicionar
				lstModel.addElement(tag.getNome());
			else if (repetida) { //avisa se houver uma tag repetida
				JOptionPane.showMessageDialog(null, "Uma ou mais tags já foram adicionadas a este pacote", "Tag já " +
						"adicionada", JOptionPane.INFORMATION_MESSAGE);
				repetida = false; //evita que o aviso seja exibido mais de uma vez
			}
	}

	//remove a tag selecionada do pacote
	private void removerTag() {
		String itemSelecionado = lstTags.getSelectedValue();
		listaTags.removeIf((Tag tag) -> tag.getNome().contentEquals(itemSelecionado));
		lstModel.removeElement(itemSelecionado);
	}

	@Override
	void testaCampos() {
		if (!txtNome.getText().isEmpty())
			btnSalvar.setEnabled(true);
		else
			btnSalvar.setEnabled(false);
	}

	@Override
	void onOK() {
		pacote.setNome(txtNome.getText());
		pacote.setAtor(ator);
		pacote.setData(data);
		pacote.setTamanho((Double) spnTamanho.getValue());
		pacote.setTipo(tipoPacote);
		pacote.setTags(listaTags);

		try {
			if (pacote.getCodigo() == null) //se for um novo item
				Pacote.cadastraPacote(pacote);
			else
				Pacote.alteraPacote(pacote);
			JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso", "Cadastrar pacote", JOptionPane
					.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
			return;
		}

		dispose();
	}
}
