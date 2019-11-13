package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.model.SectionType;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PositionServlet extends HttpServlet {
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
        String positionName = request.getParameter("positionName");
        String positionStartDate = request.getParameter("positionStartDate");
        String positionEndDate = request.getParameter("positionEndDate");
        String positionDescription = request.getParameter("positionDescription");

        Resume resume = storage.get(uuid);

        String name = request.getParameter("organization");
        String url = request.getParameter("homepage");

        Organization organization = ((OrganizationSection) resume.getSections().get(sectionType)).getItems()
                .stream()
                .filter(o -> o.getHomepage().getName().equals(name))
                .findFirst()
                .orElse(null);
        organization.getPositions().add(new Organization.Position(
                DateUtil.format(positionStartDate),
                DateUtil.format(positionEndDate),
                positionName,
                positionDescription));
        storage.update(resume);
        response.sendRedirect("resume?uuid=" + resume.getUuid() + "&action=edit");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        SectionType sectionType = SectionType.valueOf(request.getParameter("type"));
        String organizationName = request.getParameter("organization");
        String positionName = request.getParameter("position");
        String action = request.getParameter("action");
        request.setAttribute("uuid", uuid);
        request.setAttribute("type", sectionType);
        request.setAttribute("organization", organizationName);

        if (action == null) {
            request.getRequestDispatcher("/WEB-INF/jsp/managePosition.jsp").forward(request, response);
            return;
        }

        if ("delete".equals(action)) {
            Resume resume = storage.get(uuid);
            List<Organization> organizations = ((OrganizationSection) resume.getSections().get(sectionType)).getItems();
            Organization organization = organizations
                    .stream()
                    .filter(o -> o.getHomepage().getName().equals(organizationName))
                    .findFirst()
                    .get();
            List<Organization.Position> positions = organization.getPositions();
            positions.remove(positions
                    .stream()
                    .filter(p -> p.getTitle().equals(positionName))
                    .findFirst()
                    .orElse(null));
            storage.update(resume);
            response.sendRedirect("resume?uuid=" + uuid + "&action=edit");
        }
    }
}
