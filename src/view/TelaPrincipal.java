package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import model.Movimentacao;
import model.Produto;
import repository.ProdutoRepository;
import repository.MovimentacaoRepository;
import service.EstoqueService;

public class TelaPrincipal extends JFrame {

    private JPanel painelConteudo;

    // Objetos principais do sistema
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final EstoqueService estoqueService;

    public TelaPrincipal() {

        // ==========================================
        // INICIALIZAÇÃO DO SISTEMA
        // ==========================================

        produtoRepository = new ProdutoRepository();
        movimentacaoRepository = new MovimentacaoRepository();

        estoqueService = new EstoqueService(
                produtoRepository,
                movimentacaoRepository
        );

        // ==========================================
        // CONFIGURAÇÃO DA JANELA
        // ==========================================

        setTitle("Virtual Imports - Controle de Estoque");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // MENU LATERAL
        // ==========================================

        JPanel menuLateral = new JPanel();

        menuLateral.setPreferredSize(new Dimension(250, 650));
        menuLateral.setBackground(new Color(25, 25, 25));

        menuLateral.setLayout(
                new BoxLayout(menuLateral, BoxLayout.Y_AXIS)
        );

        menuLateral.setBorder(
                new EmptyBorder(25, 15, 25, 15)
        );

        // Nome da loja
        JLabel nomeLoja = new JLabel("VIRTUAL IMPORTS");
        nomeLoja.setForeground(new Color(150, 40, 255));
        nomeLoja.setFont(new Font("Arial", Font.BOLD, 24));
        nomeLoja.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitulo = new JLabel("Controle de Estoque");
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setFont(new Font("Arial", Font.BOLD, 14));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuLateral.add(nomeLoja);
        menuLateral.add(Box.createVerticalStrut(5));
        menuLateral.add(subtitulo);
        menuLateral.add(Box.createVerticalStrut(40));

        // ==========================================
        // BOTÕES
        // ==========================================

        JButton botaoInicio =
                criarBotao("Início");

        JButton botaoCadastrar =
                criarBotao("Cadastrar Produto");

        JButton botaoEntrada =
                criarBotao("Registrar Entrada");

        JButton botaoSaida =
                criarBotao("Registrar Saída");

        JButton botaoConsultar =
                criarBotao("Consultar Estoque");

        JButton botaoAtualizar =
                criarBotao("Atualizar Produto");

        JButton botaoHistorico =
                criarBotao("Histórico");

        menuLateral.add(botaoInicio);
        menuLateral.add(Box.createVerticalStrut(10));

        menuLateral.add(botaoCadastrar);
        menuLateral.add(Box.createVerticalStrut(10));

        menuLateral.add(botaoEntrada);
        menuLateral.add(Box.createVerticalStrut(10));

        menuLateral.add(botaoSaida);
        menuLateral.add(Box.createVerticalStrut(10));

        menuLateral.add(botaoConsultar);
        menuLateral.add(Box.createVerticalStrut(10));

        menuLateral.add(botaoAtualizar);
        menuLateral.add(Box.createVerticalStrut(10));

        menuLateral.add(botaoHistorico);

        add(menuLateral, BorderLayout.WEST);

        // ==========================================
        // PAINEL PRINCIPAL
        // ==========================================

        painelConteudo = new JPanel();

        painelConteudo.setBackground(
                new Color(245, 245, 250)
        );

        add(painelConteudo, BorderLayout.CENTER);

        // Mostra a página inicial
        mostrarInicio();

        // ==========================================
        // AÇÕES DOS BOTÕES
        // ==========================================

        botaoInicio.addActionListener(
                e -> mostrarInicio()
        );

        botaoCadastrar.addActionListener(
                e -> mostrarCadastroProduto()
        );

        botaoConsultar.addActionListener(
                e -> mostrarEstoque()
        );

        // Esses serão implementados nos próximos passos

        botaoEntrada.addActionListener(
            e -> mostrarRegistrarEntrada());

        botaoSaida.addActionListener(
        e -> mostrarRegistrarSaida()
        );

        botaoAtualizar.addActionListener(
        e -> mostrarAtualizarProduto()
        );
        
       botaoHistorico.addActionListener(
        e -> mostrarHistorico()
        );
    }

    // ==========================================
    // CRIAÇÃO DOS BOTÕES DO MENU
    // ==========================================

    private JButton criarBotao(String texto) {

        JButton botao = new JButton(texto);

        botao.setMaximumSize(
                new Dimension(220, 42)
        );

        botao.setPreferredSize(
                new Dimension(220, 42)
        );

        botao.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        botao.setBackground(
                new Color(45, 45, 45)
        );

        botao.setForeground(Color.WHITE);

        botao.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        botao.setFocusPainted(false);
        botao.setBorderPainted(false);

        return botao;
    }

    // ==========================================
    // TELA INICIAL
    // ==========================================
// =======================================
// INDICADORES DO DASHBOARD
// =======================================

private JPanel criarIndicadoresDashboard() {

    int totalProdutos = 0;
    int unidadesEstoque = 0;
    int produtosSemEstoque = 0;
    double valorEstoque = 0;

    // Percorre os produtos cadastrados
    for (Produto produto : produtoRepository.listarTodos()) {

        totalProdutos++;

        int quantidade = produto.getQuantidade();

        unidadesEstoque += quantidade;

        valorEstoque += produto.getPreco() * quantidade;

        if (quantidade == 0) {
            produtosSemEstoque++;
        }
    }

    // Painel com quatro indicadores
    JPanel painel = new JPanel(new GridLayout(2, 2, 20, 20));

    painel.setBackground(new Color(245, 245, 250));

    painel.setBorder(
        new EmptyBorder(25, 30, 25, 30)
    );

    painel.add(criarCardIndicador(
        "Produtos cadastrados",
        String.valueOf(totalProdutos)
    ));

    painel.add(criarCardIndicador(
        "Unidades em estoque",
        String.valueOf(unidadesEstoque)
    ));

    painel.add(criarCardIndicador(
        "Valor do estoque",
        String.format("R$ %.2f", valorEstoque)
    ));

    painel.add(criarCardIndicador(
        "Produtos sem estoque",
        String.valueOf(produtosSemEstoque)
    ));

    return painel;
}

// =======================================
// CARD INDIVIDUAL DO DASHBOARD
// =======================================

private JPanel criarCardIndicador(String titulo, String valor) {

    JPanel card = new JPanel();

    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

    card.setBackground(Color.WHITE);

    card.setBorder(
        new EmptyBorder(20, 20, 20, 20)
    );

    JLabel labelTitulo = new JLabel(titulo);

    labelTitulo.setFont(
        new Font("Arial", Font.PLAIN, 16)
    );

    labelTitulo.setForeground(Color.GRAY);

    JLabel labelValor = new JLabel(valor);

    labelValor.setFont(
        new Font("Arial", Font.BOLD, 28)
    );

    labelValor.setForeground(new Color(130, 30, 220));

    labelTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelValor.setAlignmentX(Component.LEFT_ALIGNMENT);

    card.add(labelTitulo);

    card.add(Box.createVerticalStrut(15));

    card.add(labelValor);

    return card;
}
    private void mostrarInicio() {

        painelConteudo.removeAll();

        painelConteudo.setLayout(
                new BorderLayout()
        );

        JPanel painelCentral = new JPanel();

        painelCentral.setBackground(
                new Color(245, 245, 250)
        );

        painelCentral.setLayout(
                new BoxLayout(
                        painelCentral,
                        BoxLayout.Y_AXIS
                )
        );

        painelCentral.setBorder(
                new EmptyBorder(60, 50, 50, 50)
        );

        JLabel titulo =
                new JLabel("Painel de Controle");

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        36
                )
        );

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel mensagem =
                new JLabel(
                        "Bem-vindo ao Sistema de Controle de Entrada e Saída de Produtos"
                );

        mensagem.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18
                )
        );

        mensagem.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        painelCentral.add(titulo);
        painelCentral.add(Box.createVerticalStrut(20));
        painelCentral.add(mensagem);
        painelCentral.add(Box.createVerticalStrut(50));

       
// ========================================
// INDICADORES DO DASHBOARD
// ========================================

// Cria os indicadores com dados do estoque
JPanel indicadores = criarIndicadoresDashboard();

// Centraliza os indicadores
indicadores.setAlignmentX(Component.CENTER_ALIGNMENT);

// Adiciona os indicadores ao painel principal
painelCentral.add(indicadores);

        painelConteudo.add(
                painelCentral,
                BorderLayout.CENTER
        );

        atualizarPainel();
    }

    // ==========================================
    // CARD DA TELA INICIAL
    // ==========================================

    private JPanel criarCard(
            String titulo,
            String descricao
    ) {

        JPanel card = new JPanel();

        card.setBackground(Color.WHITE);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)
                        ),
                        new EmptyBorder(25, 25, 25, 25)
                )
        );

        JLabel tituloCard =
                new JLabel(titulo);

        tituloCard.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel descricaoCard =
                new JLabel(descricao);

        descricaoCard.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        descricaoCard.setForeground(Color.GRAY);

        card.add(tituloCard);
        card.add(Box.createVerticalStrut(15));
        card.add(descricaoCard);

        return card;
    }

    // ==========================================
    // CADASTRAR PRODUTO
    // ==========================================

    private void mostrarCadastroProduto() {

        painelConteudo.removeAll();

        painelConteudo.setLayout(
                new BorderLayout()
        );

        JLabel titulo =
                new JLabel("Cadastrar Produto");

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titulo.setBorder(
                new EmptyBorder(30, 40, 20, 40)
        );

        painelConteudo.add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel formulario =
                new JPanel(new GridLayout(5, 2, 15, 15));

        formulario.setBackground(
                new Color(245, 245, 250)
        );

        formulario.setBorder(
                new EmptyBorder(30, 80, 200, 80)
        );

        JTextField campoId =
                new JTextField();

        JTextField campoNome =
                new JTextField();

        JTextField campoPreco =
                new JTextField();

        JTextField campoQuantidade =
                new JTextField();

        JButton cadastrar =
                new JButton("Cadastrar Produto");

        cadastrar.setBackground(
                new Color(120, 40, 200)
        );

        cadastrar.setForeground(Color.WHITE);

        cadastrar.setFocusPainted(false);

        formulario.add(
                new JLabel("ID:")
        );

        formulario.add(campoId);

        formulario.add(
                new JLabel("Nome:")
        );

        formulario.add(campoNome);

        formulario.add(
                new JLabel("Preço:")
        );

        formulario.add(campoPreco);

        formulario.add(
                new JLabel("Quantidade:")
        );

        formulario.add(campoQuantidade);

        formulario.add(new JLabel(""));
        formulario.add(cadastrar);

        cadastrar.addActionListener(e -> {

            try {

                int id =
                        Integer.parseInt(
                                campoId.getText()
                        );

                String nome =
                        campoNome.getText().trim();

                double preco =
                        Double.parseDouble(
                                campoPreco
                                        .getText()
                                        .replace(",", ".")
                        );

                int quantidade =
                        Integer.parseInt(
                                campoQuantidade.getText()
                        );

                // Validação básica
                if (nome.isEmpty()
                        || preco < 0
                        || quantidade < 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Preencha os dados corretamente."
                    );

                    return;
                }

                // Evita ID duplicado
                if (produtoRepository.buscarPorId(id) != null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Já existe um produto com esse ID."
                    );

                    return;
                }

                Produto produto =
                        new Produto(
                                id,
                                nome,
                                preco,
                                quantidade
                        );

                produtoRepository.salvar(produto);

                JOptionPane.showMessageDialog(
                        this,
                        "Produto cadastrado com sucesso!"
                );

                // Limpa os campos
                campoId.setText("");
                campoNome.setText("");
                campoPreco.setText("");
                campoQuantidade.setText("");

            } catch (NumberFormatException erro) {

                JOptionPane.showMessageDialog(
                        this,
                        "ID, preço e quantidade devem ser valores numéricos."
                );
            }
        });

        painelConteudo.add(
                formulario,
                BorderLayout.CENTER
        );

        atualizarPainel();
    }
// ========================================
// REGISTRAR ENTRADA
// ========================================

private void mostrarRegistrarEntrada() {

    painelConteudo.removeAll();
    painelConteudo.setLayout(new BorderLayout());

    // Título
    JLabel titulo = new JLabel("Registrar Entrada de Produto");
    titulo.setFont(new Font("Arial", Font.BOLD, 28));
    titulo.setBorder(new EmptyBorder(30, 40, 20, 40));

    painelConteudo.add(titulo, BorderLayout.NORTH);

    // Painel do formulário
    // Formulário com campos de tamanho controlado
JPanel formulario = new JPanel(
    new GridLayout(3, 2, 15, 15)
);

// Limita o tamanho do formulário
formulario.setPreferredSize(new Dimension(650, 180));
formulario.setMaximumSize(new Dimension(650, 180));

formulario.setBorder(
    new EmptyBorder(20, 30, 20, 30)
);

    // Campo ID
    JLabel labelId = new JLabel("ID do Produto:");
    JTextField campoId = new JTextField();

    // Campo quantidade
    JLabel labelQuantidade = new JLabel("Quantidade de Entrada:");
    JTextField campoQuantidade = new JTextField();

    // Botão
    JButton botaoRegistrar = new JButton("Registrar Entrada");

    formulario.add(labelId);
    formulario.add(campoId);

    formulario.add(labelQuantidade);
    formulario.add(campoQuantidade);

    formulario.add(new JLabel(""));
    formulario.add(botaoRegistrar);

    painelConteudo.add(formulario, BorderLayout.CENTER);

    // Ação do botão
    botaoRegistrar.addActionListener(e -> {

        try {

            int idProduto =
                    Integer.parseInt(campoId.getText());

            int quantidade =
                    Integer.parseInt(campoQuantidade.getText());

            boolean sucesso =
                    estoqueService.registrarEntrada(
                            idProduto,
                            quantidade
                    );

            if (sucesso) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrada registrada com sucesso!"
                );

                campoId.setText("");
                campoQuantidade.setText("");

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível registrar a entrada.\n"
                                + "Verifique o ID do produto e a quantidade."
                );
            }

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    "ID e quantidade devem ser números inteiros."
            );
        }
    });

    atualizarPainel();
}

// ========================================
// REGISTRAR SAÍDA DE PRODUTOS
// ========================================

private void mostrarRegistrarSaida() {

    // Limpa a tela anterior
    painelConteudo.removeAll();
    painelConteudo.setLayout(new BorderLayout());

    // Título da tela
    JLabel titulo = new JLabel("Registrar Saída");
    titulo.setFont(new Font("Arial", Font.BOLD, 28));
    titulo.setBorder(new EmptyBorder(30, 40, 20, 40));

    painelConteudo.add(titulo, BorderLayout.NORTH);

    // Formulário de saída
    JPanel formulario = new JPanel(new GridLayout(3, 2, 15, 20));
    formulario.setBorder(new EmptyBorder(30, 40, 30, 40));

    // Campos para informar o produto
    JTextField campoId = new JTextField();
    JTextField campoQuantidade = new JTextField();

    // Botão para confirmar a saída
    JButton botaoRegistrar = new JButton("Confirmar Saída");

    formulario.add(new JLabel("ID do Produto:"));
    formulario.add(campoId);

    formulario.add(new JLabel("Quantidade de Saída:"));
    formulario.add(campoQuantidade);

    formulario.add(new JLabel(""));
    formulario.add(botaoRegistrar);

    // Ação do botão Confirmar Saída
    botaoRegistrar.addActionListener(e -> {

        try {

            // Converte os campos para números inteiros
            int id = Integer.parseInt(
                campoId.getText().trim()
            );

            int quantidade = Integer.parseInt(
                campoQuantidade.getText().trim()
            );

            // Solicita a saída ao serviço de estoque
            boolean sucesso = estoqueService.registrarSaida(
                id,
                quantidade
            );

            if (sucesso) {

                JOptionPane.showMessageDialog(
                    this,
                    "Saída registrada com sucesso!"
                );

                // Limpa os campos
                campoId.setText("");
                campoQuantidade.setText("");

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível registrar a saída.\n"
                    + "Verifique o ID do produto, a quantidade "
                    + "e o estoque disponível.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (NumberFormatException erro) {

            JOptionPane.showMessageDialog(
                this,
                "ID e quantidade devem ser números inteiros.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    });

    // Adiciona o formulário na tela
    painelConteudo.add(formulario, BorderLayout.CENTER);

    // Atualiza a interface
    atualizarPainel();
}

    // ============================================
    // ATUALIZAR PRODUTO
    // ============================================

    private void mostrarAtualizarProduto() {

        // Limpa a tela anterior
        painelConteudo.removeAll();
        painelConteudo.setLayout(new BorderLayout());

        // Título da tela
        JLabel titulo = new JLabel("Atualizar Produto");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(25, 40, 20, 40));

        painelConteudo.add(titulo, BorderLayout.NORTH);

        // ============================================
        // FORMULÁRIO
        // ============================================

        JPanel formulario = new JPanel(
            new GridLayout(5, 2, 15, 15)
        );

        formulario.setBorder(
            new EmptyBorder(20, 30, 20, 30)
        );

        formulario.setPreferredSize(
            new Dimension(650, 280)
        );

        // Campos de texto
        JTextField campoId = new JTextField();
        JTextField campoNome = new JTextField();
        JTextField campoPreco = new JTextField();
        JTextField campoQuantidade = new JTextField();

        // Botões
        JButton botaoBuscar = new JButton("Buscar Produto");
        JButton botaoSalvar = new JButton("Salvar Alterações");

        // Adiciona os componentes ao formulário
        formulario.add(new JLabel("ID do Produto:"));
        formulario.add(campoId);

        formulario.add(new JLabel(""));
        formulario.add(botaoBuscar);

        formulario.add(new JLabel("Nome do Produto:"));
        formulario.add(campoNome);

        formulario.add(new JLabel("Preço:"));
        formulario.add(campoPreco);

        formulario.add(new JLabel("Quantidade:"));
        formulario.add(campoQuantidade);

        // ============================================
        // BUSCAR PRODUTO
        // ============================================

        botaoBuscar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(
                    campoId.getText().trim()
                );

                Produto produto =
                    produtoRepository.buscarPorId(id);

                if (produto == null) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Produto não encontrado!"
                    );

                    return;
                }

                // Preenche os campos automaticamente
                campoNome.setText(produto.getNome());

                campoPreco.setText(
                    String.valueOf(produto.getPreco())
                );

                campoQuantidade.setText(
                    String.valueOf(produto.getQuantidade())
                );

            } catch (NumberFormatException erro) {

                JOptionPane.showMessageDialog(
                    this,
                    "Informe um ID válido!"
                );
            }
        });

        // ============================================
        // SALVAR ALTERAÇÕES
        // ============================================

        botaoSalvar.addActionListener(e -> {

            try {

                int id = Integer.parseInt(
                    campoId.getText().trim()
                );

                String nome = campoNome.getText().trim();

                double preco = Double.parseDouble(
                    campoPreco.getText()
                        .trim()
                        .replace(",", ".")
                );

                int quantidade = Integer.parseInt(
                    campoQuantidade.getText().trim()
                );

                // Busca o produto cadastrado
                Produto produto =
                    produtoRepository.buscarPorId(id);

                if (produto == null) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Produto não encontrado!"
                    );

                    return;
                }

                // Valida os dados
                if (nome.isEmpty() ||
                    preco < 0 ||
                    !Double.isFinite(preco) ||
                    quantidade < 0) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Preencha os campos corretamente!"
                    );

                    return;
                }

                // Atualiza os dados do produto
                produto.setNome(nome);
                produto.setPreco(preco);
                produto.setQuantidade(quantidade);

                // Salva as alterações no MySQL
                boolean atualizado = produtoRepository.atualizar(produto);

                if (atualizado) {

                        JOptionPane.showMessageDialog(
                        this,
                        "Produto atualizado com sucesso no MySQL!"
                );

                atualizarPainel();

        } else {

        JOptionPane.showMessageDialog(
                this,
                "Não foi possível atualizar o produto!"
        );
}

            } catch (NumberFormatException erro) {

                JOptionPane.showMessageDialog(
                    this,
                    "Verifique o ID, preço e quantidade!"
                );
            }
        });

        // ============================================
        // ORGANIZAÇÃO VISUAL
        // ============================================

        // Evita que os campos fiquem enormes
        JPanel painelCentral = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 20, 20)
        );

        painelCentral.add(formulario);

        painelConteudo.add(
            painelCentral,
            BorderLayout.CENTER
        );

        // Painel inferior com botão de salvar
        JPanel painelBotoes = new JPanel();

        botaoSalvar.setPreferredSize(
            new Dimension(200, 40)
        );

        painelBotoes.add(botaoSalvar);

        painelConteudo.add(
            painelBotoes,
            BorderLayout.SOUTH
        );

        // Atualiza a interface
        atualizarPainel();
    }

   // ==========================================
    // CONSULTAR ESTOQUE
    // ==========================================


    private void mostrarEstoque() {

        painelConteudo.removeAll();

        painelConteudo.setLayout(
                new BorderLayout()
        );

        JLabel titulo =
                new JLabel("Estoque de Produtos");

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        titulo.setBorder(
                new EmptyBorder(25, 30, 20, 30)
        );

        painelConteudo.add(
                titulo,
                BorderLayout.NORTH
        );

        // Colunas da tabela
        String[] colunas = {
                "ID",
                "Produto",
                "Preço",
                "Quantidade",
                "Status"
        };

        DefaultTableModel modelo =
                new DefaultTableModel(
                        colunas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        // Percorre todos os produtos cadastrados
        for (Produto produto :
                produtoRepository.listarTodos()) {

            String status;

            if (produto.getQuantidade() > 0) {

                status = "Em estoque";

            } else {

                status = "Sem estoque";
            }

            modelo.addRow(
                    new Object[]{
                            produto.getId(),
                            produto.getNome(),

                            String.format(
                                    "R$ %.2f",
                                    produto.getPreco()
                            ),

                            produto.getQuantidade(),

                            status
                    }
            );
        }

        JTable tabela =
                new JTable(modelo);

        tabela.setRowHeight(32);

        tabela.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        tabela.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        tabela.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll =
                new JScrollPane(tabela);

        scroll.setBorder(
                new EmptyBorder(
                        10,
                        30,
                        30,
                        30
                )
        );

        painelConteudo.add(
                scroll,
                BorderLayout.CENTER
        );

        atualizarPainel();
    }


   
    // ==========================================
    // ATUALIZA A INTERFACE
    // ==========================================

    private void atualizarPainel() {

        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    // ==========================================
    // INICIALIZA O PROGRAMA
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            TelaPrincipal tela =
                    new TelaPrincipal();

            tela.setVisible(true);
        });
    }
    
    // ============================================
    // HISTÓRICO DE MOVIMENTAÇÕES
    // ============================================

    private void mostrarHistorico() {

        // Limpa a tela anterior
        painelConteudo.removeAll();
        painelConteudo.setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Histórico de Movimentações");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setBorder(new EmptyBorder(25, 40, 20, 40));

        painelConteudo.add(titulo, BorderLayout.NORTH);

        // Colunas da tabela
        String[] colunas = {
            "ID",
            "Produto",
            "Tipo",
            "Quantidade"
        };

        // Modelo da tabela
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        // Percorre as movimentações registradas
        for (Movimentacao movimentacao :
                movimentacaoRepository.listarTodos()) {

            Object[] linha = {
                movimentacao.getId(),
                movimentacao.getProduto().getNome(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade()
            };

            modelo.addRow(linha);
        }

        // Cria a tabela
        JTable tabela = new JTable(modelo);

        tabela.setRowHeight(32);
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));

        tabela.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        tabela.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        // Adiciona barra de rolagem
        JScrollPane scroll = new JScrollPane(tabela);

        scroll.setBorder(
            new EmptyBorder(10, 30, 30, 30)
        );

        painelConteudo.add(scroll, BorderLayout.CENTER);

        // Atualiza a tela
        atualizarPainel();
    }

}