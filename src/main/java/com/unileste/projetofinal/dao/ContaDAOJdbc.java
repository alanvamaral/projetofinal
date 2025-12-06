package com.unileste.projetofinal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.unileste.projetofinal.entidades.Cliente;
import com.unileste.projetofinal.entidades.Conta;
import com.unileste.projetofinal.entidades.ContaCorrente;
import com.unileste.projetofinal.entidades.ContaPoupanca;

public class ContaDAOJdbc implements ContaDAO {

    @Override
    public void inserir(Conta conta) throws Exception {
        String sql = "INSERT INTO conta (numero, tipo, saldo, proprietario_id, limite_cheque_especial) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DAOConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, conta.getNumero());
            if (conta instanceof ContaCorrente contaCorrente) {
                stmt.setString(2, "corrente");
                stmt.setDouble(5, contaCorrente.getLimiteChequeEspecial());
            } else {
                stmt.setString(2, "poupanca");
                stmt.setDouble(5, 0);
            }
            stmt.setDouble(3, conta.getSaldo());
            stmt.setInt(4, conta.getProprietario().getId());

            stmt.executeUpdate();
            System.out.println("Conta " + conta.getNumero() + " inserida com sucesso.");
        }
    }

    @Override
    public void atualizar(Conta conta) throws Exception {
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try (Connection conn = DAOConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, conta.getSaldo());
            stmt.setString(2, conta.getNumero());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("Nenhuma conta encontrada com o número: " + conta.getNumero());
            }
            System.out.println("Conta " + conta.getNumero() + " atualizada com sucesso.");
        }
    }

    @Override
    public void deletar(String numero) throws Exception {
        String sql = "DELETE FROM conta WHERE numero = ?";

        try (Connection conn = DAOConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new Exception("Nenhuma conta encontrada com o número: " + numero);
            }
            System.out.println("Conta " + numero + " deletada com sucesso.");
        }
    }

    @Override
    public Conta buscarPorNumero(String numero) throws Exception {
        String sql = "SELECT c.id, c.numero, c.tipo, c.saldo, c.limite_cheque_especial, "
                + "cl.id AS cliente_id, cl.nome, cl.cpf, cl.endereco "
                + "FROM conta c "
                + "JOIN cliente cl ON c.proprietario_id = cl.id WHERE c.numero = ?";

        try (Connection conn = DAOConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarConta(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Conta> listarPorCliente(String cpfCliente) throws Exception {
        List<Conta> conta = new ArrayList<>();
        String sql = "SELECT c.numero, c.tipo, c.saldo, c.limite_cheque_especial, "
                + "cl.id AS cliente_id, cl.nome, cl.cpf, cl.endereco FROM conta c "
                + "JOIN cliente cl ON c.proprietario_id = cl.id WHERE cl.cpf = ?";

        try (Connection conn = DAOConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    conta.add(montarConta(rs));
                }
            }
        }

        return conta;
    }

    @Override
    public List<Conta> listarTodas() throws Exception {
        List<Conta> conta = new ArrayList<>();
        String sql = "SELECT c.numero, c.tipo, c.saldo, c.limite_cheque_especial, "
                + "cl.id AS cliente_id, cl.nome, cl.cpf, cl.endereco FROM conta c "
                + "JOIN cliente cl ON c.proprietario_id = cl.id";

        try (Connection conn = DAOConfig.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    conta.add(montarConta(rs));
                }
            }
        }

        return conta;
    }

    private Conta montarConta(ResultSet rs) throws SQLException {
        String numero = rs.getString("numero");
        String tipo = rs.getString("tipo");
        double saldo = rs.getDouble("saldo");
        int clienteId = rs.getInt("cliente_id");
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String endereco = rs.getString("endereco");
        double limiteCheque = rs.getDouble("limite_cheque_especial");

        Cliente cliente = new Cliente(nome, cpf, endereco, clienteId);
        Conta conta;

        if (tipo != null && tipo.equalsIgnoreCase("corrente")) {
            conta = new ContaCorrente(numero, cliente, limiteCheque);
        } else {
            conta = new ContaPoupanca(numero, cliente);
        }

        if (saldo > 0) {
            conta.depositar(saldo);
        }

        return conta;
    }

}
