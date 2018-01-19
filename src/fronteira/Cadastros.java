package fronteira;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class Cadastros extends JDialog {
	protected JPanel pnlCampos;
	protected JButton btnSalvar;
	protected JButton btnCancel;
	private JLabel lblTitulo;
	private JPanel contentPane;

	Cadastros() {
		setContentPane(contentPane);
		setModal(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle(Pacotes.getTitulo());
		setIconImage(Toolkit.getDefaultToolkit().getImage(Pacotes.getIcone()));

		//implementa os botões
		getRootPane().setDefaultButton(btnSalvar);
		btnSalvar.addActionListener(e -> onOK());
		btnSalvar.setEnabled(false);
		btnCancel.addActionListener(e -> onCancel());

		//cancela ao pressionar ESC ou tentar fechar a janela
		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent
				.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
	}

	//altera o título exibido na janela
	void setTitulo(String titulo) {
		this.lblTitulo.setText(titulo);
	}

	//declara o método para ativar o botão OK
	abstract void testaCampos();

	//declara o método do botão OK
	abstract void onOK();

	//sai ao aperta ESC ou cancelar
	private void onCancel() {
//		String[] opcoes = {"Sim", "Não"};
//		if (JOptionPane.showOptionDialog(null, "Tem certeza que deseja cancelar o cadastro?", null, JOptionPane
//				.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[1]) != JOptionPane.YES_OPTION)
//			return;
		dispose();
	}

	//adiciona um novo painel ao Painel Campos
	void adicionaCampo(JPanel painel, JComponent item, Boolean expande) {
		//define as constraints
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL; //expande horizontalmente
		constraints.weightx = expande ? 1 : 0; //largura da coluna
		constraints.weighty = 0.6; //altura da linha
		constraints.ipadx = 5; //espaçamento horizontal
		constraints.ipady = 5; //espaçamento vertical

		//adiciona o item ao painel
		painel.add(item, constraints);
	}

	private void createUIComponents() {
		pnlCampos = new JPanel();
		pnlCampos.setLayout(new BoxLayout(pnlCampos, BoxLayout.Y_AXIS));
	}
}
