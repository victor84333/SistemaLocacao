package view;

import dao.ClienteDAO;
import dao.LocacaoDAO;
import dao.VeiculoDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {

    // DAOs
    private final ClienteDAO  clienteDAO  = new ClienteDAO();
    private final VeiculoDAO  veiculoDAO  = new VeiculoDAO();
    private final LocacaoDAO  locacaoDAO  = new LocacaoDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ─── Tabelas ───────────────────────────────────────────────────────────────
    private DefaultTableModel modelCliente  = new DefaultTableModel(
            new String[]{"ID", "Nome", "CPF"}, 0);
    private DefaultTableModel modelVeiculo  = new DefaultTableModel(
            new String[]{"ID", "Marca", "Modelo", "Categoria", "KM", "Status"}, 0);
    private DefaultTableModel modelLocacao  = new DefaultTableModel(
            new String[]{"ID", "Cliente", "Veículo", "Retirada", "Diárias", "Devolução", "Valor (R$)"}, 0);

    private JTable tblCliente  = new JTable(modelCliente);
    private JTable tblVeiculo  = new JTable(modelVeiculo);
    private JTable tblLocacao  = new JTable(modelLocacao);

    public MainFrame() {
        setTitle("Sistema de Locação de Veículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Clientes",  buildAbaCliente());
        abas.addTab("Veículos",  buildAbaVeiculo());
        abas.addTab("Locações",  buildAbaLocacao());

        add(abas);
        recarregarTodos();
        setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ABA CLIENTES
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildAbaCliente() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JTextField txtNome = new JTextField(20);
        JTextField txtCpf  = new JTextField(15);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Nome:"));   form.add(txtNome);
        form.add(new JLabel("CPF:"));    form.add(txtCpf);

        JButton btnSalvar    = new JButton("Salvar Cliente");
        JButton btnAtualizar = new JButton("Atualizar Selecionado");
        JButton btnRecarregar= new JButton("↻ Recarregar");

        form.add(btnSalvar);
        form.add(btnAtualizar);
        form.add(btnRecarregar);

        painel.add(form, BorderLayout.NORTH);
        painel.add(new JScrollPane(tblCliente), BorderLayout.CENTER);

        // Preenche form ao clicar na tabela
        tblCliente.getSelectionModel().addListSelectionListener(e -> {
            int row = tblCliente.getSelectedRow();
            if (row >= 0) {
                txtNome.setText((String) modelCliente.getValueAt(row, 1));
                txtCpf.setText((String) modelCliente.getValueAt(row, 2));
            }
        });

        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String cpf  = txtCpf.getText().trim();
            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome e CPF.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                clienteDAO.inserir(new Cliente(nome, cpf));
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                txtNome.setText(""); txtCpf.setText("");
                recarregarClientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAtualizar.addActionListener(e -> {
            int row = tblCliente.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela."); return; }
            int id = (int) modelCliente.getValueAt(row, 0);
            try {
                Cliente c = new Cliente(id, txtNome.getText().trim(), txtCpf.getText().trim());
                clienteDAO.atualizar(c);
                JOptionPane.showMessageDialog(this, "Cliente atualizado!");
                recarregarClientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRecarregar.addActionListener(e -> recarregarClientes());
        return painel;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ABA VEÍCULOS
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildAbaVeiculo() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtMarca  = new JTextField(12);
        JTextField txtModelo = new JTextField(12);
        JTextField txtKm     = new JTextField(8);

        JComboBox<Categoria> cboCat = new JComboBox<>(Categoria.values());

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Marca:"));    form.add(txtMarca);
        form.add(new JLabel("Modelo:"));   form.add(txtModelo);
        form.add(new JLabel("Categoria:")); form.add(cboCat);
        form.add(new JLabel("KM:"));       form.add(txtKm);

        JButton btnSalvar     = new JButton("Salvar Veículo");
        JButton btnRecarregar = new JButton("↻ Recarregar");

        form.add(btnSalvar);
        form.add(btnRecarregar);

        painel.add(form, BorderLayout.NORTH);
        painel.add(new JScrollPane(tblVeiculo), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> {
            String marca  = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            if (marca.isEmpty() || modelo.isEmpty() || txtKm.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                double km = Double.parseDouble(txtKm.getText().trim());
                Categoria cat = (Categoria) cboCat.getSelectedItem();
                veiculoDAO.inserir(new Veiculo(marca, modelo, cat, km));
                JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso!");
                txtMarca.setText(""); txtModelo.setText(""); txtKm.setText("");
                recarregarVeiculos();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "KM deve ser numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRecarregar.addActionListener(e -> recarregarVeiculos());
        return painel;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  ABA LOCAÇÕES
    // ═══════════════════════════════════════════════════════════════════════════
    private JPanel buildAbaLocacao() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnLocar     = new JButton("🔑 Nova Locação");
        JButton btnDevolver  = new JButton("🔄 Registrar Devolução");
        JButton btnRecarregar= new JButton("↻ Recarregar");

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(btnLocar);
        topo.add(btnDevolver);
        topo.add(btnRecarregar);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tblLocacao), BorderLayout.CENTER);

        btnLocar.addActionListener(e -> dialogNovaLocacao());
        btnDevolver.addActionListener(e -> dialogDevolucao());
        btnRecarregar.addActionListener(e -> recarregarLocacoes());

        return painel;
    }

    // ─── Dialog: Nova Locação ─────────────────────────────────────────────────
    private void dialogNovaLocacao() {
        List<Cliente> clientes = clienteDAO.listar();
        List<Veiculo> disponiveis = veiculoDAO.listarDisponiveis();

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cadastre ao menos um cliente antes de locar.");
            return;
        }
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum veículo disponível no momento.");
            return;
        }

        JComboBox<Cliente> cboCliente = new JComboBox<>(clientes.toArray(new Cliente[0]));
        JComboBox<Veiculo> cboVeiculo = new JComboBox<>(disponiveis.toArray(new Veiculo[0]));
        JTextField txtDiarias = new JTextField("1", 5);

        Object[] campos = {
            "Cliente:",    cboCliente,
            "Veículo:",    cboVeiculo,
            "Diárias:",    txtDiarias
        };

        int res = JOptionPane.showConfirmDialog(this, campos, "Nova Locação", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Cliente cliente  = (Cliente) cboCliente.getSelectedItem();
            Veiculo veiculo  = (Veiculo) cboVeiculo.getSelectedItem();
            int diarias      = Integer.parseInt(txtDiarias.getText().trim());

            Locacao loc = new Locacao(cliente, veiculo, LocalDateTime.now(),
                                      veiculo.getQuilometragem(), diarias);
            locacaoDAO.inserir(loc);

            veiculo.setStatus("LOCADO");
            veiculoDAO.atualizar(veiculo);

            JOptionPane.showMessageDialog(this,
                    "Locação registrada! ID: " + loc.getId() +
                    "\nRetirada: " + loc.getDataRetirada().format(FMT));
            recarregarTodos();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Diárias deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Dialog: Devolução ────────────────────────────────────────────────────
    private void dialogDevolucao() {
        List<Locacao> emAberto = locacaoDAO.listarEmAberto();
        if (emAberto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há locações em aberto.");
            return;
        }

        JComboBox<Locacao> cboLoc = new JComboBox<>(emAberto.toArray(new Locacao[0]));
        JTextField txtKmFinal = new JTextField(10);

        Object[] campos = {
            "Locação em aberto:", cboLoc,
            "KM Final:", txtKmFinal
        };

        int res = JOptionPane.showConfirmDialog(this, campos, "Registrar Devolução", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            Locacao loc = (Locacao) cboLoc.getSelectedItem();
            double kmFinal = Double.parseDouble(txtKmFinal.getText().trim());

            if (kmFinal < loc.getKmInicial()) {
                JOptionPane.showMessageDialog(this,
                        "KM final não pode ser menor que KM inicial (" + loc.getKmInicial() + ").",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loc.setDataDevolucao(LocalDateTime.now());
            loc.setKmFinal(kmFinal);
            double valor = loc.calcularValor();

            locacaoDAO.registrarDevolucao(loc);

            Veiculo v = loc.getVeiculo();
            v.setStatus("DISPONIVEL");
            v.setQuilometragem(kmFinal);
            veiculoDAO.atualizar(v);

            JOptionPane.showMessageDialog(this,
                    "Devolução registrada com sucesso!\n" +
                    "KM percorrida: " + String.format("%.1f", kmFinal - loc.getKmInicial()) + " km\n" +
                    "Valor total: R$ " + String.format("%.2f", valor));

            recarregarTodos();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "KM Final deve ser numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  RECARREGAR TABELAS
    // ═══════════════════════════════════════════════════════════════════════════
    private void recarregarTodos() {
        recarregarClientes();
        recarregarVeiculos();
        recarregarLocacoes();
    }

    private void recarregarClientes() {
        modelCliente.setRowCount(0);
        for (Cliente c : clienteDAO.listar()) {
            modelCliente.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf()});
        }
    }

    private void recarregarVeiculos() {
        modelVeiculo.setRowCount(0);
        for (Veiculo v : veiculoDAO.listar()) {
            modelVeiculo.addRow(new Object[]{
                v.getId(), v.getMarca(), v.getModelo(),
                v.getCategoria().toString(),
                String.format("%.1f", v.getQuilometragem()),
                v.getStatus()});
        }
    }

    private void recarregarLocacoes() {
        modelLocacao.setRowCount(0);
        for (Locacao l : locacaoDAO.listar()) {
            modelLocacao.addRow(new Object[]{
                l.getId(),
                l.getCliente().getNome(),
                l.getVeiculo().getMarca() + " " + l.getVeiculo().getModelo(),
                l.getDataRetirada().format(FMT),
                l.getQuantidadeDiarias(),
                l.getDataDevolucao() != null ? l.getDataDevolucao().format(FMT) : "Em aberto",
                l.getValorTotal() > 0 ? String.format("%.2f", l.getValorTotal()) : "-"
            });
        }
    }
}
