
package service;

import database.Conexao;
import model.Produto;
import repository.MovimentacaoRepository;
import repository.ProdutoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstoqueService {

    private ProdutoRepository repository;
    private MovimentacaoRepository movimentacaoRepository;

    public EstoqueService(
            ProdutoRepository repository,
            MovimentacaoRepository movimentacaoRepository) {

        this.repository = repository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // REGISTRAR ENTRADA DE PRODUTOS
    public boolean registrarEntrada(int idProduto, int quantidade) {
        return registrarMovimentacao(idProduto, quantidade, "ENTRADA");
    }

    // REGISTRAR SAÍDA DE PRODUTOS
    public boolean registrarSaida(int idProduto, int quantidade) {
        return registrarMovimentacao(idProduto, quantidade, "SAIDA");
    }

    // ATUALIZA O ESTOQUE E SALVA A MOVIMENTAÇÃO
    private boolean registrarMovimentacao(
            int idProduto, int quantidade, String tipo) {

        if (quantidade <= 0) {
            return false;
        }

        String buscarSql =
                "SELECT quantidade FROM produtos WHERE id = ? FOR UPDATE";

        String atualizarSql =
                "UPDATE produtos SET quantidade = ? WHERE id = ?";

        String movimentacaoSql =
                "INSERT INTO movimentacoes (produto_id, tipo, quantidade) "
                + "VALUES (?, ?, ?)";

        try (Connection conexao = Conexao.conectar()) {

            // Inicia a transação
            conexao.setAutoCommit(false);

            try {
                int estoqueAtual;

                // Consulta e bloqueia o produto durante a operação
                try (PreparedStatement stmt =
                        conexao.prepareStatement(buscarSql)) {

                    stmt.setInt(1, idProduto);

                    try (ResultSet resultado = stmt.executeQuery()) {

                        if (!resultado.next()) {
                            conexao.rollback();
                            return false;
                        }

                        estoqueAtual = resultado.getInt("quantidade");
                    }
                }

                int novaQuantidade;

                if (tipo.equals("ENTRADA")) {

                    // Impede ultrapassar o limite do inteiro
                    if (quantidade > Integer.MAX_VALUE - estoqueAtual) {
                        conexao.rollback();
                        return false;
                    }

                    novaQuantidade = estoqueAtual + quantidade;

                } else if (tipo.equals("SAIDA")) {

                    // Impede estoque negativo
                    if (quantidade > estoqueAtual) {
                        conexao.rollback();
                        return false;
                    }

                    novaQuantidade = estoqueAtual - quantidade;

                } else {
                    conexao.rollback();
                    return false;
                }

                // Atualiza a quantidade no MySQL
                try (PreparedStatement stmt =
                        conexao.prepareStatement(atualizarSql)) {

                    stmt.setInt(1, novaQuantidade);
                    stmt.setInt(2, idProduto);

                    stmt.executeUpdate();
                }

                // Registra a movimentação no histórico
                try (PreparedStatement stmt =
                        conexao.prepareStatement(movimentacaoSql)) {

                    stmt.setInt(1, idProduto);
                    stmt.setString(2, tipo);
                    stmt.setInt(3, quantidade);

                    stmt.executeUpdate();
                }

                // Confirma as duas operações
                conexao.commit();

                // Sincroniza o objeto em memória, se existir
                Produto produto = repository.buscarPorId(idProduto);

                if (produto != null) {
                    produto.setQuantidade(novaQuantidade);
                }

                return true;

            } catch (SQLException erro) {

                conexao.rollback();
                throw erro;
            }

        } catch (SQLException erro) {

            throw new RuntimeException(
                    "Erro ao registrar movimentação no MySQL", erro
            );
        }
    }
}
