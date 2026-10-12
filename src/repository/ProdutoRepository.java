
package repository;

import database.Conexao;
import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    // CADASTRAR PRODUTO NO MYSQL
    public void salvar(Produto produto) {

        String sql = """
            INSERT INTO produtos
            (id, nome, preco, quantidade)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                 conexao.prepareStatement(sql)) {

            stmt.setInt(1, produto.getId());
            stmt.setString(2, produto.getNome());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());

            stmt.executeUpdate();

            System.out.println(
                "Produto cadastrado no MySQL!"
            );

        } catch (SQLException erro) {
            throw new RuntimeException(
                "Erro ao cadastrar produto no MySQL.",
                erro
            );
        }
    }

    // LISTAR TODOS OS PRODUTOS
    public List<Produto> listarTodos() {

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produtos";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                 conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                Produto produto = new Produto(
                    resultado.getInt("id"),
                    resultado.getString("nome"),
                    resultado.getDouble("preco"),
                    resultado.getInt("quantidade")
                );

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                "Erro ao listar produtos.",
                erro
            );
        }

        return produtos;
    }

    // BUSCAR PRODUTO PELO ID
    public Produto buscarPorId(int id) {

        String sql =
            "SELECT * FROM produtos WHERE id = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                 conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet resultado =
                     stmt.executeQuery()) {

                if (resultado.next()) {

                    return new Produto(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getDouble("preco"),
                        resultado.getInt("quantidade")
                    );
                }
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                "Erro ao buscar produto.",
                erro
            );
        }

        return null;
    }

    // ATUALIZAR PRODUTO NO MYSQL
    public boolean atualizar(
            Produto produtoAtualizado) {

        String sql = """
            UPDATE produtos
            SET nome = ?,
                preco = ?,
                quantidade = ?
            WHERE id = ?
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                 conexao.prepareStatement(sql)) {

            stmt.setString(
                1, produtoAtualizado.getNome()
            );

            stmt.setDouble(
                2, produtoAtualizado.getPreco()
            );

            stmt.setInt(
                3, produtoAtualizado.getQuantidade()
            );

            stmt.setInt(
                4, produtoAtualizado.getId()
            );

            int linhas = stmt.executeUpdate();

            return linhas > 0;

        } catch (SQLException erro) {
            throw new RuntimeException(
                "Erro ao atualizar produto.",
                erro
            );
        }
    }
}
