package Entitien;

import java.util.ArrayList;
import java.util.List;

public class Banco {

    private String nomeDoBanco;
    private String codigo;
    private Integer agenciaPadrao = 101;
    private Integer ordem = 1;
    private List<Cliente> clientesDoBanco = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();

    public Banco(String nomeDoBanco, String codigo){
        this.nomeDoBanco = nomeDoBanco;
        this.codigo = codigo;
    }

    public boolean adicionarCliente(Cliente cliente){
        if(cliente != null) {
            clientesDoBanco.add(cliente);
            return true;
        }
        return false;
    }

    public boolean abrirConta(Cliente cliente, String tipo){
        if(cliente == null) {
            return false;
        }
        Conta novaConta = null;
        if(tipo.equalsIgnoreCase("corrente")) {
            double chequeEspecial = 1000;
            novaConta = new ContaCorrente(this.ordem, this.agenciaPadrao, cliente, chequeEspecial);
        }
        else if(tipo.equalsIgnoreCase("poupanca")) {
            novaConta = new ContaPoupanca(this.ordem, this.agenciaPadrao, cliente);
        }
        else {
            return false;
        }
        cliente.adicionarConta(novaConta);
        this.contas.add(novaConta);
        this.ordem++;
        return true;
    }

    public String getNomeDoBanco() {
        return nomeDoBanco;
    }

    public String getCodigo() {
        return codigo;
    }
}

