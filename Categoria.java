package model;

public enum Categoria {
    A("A", "Hatch Compacto",       50.0, 0.25),
    B("B", "Sedan Intermediário",  70.0, 0.30),
    C("C", "SUV Compacto",         90.0, 0.35),
    D("D", "Sedan Executivo",     120.0, 0.45),
    E("E", "SUV Executivo",       150.0, 0.55);

    private final String codigo;
    private final String descricao;
    private final double valorDiaria;   // R$ por diária
    private final double valorKm;       // R$ por km rodado

    Categoria(String codigo, String descricao, double valorDiaria, double valorKm) {
        this.codigo      = codigo;
        this.descricao   = descricao;
        this.valorDiaria = valorDiaria;
        this.valorKm     = valorKm;
    }

    public String getCodigo()      { return codigo; }
    public String getDescricao()   { return descricao; }
    public double getValorDiaria() { return valorDiaria; }
    public double getValorKm()     { return valorKm; }

    public static Categoria fromCodigo(String codigo) {
        for (Categoria c : values()) {
            if (c.codigo.equalsIgnoreCase(codigo)) return c;
        }
        throw new IllegalArgumentException("Categoria inválida: " + codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + descricao;
    }
}
