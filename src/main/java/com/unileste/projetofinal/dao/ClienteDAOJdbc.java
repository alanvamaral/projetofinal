package com.unileste.projetofinal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.unileste.projetofinal.entidades.Cliente;

public class ClienteDAOJdbc implements ClienteDAO {

    @Override
    public void inserir(Cliente cliente) throws Exception {
        String sql = "INSERT INTO cliente (nome, cpf, endereco) VALUES (?, ?, ?)";

        try (Connection conn = DAOConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEndereco());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new Exception("Falha ao inserir cliente, nenhuma linha afetada.");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(Cliente cliente) throws Exception {
        String sql = "UPDATE cliente SET nome = ?, endereco = ? WHERE cpf = ?";

        try (Connection conn = DAOConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getCpf());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new Exception("Nenhum cliente encontrado com o CPF: " + cliente.getCpf());
            }
        }
    }

    @Override
    public void deletar(String cpf) throws Exception {
        String sql = "DELETE FROM cliente WHERE cpf = ?";

        try (Connection conn = DAOConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new Exception("Nenhum cliente encontrado com o CPF: " + cpf);
            }
        }
    }

    @Override
    public Cliente buscarPorCpf(String cpf) throws Exception {
        String sql = "SELECT id, nome, cpf, endereco FROM cliente WHERE cpf = ?";

        try (Connection conn = DAOConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String endereco = rs.getString("endereco");
                    return new Cliente(nome, cpf, endereco, id);
                }
            }
        }

        return null;
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, endereco FROM cliente";

        try (Connection conn = DAOConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String endereco = rs.getString("endereco");
                clientes.add(new Cliente(nome, cpf, endereco, id));
            }
        }

        return clientes;
    }

    @Override
    public boolean existeCpf(String cpf) throws Exception {
        String sql = "SELECT 1 FROM cliente WHERE cpf = ? LIMIT 1";

        try (Connection conn = DAOConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

}
