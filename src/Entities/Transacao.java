package Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transacao {

    private LocalDateTime data;
    private String tipo_de_transacao;
    private Double valTransacao;

    private static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public Transacao(String tipoTransacao, double valTransacao){
        this.tipo_de_transacao = tipoTransacao;
        this.valTransacao = valTransacao;
        this.data = LocalDateTime.now();
    }

    public String registrar() {

        return "Data da Transacao: " + formato.format(data)
                + ", Tipo da Transacao: " + tipo_de_transacao
                + ", Valor da Transacao: " + String.format("%.2f", valTransacao);

    }

    public LocalDateTime getData() {
        return data;
    }

    public String getTipo_de_transacao() {
        return tipo_de_transacao;
    }

    public Double getValTransacao() {
        return valTransacao;
    }

}