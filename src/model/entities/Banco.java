package model.entities;

import java.util.ArrayList;
import java.util.List;

public class Banco {

    private String nomeDoBanco;
    private String codigo;
    private Integer agenciaPadrao = 101;

    private List<Cliente> clientesDoBanco = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();

    public Banco(String nomeDoBanco, String codigo) {
        this.nomeDoBanco = nomeDoBanco;
        this.codigo = codigo;
    }

    public Banco() {
        this.nomeDoBanco = "";
        this.codigo = "";
        this.agenciaPadrao = null;
    }

    public boolean adicionarCliente(Cliente cliente) {
        if (cliente != null) {
            clientesDoBanco.add(cliente);
            return true;
        }
        return false;
    }

    public boolean abrirConta(Cliente cliente, String tipo) {
        if (cliente == null) {
            return false;
        }
        int maiorNumero = 0;
        for (Conta c : contas) {
            if (c.getNumero() > maiorNumero) {
                maiorNumero = c.getNumero();
            }
        }
        int novoNumeroConta = maiorNumero + 1;
        Conta novaConta = null;
        if (tipo.equalsIgnoreCase("corrente")) {
            double chequeEspecial = 500.0;
            novaConta = new ContaCorrente(novoNumeroConta, this.agenciaPadrao, cliente, chequeEspecial);

        } else if (tipo.equalsIgnoreCase("poupanca")) {
            novaConta = new ContaPoupanca(novoNumeroConta, this.agenciaPadrao, cliente);
        } else {
            return false;
        }
        cliente.adicionarConta(novaConta);
        this.contas.add(novaConta);
        return true;
    }

    public boolean removerConta(Conta conta) {
        if (conta == null) return false;
        this.contas.remove(conta);
        if (conta.titular != null) {
            conta.titular.consultarContas().remove(conta);
        }
        return true;
    }

    public void setContas(List<Conta> contasCarregadas) {
        this.contas = contasCarregadas;
    }

    public String getNomeDoBanco() {
        return nomeDoBanco;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<Conta> getContas() {
        return contas;
    }
}
