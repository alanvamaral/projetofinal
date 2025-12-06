package com.unileste.projetofinal.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.unileste.projetofinal.dao.ClienteDAOJdbc;
import com.unileste.projetofinal.dao.ContaDAOJdbc;
import com.unileste.projetofinal.operacoes.BancoService;

public class MainFrame extends JFrame {

    private BancoService bancoService;
    private final JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("Sistema Bancário - Projeto Final");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            this.bancoService = new BancoService(new ClienteDAOJdbc(), new ContaDAOJdbc());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage(),
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Criar abas
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", new ClientePanel(bancoService));
        tabbedPane.addTab("Contas", new ContaPanel(bancoService));
        tabbedPane.addTab("Operações", new OperacoesPanel(bancoService));

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
