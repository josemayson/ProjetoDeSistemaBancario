package model.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PessoaJuridica extends Cliente {

    private String cnpj;
    private LocalDate dataFundacao;
    private String razaoSocial;

    public PessoaJuridica(String nome, String endereco, String cnpj, LocalDate dataFundacao, String razaoSocial) {
        super(nome, endereco);
        this.cnpj = cnpj;
        this.dataFundacao = dataFundacao;
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public LocalDate getDataFundacao() {
        return dataFundacao;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    @Override
    public String paraArquivo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "PJ;" + getNome() + ";" + getEndereco() + ";" + getCnpj() + ";" + getDataFundacao().format(fmt) + ";" + getRazaoSocial();
    }

    @Override
    public String toString() {
        return "PF, " + super.toString() + ", CNPJ: " + cnpj + ", Data de fundacao: " + dataFundacao + ", Razao social: " + razaoSocial + ".";
    }
}