/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unpam.model;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.unpam.view.PesanDialog;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author luthfimubarok71
 */
public class Pekerjaan {

    private String kodePekerjaan, namaPekerjaan;
    private int jumlahTugas;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getKodePekerjaan() {
        return kodePekerjaan;
    }

    public void setKodePekerjaan(String kodePekerjaan) {
        this.kodePekerjaan = kodePekerjaan;
    }

    public String getNamaPekerjaan() {
        return namaPekerjaan;
    }

    public void setNamaPekerjaan(String namaPekerjaan) {
        this.namaPekerjaan = namaPekerjaan;
    }

    public int getJumlahTugas() {
        return jumlahTugas;
    }

    public void setJumlahTugas(int jumlahTugas) {
        this.jumlahTugas = jumlahTugas;
    }

    public String getPesan() {
        return pesan;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection = koneksi.getConnection();
        Statement sta = null;
        String SQLStatement = "";
        int jumlahSimpan = 0;

        if (connection != null) {
            try {
                SQLStatement = "INSERT INTO tbpekerjaan VALUES ('" +
                        kodePekerjaan + "','" +
                        namaPekerjaan + "','" +
                        jumlahTugas + "')";

                sta = connection.createStatement();
                jumlahSimpan = sta.executeUpdate(SQLStatement);

                if (jumlahSimpan < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data pekerjaan";
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Terjadi kesalahan saat menyimpan data pekerjaan:\n" + ex.getMessage();
            } finally {
                try {
                    if (sta != null) sta.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    // Optional: Log atau tampilkan dialog
                }
            }

        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
    
    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection = koneksi.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Object[]> tempList = new ArrayList<>();

        if (connection != null) {
            String sql = "SELECT kodepekerjaan, namapekerjaan FROM tbpekerjaan LIMIT ?, ?";
            try {
                ps = connection.prepareStatement(sql);
                ps.setInt(1, mulai);
                ps.setInt(2, jumlah);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Object[] row = new Object[2];
                    row[0] = rs.getString("kodepekerjaan");
                    row[1] = rs.getString("namapekerjaan");
                    tempList.add(row);
                }

                // Konversi list ke array
                list = new Object[tempList.size()][2];
                for (int i = 0; i < tempList.size(); i++) {
                    list[i] = tempList.get(i);
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Terjadi kesalahan saat membaca data pekerjaan\n" + ex.getMessage();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    // abaikan
                }
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
    
    public boolean hapus(String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection = koneksi.getConnection();
        Statement sta = null;
        String SQLStatement = "";
        int jumlahHapus = 0;

        if (connection != null) {
            try {
                SQLStatement = "DELETE FROM tbpekerjaan WHERE kodePekerjaan = '" + kodePekerjaan + "'";
                sta = connection.createStatement();
                jumlahHapus = sta.executeUpdate(SQLStatement);

                if (jumlahHapus < 1) {
                    adaKesalahan = true;
                    pesan = "Data dengan kode pekerjaan tersebut tidak ditemukan atau sudah dihapus.";
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Terjadi kesalahan saat menghapus data pekerjaan:\n" + ex.getMessage();
            } finally {
                try {
                    if (sta != null) sta.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    // log optional
                }
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean baca(String kodePekerjaan) {
        boolean adaData = false;
        Connection connection = koneksi.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (connection != null) {
            try {
                String sql = "SELECT * FROM tbpekerjaan WHERE kodepekerjaan = ?";
                ps = connection.prepareStatement(sql);
                ps.setString(1, kodePekerjaan);
                rs = ps.executeQuery();

                if (rs.next()) {
                    this.kodePekerjaan = rs.getString("kodePekerjaan");
                    this.namaPekerjaan = rs.getString("namaPekerjaan");
                    this.jumlahTugas = rs.getInt("jumlahTugas");
                    adaData = true;
                } else {
                    pesan = "Data pekerjaan tidak ditemukan.";
                }

            } catch (SQLException ex) {
                pesan = "Terjadi kesalahan saat membaca data pekerjaan:\n" + ex.getMessage();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    // Optional: log error
                }
            }
        } else {
            pesan = "Tidak dapat koneksi ke database:\n" + koneksi.getPesanKesalahan();
        }

        return adaData;
    }

}