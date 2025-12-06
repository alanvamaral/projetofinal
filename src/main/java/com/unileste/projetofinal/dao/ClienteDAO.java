package com.unileste.projetofinal.dao;

import java.util.List;

import com.unileste.projetofinal.entidades.Cliente;

public interface ClienteDAO {

    void inserir(Cliente cliente) throws Exception;

    void atualizar(Cliente cliente) throws Exception;

    void deletar(String cpf) throws Exception;

    Cliente buscarPorCpf(String cpf) throws Exception;

    List<Cliente> listarTodos() throws Exception;

    boolean existeCpf(String cpf) throws Exception;
}
