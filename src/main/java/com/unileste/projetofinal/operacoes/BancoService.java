package com.unileste.projetofinal.operacoes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unileste.projetofinal.dao.ClienteDAO;
import com.unileste.projetofinal.dao.ContaDAO;
import com.unileste.projetofinal.entidades.Cliente;
import com.unileste.projetofinal.entidades.Conta;
import com.unileste.projetofinal.entidades.ContaCorrente;
import com.unileste.projetofinal.entidades.ContaPoupanca;
import com.unileste.projetofinal.utilitarios.ClienteNaoEncontradoException;
import com.unileste.projetofinal.utilitarios.ContaNaoEncontradaException;
import com.unileste.projetofinal.utilitarios.SaldoInsuficienteException;

public class BancoService {

    private final ClienteDAO clienteDAO;
    private final ContaDAO contaDAO;
    private final Map<String, Cliente> clientesPorCpf;
    private final Map<String, Conta> contasPorNumero;

    public BancoService(ClienteDAO clienteDAO, ContaDAO contaDAO) {
        this.clienteDAO = clienteDAO;
        this.contaDAO = contaDAO;
        this.clientesPorCpf = new HashMap<>();
        this.contasPorNumero = new HashMap<>();
    }

    public void cadastrarCliente(Cliente cliente) throws Exception {
        if (clientesPorCpf.containsKey(cliente.getCpf())) {
            throw new IllegalArgumentException("Cliente com CPF " + cliente.getCpf() + " já cadastrado.");
        }
        clienteDAO.inserir(cliente);
        clientesPorCpf.put(cliente.getCpf(), cliente);
        System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso.");
    }

    public Cliente buscarClientePorCpf(String cpf) throws ClienteNaoEncontradoException, Exception {
        Cliente cliente = clientesPorCpf.get(cpf);
        if (cliente == null) {
            cliente = clienteDAO.buscarPorCpf(cpf);
            if (cliente == null) {
                throw new ClienteNaoEncontradoException("Cliente com CPF " + cpf + " não encontrado.");
            }
            clientesPorCpf.put(cpf, cliente);
        }
        return cliente;
    }

    public void atualizarCliente(Cliente cliente) throws Exception {
        clienteDAO.atualizar(cliente);
        clientesPorCpf.put(cliente.getCpf(), cliente);
        System.out.println("Cliente " + cliente.getNome() + " atualizado com sucesso.");
    }

    public void deletarCliente(String cpf) throws Exception {
        clienteDAO.deletar(cpf);
        clientesPorCpf.remove(cpf);
        System.out.println("Cliente com CPF " + cpf + " deletado com sucesso.");
    }

    public List<Cliente> listarClientes() throws Exception {
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            clientesPorCpf.put(c.getCpf(), c);
        }
        return clientes;
    }

    public void abrirConta(String numeroConta, String tipo, String cpfCliente, double limiteChequeEspecial)
            throws Exception {
        Cliente cliente = buscarClientePorCpf(cpfCliente);

        Conta novaConta;
        if ("corrente".equalsIgnoreCase(tipo)) {
            novaConta = new ContaCorrente(numeroConta, cliente, limiteChequeEspecial);
        } else if ("poupanca".equalsIgnoreCase(tipo)) {
            novaConta = new ContaPoupanca(numeroConta, cliente);
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido. Use 'corrente' ou 'poupanca'.");
        }

        contaDAO.inserir(novaConta);
        contasPorNumero.put(numeroConta, novaConta);
        cliente.adicionarConta(novaConta);
        System.out.println("Conta " + numeroConta + " (" + tipo + ") criada com sucesso.");
    }

    public Conta buscarContaPorNumero(String numero) throws ContaNaoEncontradaException, Exception {
        Conta conta = contasPorNumero.get(numero);
        if (conta == null) {
            conta = contaDAO.buscarPorNumero(numero);
            if (conta == null) {
                throw new ContaNaoEncontradaException("Conta número " + numero + " não encontrada.");
            }
            contasPorNumero.put(numero, conta);
        }
        return conta;
    }

    public void depositar(String numeroConta, double valor) throws Exception {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.depositar(valor);
        contaDAO.atualizar(conta);
        System.out.println("Depósito de R$ " + valor + " realizado com sucesso na conta " + numeroConta + ".");
    }

    public void sacar(String numeroConta, double valor) throws Exception {
        Conta conta = buscarContaPorNumero(numeroConta);
        try {
            conta.sacar(valor);
        } catch (SaldoInsuficienteException e) {
            throw e;
        }
        contaDAO.atualizar(conta);
        System.out.println("Saque de R$ " + valor + " realizado com sucesso da conta " + numeroConta + ".");
    }

    public void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) throws Exception {
        Conta contaOrigem = buscarContaPorNumero(numeroContaOrigem);
        Conta contaDestino = buscarContaPorNumero(numeroContaDestino);

        try {
            contaOrigem.transferir(contaDestino, valor);
        } catch (SaldoInsuficienteException e) {
            throw e;
        }

        contaDAO.atualizar(contaOrigem);
        contaDAO.atualizar(contaDestino);
        System.out.println("Transferência de R$ " + valor + " realizada com sucesso de " + numeroContaOrigem
                + " para " + numeroContaDestino + ".");
    }

    public List<Conta> listarContasPorCliente(String cpfCliente) throws Exception {
        List<Conta> contas = contaDAO.listarPorCliente(cpfCliente);
        for (Conta c : contas) {
            contasPorNumero.put(c.getNumero(), c);
        }
        return contas;
    }

    public List<Conta> listarTodasContas() throws Exception {
        List<Conta> contas = contaDAO.listarTodas();
        for (Conta c : contas) {
            contasPorNumero.put(c.getNumero(), c);
        }
        return contas;
    }

    public Map<String, Cliente> getClientesPorCpf() {
        return clientesPorCpf;
    }

    public Map<String, Conta> getContasPorNumero() {
        return contasPorNumero;
    }
}
