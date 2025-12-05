package com.unileste.projetofinal.entidades;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private Integer id;
    private String nome;
    private String cpf;
    private String endereco;
    private List<Conta> contas;

    public Cliente(String nome, String cpf, String endereco, Integer id) {
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e CPF são obrigatórios.");
        }
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.contas = new ArrayList<>();
    }

    public Cliente(String nome, String cpf, String endereco) {
        this(nome, cpf, endereco, null);
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Setters (apenas para endereço)
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void adicionarConta(Conta conta) {
        if (conta != null) {
            this.contas.add(conta);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cliente cliente = (Cliente) obj;
        return cpf.equals(cliente.cpf); // CPF como identificador único
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }

    @Override
    public String toString() {
        return "Cliente [nome=" + nome + ", cpf=" + cpf + ", endereço=" + endereco + ", totalContas=" + contas.size() + "]";
    }
}
