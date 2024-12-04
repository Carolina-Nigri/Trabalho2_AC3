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

        // Métodos para criar as etapas (escalar ou superescalar)
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

        // Variáveis para controle do estado da simulação
        boolean[] simulacaoAtiva = {false}; // Flag para controlar se a simulação está ativa
        boolean[] pausado = {false}; // Flag para verificar se está pausado
        boolean[] recomeca = {false}; // Flag para verificar se é pra recomeçar
        Timer[] timer = {null}; // Timer para animação, agora como variável para controle

        // Métodos para fazer animação da instrução
        Runnable animarInstrucaoEscalar = new Runnable() {
            int etapaAtual = 0; // Começa no Fetch
            public void run() {
                if (simulacaoAtiva[0] && !pausado[0]) {
                    // Recorre pelas etapas e anima a instrução
                    if (timer[0] != null) {
                        timer[0].stop(); // Para a animação anterior, se existir
                    }
                    if(recomeca[0] && etapaAtual > 0){ // Reinicia a animação desde o início
                        JPanel celulaAnterior = (JPanel) painelCentral.getComponent(etapaAtual - 1);
                        celulaAnterior.setBackground(null); // Remove a cor da etapa anterior
                        etapaAtual = 0; 
                    } 
                    timer[0] = new Timer(1000, e -> {
                        if (etapaAtual < 5) {
                            JPanel celulaAtual = (JPanel) painelCentral.getComponent(etapaAtual);
                            celulaAtual.setBackground(Color.CYAN); // Marca a etapa atual com cor
                            if (etapaAtual > 0) {
                                JPanel celulaAnterior = (JPanel) painelCentral.getComponent(etapaAtual - 1);
                                celulaAnterior.setBackground(null); // Remove a cor da etapa anterior
                            }
                            etapaAtual++;
                        } else {
                            timer[0].stop(); // Para a animação
                            JPanel celulaAnterior = (JPanel) painelCentral.getComponent(etapaAtual - 1);
                            celulaAnterior.setBackground(null); // Remove a cor da etapa anterior
                        }
                    });
                    timer[0].start();
                }
            }
        };

        Runnable animarInstrucaoSuperscalar = new Runnable() {
            int linhaAtual = 0; // Começa na linha 0
            int colunaAtual = 0; // Começa na coluna 0
            public void run() {
                if (simulacaoAtiva[0] && !pausado[0]) {
                    // Animação da instrução no superescalar
                    if (timer[0] != null) {
                        timer[0].stop(); // Para a animação anterior, se existir
                    }
                    if(recomeca[0] && !(linhaAtual == 0 && colunaAtual == 0)){ // Reinicia a animação desde o início
                        JPanel celulaAnterior = (JPanel) ((JPanel) painelCentral.getComponent(1)).getComponent((linhaAtual) * 5 + (colunaAtual - 1));
                        celulaAnterior.setBackground(null); // Remove a cor da célula anterior
                        linhaAtual = 0; 
                        colunaAtual = 0;
                    }
                    timer[0] = new Timer(1000, e -> {
                        if (linhaAtual < 6 && colunaAtual < 5) {
                            JPanel celulaAtual = (JPanel) ((JPanel) painelCentral.getComponent(1)).getComponent(linhaAtual * 5 + colunaAtual);
                            celulaAtual.setBackground(Color.CYAN); // Marca a célula atual
                            if (colunaAtual > 0 || linhaAtual > 0) {
                                JPanel celulaAnterior = (JPanel) ((JPanel) painelCentral.getComponent(1)).getComponent((linhaAtual) * 5 + (colunaAtual - 1));
                                celulaAnterior.setBackground(null); // Remove a cor da célula anterior
                            }
                            colunaAtual++;
                            if (colunaAtual >= 5) {
                                colunaAtual = 0;
                                linhaAtual++;
                            }
                        } else {
                            timer[0].stop(); // Para a animação
                            JPanel celulaAnterior = (JPanel) ((JPanel) painelCentral.getComponent(1)).getComponent((linhaAtual) * 5 + (colunaAtual - 1));
                            celulaAnterior.setBackground(null); // Remove a cor da célula anterior
                        }
                    });
                    timer[0].start();
                }
            }
        };

        // Métodos para controlar a simulação
        Runnable iniciarSimulacao = () -> {
            recomeca[0] = true;
            simulacaoAtiva[0] = true;
            pausado[0] = false;
            if (escalarButton.isSelected()) {
                animarInstrucaoEscalar.run();
            } else if (superescalarButton.isSelected()) {
                animarInstrucaoSuperscalar.run();
            }
        };

        Runnable pausarSimulacao = () -> {
            pausado[0] = true;
            if (timer[0] != null) {
                timer[0].stop(); // Interrompe a animação ao pausar
            }
        };

        Runnable continuarSimulacao = () -> {
            pausado[0] = false;
            recomeca[0] = false;
            if (escalarButton.isSelected()) {
                animarInstrucaoEscalar.run();
            } else if (superescalarButton.isSelected()) {
                animarInstrucaoSuperscalar.run();
            }
        };

        // Ações dos botões de controle
        iniciarButton.addActionListener(e -> iniciarSimulacao.run());
        pausarButton.addActionListener(e -> pausarSimulacao.run());
        continuarButton.addActionListener(e -> continuarSimulacao.run());

        // Exibindo a janela
        janela.setVisible(true);
    }
}
