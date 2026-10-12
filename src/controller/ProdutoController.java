package controller;

import java.util.List;

import model.Movimentacao;
import model.Produto;
import repository.MovimentacaoRepository;
import repository.ProdutoRepository;
import service.EstoqueService;

public class ProdutoController {

    private ProdutoRepository repository;
    private EstoqueService estoqueService;
    private MovimentacaoRepository movimentacaoRepository;

    public ProdutoController(
            ProdutoRepository repository,
            EstoqueService estoqueService,
            MovimentacaoRepository movimentacaoRepository) {

        this.repository = repository;
        this.estoqueService = estoqueService;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    // Cadastrar um novo produto
    public void cadastrarProduto(Produto produto) {
        repository.salvar(produto);
    }

    // Consultar todos os produtos cadastrados
    public List<Produto> consultarEstoque() {
        return repository.listarTodos();
    }

    // Buscar um produto pelo ID
    public Produto buscarProduto(int id) {
        return repository.buscarPorId(id);
    }

    // Atualizar os dados de um produto
    public boolean atualizarProduto(Produto produto) {
        return repository.atualizar(produto);
    }

    // Registrar entrada de produto
    public boolean registrarEntrada(int idProduto, int quantidade) {
        return estoqueService.registrarEntrada(idProduto, quantidade);
    }

    // Registrar saída de produto
    public boolean registrarSaida(int idProduto, int quantidade) {
        return estoqueService.registrarSaida(idProduto, quantidade);
    }

    // Consultar histórico de entradas e saídas
    public List<Movimentacao> consultarHistorico() {
        return movimentacaoRepository.listarTodos();
    }
}