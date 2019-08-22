package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private Map storage = new HashMap<String, Resume>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        Collection<Resume> resumes = storage.values();
        return resumes.toArray(new Resume[0]);
    }

    @Override
    protected void doSave(Resume resume, int index) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void doUpdate(Resume resume, int index) {
        storage.put(resume.getUuid(), resume);;
    }

    @Override
    protected void doDelete(String uuid, int index) {
        storage.remove(uuid);
    }

    @Override
    protected Resume doGet(String uuid, int index) {
        return (Resume) storage.get(uuid);
    }

    @Override
    protected int getIndex(String uuid) {
        if (storage.get(uuid) != null){
            return 1;
        }
        return -1;
    }
}
