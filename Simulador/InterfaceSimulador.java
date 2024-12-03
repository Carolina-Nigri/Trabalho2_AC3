import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class InterfaceSimulador {
    public static void main(String[] args) {
        // Criando a janela principal
        JFrame janela = new JFrame("Simulador");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(800, 600);
        janela.setLayout(new BorderLayout());

        // Painel superior com botões de seleção de suporte multithreading
        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new FlowLayout());
        painelSuperior.setBorder(BorderFactory.createTitledBorder("Suporte Multithreading"));

        JRadioButton noneButton = new JRadioButton("Nenhum", true); // Pré-selecionado
        JRadioButton imtButton = new JRadioButton("IMT");
        JRadioButton smtButton = new JRadioButton("SMT");
        JRadioButton bmtButton = new JRadioButton("BMT");

        ButtonGroup grupoSuporte = new ButtonGroup();
        grupoSuporte.add(noneButton);
        grupoSuporte.add(imtButton);
        grupoSuporte.add(smtButton);
        grupoSuporte.add(bmtButton);

        painelSuperior.add(noneButton);
        painelSuperior.add(imtButton);
        painelSuperior.add(smtButton);
        painelSuperior.add(bmtButton);

        janela.add(painelSuperior, BorderLayout.NORTH);

        // Painel esquerdo com botões de seleção de arquitetura
        JPanel painelEsquerdo = new JPanel();
        painelEsquerdo.setLayout(new GridLayout(2, 1));
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Arquitetura"));

        JRadioButton escalarButton = new JRadioButton("Escalar", true); // Pré-selecionado
        JRadioButton superescalarButton = new JRadioButton("Superescalar");

        ButtonGroup grupoArquitetura = new ButtonGroup();
        grupoArquitetura.add(escalarButton);
        grupoArquitetura.add(superescalarButton);

        painelEsquerdo.add(escalarButton);
        painelEsquerdo.add(superescalarButton);

        janela.add(painelEsquerdo, BorderLayout.WEST);

        // Painel central com etapas (Fetch, Decode, Execute Memory, Writeback)
        JPanel painelCentral = new JPanel();
        painelCentral.setBorder(BorderFactory.createTitledBorder("Etapas do Pipeline"));

        // Método para criar as etapas (escalar ou superescalar)
        Runnable criarEtapasEscalar = () -> {
            painelCentral.removeAll();
            painelCentral.setLayout(new GridLayout(1, 5));
            String[] etapas = {"Fetch", "Decode", "Execute", "Memory", "Writeback"};
            for (String etapa : etapas) {
                JPanel painelEtapa = new JPanel();
                painelEtapa.setLayout(new FlowLayout());
                painelEtapa.setBorder(BorderFactory.createTitledBorder(etapa));
                painelCentral.add(painelEtapa);
            }
            painelCentral.revalidate();
            painelCentral.repaint();
        };

        Runnable criarEtapasSuperescalar = () -> {
            painelCentral.removeAll();
            painelCentral.setLayout(new BorderLayout());

            // Título único no topo
            JPanel painelTitulos = new JPanel(new GridLayout(1, 5));
            String[] etapas = {"Fetch", "Decode", "Execute", "Memory", "Writeback"};
            for (String etapa : etapas) {
                JLabel titulo = new JLabel(etapa, SwingConstants.CENTER);
                painelTitulos.add(titulo);
            }
            painelCentral.add(painelTitulos, BorderLayout.NORTH);

            // Células com bordas visíveis
            JPanel painelCelulas = new JPanel(new GridLayout(6, 5)); // 6 linhas para superescalar
            for (int i = 0; i < 6 * 5; i++) { // 6 linhas, 5 colunas
                JPanel celula = new JPanel();
                celula.setBorder(new LineBorder(Color.BLACK)); // Adiciona borda preta às células
                painelCelulas.add(celula);
            }
            painelCentral.add(painelCelulas, BorderLayout.CENTER);

            painelCentral.revalidate();
            painelCentral.repaint();
        };

        // Adicionar ActionListeners aos botões de arquitetura
        escalarButton.addActionListener(e -> criarEtapasEscalar.run());
        superescalarButton.addActionListener(e -> criarEtapasSuperescalar.run());

        // Adicionar painel central na janela
        criarEtapasEscalar.run(); // Inicializa como escalar
        janela.add(painelCentral, BorderLayout.CENTER);

        // Painel inferior com controle da simulação
        JPanel painelInferior = new JPanel();
        painelInferior.setLayout(new FlowLayout());
        painelInferior.setBorder(BorderFactory.createTitledBorder("Controlar Simulação"));

        JButton iniciarButton = new JButton("Iniciar");
        JButton pausarButton = new JButton("Pausar");
        JButton continuarButton = new JButton("Continuar");

        painelInferior.add(iniciarButton);
        painelInferior.add(pausarButton);
        painelInferior.add(continuarButton);

        janela.add(painelInferior, BorderLayout.SOUTH);

        // Exibe a interface
        janela.setVisible(true);
    }
}
