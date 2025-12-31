package model.entities;

import java.time.LocalDate;

public class PessoaFisica extends Cliente {

    private String cpf;
    private LocalDate dataNascimento;

    public PessoaFisica(String nome, String endereco, String cpf, LocalDate dataNascimento) {
        super(nome, endereco);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

}
