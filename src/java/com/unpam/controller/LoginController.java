/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.unpam.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.unpam.model.Karyawan;
import com.unpam.model.Enkripsi;
import com.unpam.view.MainForm;


/**
 *
 * @author luthfimubarok71
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);
        String userName = "";

        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex) {}

        if (userName == null || userName.equals("")) {
            String userId = request.getParameter("userId");
            String password = request.getParameter("password");

            String konten = "<div class='login-box'>"
                        + "<form action=LoginController method=post>"
                        + "<table>"
                        + "<tr><td>User ID</td><td><input type=text name=userId></td></tr>"
                        + "<tr><td>Password</td><td><input type=password name=password></td></tr>"
                        + "<tr><td colspan=2 align=center><input type=submit value=Login></td></tr>"
                        + "</table>"
                        + "</form>";

            String pesan = "";

            if (userId == null || userId.equals("")) {
                pesan = "<div class='error'>User ID harus diisi</div>";
            } else {
                Karyawan karyawan = new Karyawan();
                Enkripsi enkripsi = new Enkripsi();
                pesan = "<br><br><font style='color:red'>User ID atau password salah</font>";

                if (karyawan.baca(userId)) {
                    String passwordEncrypted = "";
                    try {
                        passwordEncrypted = enkripsi.hashMD5(password);
                    } catch (Exception ex) {}

                    if (passwordEncrypted.equals(karyawan.getPassword())) {
                        pesan = "";
                        session.setAttribute("userName", karyawan.getNama().equals("") ? "No Name" : karyawan.getNama());

                        String menu = "<br><b>Master Data</b><br>"
                                + "<a href=KaryawanController>Karyawan</a><br>"
                                + "<a href=PekerjaanController>Pekerjaan</a><br>"
                                + "<b>Transaksi</b><br>"
                                + "<a href=GajiController>Gaji</a><br>"
                                + "<b>Laporan</b><br>"
                                + "<a href=LaporanGajiController>Gaji</a><br>"
                                + "<a href=LogoutController>Logout</a><br>";

                        session.setAttribute("menu", menu);

                        String topMenu = "<nav><ul>"
                                + "<li><a href=./>Home</a></li>"
                                + "<li><a href=#>Master Data</a>"
                                + "<ul>"
                                + "<li><a href=KaryawanController>Karyawan</a></li>"
                                + "<li><a href=PekerjaanController>Pekerjaan</a></li>"
                                + "</ul></li>"
                                + "<li><a href=#>Transaksi</a>"
                                + "<ul><li><a href=GajiController>Gaji</a></li></ul></li>"
                                + "<li><a href=#>Laporan</a>"
                                + "<ul><li><a href=LaporanGajiController>Gaji</a></li></ul></li>"
                                + "<li><a href=LogoutController>Logout</a></li>"
                                + "</ul></nav>";

                        session.setAttribute("topMenu", topMenu);
                        session.setMaxInactiveInterval(15 * 60); // 15 menit

                        konten = "";
                    } else {
                        if (!karyawan.getPesan().substring(0, 3).equals("KTP")) {
                            pesan = "<br><br><font style='color:red'>" + karyawan.getPesan().replace("\n", "<br>") + "</font>";
                        }
                    }
                }
            }

            new MainForm().tampilkan(konten + pesan, request, response);
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
        return "Login Controller untuk autentikasi user";
    }
}
