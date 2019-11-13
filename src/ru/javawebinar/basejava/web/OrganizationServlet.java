package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrganizationServlet extends HttpServlet {
    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String type = request.getParameter("type");
        SectionType sectionType = SectionType.valueOf(type);
        Resume resume = storage.get(uuid);
        String name = request.getParameter("organization");
        String url = request.getParameter("homepage");
        Organization organization = new Organization(name, url);
        OrganizationSection organizationSection = ((OrganizationSection) resume.getSections().get(sectionType));

        if (organizationSection != null) {
            organizationSection.getItems().add(organization);
        } else {
            resume.addSection(sectionType, new OrganizationSection(organization));
        }

        storage.update(resume);
        response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        SectionType sectionType = SectionType.valueOf(request.getParameter("type"));
        String action = request.getParameter("action");
        String organization = request.getParameter("organization");
        request.setAttribute("uuid", uuid);
        request.setAttribute("type", sectionType);
        request.setAttribute("organization", organization);

        if (action == null) {
            request.getRequestDispatcher("/WEB-INF/jsp/manageOrganization.jsp").forward(request, response);
            return;
        }
        if ("delete".equals(action)) {
            Resume resume = storage.get(uuid);
            List<Organization> organizations = ((OrganizationSection) resume.getSections().get(sectionType)).getItems();
            organizations.remove(organizations.stream()
                    .filter(o -> o.getHomepage().getName().equals(organization))
                    .findFirst()
                    .orElse(null));
            storage.update(resume);
            response.sendRedirect("resume?uuid=" + uuid + "&action=edit");
        }
    }
}
