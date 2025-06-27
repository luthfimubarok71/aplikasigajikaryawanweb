package com.unpam.controller;

import com.unpam.model.Gaji;
import com.unpam.view.MainForm;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
// Pastikan semua library JasperReports sudah ditambahkan ke proyek
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author luthfimubarok71
 */

@WebServlet(name = "LaporanGajiController", urlPatterns = {"/LaporanGajiController"})
public class LaporanGajiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Definisikan format laporan yang didukung
        String[][] formatTypeData = {
            {"PDF (Portable Document Format)", "pdf", "application/pdf"},
            {"XLSX (Microsoft Excel)", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"XLS (Microsoft Excel 97-2003)", "xls", "application/vnd.ms-excel"},
            {"DOCX (Microsoft Word)", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"ODT (OpenDocument Text)", "odt", "application/vnd.oasis.opendocument.text"},
            {"RTF (Rich Text Format)", "rtf", "text/rtf"}
        };

        HttpSession session = request.getSession(true);
        String userName = "";
        try {
            userName = session.getAttribute("userName").toString();
        } catch (Exception ex) {
            response.sendRedirect("LoginController");
            return;
        }

        // Pastikan user sudah login
        if (!((userName == null) || userName.isEmpty())) {
            String tombol = request.getParameter("tombol");
            String opsi = request.getParameter("opsi");
            String ktp = request.getParameter("ktp");
            String ruang = request.getParameter("ruang");
            String formatType = request.getParameter("formatType");

            // Inisialisasi nilai default
            if (tombol == null) tombol = "";
            if (opsi == null) opsi = "Semua";
            if (ktp == null) ktp = "";
            if (ruang == null) ruang = "0";
            if (formatType == null) formatType = formatTypeData[0][0];

            String keterangan = "<br>";
            
            int noType = 0;
            for (int i = 0; i < formatTypeData.length; i++) {
                if (formatTypeData[i][0].equals(formatType)) {
                    noType = i;
                    break;
                }
            }

            if (tombol.equals("Cetak")) {
                Gaji gaji = new Gaji();
                int ruangDipilih = 0;
                try {
                    ruangDipilih = Integer.parseInt(ruang);
                } catch (NumberFormatException ex) {}
                
                String reportsFolderPath = getServletContext().getRealPath("reports") + java.io.File.separator;
                
                if (gaji.cetakLaporan(opsi, ktp, ruangDipilih, reportsFolderPath)) {
                    byte[] reportBytes = gaji.getReportBytes();
                    response.setHeader("Content-Disposition", "inline; filename=GajiReport." + formatTypeData[noType][1]);
                    response.setContentType(formatTypeData[noType][2]);
                    response.setContentLength(reportBytes.length);
                    
                    try (OutputStream outStream = response.getOutputStream()) {
                        outStream.write(reportBytes, 0, reportBytes.length);
                        outStream.flush();
                    }
                    return; 
                } else {
                    keterangan = "Gagal mencetak laporan: " + gaji.getPesan();
                }
            }

            // Membangun Konten HTML Form
            boolean opsiSelected = false;
            String konten = "<h2>Mencetak Laporan Gaji</h2>";
            konten += "<form action='LaporanGajiController' method='post'>";
            konten += "<table>";
            konten += "<tr>";
            konten += "<td align='right'><input type='radio' " + (opsi.equalsIgnoreCase("KTP") ? "checked" : "") + " name='opsi' value='KTP'></td>";
            konten += "<td align='left'>Berdasarkan KTP</td>";
            konten += "<td align='left'><input type='text' value='" + ktp + "' name='ktp' maxlength='15' size='15'></td>";
            konten += "</tr>";
            konten += "<tr>";
            konten += "<td align='right'><input type='radio' " + (opsi.equalsIgnoreCase("ruang") ? "checked" : "") + " name='opsi' value='ruang'></td>";
            konten += "<td align='left'>Berdasarkan Ruang</td>";
            konten += "<td align='left'><select name='ruang'><option value=0>Semua</option>";
            for (int i = 1; i <= 4; i++) {
                konten += "<option value='" + i + "'" + (String.valueOf(i).equals(ruang) ? " selected" : "") + ">" + i + "</option>";
            }
            konten += "</select></td></tr>";
            konten += "<tr>";
            konten += "<td align='right'><input type='radio' " + (opsi.equalsIgnoreCase("Semua") ? "checked" : "") + " name='opsi' value='Semua'></td>";
            konten += "<td align='left' colspan='2'>Semua Karyawan</td>";
            konten += "</tr>";
            konten += "<tr><td colspan='3'><hr></td></tr>";
            konten += "<tr><td>Format Laporan</td>";
            konten += "<td colspan=2><select name='formatType'>";
            for (String[] formatLaporan : formatTypeData) {
                konten += "<option value='" + formatLaporan[0] + "'" + (formatLaporan[0].equals(formatType) ? " selected" : "") + ">" + formatLaporan[0] + "</option>";
            }
            konten += "</select></td></tr>";
            konten += "<tr><td colspan='3'><b>" + keterangan.replaceAll("\n", "<br>") + "</b></td></tr>";
            konten += "<tr><td colspan='3' align='center'><input type='submit' name='tombol' class='btn' value='Cetak' style='width: 120px'></td></tr>";
            konten += "</table></form>";

            new MainForm().tampilkan(konten, request, response);
        } else {
            response.sendRedirect(".");
        }
    }

    // --- BAGIAN PENTING YANG MEMPERBAIKI ERROR ---
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
        return "Short description";
    }
}