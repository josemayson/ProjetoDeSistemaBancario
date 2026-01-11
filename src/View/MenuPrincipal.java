package View;

import model.entities.*;
import repositories.BancoDeDados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private Banco banco;
    private BancoDeDados bancoDeDados;

    public MenuPrincipal() {
        banco = new Banco("Quixas Bank", "001");
        bancoDeDados = new BancoDeDados();
        carregarDados();

        setTitle("Sistema Bancário - Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Bem-vindo ao " + banco.getNomeDoBanco(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));

        Font fonteBotoes = new Font("Arial", Font.PLAIN, 22);

        JButton btnCadastro = new JButton("Cadastros (Clientes e Contas)");
        JButton btnOperacoes = new JButton("Operações Bancárias");
        JButton btnSair = new JButton("Sair");

        btnCadastro.setFont(fonteBotoes);
        btnOperacoes.setFont(fonteBotoes);
        btnSair.setFont(fonteBotoes);
        btnCadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new TelaCadastro(banco, bancoDeDados, MenuPrincipal.this).setVisible(true);
            }
        });

        btnOperacoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new TelaOperacoes(banco, bancoDeDados, MenuPrincipal.this).setVisible(true);
            }
        });

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(lblTitulo);
        add(btnCadastro);
        add(btnOperacoes);
        add(btnSair);
    }

    private void carregarDados() {
        try {
            ArrayList<Cliente> clientesCarregados = bancoDeDados.lerClientes();
            for (Cliente c : clientesCarregados) {
                banco.adicionarCliente(c);
            }
            ArrayList<Conta> contasCarregadas = bancoDeDados.lerContas(clientesCarregados);
        } catch (IOException e) {
            System.out.println("Nenhum dado anterior encontrado ou erro ao ler: " + e.getMessage());
        }
    }
}