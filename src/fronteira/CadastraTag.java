package fronteira;

import entidade.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class CadastraTag extends Cadastros {
	private JTextField txtNome;

	private CadastraTag() {
		setTitulo("Cadastrar tag");

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
		CadastraTag janela = new CadastraTag();
		janela.setVisible(true);
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
			Tag.cadastrarTag(txtNome.getText());

			JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso.", "Cadastrar tag", JOptionPane
					.INFORMATION_MESSAGE);
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
		}
	}
}
