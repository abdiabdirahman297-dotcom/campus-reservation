package com.ajman.campus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {

    // Change these to match your MySQL login
    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_facility_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Campus#2026Sql";

    // If someone opens the servlet address directly, send them to the form
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect("booking_form.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        // ----- Step 1: get the form data -----
        String userName = req.getParameter("userName");
        String userRole = req.getParameter("userRole");
        String itemCategory = req.getParameter("itemCategory");
        String quantityText = req.getParameter("quantity");
        String daysText = req.getParameter("bookingDays");

        // ----- Step 2: clean the name (trim + UPPERCASE) -----
        if (userName == null) {
            userName = "";
        }
        userName = userName.trim().toUpperCase();

        // ----- Step 3: convert the numbers safely -----
        int quantity = 0;
        int bookingDays = 0;
        try {
            quantity = Integer.parseInt(quantityText.trim());
            bookingDays = Integer.parseInt(daysText.trim());
        } catch (NumberFormatException | NullPointerException e) {
            showError(out, "Quantity and booking days must be whole numbers.");
            return;
        }

        // ----- Step 4: check the values -----
        if (userName.isEmpty()) {
            showError(out, "Please enter your name.");
            return;
        }
        if (quantity <= 0 || bookingDays <= 0) {
            showError(out, "Quantity and booking days must be greater than 0.");
            return;
        }
        if (userRole == null || !(userRole.equals("STUDENT") || userRole.equals("FACULTY"))) {
            showError(out, "Role must be STUDENT or FACULTY.");
            return;
        }

        // ----- Step 5: daily rate for the item -----
        double dailyRate = 0;
        if ("LAPTOP".equals(itemCategory)) {
            dailyRate = 50;
        } else if ("PROJECTOR".equals(itemCategory)) {
            dailyRate = 80;
        } else if ("POD".equals(itemCategory)) {
            dailyRate = 100;
        } else {
            showError(out, "Item must be LAPTOP, PROJECTOR or POD.");
            return;
        }

        // ----- Step 6: calculate the money -----
        double subtotal = quantity * bookingDays * dailyRate;

        double discountPercent = 0;
        if (userRole.equals("FACULTY")) {
            discountPercent = 15;
        }
        double discountAmount = subtotal * discountPercent / 100;
        double netTotal = subtotal - discountAmount;

        // ----- Step 7: save into MySQL with a PreparedStatement -----
        String sql = "INSERT INTO reservations "
                + "(user_name, user_role, item_category, quantity, booking_days, daily_rate, subtotal, net_total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, userName);
                ps.setString(2, userRole);
                ps.setString(3, itemCategory);
                ps.setInt(4, quantity);
                ps.setInt(5, bookingDays);
                ps.setDouble(6, dailyRate);
                ps.setDouble(7, subtotal);
                ps.setDouble(8, netTotal);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(out, "Could not save the reservation. Check the database.");
            return;
        }

        // ----- Step 8: show the receipt -----
        // Replace < and > so nobody can put HTML or scripts in their name
        String safeName = userName.replace("<", "&lt;").replace(">", "&gt;");

        out.println("<html><head><title>Booking Receipt</title></head>");
        out.println("<body style='font-family:Arial; padding:20px;'>");
        out.println("<h2>Booking Receipt</h2>");
        out.println("<p>Name: " + safeName + "</p>");
        out.println("<p>Role: " + userRole + "</p>");
        out.println("<p>Item: " + itemCategory + "</p>");
        out.println("<p>Quantity: " + quantity + "</p>");
        out.println("<p>Booking days: " + bookingDays + "</p>");
        out.println("<p>Daily rate: " + String.format("%.2f", dailyRate) + " AED</p>");
        out.println("<p>Subtotal: " + String.format("%.2f", subtotal) + " AED</p>");
        out.println("<p>Discount (" + (int) discountPercent + "%): " + String.format("%.2f", discountAmount) + " AED</p>");
        out.println("<h3>Net Total: " + String.format("%.2f", netTotal) + " AED</h3>");
        out.println("<a href='booking_form.html'>Make another reservation</a>");
        out.println("</body></html>");
    }

    // Small helper that prints an error page
    private void showError(PrintWriter out, String message) {
        out.println("<html><body style='font-family:Arial; padding:20px;'>");
        out.println("<h2 style='color:red;'>Reservation failed</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='booking_form.html'>Back to the form</a>");
        out.println("</body></html>");
    }
}
