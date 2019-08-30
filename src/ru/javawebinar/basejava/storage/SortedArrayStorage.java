package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {
    /*
        private static class ResumeComparator implements Comparator<Resume> {
            @Override
            public int compare(Resume o1, Resume o2) {
                return o1.getUuid().compareTo(o2.getUuid());
            }
        }
    */
    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected void insert(Resume resume, int index) {
        int insertPosition = -1 - index;
        System.arraycopy(storage, insertPosition, storage, insertPosition + 1, size - insertPosition);
        storage[insertPosition] = resume;
    }

    @Override
    protected void remove(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "fullName");
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
    }
}
