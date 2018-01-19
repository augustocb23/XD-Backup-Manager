package fronteira;

import entidade.TipoPacote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class CadastraTipoPacote extends Cadastros {
	private JTextField txtNome;

	private CadastraTipoPacote() {
		setTitulo("Cadastrar tipo de pacote");

		//campo nome
		JPanel pnlNome = new JPanel(new GridBagLayout());
		JLabel lblNome = new JLabel("Nome:");
		adicionaCampo(pnlNome, lblNome, false);
		txtNome = new JTextField();
		adicionaCampo(pnlNome, txtNome, true);
		adicionaCampo(pnlCampos, pnlNome, true);
		pnlNome.setToolTipText("Máximo de 255 caracteres");

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

	static void cadastra() {
		CadastraTipoPacote janela = new CadastraTipoPacote();
		janela.setVisible(true);
	}

	static String renomear(String nome) {
		CadastraTipoPacote janela = new CadastraTipoPacote();
		janela.txtNome.setText(nome);    //preenche o campo com o nome anterior
		janela.txtNome.selectAll();        //seleciona o nome antigo

		//remove o listener padrão do botão Salvar
		for (ActionListener listener : janela.btnSalvar.getActionListeners())
			janela.btnSalvar.removeActionListener(listener);
		janela.btnSalvar.addActionListener(e -> janela.setVisible(false));
		//remove o listener padrão do botão Cancelar
		for (ActionListener listener : janela.btnCancel.getActionListeners())
			janela.btnCancel.removeActionListener(listener);
		janela.btnCancel.addActionListener(e -> {
			//volta ao nome anterior antes de fechar a janela
			janela.txtNome.setText(nome);
			janela.dispose();
		});

		//exibe a janela - aguarda fechar (botão OK) para retornar
		janela.setVisible(true);
		return janela.txtNome.getText();
	}

	@Override
	void testaCampos() {
		if (txtNome.getText().isEmpty())
			btnSalvar.setEnabled(false);
		else
			btnSalvar.setEnabled(true);
	}

	@Override
	void onOK() {
		try {
			TipoPacote.cadastrarTipo(txtNome.getText());
			JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso.", "Cadastrar tipo de pacote",
					JOptionPane
							.INFORMATION_MESSAGE);
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
		}
	}
}
