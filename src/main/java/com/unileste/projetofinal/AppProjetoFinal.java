package com.unileste.projetofinal;

import java.sql.SQLException;

import com.unileste.projetofinal.dao.ContaDAO;
import com.unileste.projetofinal.dao.ContaDAOJdbc;

public class AppProjetoFinal {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World!");

        // Cliente cliente = new Cliente("Alan", "161.535.246-56", "rua 1", 1);
        // ContaCorrente corrente = new ContaCorrente("11b", cliente, 0);

        ContaDAO contaJdbc = new ContaDAOJdbc();
        // ClienteDAO clienteJdbc = new ClienteDAOJdbc();
        try {

            // clienteJdbc.inserir(cliente);
            // contaJdbc.inserir(corrente);
                var account
                    = contaJdbc.buscarPorNumero("11b");
            System.out.println(account.getProprietario());
       
        } catch (Exception e) {
            System.err.println("erro ao criar conta...");
            System.err.println(e.toString());
        }
    }
}
