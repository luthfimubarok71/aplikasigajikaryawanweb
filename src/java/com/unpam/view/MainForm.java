/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unpam.view;

/**
 *
 * @author luthfimubarok71
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "MainForm", urlPatterns = {"/MainForm"})
public class MainForm extends HttpServlet {

   public void tampilkan(String konten, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);

        String menu = "<br><b>Master Data</b><br>"
                    + "<a href='Karyawan'>Karyawan</a><br>"
                    + "<a href='Pekerjaan'>Pekerjaan</a><br>"
                    + "<b>Transaksi</b><br>"
                    + "<a href='Gaji'>Gaji</a><br>"
                    + "<b>Laporan</b><br>"
                    + "<a href='Gaji'>Gaji</a><br>"
                    + "<a href='LoginController'>Login</a><br><br>";

        String topMenu = "<nav><ul>"
                       + "<li><a href='.'>Home</a></li>"
                       + "<li><a href='#'>Master Data</a>"
                       + "<ul>"
                       + "<li><a href='Karyawan'>Karyawan</a></li>"
                       + "<li><a href='Pekerjaan'>Pekerjaan</a></li>"
                       + "</ul></li>"
                       + "<li><a href='#'>Transaksi</a>"
                       + "<ul>"
                       + "<li><a href='Gaji'>Gaji</a></li>"
                       + "</ul></li>"
                       + "<li><a href='#'>Laporan</a>"
                       + "<ul>"
                       + "<li><a href='Gaji'>Gaji</a></li>"
                       + "</ul></li>"
                       + "<li><a href='LoginController'>Login</a></li>"
                       + "</ul></nav>";

        String userName = "";

        if (!session.isNew()) {
            try {
                userName = session.getAttribute("userName").toString();
            } catch (Exception ex) {
                // kosongkan jika error
            }

            if (!(userName == null || userName.equals(""))) {
                if (konten.equals("")) {
                    konten = "<br><h1>Selamat Datang</h1><h2>" + userName + "</h2>";
                }
            }

            try {
                menu = session.getAttribute("menu").toString();
            } catch (Exception ex) {
            }

            try {
                topMenu = session.getAttribute("topMenu").toString();
            } catch (Exception ex) {
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link href='style.css' rel='stylesheet' type='text/css' />");
            out.println("<title>Informasi Gaji Karyawan</title>");
            out.println("</head>");
            out.println("<body bgcolor='#808080'>");
            out.println("<center>");
            out.println("<table width='80%' bgcolor='#eeeeee'>");

            out.println("<tr>");
            out.println("<td colspan='2' align='center'>");
            out.println("<br>");
            out.println("<h2 style='margin-bottom:0px; margin-top:0px;'>");
            out.println("Informasi Gaji Karyawan");
            out.println("</h2>");
            out.println("<h1 style='margin-bottom:0px; margin-top:0px;'>PT Luthfi Mubarok</h1>");
            out.println("<h3 style='margin-bottom:0px; margin-top:0px;'>");
            out.println("Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten");
            out.println("</h3>");
            out.println("</td>");
            out.println("</tr>");

            out.println("<tr height='400'>");
            out.println("<td width='200' align='center' valign='top' bgcolor='#eeeeee'>");
            out.println("<div id='menu'>");
            out.println(menu);
            out.println("</div>");
            out.println("</td>");
            out.println("<td align='center' valign='top' bgcolor='#ffffff'>");
            out.println(topMenu);
            out.println("<br>");
            out.println(konten);
            out.println("</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td colspan='2' align='center' bgcolor='#eeeeff'>");
            out.println("<small>");
            out.println("Copyright &copy; 2025 PT Luthfi Mubarok<br>");
            out.println("Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten");
            out.println("</small>");
            out.println("</td>");
            out.println("</tr>");

            out.println("</table>");
            out.println("</center>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String konten = request.getAttribute("konten") != null
                ? (String) request.getAttribute("konten")
                : "";
        tampilkan(konten, request, response);
    }
}