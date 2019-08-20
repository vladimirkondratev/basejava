package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

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
       return storage.toArray(new Resume[0]);
    }

    @Override
    protected void doSave(Resume resume, int index) {
        storage.add(resume);
    }

    @Override
    public void doUpdate(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    protected void doDelete(int index) {
        storage.remove(index);
    }

    @Override
    protected Resume doGet(int index) {
        return storage.get(index);
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
