package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size < storage.length) {
            int index = resumeIndexInStorage(r.getUuid());
            if (index != -1) {
                System.out.println("The resume is already present.");
            } else {
                storage[size++] = r;
            }
        } else {
            System.out.println("The storage is full.");
        }
    }

    public void update(Resume resume) {
        int index = resumeIndexInStorage(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("The resume not found.");
        }
    }

    public Resume get(String uuid) {
        int index = resumeIndexInStorage(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("The resume not found.");
            return null;
        }
    }

    public void delete(String uuid) {
        int index = resumeIndexInStorage(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("The resume not found.");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int resumeIndexInStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
