package dao;

import model.Categoria;
import model.Veiculo;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (marca, modelo, categoria, quilometragem, status) VALUES (?,?,?,?,?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, veiculo.getMarca());
            ps.setString(2, veiculo.getModelo());
            ps.setString(3, veiculo.getCategoria().getCodigo());
            ps.setDouble(4, veiculo.getQuilometragem());
            ps.setString(5, veiculo.getStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) veiculo.setId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir veículo: " + e.getMessage(), e);
        }
    }

    public List<Veiculo> listar() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT id, marca, modelo, categoria, quilometragem, status FROM veiculo ORDER BY marca, modelo";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar veículos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Veiculo> listarDisponiveis() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT id, marca, modelo, categoria, quilometragem, status FROM veiculo WHERE status = 'DISPONIVEL' ORDER BY marca, modelo";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar veículos disponíveis: " + e.getMessage(), e);
        }
        return lista;
    }

    public void atualizar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET marca=?, modelo=?, categoria=?, quilometragem=?, status=? WHERE id=?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, veiculo.getMarca());
            ps.setString(2, veiculo.getModelo());
            ps.setString(3, veiculo.getCategoria().getCodigo());
            ps.setDouble(4, veiculo.getQuilometragem());
            ps.setString(5, veiculo.getStatus());
            ps.setInt(6, veiculo.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar veículo: " + e.getMessage(), e);
        }
    }

    private Veiculo mapear(ResultSet rs) throws SQLException {
        return new Veiculo(
                rs.getInt("id"),
                rs.getString("marca"),
                rs.getString("modelo"),
                Categoria.fromCodigo(rs.getString("categoria")),
                rs.getDouble("quilometragem"),
                rs.getString("status"));
    }
}
