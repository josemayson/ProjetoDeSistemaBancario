package View;

import model.entities.*;
import repositories.BancoDeDados;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TelaCadastro extends JFrame {

    private Banco banco;
    private BancoDeDados bancoDeDados;

    public TelaCadastro(Banco banco, BancoDeDados bancoDeDados) {
        this.banco = banco;
        this.bancoDeDados = bancoDeDados;

        setTitle("Cadastros - Sistema Bancário");
        setSize(600, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.add("Cadastrar Cliente", painelCliente());
        abas.add("Abrir Conta", painelConta());

        add(abas);
    }

    private JPanel painelCliente() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtNome = new JTextField();
        JTextField txtEndereco = new JTextField();
        String[] tipos = {"Pessoa Física", "Pessoa Jurídica"};
        JComboBox<String> cmbTipo = new JComboBox<>(tipos);

        JTextField txtDoc = new JTextField();
        JTextField txtData = new JTextField();
        JTextField txtRazaoSocial = new JTextField();

        JLabel lblDoc = new JLabel("CPF:");
        JLabel lblData = new JLabel("Data Nasc (dd/MM/yyyy):");
        JLabel lblRazao = new JLabel("Razão Social:");

        txtRazaoSocial.setEnabled(false);
        txtRazaoSocial.setText("N/A");
        txtRazaoSocial.setBackground(Color.LIGHT_GRAY);

        cmbTipo.addActionListener(e -> {
            if (cmbTipo.getSelectedIndex() == 0) { // PF
                lblDoc.setText("CPF:");
                lblData.setText("Data Nasc (dd/MM/yyyy):");
                txtRazaoSocial.setEnabled(false);
                txtRazaoSocial.setText("N/A");
                txtRazaoSocial.setBackground(Color.LIGHT_GRAY);
            } else { // PJ
                lblDoc.setText("CNPJ:");
                lblData.setText("Data Fund. (dd/MM/yyyy):");
                txtRazaoSocial.setEnabled(true);
                txtRazaoSocial.setText("");
                txtRazaoSocial.setBackground(Color.WHITE);
            }
        });

        JButton btnSalvar = new JButton("Salvar Cliente");
        btnSalvar.addActionListener(e -> {
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
        panel.add(new JLabel(""));
        panel.add(btnSalvar);

        return panel;
    }

    private JPanel painelConta() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtDocCliente = new JTextField();
        String[] tiposConta = {"Corrente", "Poupanca"};
        JComboBox<String> cmbTipoConta = new JComboBox<>(tiposConta);

        JButton btnCriar = new JButton("Criar Conta");

        btnCriar.addActionListener(e -> {
            try {
                String doc = txtDocCliente.getText();
                ArrayList<Cliente> listaClientes = bancoDeDados.lerClientes();
                Cliente titular = bancoDeDados.stringParaCliente(doc, listaClientes);

                if (titular == null) {
                    Utilitarios.msgErro("Cliente não encontrado (CPF/CNPJ: " + doc + ")");
                    return;
                }

                String tipo = (String) cmbTipoConta.getSelectedItem();

                // --- CHAMA O MÉTODO SEM LIMITE (O BANCO PÕE FIXO) ---
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
        });

        panel.add(new JLabel("CPF/CNPJ do Titular:"));
        panel.add(txtDocCliente);
        panel.add(new JLabel("Tipo de Conta:"));
        panel.add(cmbTipoConta);
        panel.add(new JLabel(""));
        panel.add(btnCriar);

        panel.add(new JLabel("")); panel.add(new JLabel(""));
        panel.add(new JLabel("")); panel.add(new JLabel(""));

        return panel;
    }
}