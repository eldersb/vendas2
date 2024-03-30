package vendas2;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ViewProduto extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Connection objConexaoBanco;
    private Produto produto;
    private FrameNovo novoCad;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewProduto frame = new ViewProduto();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ViewProduto() {
    	setTitle("Gerenciador de Produto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnListar = new JButton("Listar");
        btnListar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    listaProduto();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + ex.getMessage());
                }
            }
        });
        btnListar.setBounds(10, 55, 89, 23);
        contentPane.add(btnListar);

        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirNovoFrame();
                try {
					listaProduto();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnNovo.setBounds(169, 218, 89, 23);
        contentPane.add(btnNovo);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarProduto();
            }
        });
        btnAtualizar.setBounds(109, 55, 89, 23);
        contentPane.add(btnAtualizar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });
        btnExcluir.setBounds(208, 55, 89, 23);
        contentPane.add(btnExcluir);
         
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = JOptionPane.showInputDialog(ViewProduto.this, "Digite o ID ou a descrição do produto:");
                if (termoPesquisa != null && !termoPesquisa.isEmpty()) {
                    try {
                        ResultSet rs = produto.pesquisarProduto(objConexaoBanco, termoPesquisa);
                        exibirResultadoPesquisa(rs);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(ViewProduto.this, "Erro ao pesquisar o produto: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(ViewProduto.this, "Termo de pesquisa não fornecido.");
                }
            }
        });
        btnPesquisar.setBounds(307, 55, 99, 23);
        contentPane.add(btnPesquisar);

        table = new JTable();
        table.setBounds(43, 89, 342, 118);
        contentPane.add(table);

        objConexaoBanco = Conexao.conectar();
        if (objConexaoBanco == null) {
            JOptionPane.showMessageDialog(null, "Conexão não realizada");
        } else {
            produto = new Produto();
        }
    }

    private void exibirResultadoPesquisa(ResultSet rs) throws SQLException {
        String[] colunasTabela = new String[]{"Id", "Descrição", "Preço"};
        DefaultTableModel modeloTabela = new DefaultTableModel(null, colunasTabela);
        modeloTabela.addRow(new String[]{"ID", "DESCRIÇÃO", "PREÇO"});
        if (rs != null) {
            while (rs.next()) {
                modeloTabela.addRow(new String[]{
                    String.valueOf(rs.getInt("Id")),
                    rs.getString("Descricao"),
                    String.valueOf(rs.getDouble("Preco"))
                });
            }
        }
        table.setModel(modeloTabela);
    }

    public void listaProduto() throws SQLException {
        try (ResultSet rs = produto.listarAllProduto(objConexaoBanco)) {
            String[] colunasTabela = new String[]{"Id", "Descrição", "Preço"};
            DefaultTableModel modeloTabela = new DefaultTableModel(null, colunasTabela);
            modeloTabela.addRow(new String[]{"ID", "DESCRIÇÃO", "PREÇO"});
            if (rs != null) {
                while (rs.next()) {
                    modeloTabela.addRow(new String[]{
                            String.valueOf(rs.getInt("Id")),
                            rs.getString("Descricao"),
                            String.valueOf(rs.getDouble("Preco"))
                    });
                }
            }
            table.setModel(modeloTabela);
        } finally {
            if (objConexaoBanco != null) {
               // objConexaoBanco.close();
            }
        }
    }

    public void abrirNovoFrame() {
        if (novoCad == null) {
            novoCad = new FrameNovo();
            ((FrameNovo) novoCad).setConexao(objConexaoBanco);
            novoCad.setLocationRelativeTo(null);
            novoCad.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
        novoCad.setVisible(true);
    }

    public void atualizarProduto() {
        // Solicitar ao usuário que insira o ID, a nova descrição e o novo preço
        String idProduto = JOptionPane.showInputDialog(this, "Digite o ID do produto:");
        String novaDescricao = JOptionPane.showInputDialog(this, "Digite a nova descrição:");
        String novoPrecoStr = JOptionPane.showInputDialog(this, "Digite o novo preço:");
        
        System.out.println(idProduto + novaDescricao + novoPrecoStr);
        
        // Verificar se o ID do produto é válido
        if (idProduto != null && !idProduto.isEmpty()) {
            try {
                int id = Integer.parseInt(idProduto);
                double novoPreco = Double.parseDouble(novoPrecoStr);

                // Atualizar o produto no banco de dados
                produto.atualizarProduto(objConexaoBanco, id, novaDescricao, novoPreco);

                // Atualizar a tabela na interface gráfica
                listaProduto();

                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID ou preço inválido.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar o produto: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID do produto não fornecido.");
        }
    }

    public void excluirProduto() {
        // Solicitar ao usuário que insira o ID do produto que deseja excluir
        String idProduto = JOptionPane.showInputDialog(this, "Digite o ID do produto que deseja excluir:");

        // Verificar se o ID do produto é válido
        if (idProduto != null && !idProduto.isEmpty()) {
            try {
                int id = Integer.parseInt(idProduto);

                // Excluir o produto do banco de dados
                produto.excluirProduto(objConexaoBanco, id);

                // Atualizar a tabela na interface gráfica
                listaProduto();

                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir o produto: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "ID do produto não fornecido.");
        }
    }
}  
