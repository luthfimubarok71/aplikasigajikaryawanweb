/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unpam.controller;

import com.unpam.model.Enkripsi;
import com.unpam.model.Karyawan;
import com.unpam.view.MainForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author luthfimubarok71
 */
@WebServlet(name = "KaryawanController", urlPatterns = {"/KaryawanController"})
public class KaryawanController extends HttpServlet {

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
Karyawan karyawan = new Karyawan();
Enkripsi enkripsi = new Enkripsi();
String userName = "";

String tombol = request.getParameter("tombol");
String ktp = request.getParameter("ktp");
String nama = request.getParameter("nama");
String ruang = request.getParameter("ruang");
String password = request.getParameter("password");
String mulaiParameter = request.getParameter("mulai");
String jumlahParameter = request.getParameter("jumlah");
String ktpDipilih = request.getParameter("ktpDipilih");

if (tombol == null) tombol = "";
if (ktp == null) ktp = "";
if (nama == null) nama = "";
if (ruang == null) ruang = "1";
if (password == null) password = "";
if (ktpDipilih == null) ktpDipilih = "";

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
            switch (tombol) {
                case "Simpan":
                    if (!ktp.equals("")) {
                        karyawan.setKtp(ktp);
                        karyawan.setNama(nama);
                        karyawan.setRuang(Integer.parseInt(ruang));
                        String passwordEncrypted = "";
                        try {
                            passwordEncrypted = enkripsi.hashMD5(password);
                        } catch (Exception ex) {}
                        karyawan.setPassword(passwordEncrypted);
                        
                        if (karyawan.simpan()) {
                            ktp = "";
                            nama = "";
                            ruang = "1";
                            password = "";
                            keterangan = "Sudah tersimpan";
                        } else {
                            keterangan = karyawan.getPesan();
                        }
                    } else {
                        keterangan = "KTP tidak boleh kosong";
                    }           break;
                case "Hapus":
                    if (!ktp.equals("")) {
                        if (karyawan.hapus(ktp)) {
                            ktp = "";
                            nama = "";
                            ruang = "1";
                            password = "";
                            keterangan = "Data sudah dihapus";
                        } else {
                            keterangan = karyawan.getPesan();
                        }
                    } else {
                        keterangan = "KTP masih kosong";
                    }           break;
                case "Cari":
                    if (!ktp.equals("")) {
                        if (karyawan.baca(ktp)) {
                            ktp = karyawan.getKtp();
                            nama = karyawan.getNama();
                            ruang = Integer.toString(karyawan.getRuang());
                            password = karyawan.getPassword();
                            keterangan = "<br>";
                        } else {
                            nama = "";
                            ruang = "1";
                            password = "";
                            keterangan = "KTP \"" + ktp + "\" tidak ada";
                        }
                    } else {
                        keterangan = "KTP harus diisi";
                    }           break;
                case "Pilih":
                    ktp = ktpDipilih;
                    nama = "";
                    ruang = "1";
                    if (!ktpDipilih.equals("")) {
                        if (karyawan.baca(ktpDipilih)) {
                            ktp = karyawan.getKtp();
                            nama = karyawan.getNama();
                            ruang = Integer.toString(karyawan.getRuang());
                            password = karyawan.getPassword();
                            keterangan = "<br>";
                        } else {
                            keterangan = "KTP \"" + ktpDipilih + "\" tidak ada";
                        }
                    } else {
                        keterangan = "Tidak ada yang dipilih";
                    }           break;
                default:
                    break;
            }

    // Navigasi tabel (Lihat, Sebelumnya, Berikutnya)
    String kontenTabel = "";
    if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")) {
        if (tombol.equals("Sebelumnya")) {
            mulai -= jumlah;
            if (mulai < 0) mulai = 0;
        }

        if (tombol.equals("Berikutnya")) {
            mulai += jumlah;
        }

        Object[][] listKaryawan = null;
        if (karyawan.bacaData(mulai, jumlah)) {
            listKaryawan = karyawan.getList();
        } else {
            keterangan = karyawan.getPesan();
        }

        kontenTabel += "<table class='data-table'>";
        kontenTabel += "<tr><th></th><th>KTP</th><th>Nama</th><th>Ruang</th></tr>";

        if (listKaryawan != null) {
            for (int i = 0; i < listKaryawan.length; i++) {
                kontenTabel += "<tr>";
                kontenTabel += "<td><input type='radio' name='ktpDipilih' " + (i == 0 ? "checked" : "") +
                               " value='" + listKaryawan[i][0].toString() + "'></td>";
                kontenTabel += "<td>" + listKaryawan[i][0].toString() + "</td>";
                kontenTabel += "<td>" + listKaryawan[i][1].toString() + "</td>";
                kontenTabel += "<td>" + listKaryawan[i][2].toString() + "</td>";
                kontenTabel += "</tr>";
            }
        }
        kontenTabel += "</table>";

        kontenTabel += "<div class='btn-group'>" +
                       "<input type='submit' name='tombol' value='Sebelumnya'>" +
                       "<input type='submit' name='tombol' value='Pilih'>" +
                       "<input type='submit' name='tombol' value='Berikutnya'>" +
                       "</div>";
    }

    String konten =
        "<div class='form-box'>" +
        "<h2>Entri Data Karyawan</h2>" +
        "<form action='KaryawanController' method='post'>" +
        "<table>" +
        "<tr><td>KTP</td><td><input type='text' name='ktp' value='" + ktp + "' maxlength='15'></td></tr>" +
        "<tr><td>Nama</td><td><input type='text' name='nama' value='" + nama + "' maxlength='30'></td></tr>" +
        "<tr><td>Ruang</td><td><select name='ruang'>";

    for (int i = 1; i <= 10; i++) {
        if (Integer.parseInt(ruang) == i) {
            konten += "<option selected value='" + i + "'>" + i + "</option>";
        } else {
            konten += "<option value='" + i + "'>" + i + "</option>";
        }
    }

    konten += "</select></td></tr>" +
              "<tr><td>Password</td><td><input type='password' name='password' value='" + password + "' maxlength='15'></td></tr>" +
              "<tr><td colspan='2'><div style='color:red'><b>" + keterangan.replaceAll("\n", "<br>").replaceAll("\"", "'") + "</b></div></td></tr>" +
              "</table>" +

              "<div class='btn-group'>" +
              "<input type='submit' name='tombol' value='Simpan'>" +
              "<input type='submit' name='tombol' value='Hapus'>" +
              "<input type='submit' name='tombol' value='Lihat'>" +
              "</div>" +
              kontenTabel +
              "</form></div>";


    new MainForm().tampilkan(konten, request, response);
} else {
    response.sendRedirect(".");
}
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
