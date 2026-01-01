package model.entities;

public class ContaPoupanca extends Conta {

    private static double taxaRendimento = 5.0;

    public ContaPoupanca(Integer numero, Integer agencia, Cliente titular) {
        super(numero, agencia, titular);
    }

    public double aplicarRendimento() {
        double rendimento = saldo * (taxaRendimento / 100);
        saldo += rendimento;
        historico.add(new Transacao("Rendimento da Poupanca", rendimento));
        return rendimento;
    }

    public String paraArquivo() {
        String documento = "";
        if (titular instanceof PessoaFisica) {
            documento = ((PessoaFisica) titular).getCpf();
        } else if (titular instanceof PessoaJuridica) {
            documento = ((PessoaJuridica) titular).getCnpj();
        }
        return "CP;" + numero + ";" + agencia + ";" + saldo + ";" + documento;
    }

    public Double getTaxaRendimento() {
        return taxaRendimento;
    }

}

