package repositories;

import model.entities.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BancoDeDados {
    private static String ARQClientes = "Cliente.txt";
    private static String ARQContas = "Contas.txt";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void salvarContas(Conta conta) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQContas, true))) {
            String novaConta = conta.paraArquivo();
            bw.write(novaConta);
            bw.newLine();
        }
    }

    public void salvarClientes(Cliente cliente) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQClientes, true))) {
            String novoCliente = cliente.paraArquivo();
            bw.write(novoCliente);
            bw.newLine();
        }
    }

    public ArrayList<Cliente> lerClientes() throws IOException {
        ArrayList<Cliente> listarClientes = new ArrayList<>();
        File arquivo = new File(ARQClientes);

        if (!arquivo.exists()) {
            return listarClientes;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dado = linha.split(";");
                Cliente cliente = null;
                String tipo = dado[0];
                if (tipo.equalsIgnoreCase("pf")) {
                    cliente = new PessoaFisica(dado[1], dado[2], dado[3], LocalDate.parse(dado[4], formatter));
                } else if (tipo.equalsIgnoreCase("pj")) {
                    cliente = new PessoaJuridica(dado[1], dado[2], dado[3], LocalDate.parse(dado[4], formatter), dado[5]);
                }

                if (cliente != null) {
                    listarClientes.add(cliente);
                }
            }
        }
        return listarClientes;
    }

    public ArrayList<Conta> lerContas(ArrayList<Cliente> clienteCadastrado) throws IOException {
        ArrayList<Conta> listarContas = new ArrayList<>();
        File arquivo = new File(ARQContas);

        if (!arquivo.exists()) {
            return listarContas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                String dado[] = linha.split(";");
                String tipo = dado[0];
                Integer numero = Integer.parseInt(dado[1]);
                Integer agencia = Integer.parseInt(dado[2]);
                double saldoArquivo = Double.parseDouble(dado[3]);
                String documentoTitular = dado[4];
                Cliente clienteTitular = stringParaCliente(documentoTitular, clienteCadastrado);
                Conta conta = null;
                if (tipo.equalsIgnoreCase("CP")) {
                    conta = new ContaPoupanca(numero, agencia, clienteTitular);
                } else if (tipo.equalsIgnoreCase("CC")) {
                    double limite = Double.parseDouble(dado[5]);
                    conta = new ContaCorrente(numero, agencia, clienteTitular, limite);
                }

                if (conta != null) {
                    conta.depositar(saldoArquivo);
                    listarContas.add(conta);
                }
            }


        }
        return listarContas;
    }

    public Cliente stringParaCliente(String documento, ArrayList<Cliente> listaClientes) {

        for (Cliente c : listaClientes) {
            if (c instanceof PessoaFisica) {
                if (((PessoaFisica) c).getCpf().equals(documento)) {
                    return c;
                }
            } else if (c instanceof PessoaJuridica) {
                if (((PessoaJuridica) c).getCnpj().equals(documento)) {
                    return c;
                }
            }
        }
        return null;
    }
}
