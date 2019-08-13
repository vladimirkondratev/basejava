package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        if (size > 0) {
            Arrays.fill(storage, 0, size - 1, null);
            size = 0;
        }
    }

    public void save(Resume resume){
        if (size < storage.length) {
            int index = getIndex(resume.getUuid());
            if (index < 0){
                insert(resume, index);
            } else {
                System.out.println("The resume is already present.");
            }
        }else {
            System.out.println("The storage is full.");
        }
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("The resume not found.");
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("The resume not found.");
        return null;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public void delete(String uuid){
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("The resume not found.");
        } else {
            remove(index);
        }
    }

    protected abstract void insert(Resume resume, int index);

    protected abstract void remove(int index);

    protected abstract int getIndex(String uuid);
}