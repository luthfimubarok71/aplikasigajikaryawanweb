/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

/**
 *
 * @author luthfimubarok71
 */
package com.unpam.controller;

import com.unpam.model.Gaji;
import com.unpam.model.Karyawan;
import com.unpam.model.Pekerjaan;
import com.unpam.view.MainForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GajiController", urlPatterns = {"/GajiController"})
public class GajiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true); 

        // 1. Cek Sesi Login
        try {
            if (session.getAttribute("userName") == null) {
                response.sendRedirect("LoginController");
                return;
            }
        } catch (Exception e) {
            response.sendRedirect("LoginController");
            return;
        }

        // 2. Deklarasi Model dan Variabel
        Karyawan karyawan = new Karyawan();
        Pekerjaan pekerjaan = new Pekerjaan();
        Gaji gaji = new Gaji();

        // Mengambil semua parameter dari request
        String tombol = request.getParameter("tombol");
        String tombolKaryawan = request.getParameter("tombolKaryawan");
        String tombolPekerjaan = request.getParameter("tombolPekerjaan");
        
        String ktp = request.getParameter("ktp");
        String namaKaryawan = request.getParameter("namaKaryawan");
        String ruang = request.getParameter("ruang");
        
        String kodePekerjaan = request.getParameter("kodePekerjaan");
        String namaPekerjaan = request.getParameter("namaPekerjaan");
        String jumlahTugas = request.getParameter("jumlahTugas");

        String gajikotor = request.getParameter("gajikotor");
        String tunjangan = request.getParameter("tunjangan");
        String gajibersih = request.getParameter("gajibersih");

        // 3. Inisialisasi Nilai Default
        tombol = (tombol == null) ? "" : tombol;
        tombolKaryawan = (tombolKaryawan == null) ? "" : tombolKaryawan;
        tombolPekerjaan = (tombolPekerjaan == null) ? "" : tombolPekerjaan;
        ktp = (ktp == null) ? "" : ktp;
        namaKaryawan = (namaKaryawan == null) ? "" : namaKaryawan;
        ruang = (ruang == null) ? "" : ruang;
        kodePekerjaan = (kodePekerjaan == null) ? "" : kodePekerjaan;
        namaPekerjaan = (namaPekerjaan == null) ? "" : namaPekerjaan;
        jumlahTugas = (jumlahTugas == null) ? "" : jumlahTugas;
        gajikotor = (gajikotor == null || gajikotor.isEmpty()) ? "0" : gajikotor;
        tunjangan = (tunjangan == null || tunjangan.isEmpty()) ? "0" : tunjangan;
        gajibersih = (gajibersih == null || gajibersih.isEmpty()) ? "0" : gajibersih;
        
        String keterangan = "<br>";

        // 4. Proses Logika Tombol
        if (tombolKaryawan.equals("Cari") && !ktp.isEmpty()) {
            if (karyawan.baca(ktp)) {
                namaKaryawan = karyawan.getNama();
                ruang = Integer.toString(karyawan.getRuang());
            } else {
                keterangan = karyawan.getPesan();
            }
        }

        if (tombolPekerjaan.equals("Cari") && !kodePekerjaan.isEmpty()) {
            if (pekerjaan.baca(kodePekerjaan)) {
                namaPekerjaan = pekerjaan.getNamaPekerjaan();
                jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
            } else {
                keterangan = pekerjaan.getPesan();
            }
        }
        
        if (tombol.equals("Simpan")) {
            if (!ktp.isEmpty() && !kodePekerjaan.isEmpty()) {
                gaji.setKtp(ktp);
                gaji.setKodePekerjaan(kodePekerjaan);
                gaji.setListGaji(new Object[][]{{gajibersih, gajikotor, tunjangan}});
                if (gaji.simpan()) {
                    keterangan = "Gaji sudah disimpan.";
                    ktp = ""; namaKaryawan = ""; ruang = ""; kodePekerjaan = ""; namaPekerjaan = "";
                    jumlahTugas = ""; gajibersih = "0"; gajikotor = "0"; tunjangan = "0";
                } else {
                    keterangan = "Gagal menyimpan: " + gaji.getPesan();
                }
            } else {
                keterangan = "KTP dan Kode Pekerjaan tidak boleh kosong.";
            }
        }
        
        String konten = "<div class='form-box'>" +
            "<h2>Input Gaji Karyawan</h2>" +
            "<form action='GajiController' method='post'>" +
            "<table>" +
            "<tr><td align='right'>KTP</td>" +
            "<td><input type='text' name='ktp' value='" + ktp + "'></td></tr>" +
            "<tr><td align='right'></td>" +
            "<td><input type='submit' name='tombolKaryawan' value='Cari' class='btn-small'></td></tr>" +

            "<tr><td align='right'>Nama</td><td><input type='text' readonly name='namaKaryawan' value='" + namaKaryawan + "'></td></tr>" +
            "<tr><td align='right'>Ruang</td><td><input type='text' readonly name='ruang' value='" + ruang + "' style='width: 60px;'></td></tr>" +

            "<tr><td colspan='2'><hr></td></tr>" +

            "<tr><td align='right'>Kode Pekerjaan</td>" +
            "<td><input type='text' name='kodePekerjaan' value='" + kodePekerjaan + "'></td></tr>" +
            "<tr><td align='right'></td>" +
            "<td><input type='submit' name='tombolPekerjaan' value='Cari' class='btn-small'></td></tr>" +

            "<tr><td align='right'>Nama Pekerjaan</td><td><input type='text' readonly name='namaPekerjaan' value='" + namaPekerjaan + "'></td></tr>" +
            "<tr><td align='right'>Jumlah Tugas</td><td><input type='text' readonly name='jumlahTugas' value='" + jumlahTugas + "' style='width: 60px;'></td></tr>" +

            "<tr><td colspan='2'><hr></td></tr>" +

            "<tr><td align='right'>Gaji Kotor</td><td><input type='text' name='gajikotor' value='" + gajikotor + "'></td></tr>" +
            "<tr><td align='right'>Tunjangan</td><td><input type='text' name='tunjangan' value='" + tunjangan + "'></td></tr>" +
            "<tr><td align='right'>Gaji Bersih</td><td><input type='text' name='gajibersih' value='" + gajibersih + "'></td></tr>" +
            "</table>" +

            "<div class='error-message'>" + keterangan.replaceAll("\n", "<br>") + "</div>" +

            "<div class='btn-group'>" +
            "<input type='submit' name='tombol' value='Simpan'>" +
            "<input type='submit' name='tombol' value='Hapus'>" +
            "</div>" +
            "</form></div>";


        new MainForm().tampilkan(konten, request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}