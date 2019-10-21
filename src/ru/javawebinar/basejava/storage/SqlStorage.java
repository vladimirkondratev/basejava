package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        String statement = "SELECT * FROM resume r WHERE r.uuid =?";
        return sqlHelper.connectAndExecute(statement, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void update(Resume resume) {
        String statement = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        sqlHelper.connectAndExecute(statement, ps -> {
            ps.setString(1, resume.getFullName());
            ps.setString(2, resume.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        String statement = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
        sqlHelper.connectAndExecute(statement, ps -> {
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            ps.execute();
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
        String statement = "SELECT * FROM resume";
        return sqlHelper.connectAndExecute(statement, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (rs.next()) {
                resumes.add(new Resume(
                        rs.getString("uuid").trim(),
                        rs.getString("full_name").trim()
                ));
            }
            return resumes.stream().sorted().collect(Collectors.toList());
        });
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
}
