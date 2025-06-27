/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unpam.controller;

import com.unpam.model.Pekerjaan;
import com.unpam.view.MainForm;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 *
 * @author luthfimubarok71
 */
@WebServlet(name = "PekerjaanController", urlPatterns = {"/PekerjaanController"})
public class PekerjaanController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
       HttpSession session = request.getSession(true);
        Pekerjaan pekerjaan = new Pekerjaan();
        String userName = "";

        String tombol = request.getParameter("tombol");
        String kodePekerjaan = request.getParameter("kodePekerjaan");
        String namaPekerjaan = request.getParameter("namaPekerjaan");
        String jumlahTugas = request.getParameter("jumlahTugas");
        String mulaiParameter = request.getParameter("mulai");
        String jumlahParameter = request.getParameter("jumlah");
        String kodePekerjaanDipilih = request.getParameter("kodePekerjaanDipilih");

        if (tombol == null) tombol = "";
        if (kodePekerjaan == null) kodePekerjaan = "";
        if (namaPekerjaan == null) namaPekerjaan = "";
        if (jumlahTugas == null) jumlahTugas = "2";
        if (kodePekerjaanDipilih == null) kodePekerjaanDipilih = "";

        int mulai = 0, jumlah = 10;

        try {
            mulai = Integer.parseInt(mulaiParameter);
        } catch (NumberFormatException ex) {}

        try {
            jumlah = Integer.parseInt(jumlahParameter);
        } catch (NumberFormatException ex) {}

        String keterangan = "<br>";

        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex) {}

        if (!((userName == null) || userName.equals(""))) {
            if (tombol.equals("Simpan")) {
                if (!kodePekerjaan.equals("")) {
                    pekerjaan.setKodePekerjaan(kodePekerjaan);
                    pekerjaan.setNamaPekerjaan(namaPekerjaan);
                    pekerjaan.setJumlahTugas(Integer.parseInt(jumlahTugas));

                    if (pekerjaan.simpan()) {
                        kodePekerjaan = "";
                        namaPekerjaan = "";
                        jumlahTugas = "2";
                        keterangan = "Sudah tersimpan";
                    } else {
                        keterangan = "Gagal menyimpan:\n" + pekerjaan.getPesan();
                    }
                } else {
                    keterangan = "Gagal menyimpan, kode pekerjaan tidak boleh kosong";
                }
            } else if (tombol.equals("Hapus")) {
                if (!kodePekerjaan.equals("")) {
                    if (pekerjaan.hapus(kodePekerjaan)) {
                        kodePekerjaan = "";
                        namaPekerjaan = "";
                        jumlahTugas = "2";
                        keterangan = "Data sudah dihapus";
                    } else {
                        keterangan = "Kode pekerjaan tersebut tidak ada, atau ada kesalahan:\n" + pekerjaan.getPesan();
                    }
                } else {
                    keterangan = "Kode pekerjaan masih kosong";
                }
            } else if (tombol.equals("Cari")) {
                if (!kodePekerjaan.equals("")) {
                    if (pekerjaan.baca(kodePekerjaan)) {
                        kodePekerjaan = pekerjaan.getKodePekerjaan();
                        namaPekerjaan = pekerjaan.getNamaPekerjaan();
                        jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
                        keterangan = "<br>";
                    } else {
                        keterangan = "Kode pekerjaan tersebut tidak ada";
                    }
                } else {
                    keterangan = "Kode pekerjaan masih kosong";
                }
            } else if (tombol.equals("Pilih")) {
                kodePekerjaan = kodePekerjaanDipilih;
                namaPekerjaan = "";
                jumlahTugas = "2";

                if (!kodePekerjaanDipilih.equals("")) {
                    if (pekerjaan.baca(kodePekerjaanDipilih)) {
                        kodePekerjaan = pekerjaan.getKodePekerjaan();
                        namaPekerjaan = pekerjaan.getNamaPekerjaan();
                        jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
                        keterangan = "<br>";
                    } else {
                        keterangan = "Kode pekerjaan tersebut tidak ada";
                    }
                } else {
                    keterangan = "Tidak ada yang dipilih";
                }
            }

            // Tabel navigasi
            String kontenLihat = "";
            if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")) {
                if (tombol.equals("Sebelumnya")) {
                    mulai -= jumlah;
                    if (mulai < 0) mulai = 0;
                }

                if (tombol.equals("Berikutnya")) {
                    mulai += jumlah;
                }

                Object[][] listPekerjaan = null;
                if (pekerjaan.bacaData(mulai, jumlah)) {
                    listPekerjaan = pekerjaan.getList();
                } else {
                    keterangan = pekerjaan.getPesan();
                }

                kontenLihat += "<table class='data-table'>";
                kontenLihat += "<tr><th></th><th>Kode Pekerjaan</th><th>Nama Pekerjaan</th></tr>";

                if (listPekerjaan != null) {
                    for (int i = 0; i < listPekerjaan.length; i++) {
                        kontenLihat += "<tr>";
                        kontenLihat += "<td><input type='radio' name='kodePekerjaanDipilih' " + (i == 0 ? "checked" : "") +
                                       " value='" + listPekerjaan[i][0] + "'></td>";
                        kontenLihat += "<td>" + listPekerjaan[i][0] + "</td>";
                        kontenLihat += "<td>" + listPekerjaan[i][1] + "</td>";
                        kontenLihat += "</tr>";
                    }
                }

                kontenLihat += "</table>";

                kontenLihat += "<div class='btn-group'>" +
                               "<input type='submit' name='tombol' value='Sebelumnya'>" +
                               "<input type='submit' name='tombol' value='Pilih'>" +
                               "<input type='submit' name='tombol' value='Berikutnya'>" +
                               "</div>";

                // Field kontrol paginasi
                kontenLihat += "<div style='text-align:center; margin-top: 20px;'>" +
                               "Mulai <input type='text' name='mulai' value='" + mulai + "' style='width: 50px'> " +
                               "Jumlah <select name='jumlah'>";

                for (int i = 1; i <= 10; i++) {
                    int val = i * 10;
                    kontenLihat += "<option value='" + val + "'" + (jumlah == val ? " selected" : "") + ">" + val + "</option>";
                }

                kontenLihat += "</select> " +
                               "</div>"+
                               "<div class='btn-group'>" +
                               "<input type='submit' name='tombol' value='Tampilkan' style='width: 90px'>" +
                               "</div>";
            }


            String konten =
                "<div class='form-box'>" +
                "<h2>Master Data Pekerjaan</h2>" +
                "<form action='PekerjaanController' method='post'>" +
                "<table>" +
                "<tr><td>Kode Pekerjaan</td><td><input type='text' name='kodePekerjaan' value='" + kodePekerjaan + "' maxlength='15'></td></tr>" +
                "<tr><td>Nama Pekerjaan</td><td><input type='text' name='namaPekerjaan' value='" + namaPekerjaan + "' maxlength='30'></td></tr>" +
                "<tr><td>Jumlah Tugas</td><td><select name='jumlahTugas'>";

            for (int i = 1; i <= 5; i++) {
                konten += "<option value='" + i + "'" + (Integer.parseInt(jumlahTugas) == i ? " selected" : "") + ">" + i + "</option>";
            }

            konten += "</select></td></tr>" +
                      "<tr><td colspan='2'><div style='color:red'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll("\"", "'") + "</b></div></td></tr>" +
                      "</table>" +

                      "<div class='btn-group'>" +
                      "<input type='submit' name='tombol' value='Simpan'>" +
                      "<input type='submit' name='tombol' value='Hapus'>" +
                      "<input type='submit' name='tombol' value='Lihat'>" +
                      "</div>" +
                      kontenLihat +
                      "</form></div>";

            new MainForm().tampilkan(konten, request, response);
        } else {
            response.sendRedirect(".");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controller untuk manajemen data pekerjaan";
 
        }
    }

