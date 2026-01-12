package View;

import model.entities.*;
import repositories.BancoDeDados;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TelaCadastro extends JFrame {

    private Banco banco;
    private BancoDeDados bancoDeDados;
    private MenuPrincipal menuPrincipal;

    public TelaCadastro(Banco banco, BancoDeDados bancoDeDados, MenuPrincipal menuPrincipal) {
        this.banco = banco;
        this.bancoDeDados = bancoDeDados;
        this.menuPrincipal = menuPrincipal;

        setTitle("Cadastros - Sistema Bancário");
        setSize(600, 550);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dispose();
                menuPrincipal.setVisible(true);
            }
        });

        JTabbedPane abas = new JTabbedPane();
        abas.add("Cadastrar Cliente", painelCliente());
        abas.add("Abrir Conta", painelConta());

        add(abas);
    }

    private JPanel painelCliente() {
        final JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        final JTextField txtNome = new JTextField();
        final JTextField txtEndereco = new JTextField();
        String[] tipos = {"Pessoa Física", "Pessoa Jurídica"};
        final JComboBox<String> cmbTipo = new JComboBox<>(tipos);

        final JTextField txtDoc = new JTextField();
        final JTextField txtData = new JTextField();
        final JTextField txtRazaoSocial = new JTextField();

        final JLabel lblDoc = new JLabel("CPF:");
        final JLabel lblData = new JLabel("Data Nasc (dd/MM/yyyy):");
        final JLabel lblRazao = new JLabel("Razão Social:");

        txtRazaoSocial.setEnabled(false);
        txtRazaoSocial.setText("N/A");
        txtRazaoSocial.setBackground(Color.LIGHT_GRAY);

        cmbTipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbTipo.getSelectedIndex() == 0) { // PF
                    lblDoc.setText("CPF:");
                    lblData.setText("Data Nasc (dd/MM/yyyy):");
                    txtRazaoSocial.setEnabled(false);
                    txtRazaoSocial.setText("N/A");
                    txtRazaoSocial.setBackground(Color.LIGHT_GRAY);
                } else {
                    lblDoc.setText("CNPJ:");
                    lblData.setText("Data Fund. (dd/MM/yyyy):");
                    txtRazaoSocial.setEnabled(true);
                    txtRazaoSocial.setText("");
                    txtRazaoSocial.setBackground(Color.WHITE);
                }
            }
        });

        JButton btnSalvar = new JButton("Salvar Cliente");
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nome = txtNome.getText();
                    String end = txtEndereco.getText();
                    String doc = txtDoc.getText();
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate data = LocalDate.parse(txtData.getText(), fmt);

                    Cliente novoCliente = null;

                    if (cmbTipo.getSelectedIndex() == 0) {
                        novoCliente = new PessoaFisica(nome, end, doc, data);
                    } else {
                        String razao = txtRazaoSocial.getText();
                        if(razao.isEmpty()) throw new Exception("Razão Social vazia");
                        novoCliente = new PessoaJuridica(nome, end, doc, data, razao);
                    }

                    if (banco.adicionarCliente(novoCliente)) {
                        bancoDeDados.salvarClientes(novoCliente);
                        Utilitarios.msgSucesso("Cliente cadastrado com sucesso!");
                        Utilitarios.limparCampos(panel);
                        if (cmbTipo.getSelectedIndex() == 0) txtRazaoSocial.setText("N/A");
                    } else {
                        Utilitarios.msgErro("Erro ao adicionar.");
                    }

                } catch (Exception ex) {
                    Utilitarios.msgErro("Erro nos dados: " + ex.getMessage());
                }
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                menuPrincipal.setVisible(true);
            }
        });

        panel.add(new JLabel("Tipo de Cliente:"));
        panel.add(cmbTipo);
        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Endereço:"));
        panel.add(txtEndereco);
        panel.add(lblDoc);
        panel.add(txtDoc);
        panel.add(lblData);
        panel.add(txtData);
        panel.add(lblRazao);
        panel.add(txtRazaoSocial);
        panel.add(btnVoltar);
        panel.add(btnSalvar);

        return panel;
    }

    private JPanel painelConta() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        final JTextField txtDocCliente = new JTextField();
        String[] tiposConta = {"Corrente", "Poupanca"};
        final JComboBox<String> cmbTipoConta = new JComboBox<>(tiposConta);

        JButton btnCriar = new JButton("Criar Conta");

        btnCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String doc = txtDocCliente.getText();
                    ArrayList<Cliente> listaClientes = bancoDeDados.lerClientes();
                    Cliente titular = bancoDeDados.stringParaCliente(doc, listaClientes);

                    if (titular == null) {
                        Utilitarios.msgErro("Cliente não encontrado (CPF/CNPJ: " + doc + ")");
                        return;
                    }

                    String tipo = (String) cmbTipoConta.getSelectedItem();

                    boolean sucesso = banco.abrirConta(titular, tipo);

                    if (sucesso) {
                        List<Conta> contasCli = titular.consultarContas();
                        if (!contasCli.isEmpty()) {
                            Conta novaConta = contasCli.get(contasCli.size() - 1);
                            bancoDeDados.salvarContas(novaConta);

                            Utilitarios.msgSucesso("Conta criada com sucesso! Nº: " + novaConta.getNumero());
                            txtDocCliente.setText("");
                        }
                    } else {
                        Utilitarios.msgErro("Erro ao criar conta.");
                    }

                } catch (IOException ex) {
                    Utilitarios.msgErro("Erro de arquivo: " + ex.getMessage());
                } catch (Exception ex) {
                    Utilitarios.msgErro("Erro inesperado: " + ex.getMessage());
                }
            }
        });

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                menuPrincipal.setVisible(true);
            }
        });

        panel.add(new JLabel("CPF/CNPJ do Titular:"));
        panel.add(txtDocCliente);
        panel.add(new JLabel("Tipo de Conta:"));
        panel.add(cmbTipoConta);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        panel.add(btnVoltar);
        panel.add(btnCriar);

        return panel;
    }
}