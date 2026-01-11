package View;

import model.entities.*;
import repositories.BancoDeDados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class TelaOperacoes extends JFrame {

    private Banco banco;
    private BancoDeDados bancoDeDados;
    private MenuPrincipal menuPrincipal;

    private ArrayList<Conta> listaDeContas;
    private Conta contaSelecionada;

    private JLabel lblSaldo;
    private JLabel lblTitular;
    private JTextArea txtExtrato;
    private JTextField txtNumConta;

    public TelaOperacoes(Banco banco, BancoDeDados bancoDeDados, MenuPrincipal menuPrincipal) {
        this.banco = banco;
        this.bancoDeDados = bancoDeDados;
        this.menuPrincipal = menuPrincipal;

        try {
            ArrayList<Cliente> clientes = bancoDeDados.lerClientes();
            this.listaDeContas = bancoDeDados.lerContas(clientes);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
            this.listaDeContas = new ArrayList<>();
        }

        setTitle("Operações Bancárias");
        setSize(700, 500);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dispose();
                menuPrincipal.setVisible(true);
            }
        });

        setLayout(new BorderLayout());

        JPanel panelTopo = new JPanel(new FlowLayout());
        txtNumConta = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar Conta");

        panelTopo.add(new JLabel("Número da Conta:"));
        panelTopo.add(txtNumConta);
        panelTopo.add(btnBuscar);

        JPanel panelDados = new JPanel(new GridLayout(2, 2));
        lblTitular = new JLabel("Titular: -");
        lblSaldo = new JLabel("Saldo: R$ 0.00");
        lblTitular.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));

        panelDados.add(lblTitular);
        panelDados.add(lblSaldo);

        txtExtrato = new JTextArea();
        txtExtrato.setEditable(false);
        txtExtrato.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollExtrato = new JScrollPane(txtExtrato);
        scrollExtrato.setBorder(BorderFactory.createTitledBorder("Extrato"));

        JPanel centroContainer = new JPanel(new BorderLayout());
        centroContainer.add(panelDados, BorderLayout.NORTH);
        centroContainer.add(scrollExtrato, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout());
        JButton btnDepositar = new JButton("Depositar");
        JButton btnSacar = new JButton("Sacar");
        JButton btnTransferir = new JButton("Transferir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnVoltar = new JButton("Voltar");

        panelBotoes.add(btnDepositar);
        panelBotoes.add(btnSacar);
        panelBotoes.add(btnTransferir);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnVoltar);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int num = Integer.parseInt(txtNumConta.getText());
                    contaSelecionada = buscarContaNaMemoria(num);
                    atualizarTela();

                    if (contaSelecionada == null) {
                        JOptionPane.showMessageDialog(TelaOperacoes.this, "Conta não encontrada.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TelaOperacoes.this, "Digite um número válido.");
                }
            }
        });

        btnDepositar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contaSelecionada == null) {
                    JOptionPane.showMessageDialog(TelaOperacoes.this, "Selecione uma conta primeiro.");
                    return;
                }
                String valorStr = JOptionPane.showInputDialog("Valor para Depósito:");
                if (valorStr != null) {
                    try {
                        double val = Double.parseDouble(valorStr);
                        if (contaSelecionada.depositar(val)) {
                            salvarAlteracoes();
                            JOptionPane.showMessageDialog(TelaOperacoes.this, "Depósito realizado!");
                            atualizarTela();
                        } else {
                            JOptionPane.showMessageDialog(TelaOperacoes.this, "Valor inválido.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(TelaOperacoes.this, "Valor inválido.");
                    }
                }
            }
        });

        btnSacar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contaSelecionada == null) {
                    JOptionPane.showMessageDialog(TelaOperacoes.this, "Selecione uma conta primeiro.");
                    return;
                }
                String valorStr = JOptionPane.showInputDialog("Valor para Saque:");
                if (valorStr != null) {
                    try {
                        double val = Double.parseDouble(valorStr);
                        if (contaSelecionada.sacar(val)) {
                            salvarAlteracoes();
                            JOptionPane.showMessageDialog(TelaOperacoes.this, "Saque realizado!");
                            atualizarTela();
                        } else {
                            JOptionPane.showMessageDialog(TelaOperacoes.this, "Saldo insuficiente ou valor inválido.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(TelaOperacoes.this, "Valor inválido.");
                    }
                }
            }
        });

        btnTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contaSelecionada == null) {
                    JOptionPane.showMessageDialog(TelaOperacoes.this, "Selecione uma conta primeiro.");
                    return;
                }

                String contaDestStr = JOptionPane.showInputDialog("Número da Conta Destino:");
                if (contaDestStr == null) return;

                String valorStr = JOptionPane.showInputDialog("Valor da Transferência:");
                if (valorStr == null) return;

                try {
                    int numDest = Integer.parseInt(contaDestStr);
                    double val = Double.parseDouble(valorStr);

                    Conta contaDestino = buscarContaNaMemoria(numDest);

                    if (contaDestino != null) {
                        if (contaSelecionada.transferir(contaDestino, val)) {
                            salvarAlteracoes();
                            JOptionPane.showMessageDialog(TelaOperacoes.this, "Transferência realizada!");
                            atualizarTela();
                        } else {
                            JOptionPane.showMessageDialog(TelaOperacoes.this, "Saldo insuficiente.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(TelaOperacoes.this, "Conta destino não encontrada.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TelaOperacoes.this, "Dados inválidos.");
                }
            }
        });

        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contaSelecionada = null;
                txtNumConta.setText("");
                atualizarTela();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                menuPrincipal.setVisible(true);
            }
        });

        add(panelTopo, BorderLayout.NORTH);
        add(centroContainer, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void atualizarTela() {
        if (contaSelecionada != null) {
            lblTitular.setText(contaSelecionada.toString());
            lblSaldo.setText(String.format("Saldo: R$ %.2f", contaSelecionada.verSaldo()));
            txtExtrato.setText(contaSelecionada.gerarExtrato());
        } else {
            lblTitular.setText("Titular: -");
            lblSaldo.setText("Saldo: R$ 0.00");
            txtExtrato.setText("");
        }
    }

    private Conta buscarContaNaMemoria(int numero) {
        if (listaDeContas == null) return null;

        for (Conta c : listaDeContas) {
            if (c.getNumero() == numero) {
                return c;
            }
        }
        return null;
    }

    private void salvarAlteracoes() {
        try {
            bancoDeDados.atualizarArquivoContas(this.listaDeContas);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "ERRO CRÍTICO ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}