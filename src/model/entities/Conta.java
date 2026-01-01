package model.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Conta {

    protected Integer numero;
    protected Integer agencia;
    protected double saldo;
    protected Cliente titular;
    protected List<Transacao> historico = new ArrayList<>();

    public Conta(Integer numero, Integer agencia, Cliente titular) {
        this.numero = numero;
        this.agencia = agencia;
        this.saldo = 0.0;
        this.titular = titular;
    }

    public boolean depositar(double valorDeposito) {
        if (valorDeposito <= 0) {
            return false;
        }
        saldo += valorDeposito;
        historico.add(new Transacao("Deposito", valorDeposito));
        return true;
    }

    public boolean sacar(double valorSaque) {
        if (saldo < valorSaque) {
            return false;
        }
        this.saldo -= valorSaque;
        historico.add(new Transacao("Saque", valorSaque));
        return true;
    }

    public boolean transferir(Conta conta, double valorTransferencia) {
        if (this.saldo < valorTransferencia) {
            return false;
        }
        this.saldo -= valorTransferencia;
        historico.add(new Transacao("Transferencia", valorTransferencia));
        conta.depositar(valorTransferencia);
        return true;
    }

    public String gerarExtrato() {
        StringBuilder registro = new StringBuilder();

        registro.append("---------- EXTRATO BANCÁRIO ----------\n");
        for (Transacao c : historico) {
            registro.append(c.registrar()).append("\n");
        }
        registro.append("--------------------------------------\n");
        return registro.toString();
    }

    public double verSaldo() {
        return saldo;
    }

    public abstract String paraArquivo();

    public String toString() {
        return "Numero: " +
                numero +
                ", Agencia: " +
                agencia +
                ", Saldo: " +
                saldo +
                ", Titular: " +
                titular;
    }

}

