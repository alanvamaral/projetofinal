package com.unileste.projetofinal.entidades;

import java.util.ArrayList;
import java.util.List;

import com.unileste.projetofinal.utilitarios.SaldoInsuficienteException;

public abstract class Conta {

    private String numero;
    protected double saldo;
    private Cliente proprietario;
    private List<String> historicoTransacoes;

    public Conta(String numero, Cliente proprietario) {
        if (numero == null || proprietario == null) {
            throw new IllegalArgumentException("Número da conta e proprietário não podem ser nulos.");
        }
        this.numero = numero;
        this.proprietario = proprietario;
        this.saldo = 0.0;
        this.historicoTransacoes = new ArrayList<>();
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getProprietario() {
        return proprietario;
    }

    public List<String> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    protected void adicionarTransacao(String descricao) {
        this.historicoTransacoes.add(descricao);
    }

    public abstract void depositar(double valor);

    public abstract void sacar(double valor) throws SaldoInsuficienteException;

    public abstract void transferir(Conta destino, double valor) throws SaldoInsuficienteException;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conta conta = (Conta) o;
        return numero.equals(conta.numero);
    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }

    @Override
    public String toString() {
        return "Conta [número=" + numero + ", saldo=" + saldo + ", proprietário=" + proprietario.getNome() + "]";
    }
}
