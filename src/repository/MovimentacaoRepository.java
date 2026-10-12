
package repository;

import database.Conexao;
import model.Movimentacao;
import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoRepository {

    // Salva uma movimentação no banco de dados
    public void salvar(Movimentacao movimentacao) {

        String sql = """
            INSERT INTO movimentacoes
            (produto_id, tipo, quantidade, data_movimentacao)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, movimentacao.getProduto().getId());
            stmt.setString(2, movimentacao.getTipo());
            stmt.setInt(3, movimentacao.getQuantidade());
            stmt.setTimestamp(
                4,
                java.sql.Timestamp.valueOf(movimentacao.getDataHora())
            );

            stmt.executeUpdate();

            System.out.println("Movimentação salva no MySQL!");

        } catch (SQLException erro) {
            throw new RuntimeException(
                "Erro ao salvar movimentação no MySQL", erro
            );
        }
    }

    // Recupera todas as movimentações do MySQL
    public List<Movimentacao> listarTodos() {

        List<Movimentacao> movimentacoes = new ArrayList<>();

        String sql = """
            SELECT
                m.id,
                m.tipo,
                m.quantidade AS quantidade_movimentada,
                m.data_movimentacao,
                p.id AS produto_id,
                p.nome,
                p.preco,
                p.quantidade AS estoque_atual
            FROM movimentacoes m
            INNER JOIN produtos p
                ON m.produto_id = p.id
            ORDER BY m.data_movimentacao DESC, m.id DESC
            """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                // Recupera o produto relacionado
                Produto produto = new Produto(
                    resultado.getInt("produto_id"),
                    resultado.getString("nome"),
                    resultado.getDouble("preco"),
                    resultado.getInt("estoque_atual")
                );

                // Recupera a movimentação e sua data original
                Movimentacao movimentacao = new Movimentacao(
                    resultado.getInt("id"),
                    produto,
                    resultado.getString("tipo"),
                    resultado.getInt("quantidade_movimentada"),
                    resultado.getTimestamp("data_movimentacao")
                             .toLocalDateTime()
                );

                movimentacoes.add(movimentacao);
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                "Erro ao consultar movimentações no MySQL", erro
            );
        }

        return movimentacoes;
    }
}
