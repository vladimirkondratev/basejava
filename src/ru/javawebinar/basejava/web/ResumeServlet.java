package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;
        if (uuid == null || "".equals(uuid)) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if ((value == null || value.trim().length() == 0) && (values == null || values.length < 2)) {
                resume.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, new ListSection(value.split("\\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> organizations = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "homepage");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!(name == null || name.trim().length() == 0)) {
                                List<Organization.Position> positions = new ArrayList<>();
                                String prefix = type.name() + i;
                                String[] titles = request.getParameterValues(prefix + "title");
                                String[] startDates = request.getParameterValues(prefix + "startDate");
                                String[] endDates = request.getParameterValues(prefix + "endDate");
                                String[] descriptions = request.getParameterValues(prefix + "description");
                                if (titles != null) {
                                    for (int j = 0; j < titles.length; j++) {
                                        if (titles[j].trim().length() != 0) {
                                            positions.add(new Organization.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                        }
                                    }
                                }
                                organizations.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        resume.addSection(type, new OrganizationSection(organizations));
                        break;
                }
            }
        }
        if (uuid == null || "".equals(uuid)) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.setAttribute("count", storage.size());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                resume = storage.get(uuid);
                break;
            case "edit":
            case "create":
                if ("edit".equals(action)) {
                    resume = storage.get(uuid);
                } else {
                    resume = new Resume();
                }
                for (SectionType sectionType : Arrays.asList(SectionType.EXPERIENCE, SectionType.EDUCATION)) {
                    OrganizationSection section = (OrganizationSection) resume.getSection(sectionType);
                    if (section == null) {
                        section = new OrganizationSection(new ArrayList());
                    }
                    section.getItems().add(new Organization(new Link(), new ArrayList<>()));
                    for (Organization organization : section.getItems()) {
                        List<Organization.Position> positions = new ArrayList<>();
                        positions.addAll(organization.getPositions());
                        positions.add(new Organization.Position());
                        organization.setPositions(positions);
                    }
                    resume.addSection(sectionType, section);
                }

                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}
