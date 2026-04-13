package dao;

import model.Categoria;
import model.Cliente;
import model.Locacao;
import model.Veiculo;
import util.Conexao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO {

    public void inserir(Locacao locacao) {
        String sql = "INSERT INTO locacao (cliente_id, veiculo_id, data_retirada, km_inicial, quantidade_diarias) VALUES (?,?,?,?,?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, locacao.getCliente().getId());
            ps.setInt(2, locacao.getVeiculo().getId());
            ps.setTimestamp(3, Timestamp.valueOf(locacao.getDataRetirada()));
            ps.setDouble(4, locacao.getKmInicial());
            ps.setInt(5, locacao.getQuantidadeDiarias());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) locacao.setId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir locação: " + e.getMessage(), e);
        }
    }

    public void registrarDevolucao(Locacao locacao) {
        String sql = "UPDATE locacao SET data_devolucao=?, km_final=?, valor_total=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(locacao.getDataDevolucao()));
            ps.setDouble(2, locacao.getKmFinal());
            ps.setDouble(3, locacao.getValorTotal());
            ps.setInt(4, locacao.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar devolução: " + e.getMessage(), e);
        }
    }

    public List<Locacao> listar() {
        List<Locacao> lista = new ArrayList<>();
        String sql = "SELECT l.id, l.data_retirada, l.km_inicial, l.quantidade_diarias, " +
                     "       l.data_devolucao, l.km_final, l.valor_total, " +
                     "       c.id AS cid, c.nome, c.cpf, " +
                     "       v.id AS vid, v.marca, v.modelo, v.categoria, v.quilometragem, v.status " +
                     "FROM locacao l " +
                     "JOIN cliente c ON c.id = l.cliente_id " +
                     "JOIN veiculo v ON v.id = l.veiculo_id " +
                     "ORDER BY l.id DESC";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Locacao loc = new Locacao();
                loc.setId(rs.getInt("id"));
                loc.setCliente(new Cliente(rs.getInt("cid"), rs.getString("nome"), rs.getString("cpf")));
                loc.setVeiculo(new Veiculo(rs.getInt("vid"), rs.getString("marca"), rs.getString("modelo"),
                        Categoria.fromCodigo(rs.getString("categoria")),
                        rs.getDouble("quilometragem"), rs.getString("status")));
                loc.setDataRetirada(rs.getTimestamp("data_retirada").toLocalDateTime());
                loc.setKmInicial(rs.getDouble("km_inicial"));
                loc.setQuantidadeDiarias(rs.getInt("quantidade_diarias"));

                Timestamp devTs = rs.getTimestamp("data_devolucao");
                if (devTs != null) {
                    loc.setDataDevolucao(devTs.toLocalDateTime());
                    loc.setKmFinal(rs.getDouble("km_final"));
                    loc.setValorTotal(rs.getDouble("valor_total"));
                }
                lista.add(loc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar locações: " + e.getMessage(), e);
        }
        return lista;
    }

    /** Retorna locações em aberto (sem devolução) */
    public List<Locacao> listarEmAberto() {
        List<Locacao> lista = new ArrayList<>();
        String sql = "SELECT l.id, l.data_retirada, l.km_inicial, l.quantidade_diarias, " +
                     "       c.id AS cid, c.nome, c.cpf, " +
                     "       v.id AS vid, v.marca, v.modelo, v.categoria, v.quilometragem, v.status " +
                     "FROM locacao l " +
                     "JOIN cliente c ON c.id = l.cliente_id " +
                     "JOIN veiculo v ON v.id = l.veiculo_id " +
                     "WHERE l.data_devolucao IS NULL " +
                     "ORDER BY l.id DESC";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Locacao loc = new Locacao();
                loc.setId(rs.getInt("id"));
                loc.setCliente(new Cliente(rs.getInt("cid"), rs.getString("nome"), rs.getString("cpf")));
                loc.setVeiculo(new Veiculo(rs.getInt("vid"), rs.getString("marca"), rs.getString("modelo"),
                        Categoria.fromCodigo(rs.getString("categoria")),
                        rs.getDouble("quilometragem"), rs.getString("status")));
                loc.setDataRetirada(rs.getTimestamp("data_retirada").toLocalDateTime());
                loc.setKmInicial(rs.getDouble("km_inicial"));
                loc.setQuantidadeDiarias(rs.getInt("quantidade_diarias"));
                lista.add(loc);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar locações em aberto: " + e.getMessage(), e);
        }
        return lista;
    }
}
