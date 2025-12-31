package model.entities;

public class ContaPoupanca extends Conta {

    private static double taxaRendimento = 5.0;

    public ContaPoupanca(Integer numero, Integer agencia, Cliente titular) {
        super(numero, agencia, titular);
    }

    public double aplicarRendimento(){
        double rendimento = saldo * (taxaRendimento / 100);
        saldo += rendimento;
        historico.add(new Transacao("Rendimento da Poupanca", rendimento));
        return rendimento;
    }

    public Double getTaxaRendimento() {
        return taxaRendimento;
    }

}

