package vendas2;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Produto {

    // Método para listar todos os produtos
    public ResultSet listarAllProduto(Connection objConexao) throws SQLException {
        Statement stmt = objConexao.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM db_pedido.produto");
        return rs;
    }

    // Método para gravar um novo produto
    public void gravaProduto(Connection objConexao, String descricao, double preco) throws SQLException {
        Statement stmt = objConexao.createStatement();
        String query = "INSERT INTO db_pedido.produto (Descricao, Preco) VALUES ('" + descricao + "', " + preco + ")";
        stmt.executeUpdate(query);
    }
    
    public void atualizarProduto(Connection objConexao, int id, String novaDescricao, double novoPreco) throws SQLException {
        Statement stmt = objConexao.createStatement();
        String query = "UPDATE db_pedido.produto SET Descricao = '" + novaDescricao + "', Preco = " + novoPreco + " WHERE Id = " + id;
        stmt.executeUpdate(query);
    }
    
   
     public void excluirProduto(Connection objConexao, int id) throws SQLException {
            Statement stmt = objConexao.createStatement();
            String query = "DELETE FROM db_pedido.produto WHERE Id = " + id;
            stmt.executeUpdate(query);
        }
     
     public ResultSet pesquisarProduto(Connection objConexao, String termoPesquisa) throws SQLException {
         String query = "SELECT * FROM Produto WHERE Id = ? OR Descricao LIKE ?";
         PreparedStatement stmt = objConexao.prepareStatement(query);
         stmt.setString(1, termoPesquisa);
         stmt.setString(2, "%" + termoPesquisa + "%");
         return stmt.executeQuery();
     }
     
    
 }
    