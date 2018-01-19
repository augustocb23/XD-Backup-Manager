package fronteira;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class Consultas extends JDialog {

	protected JLabel lblTitulo;
	protected JTable tabelaConsulta;
	protected JButton btnAtualizar;
	protected JButton btnNovo;
	protected JButton btnExcluir;
	protected JPanel pnlRodape;
	protected JTextField txtBusca;
	protected JPanel pnlCentral;
	private JPanel contentPane;
	private JButton btnVoltar;

	Consultas() {
		//cria a janela
		setContentPane(contentPane);
		setModal(true);
		setSize(520, 400);
		setLocationRelativeTo(null);
		setTitle(Pacotes.getTitulo());
		setIconImage(Toolkit.getDefaultToolkit().getImage(Pacotes.getIcone()));

		//apaga a tabela
		tabelaConsulta.removeAll();

		//fecha ao apertar ESC ou clicar em SAIR
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent
				.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		getRootPane().setDefaultButton(btnVoltar);
		btnVoltar.addActionListener(e -> onCancel());

		//declara os botões
		btnNovo.addActionListener(e -> onNovo());
		btnExcluir.addActionListener(e -> onExcluir());
		btnAtualizar.addActionListener(e -> onAtualizar());

		//cria o listener da busca
		txtBusca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				//verifica se o campo está vazio
				if (txtBusca.getText().isEmpty())
					onAtualizar();
				else
					filtraTabela();
			}
		});
	}

	//filtra os itens de acordo a barra de busca
	abstract void filtraTabela();

	//atualiza a lista - deve ser chamado ao cria a janela
	abstract void onAtualizar();

	//declara o método para excluir um item
	abstract void onExcluir();

	//declara o método para criar um item
	abstract void onNovo();

	void setTitulo(String titulo) {
		lblTitulo.setText(titulo);
	}

	private void onCancel() {
		dispose();
	}

	private void createUIComponents() {
		pnlCentral = new JPanel();
	}
}
