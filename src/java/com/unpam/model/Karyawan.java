/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.unpam.view.PesanDialog;


/**
 *
// * @author luthfimubarok71
 */

public class Karyawan {
    private String ktp, nama, password;
    private int ruang;
    private String pesan;
    private Object[][] list;
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getRuang() {
        return ruang;
    }

    public void setRuang(int ruang) {
        this.ruang = ruang;
    }

    public String getPesan() {
        return pesan;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        PreparedStatement ps = null;
        String sql = "";
        int hasil = 0;

        if (connection != null) {
            try {
                sql = "INSERT INTO tbkaryawan (ktp, nama, ruang, password) VALUES (?, ?, ?, ?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, ktp);
                ps.setString(2, nama);
                ps.setInt(3, ruang);
                ps.setString(4, password);
                hasil = ps.executeUpdate();

                if (hasil < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data karyawan.";
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Terjadi kesalahan saat menyimpan: \n" + ex.getMessage();
            } finally {
                try {
                    if (ps != null) ps.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    // abaikan
                }
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak bisa koneksi ke database.\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean baca(String ktp) {
        boolean adaKesalahan = false;
        Connection connection = koneksi.getConnection();

        if (connection != null) {
            String sql = "SELECT * FROM tbkaryawan WHERE ktp=?";
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, ktp);
                rs = ps.executeQuery();

                if (rs.next()) {
                    this.ktp = rs.getString("ktp");
                    this.nama = rs.getString("nama");
                    this.ruang = rs.getInt("ruang");
                    this.password = rs.getString("password");
                } else {
                    adaKesalahan = true;
                    pesan = "KTP \"" + ktp + "\" tidak ditemukan.";
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal membaca data karyawan.\n" + ex.getMessage();
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
            pesan = "Tidak bisa koneksi ke database.\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean hapus(String ktp) {
        boolean adaKesalahan = false;
        Connection connection = koneksi.getConnection();
        PreparedStatement ps = null;

        if (connection != null) {
            String sql = "DELETE FROM karyawan WHERE ktp=?";
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, ktp);

                int hasil = ps.executeUpdate();

                if (hasil < 1) {
                    adaKesalahan = true;
                    pesan = "Karyawan dengan KTP \"" + ktp + "\" tidak ditemukan.";
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal menghapus data.\n" + ex.getMessage();
            } finally {
                try {
                    if (ps != null) ps.close();
                    if (connection != null) connection.close();
                } catch (SQLException ex) {
                    // abaikan
                }
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak bisa koneksi ke database.\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection = koneksi.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        list = new Object[0][0];

        if (connection != null) {
            String sql = "SELECT * FROM tbkaryawan LIMIT ?, ?";
            try {
                ps = connection.prepareStatement(sql);
                ps.setInt(1, mulai);
                ps.setInt(2, jumlah);
                rs = ps.executeQuery();

                rs.last();
                int baris = rs.getRow();
                rs.beforeFirst();
                list = new Object[baris][3];
                int i = 0;
                while (rs.next()) {
                    list[i][0] = rs.getString("ktp");
                    list[i][1] = rs.getString("nama");
                    list[i][2] = rs.getInt("ruang");
                    i++;
                }

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal membaca data dari database.\n" + ex.getMessage();
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
            pesan = "Koneksi database gagal.\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
}