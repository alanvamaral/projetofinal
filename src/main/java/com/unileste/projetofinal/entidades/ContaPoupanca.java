package com.unileste.projetofinal.entidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.unileste.projetofinal.utilitarios.SaldoInsuficienteException;

public class ContaPoupanca extends Conta {

    private final double taxaRendimentoMensal = 0.005;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ContaPoupanca(String numero, Cliente proprietario) {
        super(numero, proprietario);
    }

    public double getTaxaRendimentoMensal() {
        return taxaRendimentoMensal;
    }

    public void renderJuros() {
        double rendimento = this.saldo * this.taxaRendimentoMensal;
        this.saldo += rendimento;
        adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - RENDIMENTO APLICADO: +" + rendimento + " | Saldo Atual: " + this.saldo);
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

        if (valor > this.saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente na Conta Poupança. Saldo disponível: R$ " + this.saldo);
        }

        this.saldo -= valor;
        adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - SAQUE CP: -" + valor + " | Saldo Atual: " + this.saldo);
    }

    @Override
    public void transferir(Conta destino, double valor) throws SaldoInsuficienteException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

        sacar(valor);

        destino.depositar(valor);

        adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - TRANSFERÊNCIA ENVIADA para conta " + destino.getNumero() + ": -" + valor + " | Saldo Atual: " + this.saldo);
        destino.adicionarTransacao(LocalDateTime.now().format(FORMATTER) + " - TRANSFERÊNCIA RECEBIDA de conta " + this.getNumero() + ": +" + valor + " | Saldo Atual: " + destino.getSaldo());
    }

    @Override
    public String toString() {
        return "Conta Poupança [número=" + getNumero() + ", saldo=" + saldo + ", proprietário=" + getProprietario().getNome() + "]";
    }
}
