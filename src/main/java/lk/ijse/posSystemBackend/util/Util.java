package lk.ijse.posSystemBackend.util;

public class Util {
    private static int counter = 1; // Initial value of the counter
    public static synchronized String generateId() {

        String newId = String.format("C%03d", counter);
        counter++;
        return newId;
    }
}

//
//package lk.ijse.posSystemBackend.util;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@WebServlet("/generateId")
//public class Util extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        String newId = Util.generateId();
//
//        try (PrintWriter writer = resp.getWriter()) {
//            writer.write("{\"id\": \"" + newId + "\"}");
//        }
//    }
//}
