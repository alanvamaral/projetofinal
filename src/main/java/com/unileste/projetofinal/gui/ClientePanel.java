package com.unileste.projetofinal.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.unileste.projetofinal.entidades.Cliente;
import com.unileste.projetofinal.operacoes.BancoService;

public class ClientePanel extends JPanel {

    private final BancoService bancoService;
    private final JTextField nomeField;
    private final JTextField cpfField, enderecoField;
    private final JTextArea clientesArea;
    private final JButton cadastrarBtn, listarBtn, buscarBtn, atualizarBtn, deletarBtn;

    public ClientePanel(BancoService bancoService) {
        this.bancoService = bancoService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Cliente"));

        inputPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        inputPanel.add(nomeField);

        inputPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        inputPanel.add(cpfField);

        inputPanel.add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        inputPanel.add(enderecoField);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cadastrarBtn = new JButton("Cadastrar");
        listarBtn = new JButton("Listar Todos");
        buscarBtn = new JButton("Buscar por CPF");
        atualizarBtn = new JButton("Atualizar");
        deletarBtn = new JButton("Deletar");

        buttonPanel.add(cadastrarBtn);
        buttonPanel.add(listarBtn);
        buttonPanel.add(buscarBtn);
        buttonPanel.add(atualizarBtn);
        buttonPanel.add(deletarBtn);

        // Painel de exibição
        clientesArea = new JTextArea(15, 70);
        clientesArea.setEditable(false);
        clientesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(clientesArea);

        // Adicionar painéis
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        cadastrarBtn.addActionListener(e -> cadastrarCliente());
        listarBtn.addActionListener(e -> listarClientes());
        buscarBtn.addActionListener(e -> buscarCliente());
        atualizarBtn.addActionListener(e -> atualizarCliente());
        deletarBtn.addActionListener(e -> deletarCliente());
    }

    private void cadastrarCliente() {
        try {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().trim();
            String endereco = enderecoField.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e CPF são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente cliente = new Cliente(nome, cpf, endereco);
            bancoService.cadastrarCliente(cliente);

            clientesArea.append("✓ Cliente " + nome + " cadastrado com sucesso!\n");
            nomeField.setText("");
            cpfField.setText("");
            enderecoField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarClientes() {
        try {
            clientesArea.setText("=== LISTA DE CLIENTES ===\n\n");
            var clientes = bancoService.listarClientes();
            for (Cliente c : clientes) {
                clientesArea.append("ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpf()
                        + " | Endereço: " + c.getEndereco() + "\n");
            }
            if (clientes.isEmpty()) {
                clientesArea.append("Nenhum cliente cadastrado.\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        try {
            String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente:");
            if (cpf == null || cpf.trim().isEmpty()) {
                return;
            }

            Cliente cliente = bancoService.buscarClientePorCpf(cpf.trim());
            clientesArea.setText("=== CLIENTE ENCONTRADO ===\n\n");
            clientesArea.append("ID: " + cliente.getId() + "\nNome: " + cliente.getNome() + "\nCPF: " + cliente.getCpf()
                    + "\nEndereço: " + cliente.getEndereco() + "\nTotal de Contas: " + cliente.getContas().size() + "\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarCliente() {
        try {
            String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente a atualizar:");
            if (cpf == null || cpf.trim().isEmpty()) {
                return;
            }

            Cliente cliente = bancoService.buscarClientePorCpf(cpf.trim());
            String novoEndereco = JOptionPane.showInputDialog(this, "Novo endereço:", cliente.getEndereco());
            if (novoEndereco != null && !novoEndereco.trim().isEmpty()) {
                cliente.setEndereco(novoEndereco.trim());
                bancoService.atualizarCliente(cliente);
                clientesArea.append("✓ Cliente " + cliente.getNome() + " atualizado!\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarCliente() {
        try {
            String cpf = JOptionPane.showInputDialog(this, "Digite o CPF do cliente a deletar:");
            if (cpf == null || cpf.trim().isEmpty()) {
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bancoService.deletarCliente(cpf.trim());
                clientesArea.append("✓ Cliente com CPF " + cpf + " deletado!\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
