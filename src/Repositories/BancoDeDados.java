package Repositories;

import Entities.Cliente;
import Entities.Conta;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BancoDeDados {
    private static String ARQClientes = "Cliente.txt";
    private static String ARQContas = "Contas.txt";

    public void salvarContas(Conta conta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQContas, true))) {
            String novaConta = conta.toString();
            bw.write(novaConta);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar a conta no arquivo:\n" + e.getMessage(),
                    "Erro de Gravação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void salvarClientes(Cliente cliente) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQClientes, true))) {
            String novoCliente = cliente.toString();
            bw.write(novoCliente);
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar o cliente no arquivo:\n" + e.getMessage(),
                    "Erro de Gravação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
