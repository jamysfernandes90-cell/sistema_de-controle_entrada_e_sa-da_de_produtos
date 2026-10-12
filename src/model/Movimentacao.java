
package model;

import java.time.LocalDateTime;

public class Movimentacao {

    private int id;
    private Produto produto;
    private String tipo;
    private int quantidade;
    private LocalDateTime dataHora;

    // Construtor utilizado ao registrar movimentações
    public Movimentacao(int id, Produto produto, String tipo, int quantidade) {
        this(id, produto, tipo, quantidade, LocalDateTime.now());
    }

    // Construtor utilizado para recuperar dados do MySQL
    public Movimentacao(int id, Produto produto, String tipo,
                        int quantidade, LocalDateTime dataHora) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "id=" + id +
                ", produto=" + produto.getNome() +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade +
                ", dataHora=" + dataHora +
                '}';
    }
}
