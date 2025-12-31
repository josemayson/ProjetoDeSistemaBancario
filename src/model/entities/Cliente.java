package model.entities;

import java.util.ArrayList;
import java.util.List;
public abstract class Cliente {

    private String nome;
    private String endereco;
    private List<Conta> contas = new ArrayList<>();

    public Cliente(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public boolean atualizarDados(String nome, String endereco) {
        if(nome != null && endereco != null) {
            this.nome = nome;
            this.endereco = endereco;
            return true;
        }
        return false;
    }

    public boolean adicionarConta(Conta conta) {
        if(conta != null) {
            contas.add(conta);
            return true;
        }
        return false;
    }

    public List<Conta> consultarContas() {
        return this.contas;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String toString() {
        return "Nome: " + nome + ", Endereco: " + endereco;
    }

}
