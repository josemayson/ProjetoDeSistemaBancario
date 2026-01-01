package model.entities;

public class ContaCorrente extends Conta implements Tributavel {

    private double limiteChequeEspecial;
    private static double imposto = 1.0;

    public ContaCorrente(Integer numero, Integer agencia, Cliente titular, double limite) {
        super(numero, agencia, titular);
        this.limiteChequeEspecial = limite;
    }


    @Override
    public boolean sacar(double valorSacar) {
        if (valorSacar > saldo + this.limiteChequeEspecial) {
            return false;
        }
        saldo -= valorSacar;
        historico.add(new Transacao("Saque", valorSacar));
        return true;
    }

    @Override
    public double calcularImposto() {
        double taxaimposto = saldo * (imposto / 100);
        this.saldo -= taxaimposto;
        historico.add(new Transacao("Imposto", taxaimposto));
        return taxaimposto;
    }

    public Double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public String paraArquivo() {
        String documento = "";
        if (titular instanceof PessoaFisica) {
            documento = ((PessoaFisica) titular).getCpf();
        } else if (titular instanceof PessoaJuridica) {
            documento = ((PessoaJuridica) titular).getCnpj();
        }
        return "CC;" + numero + ";" + agencia + ";" + saldo + ";" + documento + ";" + limiteChequeEspecial;
    }

    @Override
    public String toString() {
        return super.toString() + ", Limite de cheque especial: " + limiteChequeEspecial;
    }
}
