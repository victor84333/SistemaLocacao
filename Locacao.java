package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Locacao {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int           id;
    private Cliente       cliente;
    private Veiculo       veiculo;
    private LocalDateTime dataRetirada;
    private double        kmInicial;
    private int           quantidadeDiarias;

    // Preenchidos na devolução
    private LocalDateTime dataDevolucao;
    private double        kmFinal;
    private double        valorTotal;

    public Locacao() {}

    public Locacao(Cliente cliente, Veiculo veiculo,
                   LocalDateTime dataRetirada, double kmInicial, int quantidadeDiarias) {
        this.cliente           = cliente;
        this.veiculo           = veiculo;
        this.dataRetirada      = dataRetirada;
        this.kmInicial         = kmInicial;
        this.quantidadeDiarias = quantidadeDiarias;
    }

    /** Calcula o valor total com base em categoria, diárias e km rodados. */
    public double calcularValor() {
        if (kmFinal <= 0) return 0;
        Categoria cat = veiculo.getCategoria();
        double kmPercorrida = kmFinal - kmInicial;
        valorTotal = (cat.getValorDiaria() * quantidadeDiarias)
                   + (cat.getValorKm()     * kmPercorrida);
        return valorTotal;
    }

    // Getters & Setters
    public int           getId()                    { return id; }
    public void          setId(int id)              { this.id = id; }

    public Cliente       getCliente()               { return cliente; }
    public void          setCliente(Cliente c)      { this.cliente = c; }

    public Veiculo       getVeiculo()               { return veiculo; }
    public void          setVeiculo(Veiculo v)      { this.veiculo = v; }

    public LocalDateTime getDataRetirada()          { return dataRetirada; }
    public void          setDataRetirada(LocalDateTime d) { this.dataRetirada = d; }

    public double        getKmInicial()             { return kmInicial; }
    public void          setKmInicial(double km)    { this.kmInicial = km; }

    public int           getQuantidadeDiarias()     { return quantidadeDiarias; }
    public void          setQuantidadeDiarias(int q){ this.quantidadeDiarias = q; }

    public LocalDateTime getDataDevolucao()         { return dataDevolucao; }
    public void          setDataDevolucao(LocalDateTime d) { this.dataDevolucao = d; }

    public double        getKmFinal()               { return kmFinal; }
    public void          setKmFinal(double km)      { this.kmFinal = km; }

    public double        getValorTotal()            { return valorTotal; }
    public void          setValorTotal(double v)    { this.valorTotal = v; }

    @Override
    public String toString() {
        return "[" + id + "] " +
               cliente.getNome() + " | " + veiculo.getMarca() + " " + veiculo.getModelo() +
               " | Retirada: " + (dataRetirada != null ? dataRetirada.format(FMT) : "-") +
               " | Diárias: " + quantidadeDiarias +
               (valorTotal > 0 ? " | Valor: R$ " + String.format("%.2f", valorTotal) : "");
    }
}
