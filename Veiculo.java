package model;

public class Veiculo {

    private int       id;
    private String    marca;
    private String    modelo;
    private Categoria categoria;
    private double    quilometragem;
    private String    status;   // "DISPONIVEL" ou "LOCADO"

    public Veiculo() {}

    public Veiculo(String marca, String modelo, Categoria categoria, double quilometragem) {
        this.marca         = marca;
        this.modelo        = modelo;
        this.categoria     = categoria;
        this.quilometragem = quilometragem;
        this.status        = "DISPONIVEL";
    }

    public Veiculo(int id, String marca, String modelo, Categoria categoria,
                   double quilometragem, String status) {
        this.id            = id;
        this.marca         = marca;
        this.modelo        = modelo;
        this.categoria     = categoria;
        this.quilometragem = quilometragem;
        this.status        = status;
    }

    // Getters & Setters
    public int       getId()            { return id; }
    public void      setId(int id)      { this.id = id; }

    public String    getMarca()         { return marca; }
    public void      setMarca(String m) { this.marca = m; }

    public String    getModelo()         { return modelo; }
    public void      setModelo(String m) { this.modelo = m; }

    public Categoria getCategoria()              { return categoria; }
    public void      setCategoria(Categoria c)   { this.categoria = c; }

    public double    getQuilometragem()           { return quilometragem; }
    public void      setQuilometragem(double km)  { this.quilometragem = km; }

    public String    getStatus()             { return status; }
    public void      setStatus(String s)     { this.status = s; }

    public boolean   isDisponivel() { return "DISPONIVEL".equals(status); }

    @Override
    public String toString() {
        return "[" + id + "] " + marca + " " + modelo +
               " | Cat: " + categoria.getCodigo() +
               " | KM: " + quilometragem +
               " | " + status;
    }
}
