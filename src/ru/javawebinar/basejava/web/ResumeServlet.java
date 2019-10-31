package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Storage storage = Config.get().getStorage();
        List<Resume> resumes = storage.getAllSorted();

        response.getWriter().write("<table border=\"1\">");
        response.getWriter().write("<tr>");
        response.getWriter().write("<td>UUID</td>");
        response.getWriter().write("<td>FULL NAME</td>");
        response.getWriter().write("</tr>");

        for (Resume resume : resumes) {
            response.getWriter().write("<tr>");

            response.getWriter().write("<td>");
            response.getWriter().write(resume.getUuid());
            response.getWriter().write("</td>");

            response.getWriter().write("<td>");
            response.getWriter().write(resume.getFullName());
            response.getWriter().write("</td>");

            response.getWriter().write("</tr>");
        }

        response.getWriter().write("</table>");
    }
}