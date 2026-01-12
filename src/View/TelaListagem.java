package View;

import model.entities.*;
import repositories.BancoDeDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class TelaListagem extends JFrame {

    private BancoDeDados bancoDeDados;
    private MenuPrincipal menuPrincipal;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaListagem(BancoDeDados bancoDeDados, MenuPrincipal menuPrincipal) {
        this.bancoDeDados = bancoDeDados;
        this.menuPrincipal = menuPrincipal;

        setTitle("Relatório de Contas");
        setSize(800, 500);
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

        JLabel lblTitulo = new JLabel("Contas Cadastradas no Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] colunas = {"Nº Conta", "Agência", "Tipo", "Titular", "Documento", "Saldo (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25); // Altura da linha
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(tabela);

        JPanel panelBotoes = new JPanel(new FlowLayout());
        JButton btnAtualizar = new JButton("Atualizar Lista");
        JButton btnVoltar = new JButton("Voltar ao Menu");

        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnVoltar);


        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDadosNaTabela();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                menuPrincipal.setVisible(true);
            }
        });

        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);

        carregarDadosNaTabela();
    }

    private void carregarDadosNaTabela() {
        modeloTabela.setRowCount(0);

        try {
            ArrayList<Cliente> clientes = bancoDeDados.lerClientes();
            ArrayList<Conta> contas = bancoDeDados.lerContas(clientes);

            if (contas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma conta encontrada no arquivo.");
                return;
            }

            for (Conta c : contas) {
                String tipo = (c instanceof ContaCorrente) ? "Corrente" : "Poupança";
                if (c instanceof ContaCorrente || c instanceof ContaPoupanca) {
                }
                String saldoFormatado = String.format("%.2f", c.verSaldo());

                Object[] linha = {
                        c.getNumero(),
                        101,
                        tipo,
                        "Ver Detalhes",
                        "---",
                        saldoFormatado
                };

                modeloTabela.addRow(linha);
            }

        } catch (IOException e) {
            Utilitarios.msgErro("Erro ao ler arquivo: " + e.getMessage());
        }
    }
}