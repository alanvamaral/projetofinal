package com.unileste.projetofinal.entidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.unileste.projetofinal.utilitarios.SaldoInsuficienteException;

public class ContaCorrente extends Conta {

    private final double limiteChequeEspecial;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ContaCorrente(String numero, Cliente proprietario, double limiteChequeEspecial) {
        super(numero, proprietario);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    @Override
    public void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        this.saldo += valor;
        adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - DEPÓSITO: +" + valor + " | Saldo Atual: " + this.saldo);
    }

    @Override
    public void sacar(double valor) throws SaldoInsuficienteException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser positivo.");
        }

        double totalDisponivel = this.saldo + this.limiteChequeEspecial;

        if (valor > totalDisponivel) {
            throw new SaldoInsuficienteException("Saldo insuficiente. O valor máximo para saque é: R$ " + totalDisponivel);
        }

        this.saldo -= valor;
        adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - SAQUE CC: -" + valor + " | Saldo Atual: " + this.saldo);
    }

    @Override
    public void transferir(Conta destino, double valor) throws SaldoInsuficienteException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

        // 1. Tenta sacar da conta de origem (o saque na CC já considera o cheque especial)
        sacar(valor);

        // 2. Se o saque for bem-sucedido, deposita na conta de destino
        destino.depositar(valor);

        adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - TRANSFERÊNCIA ENVIADA para conta " + destino.getNumero() + ": -" + valor + " | Saldo Atual: " + this.saldo);
        destino.adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - TRANSFERÊNCIA RECEBIDA de conta " + this.getNumero() + ": +" + valor + " | Saldo Atual: " + destino.getSaldo());
    }

    @Override
    public String toString() {
        return "Conta Corrente [número=" + getNumero() + ", saldo=" + saldo + ", limiteChequeEspecial=" + limiteChequeEspecial + ", proprietário=" + getProprietario().getNome() + "]";
    }
}
