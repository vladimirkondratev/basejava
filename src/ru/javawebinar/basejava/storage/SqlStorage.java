package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {
    private SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        String statement = "DELETE FROM resume";
        sqlHelper.connectAndExecute(statement, ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            saveContacts(conn, resume);
            saveSections(conn, resume);
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteContacts(conn, resume);
            saveContacts(conn, resume);

            deleteSections(conn, resume);
            saveSections(conn, resume);

            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        String statement = "DELETE FROM resume WHERE uuid = ?";
        sqlHelper.connectAndExecute(statement, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid=?")) {
                ps.setString(1, uuid);
                ps.execute();
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            getContactsAndSections(conn, resume);
            return resume;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> resumes = new TreeMap<>();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume resume = new Resume(rs.getString("uuid").trim(),
                            rs.getString("full_name"));
                    resumes.put(resume.getUuid(), resume);
                }
            }
            for (Resume resume : resumes.values()) {
                getContactsAndSections(conn, resume);
            }
            return null;
        });
        return new ArrayList<>(resumes.values());
    }

    @Override
    public int size() {
        String statement = "SELECT COUNT(*) FROM resume";
        return sqlHelper.connectAndExecute(statement, ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private interface GetExecute {
        void execute(ResultSet rs, Resume resume) throws SQLException;
    }

    private void getContactsAndSections(Connection conn, Resume resume) throws SQLException {
        getItems(conn, "SELECT * FROM contact c WHERE c.resume_uuid =?", resume, this::addContact);
        getItems(conn, "SELECT * FROM section s WHERE s.resume_uuid =?", resume, this::addSection);
    }

    private void getItems(Connection conn, String statement, Resume resume, GetExecute executeCode) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                executeCode.execute(rs, resume);
            }
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        resume.addContact(type, value);
    }

    private void saveContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, contact.getKey().name());
                ps.setString(3, contact.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume resume) throws SQLException {
        deleteItem(conn, resume, "DELETE FROM contact WHERE resume_uuid =?");
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        SectionType type = SectionType.valueOf(rs.getString("type"));
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                resume.addSection(type, new TextSection(value));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                resume.addSection(type, new ListSection(value.split("\n")));
                break;
        }
    }

    private void saveSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                SectionType type = section.getKey();
                ps.setString(2, type.name());
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        ps.setString(3, ((TextSection) section.getValue()).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = ((ListSection) section.getValue()).getItems();
                        ps.setString(3, String.join("\n", items));
                        break;
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteSections(Connection conn, Resume resume) throws SQLException {
        deleteItem(conn, resume, "DELETE FROM section WHERE resume_uuid =?");
    }

    private void deleteItem(Connection conn, Resume resume, String statement) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }
}
