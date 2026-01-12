package View;

import model.entities.Cliente;
import model.entities.PessoaFisica;
import model.entities.PessoaJuridica;
import repositories.BancoDeDados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class TelaListagemClientes extends JFrame {

    private BancoDeDados bancoDeDados;
    private JFrame telaAnterior;

    public TelaListagemClientes(BancoDeDados bancoDeDados, JFrame telaAnterior) {
        this.bancoDeDados = bancoDeDados;
        this.telaAnterior = telaAnterior;

        setTitle("Listagem de Clientes - Quixas Bank");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Clientes Cadastrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        String[] colunas = {"Nome", "Documento (CPF/CNPJ)", "Tipo"};

        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        carregarDadosNaTabela(model);

        JTable tabelaClientes = new JTable(model);
        tabelaClientes.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaClientes.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                telaAnterior.setVisible(true);
                dispose();
            }
        });

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnVoltar);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarDadosNaTabela(DefaultTableModel model) {
        try {
            ArrayList<Cliente> clientes = bancoDeDados.lerClientes();

            if (clientes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado.");
            }

            for (Cliente c : clientes) {
                String documento = "N/A";
                String tipo = "Indefinido";

                if (c instanceof PessoaFisica) {
                    PessoaFisica pf = (PessoaFisica) c;
                    documento = pf.getCpf();
                    tipo = "Pessoa Física";
                }
                else if (c instanceof PessoaJuridica) {
                    PessoaJuridica pj = (PessoaJuridica) c;
                    documento = pj.getCnpj();
                    tipo = "Pessoa Jurídica";
                }

                Object[] linha = {
                        c.getNome(),
                        documento,
                        tipo
                };
                model.addRow(linha);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}