package Entitien;

import java.time.LocalDate;

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

}