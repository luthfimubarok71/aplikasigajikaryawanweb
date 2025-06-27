/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author luthfimubarok71
 */
package com.unpam.model;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletException; // Tambahkan import ini jika belum ada
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class Gaji {
    private String ktp;
    private String kodePekerjaan;

    public void setKodePekerjaan(String kodePekerjaan) {
        this.kodePekerjaan = kodePekerjaan;
    }

    private String pesan;
    private Object[][] listGaji;
    private byte[] reportBytes;
    private final Koneksi koneksi = new Koneksi();

    public String getKtp() { return ktp; }
    public void setKtp(String ktp) { this.ktp = ktp; }
    public String getPesan() { return pesan; }
    public Object[][] getListGaji() { return listGaji; }
    public void setListGaji(Object[][] listGaji) { this.listGaji = listGaji; }
    
    // --- PASTIKAN METHOD INI ADA UNTUK MENGATASI ERROR ---
    public byte[] getReportBytes() {
        return reportBytes;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;
        if ((connection = koneksi.getConnection()) != null) {
            String sql = "INSERT INTO tbgaji (ktp, kodepekerjaan, tanggal_gaji, gajikotor, tunjangan, potongan, gajibersih) " + "VALUES (?, ?, NOW(), ?, ?, ?, ?)";
            try {
                for (Object[] barisGaji : listGaji) {
                    try (java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, ktp);
                        preparedStatement.setString(2, kodePekerjaan); // 👈 DITAMBAHKAN
                        preparedStatement.setDouble(3, Double.parseDouble(barisGaji[1].toString())); // gajikotor
                        preparedStatement.setDouble(4, Double.parseDouble(barisGaji[2].toString())); // tunjangan
                        preparedStatement.setDouble(5, 0.0); // potongan default
                        preparedStatement.setDouble(6, Double.parseDouble(barisGaji[0].toString())); // gajibersih

                        int jumlahSimpan = preparedStatement.executeUpdate();
                        if (jumlahSimpan < 1) {
                            adaKesalahan = true;
                            pesan = "Gagal menyimpan detail gaji";
                            break;
                        }
                    }
                }
            } catch (SQLException | NumberFormatException ex) {
                adaKesalahan = true;
                pesan = "Gagal menyimpan data gaji: " + ex.getMessage();
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException ex) {}
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server: " + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }

    public boolean cetakLaporan(String opsi, String ktp, int ruang, String reportsFolderPath) {
        boolean adaKesalahan = false;
        Connection connection;
        reportBytes = null;

        if ((connection = koneksi.getConnection()) != null) {
            String sql;
            if (opsi.equalsIgnoreCase("KTP") && !ktp.isEmpty()) {
                sql = "SELECT g.*, k.nama as nama_karyawan FROM tbgaji g JOIN tbkaryawan k ON g.ktp = k.ktp WHERE g.ktp = '" + ktp + "'";
            } else if (opsi.equalsIgnoreCase("ruang") && ruang > 0) {
                sql = "SELECT g.*, k.nama as nama_karyawan FROM tbgaji g JOIN tbkaryawan k ON g.ktp = k.ktp WHERE k.ruang = " + ruang;
            } else {
                sql = "SELECT g.*, k.nama as nama_karyawan FROM tbgaji g JOIN tbkaryawan k ON g.ktp = k.ktp";
            }

            try {
                ResultSet rset = connection.createStatement().executeQuery(sql);
                JRResultSetDataSource rsetDataSource = new JRResultSetDataSource(rset);
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("IMAGE_PATH", reportsFolderPath);
                
                String jasperFilePath = reportsFolderPath + "GajiReport.jasper";

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, parameters, rsetDataSource);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                reportBytes = out.toByteArray();
                
            } catch (SQLException | JRException ex) {
                adaKesalahan = true;
                pesan = "Gagal mencetak laporan: " + ex.getMessage();
                ex.printStackTrace();
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException ex) {}
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server: " + koneksi.getPesanKesalahan();
        }
        return !adaKesalahan;
    }
}