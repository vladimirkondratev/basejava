package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.*;

public class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveStorageFull() throws Exception {
        for (int i = 3; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
            Resume resume = new Resume(String.valueOf(i), "fullName");
            try {
                storage.save(resume);
            } catch (Exception e) {
                Assert.fail();
            }
        }
        storage.save(new Resume("extra", "extra"));
    }
}