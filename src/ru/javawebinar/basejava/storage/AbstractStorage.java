package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume resume) {
        int index = getIndexExistException(resume.getUuid());
        doSave(resume, index);
    }

    public void update(Resume resume) {
        int index = getIndexNotExistException(resume.getUuid());
        doUpdate(resume, index);
    }

    public Resume get(String uuid) {
        int index = getIndexNotExistException(uuid);
        return doGet(index);
    }

    public void delete(String uuid) {
        int index = getIndexNotExistException(uuid);
        doDelete(index);
    }

    private int getIndexNotExistException(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private int getIndexExistException(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    protected abstract void doSave(Resume resume, int index);

    protected abstract void doUpdate(Resume resume, int index);

    protected abstract void doDelete(int index);

    protected abstract Resume doGet(int index);

    protected abstract int getIndex(String uuid);
}
