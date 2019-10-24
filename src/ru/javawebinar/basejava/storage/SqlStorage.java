package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Resume get(String uuid) {
        String statement = ("" +
                "    SELECT * FROM resume r " +
                " LEFT JOIN contact c " +
                "        ON r.uuid = c.resume_uuid " +
                "     WHERE r.uuid =? ");
        return sqlHelper.connectAndExecute(statement, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                addContactToResume(rs, resume);
            } while (rs.next());
            return resume;
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
            try (PreparedStatement ps = conn.prepareStatement("UPDATE contact SET value = ? WHERE type = ? AND resume_uuid = ?")) {
                addContactsToBD(ps, resume);
            }
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
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid) VALUES (?,?,?)")) {
                addContactsToBD(ps, resume);
            }
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
    public List<Resume> getAllSorted() {
        Map<String, Resume> resumes = new HashMap<>();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume resume = new Resume(rs.getString("uuid").trim(),
                            rs.getString("full_name"));
                    resumes.put(resume.getUuid(), resume);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContactToResume(rs, resumes.get(rs.getString("resume_uuid").trim()));
                }
            }
            return null;
        });
        return new ArrayList<>(resumes.values())
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public int size() {
        String statement = "SELECT COUNT(*) FROM resume";
        return sqlHelper.connectAndExecute(statement, ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void addContactToResume(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        resume.addContact(type, value);
    }

    private void addContactsToBD(PreparedStatement ps, Resume resume) throws SQLException {
        for (Map.Entry<ContactType, String> contact : resume.getContacts().entrySet()) {
            ps.setString(1, contact.getValue());
            ps.setString(2, contact.getKey().name());
            ps.setString(3, resume.getUuid());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}
