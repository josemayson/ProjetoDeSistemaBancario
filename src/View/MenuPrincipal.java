package View;

import model.entities.*;
import repositories.BancoDeDados;

import javax.swing.*;
import java.awt.*;
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

        btnCadastro.addActionListener(e -> {
            new TelaCadastro(banco, bancoDeDados).setVisible(true);
        });

        btnOperacoes.addActionListener(e -> {
            new TelaOperacoes(banco, bancoDeDados).setVisible(true);
        });

        btnSair.addActionListener(e -> System.exit(0));

        JButton btnCaixa = new JButton("Ir para o Caixa (Depósito/Saque)");
        btnCaixa.setFont(fonteBotoes);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
