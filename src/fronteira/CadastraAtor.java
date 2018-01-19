package fronteira;

import entidade.Ator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CadastraAtor extends Cadastros {
	private JTextField txtNome;

	private CadastraAtor() {
		setTitulo("Cadastrar ator");

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
		CadastraAtor janela = new CadastraAtor();
		janela.setVisible(true);
	}

	static String renomear(String nome) {
		CadastraAtor janela = new CadastraAtor();
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
			Ator.cadastrarAtor(txtNome.getText());

			JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso.", "Cadastrar ator", JOptionPane
					.INFORMATION_MESSAGE);
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro ao cadastrar", JOptionPane.ERROR_MESSAGE);
		}
	}
}
