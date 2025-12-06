package com.unileste.projetofinal.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.unileste.projetofinal.operacoes.BancoService;

public class ContaPanel extends JPanel {

    private final BancoService bancoService;
    private final JTextField numContaField, cpfClienteField, limiteField;
    private final JComboBox<String> tipoContaCombo;
    private final JTextArea contasArea;
    private final JButton abrirContaBtn, listarBtn, buscarBtn;

    public ContaPanel(BancoService bancoService) {
        this.bancoService = bancoService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Abrir Nova Conta"));

        inputPanel.add(new JLabel("Número da Conta:"));
        numContaField = new JTextField();
        inputPanel.add(numContaField);

        inputPanel.add(new JLabel("CPF do Cliente:"));
        cpfClienteField = new JTextField();
        inputPanel.add(cpfClienteField);

        inputPanel.add(new JLabel("Tipo de Conta:"));
        tipoContaCombo = new JComboBox<>(new String[]{"poupanca", "corrente"});
        inputPanel.add(tipoContaCombo);

        inputPanel.add(new JLabel("Limite Cheque Especial:"));
        limiteField = new JTextField("0");
        inputPanel.add(limiteField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        abrirContaBtn = new JButton("Abrir Conta");
        listarBtn = new JButton("Listar Todas");
        buscarBtn = new JButton("Buscar por Número");

        buttonPanel.add(abrirContaBtn);
        buttonPanel.add(listarBtn);
        buttonPanel.add(buscarBtn);

        contasArea = new JTextArea(15, 70);
        contasArea.setEditable(false);
        contasArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(contasArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        abrirContaBtn.addActionListener(e -> abrirConta());
        listarBtn.addActionListener(e -> listarContas());
        buscarBtn.addActionListener(e -> buscarConta());
    }

    private void abrirConta() {
        try {
            String numero = numContaField.getText().trim();
            String cpf = cpfClienteField.getText().trim();
            String tipo = (String) tipoContaCombo.getSelectedItem();
            double limite = 0;

            if (numero.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Número e CPF são obrigatórios.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if ("corrente".equals(tipo)) {
                try {
                    limite = Double.parseDouble(limiteField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Limite deve ser um número válido.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            bancoService.abrirConta(numero, tipo, cpf, limite);
            contasArea.append("✓ Conta " + numero + " (" + tipo + ") aberta com sucesso!\n");

            numContaField.setText("");
            cpfClienteField.setText("");
            limiteField.setText("0");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarContas() {
        try {
            contasArea.setText("=== LISTA DE CONTAS ===\n\n");
            var contas = bancoService.listarTodasContas();
            for (var c : contas) {
                contasArea.append("Número: " + c.getNumero() + " | Tipo: "
                        + (c instanceof com.unileste.projetofinal.entidades.ContaCorrente ? "Corrente" : "Poupança")
                        + " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) + " | Proprietário: "
                        + c.getProprietario().getNome() + "\n");
            }
            if (contas.isEmpty()) {
                contasArea.append("Nenhuma conta cadastrada.\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarConta() {
        try {
            String numero = JOptionPane.showInputDialog(this, "Digite o número da conta:");
            if (numero == null || numero.trim().isEmpty()) {
                return;
            }

            var conta = bancoService.buscarContaPorNumero(numero.trim());
            contasArea.setText("=== CONTA ENCONTRADA ===\n\n");
            contasArea.append("Número: " + conta.getNumero() + "\nTipo: "
                    + (conta instanceof com.unileste.projetofinal.entidades.ContaCorrente ? "Corrente" : "Poupança")
                    + "\nSaldo: R$ " + String.format("%.2f", conta.getSaldo()) + "\nProprietário: "
                    + conta.getProprietario().getNome() + " (" + conta.getProprietario().getCpf() + ")\n");

            if (conta instanceof com.unileste.projetofinal.entidades.ContaCorrente cc) {
                contasArea.append("Limite Cheque Especial: R$ " + String.format("%.2f", cc.getLimiteChequeEspecial())
                        + "\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
