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

import com.unileste.projetofinal.operacoes.BancoService;

public class OperacoesPanel extends JPanel {

    private final BancoService bancoService;
    private final JTextField numContaField, valorField, numContaDestinoField;
    private final JTextArea resultadoArea;
    private final JButton depositarBtn, sacarBtn, transferirBtn, consultarSaldoBtn;

    public OperacoesPanel(BancoService bancoService) {
        this.bancoService = bancoService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Operações Bancárias"));

        inputPanel.add(new JLabel("Número da Conta:"));
        numContaField = new JTextField();
        inputPanel.add(numContaField);

        inputPanel.add(new JLabel("Valor:"));
        valorField = new JTextField();
        inputPanel.add(valorField);

        inputPanel.add(new JLabel("Conta Destino (Transferência):"));
        numContaDestinoField = new JTextField();
        inputPanel.add(numContaDestinoField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        depositarBtn = new JButton("Depositar");
        sacarBtn = new JButton("Sacar");
        transferirBtn = new JButton("Transferir");
        consultarSaldoBtn = new JButton("Consultar Saldo");

        buttonPanel.add(depositarBtn);
        buttonPanel.add(sacarBtn);
        buttonPanel.add(transferirBtn);
        buttonPanel.add(consultarSaldoBtn);

        resultadoArea = new JTextArea(15, 70);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultadoArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        depositarBtn.addActionListener(e -> depositar());
        sacarBtn.addActionListener(e -> sacar());
        transferirBtn.addActionListener(e -> transferir());
        consultarSaldoBtn.addActionListener(e -> consultarSaldo());
    }

    private void depositar() {
        try {
            String numero = numContaField.getText().trim();
            double valor = Double.parseDouble(valorField.getText().trim());

            if (numero.isEmpty() || valor <= 0) {
                JOptionPane.showMessageDialog(this, "Número e valor válido são obrigatórios.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            bancoService.depositar(numero, valor);
            resultadoArea.append("\n✓ DEPÓSITO REALIZADO\n");
            resultadoArea.append("Conta: " + numero + "\n");
            resultadoArea.append("Valor: R$ " + String.format("%.2f", valor) + "\n");

            var conta = bancoService.buscarContaPorNumero(numero);
            resultadoArea.append("Novo Saldo: R$ " + String.format("%.2f", conta.getSaldo()) + "\n");
            resultadoArea.append("----------------------------\n");

            numContaField.setText("");
            valorField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor deve ser um número válido.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sacar() {
        try {
            String numero = numContaField.getText().trim();
            double valor = Double.parseDouble(valorField.getText().trim());

            if (numero.isEmpty() || valor <= 0) {
                JOptionPane.showMessageDialog(this, "Número e valor válido são obrigatórios.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            bancoService.sacar(numero, valor);
            resultadoArea.append("\n✓ SAQUE REALIZADO\n");
            resultadoArea.append("Conta: " + numero + "\n");
            resultadoArea.append("Valor: R$ " + String.format("%.2f", valor) + "\n");

            var conta = bancoService.buscarContaPorNumero(numero);
            resultadoArea.append("Novo Saldo: R$ " + String.format("%.2f", conta.getSaldo()) + "\n");
            resultadoArea.append("----------------------------\n");

            numContaField.setText("");
            valorField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor deve ser um número válido.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transferir() {
        try {
            String numeroOrigem = numContaField.getText().trim();
            String numeroDestino = numContaDestinoField.getText().trim();
            double valor = Double.parseDouble(valorField.getText().trim());

            if (numeroOrigem.isEmpty() || numeroDestino.isEmpty() || valor <= 0) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos com valores válidos.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            bancoService.transferir(numeroOrigem, numeroDestino, valor);
            resultadoArea.append("\n✓ TRANSFERÊNCIA REALIZADA\n");
            resultadoArea.append("De: " + numeroOrigem + "\n");
            resultadoArea.append("Para: " + numeroDestino + "\n");
            resultadoArea.append("Valor: R$ " + String.format("%.2f", valor) + "\n");

            var contaOrigem = bancoService.buscarContaPorNumero(numeroOrigem);
            resultadoArea.append("Novo Saldo (Origem): R$ " + String.format("%.2f", contaOrigem.getSaldo()) + "\n");
            resultadoArea.append("----------------------------\n");

            numContaField.setText("");
            numContaDestinoField.setText("");
            valorField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor deve ser um número válido.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarSaldo() {
        try {
            String numero = numContaField.getText().trim();

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o número da conta.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            var conta = bancoService.buscarContaPorNumero(numero);
            resultadoArea.append("\n=== CONSULTA DE SALDO ===\n");
            resultadoArea.append("Conta: " + conta.getNumero() + "\n");
            resultadoArea.append("Proprietário: " + conta.getProprietario().getNome() + "\n");
            resultadoArea.append("Saldo: R$ " + String.format("%.2f", conta.getSaldo()) + "\n");
            resultadoArea.append("----------------------------\n");

            numContaField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
