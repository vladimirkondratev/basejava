package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

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
            deleteItem(conn, resume, "DELETE FROM contact WHERE resume_uuid =?");
            saveContacts(conn, resume);

            deleteItem(conn, resume, "DELETE FROM section WHERE resume_uuid =?");
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
            getItems(conn, "SELECT * FROM contact c WHERE c.resume_uuid =?", resume, this::addContact);
            getItems(conn, "SELECT * FROM section s WHERE s.resume_uuid =?", resume, this::addSection);
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
            getItems(conn, "SELECT * FROM contact", resumes, this::addContact);
            getItems(conn, "SELECT * FROM section", resumes, this::addSection);
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

    private void getItems(Connection conn, String statement, Resume resume, GetExecute executeCode) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                executeCode.execute(rs, resume);
            }
        }
    }

    private void getItems(Connection conn, String statement, Map<String, Resume> resumes, GetExecute executeCode) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                executeCode.execute(rs, resumes.get(rs.getString("resume_uuid").trim()));
            }
        }
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        resume.addContact(type, value);
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        SectionType type = SectionType.valueOf(rs.getString("type"));
        resume.addSection(type, JsonParser.read(value, AbstractSection.class));
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

    private void saveSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> section : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                SectionType type = section.getKey();
                ps.setString(2, type.name());
                ps.setString(3, JsonParser.write(section.getValue(), AbstractSection.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteItem(Connection conn, Resume resume, String statement) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }
}
