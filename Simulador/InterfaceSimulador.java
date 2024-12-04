import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class InterfaceSimulador {
    public static void main(String[] args) {
        new InterfaceSimulador().iniciar();
    }

    private JFrame janela;
    private JPanel painelCentral;
    private JRadioButton escalarButton;
    private JRadioButton superescalarButton;
    private Timer animacaoTimer;

    // Variáveis para controle do estado da simulação
    private boolean recomeca = false; // Flag para verificar se é pra recomeçar
    private int etapaAtual = 0; // Usado para escalar
    private int linhaAtual = 0, colunaAtual = 0; // Usado para superescalar

    public void iniciar() {
        // Configuração inicial da janela
        janela = new JFrame("Simulador");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(800, 600);
        janela.setLayout(new BorderLayout());

        // Componentes da interface
        janela.add(criarPainelSuperior(), BorderLayout.NORTH);
        janela.add(criarPainelEsquerdo(), BorderLayout.WEST);
        painelCentral = criarPainelCentral();
        janela.add(painelCentral, BorderLayout.CENTER);
        janela.add(criarPainelInferior(), BorderLayout.SOUTH);

        // Inicializa com arquitetura escalar
        configurarEtapasEscalar();

        // Exibe a janela
        janela.setVisible(true);
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new FlowLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Suporte Multithreading"));

        JRadioButton noneButton = criarBotaoRadio("Nenhum", true);
        JRadioButton imtButton = criarBotaoRadio("IMT", false);
        JRadioButton smtButton = criarBotaoRadio("SMT", false);
        JRadioButton bmtButton = criarBotaoRadio("BMT", false);

        ButtonGroup grupoSuporte = new ButtonGroup();
        grupoSuporte.add(noneButton);
        grupoSuporte.add(imtButton);
        grupoSuporte.add(smtButton);
        grupoSuporte.add(bmtButton);

        painel.add(noneButton);
        painel.add(imtButton);
        painel.add(smtButton);
        painel.add(bmtButton);

        return painel;
    }

    private JPanel criarPainelEsquerdo() {
        JPanel painel = new JPanel(new GridLayout(2, 1));
        painel.setBorder(BorderFactory.createTitledBorder("Arquitetura"));

        escalarButton = criarBotaoRadio("Escalar", true);
        superescalarButton = criarBotaoRadio("Superescalar", false);

        ButtonGroup grupoArquitetura = new ButtonGroup();
        grupoArquitetura.add(escalarButton);
        grupoArquitetura.add(superescalarButton);

        escalarButton.addActionListener(e -> configurarEtapasEscalar());
        superescalarButton.addActionListener(e -> configurarEtapasSuperescalar());

        painel.add(escalarButton);
        painel.add(superescalarButton);

        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel();
        painel.setBorder(BorderFactory.createTitledBorder("Etapas do Pipeline"));
        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new FlowLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Controlar Simulação"));

        JButton iniciarButton = new JButton("Iniciar");
        iniciarButton.addActionListener(e -> controlarSimulacao("iniciar"));

        JButton pausarButton = new JButton("Pausar");
        pausarButton.addActionListener(e -> controlarSimulacao("pausar"));

        JButton continuarButton = new JButton("Continuar");
        continuarButton.addActionListener(e -> controlarSimulacao("continuar"));

        painel.add(iniciarButton);
        painel.add(pausarButton);
        painel.add(continuarButton);

        return painel;
    }

    private JRadioButton criarBotaoRadio(String texto, boolean selecionado) {
        JRadioButton botao = new JRadioButton(texto, selecionado);
        return botao;
    }

    private void configurarEtapasEscalar() {
        painelCentral.removeAll();
        painelCentral.setLayout(new GridLayout(1, 5));

        String[] etapas = {"Fetch", "Decode", "Execute", "Memory", "Writeback"};
        for (String etapa : etapas) {
            JPanel painelEtapa = new JPanel(new FlowLayout());
            painelEtapa.setBorder(BorderFactory.createTitledBorder(etapa));
            painelCentral.add(painelEtapa);
        }

        painelCentral.revalidate();
        painelCentral.repaint();
    }

    private void configurarEtapasSuperescalar() {
        painelCentral.removeAll();
        painelCentral.setLayout(new BorderLayout());

        // Títulos das etapas
        JPanel painelTitulos = new JPanel(new GridLayout(1, 5));
        String[] etapas = {"Fetch", "Decode", "Execute", "Memory", "Writeback"};
        for (String etapa : etapas) {
            painelTitulos.add(new JLabel(etapa, SwingConstants.CENTER));
        }
        painelCentral.add(painelTitulos, BorderLayout.NORTH);

        // Células das etapas
        JPanel painelCelulas = new JPanel(new GridLayout(6, 5));
        for (int i = 0; i < 6 * 5; i++) { // 6 linhas, 5 colunas
            JPanel celula = new JPanel();
            celula.setBorder(new LineBorder(Color.BLACK));
            painelCelulas.add(celula);
        }
        painelCentral.add(painelCelulas, BorderLayout.CENTER);

        painelCentral.revalidate();
        painelCentral.repaint();
    }

    private void controlarSimulacao(String comando) {
        if (comando.equals("pausar") && animacaoTimer != null) {
            animacaoTimer.stop();
        } else{
            if(comando.equals("iniciar")) recomeca = true;
            else if(comando.equals("continuar")) recomeca = false;

            if (escalarButton.isSelected()) {
                executarAnimacaoEscalar();
            } else if (superescalarButton.isSelected()) {
                executarAnimacaoSuperescalar();
            }
        }
    }

    private void executarAnimacaoEscalar() {
        if (animacaoTimer != null) {
            animacaoTimer.stop();
        }
        if (recomeca && etapaAtual != 0) {
            JPanel painelAnterior = (JPanel) painelCentral.getComponent(etapaAtual - 1);
            painelAnterior.removeAll(); // Remove o quadrado da última etapa
            painelAnterior.repaint();
            etapaAtual = 0;
        }

        animacaoTimer = new Timer(1000, e -> {
            if (etapaAtual < 5) {
                JPanel painelEtapa = (JPanel) painelCentral.getComponent(etapaAtual);
                painelEtapa.removeAll(); // Limpa a etapa antes de adicionar um novo quadrado
                
                JPanel instrucaoPanel = new JPanel(new GridBagLayout());
                instrucaoPanel.setBackground(Color.CYAN);
                instrucaoPanel.setPreferredSize(new Dimension(70, 70));
                instrucaoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                JLabel nomeInstrucao = new JLabel("ADD");
                nomeInstrucao.setFont(new Font("Arial", Font.BOLD, 15));
                instrucaoPanel.add(nomeInstrucao); // Adiciona o texto ao painel
                instrucaoPanel.add(nomeInstrucao);

                painelEtapa.add(instrucaoPanel);
                painelEtapa.revalidate();
                painelEtapa.repaint();

                if (etapaAtual > 0) {
                    JPanel painelAnterior = (JPanel) painelCentral.getComponent(etapaAtual - 1);
                    painelAnterior.removeAll(); // Remove o quadrado da etapa anterior
                    painelAnterior.repaint();
                }
                etapaAtual++;
            } else {
                animacaoTimer.stop();
                JPanel painelAnterior = (JPanel) painelCentral.getComponent(etapaAtual - 1);
                painelAnterior.removeAll(); // Remove o quadrado da última etapa
                painelAnterior.repaint();
            }
        });

        animacaoTimer.start();
    }

    private void executarAnimacaoSuperescalar() {
        if (animacaoTimer != null) {
            animacaoTimer.stop();
        }
        if (recomeca && !(linhaAtual == 0 && colunaAtual == 0)) {
            int indiceAnterior = linhaAtual * 5 + (colunaAtual - 1);
            if (colunaAtual == 0 && linhaAtual > 0) { // Caso especial: mudança de linha
                indiceAnterior = (linhaAtual - 1) * 5 + 4;
            }
            JPanel painelCelulas = (JPanel) painelCentral.getComponent(1);
            JPanel celulaAnterior = (JPanel) painelCelulas.getComponent(indiceAnterior);
            celulaAnterior.removeAll();
            celulaAnterior.setBackground(null);
            celulaAnterior.repaint();
            
            linhaAtual = 0;
            colunaAtual = 0;
        }

        animacaoTimer = new Timer(1000, e -> {
            JPanel painelCelulas = (JPanel) painelCentral.getComponent(1);

            if (linhaAtual < 6 && colunaAtual < 5) {
                // Destaca a célula atual
                JPanel celulaAtual = (JPanel) painelCelulas.getComponent(linhaAtual * 5 + colunaAtual);
                celulaAtual.setBackground(Color.CYAN); // Cor de destaque
                celulaAtual.setLayout(new GridBagLayout());

                // Adiciona um rótulo com a instrução "ADD"
                JLabel labelInstrucao = new JLabel("ADD");
                labelInstrucao.setFont(new Font("Arial", Font.BOLD, 12));
                celulaAtual.add(labelInstrucao);

                // Remove o destaque da célula anterior, se aplicável
                if (colunaAtual > 0 || linhaAtual > 0) {
                    int indiceAnterior = linhaAtual * 5 + (colunaAtual - 1);
                    if (colunaAtual == 0 && linhaAtual > 0) { // Caso especial: mudança de linha
                        indiceAnterior = (linhaAtual - 1) * 5 + 4;
                    }
                    JPanel celulaAnterior = (JPanel) painelCelulas.getComponent(indiceAnterior);
                    celulaAnterior.removeAll();
                    celulaAnterior.setBackground(null);
                    celulaAnterior.repaint();
                }

                colunaAtual++;
                if (colunaAtual >= 5) { // Avança para a próxima linha
                    colunaAtual = 0;
                    linhaAtual++;
                }
            } else {
                animacaoTimer.stop();

                int indiceAnterior = linhaAtual * 5 + (colunaAtual - 1);
                if (colunaAtual == 0 && linhaAtual > 0) { // Caso especial: mudança de linha
                    indiceAnterior = (linhaAtual - 1) * 5 + 4;
                }
                JPanel celulaAnterior = (JPanel) painelCelulas.getComponent(indiceAnterior);
                celulaAnterior.removeAll();
                celulaAnterior.setBackground(null);
                celulaAnterior.repaint();
                
                linhaAtual = 0;
                colunaAtual = 0;
            }
                painelCelulas.revalidate();
                painelCelulas.repaint();
        });

        animacaoTimer.start();
    }
}
