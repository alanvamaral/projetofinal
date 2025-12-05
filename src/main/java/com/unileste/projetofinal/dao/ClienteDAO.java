package com.unileste.projetofinal.dao;

import java.util.List;

import com.unileste.projetofinal.entidades.Cliente;

// Interface que define o contrato para operações de persistência do Cliente
public interface ClienteDAO {

    void inserir(Cliente cliente) throws Exception;

    void atualizar(Cliente cliente) throws Exception;

    void deletar(String cpf) throws Exception;

    Cliente buscarPorCpf(String cpf) throws Exception;

    List<Cliente> listarTodos() throws Exception;

    // Opcional: Adicionar método para verificar unicidade de CPF, se necessário
    boolean existeCpf(String cpf) throws Exception;
}
