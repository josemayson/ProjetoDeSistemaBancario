package model.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    @Override
    public String paraArquivo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "PF;" + getNome() + ";" + getEndereco() + ";" + getCpf() + ";" + getDataNascimento().format(fmt);
    }

    @Override
    public String toString() {
        return "PF, " + super.toString() + ", CPF: " + cpf + ", Data de nascimento: " + dataNascimento;
    }
}
