package com.unileste.projetofinal.dao;

import java.util.List;

import com.unileste.projetofinal.entidades.Conta;

public interface ContaDAO {

    void inserir(Conta conta) throws Exception;

    void atualizar(Conta conta) throws Exception;

    void deletar(String numero) throws Exception;

    Conta buscarPorNumero(String numero) throws Exception;

    List<Conta> listarPorCliente(String cpfCliente) throws Exception;

    List<Conta> listarTodas() throws Exception;
}
