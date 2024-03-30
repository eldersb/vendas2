package vendas2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class FrameNovo extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtDescricao;
    private JTextField txtPreco;
    private Connection objConexaoBanco;
    private Produto produto;

    public FrameNovo() {
        setTitle("Novo Produto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Centraliza o frame na tela

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        getContentPane().add(panel);

        JLabel lblDescricao = new JLabel("Descrição:");
        panel.add(lblDescricao);

        txtDescricao = new JTextField();
        panel.add(txtDescricao);

        JLabel lblPreco = new JLabel("Preço:");
        panel.add(lblPreco);

        txtPreco = new JTextField();
        panel.add(txtPreco);

        JButton btnGravar = new JButton("Gravar");
        btnGravar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					gravarProduto();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        panel.add(btnGravar);
    }

    private void gravarProduto() throws SQLException {
        // Aqui você implementaria a lógica para gravar o produto no banco de dados
        String descricao = txtDescricao.getText();
        double preco = Double.parseDouble(txtPreco.getText());

        
          
        objConexaoBanco = Conexao.conectar();
        if (objConexaoBanco == null) {
            JOptionPane.showMessageDialog(null, "Conexão não realizada");
        } else {
           produto = new Produto();
           produto.gravaProduto(objConexaoBanco, descricao, preco);
        }
        
        
     
		
      
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FrameNovo frame = new FrameNovo();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	public void setConexao1(Connection objConexaoBanco) {
		// TODO Auto-generated method stub
		
	}

	public void setConexao(Connection objConexaoBanco) {
		// TODO Auto-generated method stub
		
	}
}

