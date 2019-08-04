import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size;

    void clear() {
        size = 0;
    }

    void save(Resume r) {
        storage[size++] = r;
    }

    Resume get(String uuid) {
        int i = 0;
        while (i < size && uuid.compareTo(storage[i].uuid) != 0) {
            i++;
        }
        if (i < size) {
            return storage[i];
        } else {
            return null;
        }
    }

    void delete(String uuid) {
        int i = 0;
        while (i < size && uuid.compareTo(storage[i].uuid) != 0) {
            i++;
        }
        if (i < size) {
            while (i < size - 1) {
                storage[i] = storage[i + 1];
                i++;
            }
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    int size() {
        return size;
    }
}
