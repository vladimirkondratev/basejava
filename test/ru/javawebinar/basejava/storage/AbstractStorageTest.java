package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public abstract class AbstractStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final String FULL_NAME_1 = "FULL_NAME_1";
    private static final String FULL_NAME_2 = "FULL_NAME_2";
    private static final String FULL_NAME_3 = "FULL_NAME_3";
    private static final String FULL_NAME_4 = "FULL_NAME_4";

    private static final Resume resume1;
    private static final Resume resume2;
    private static final Resume resume3;
    private static final Resume resume4;

    static {
        resume1 = new Resume(UUID_1, FULL_NAME_1);
        resume2 = new Resume(UUID_2, FULL_NAME_2);
        resume3 = new Resume(UUID_3, FULL_NAME_3);
        resume4 = new Resume(UUID_4, FULL_NAME_4);
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
        Assert.assertEquals(0, storage.getAllSorted().size());
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(resume4);
    }

    @Test
    public void updateExist() throws Exception {
        storage.update(resume1);
        Assert.assertEquals(resume1, storage.get(UUID_1));
    }

    @Test
    public void getAllSorted() throws Exception {
        Resume[] expected = {resume1, resume2, resume3};
        List<Resume> actual = storage.getAllSorted();
        Assert.assertArrayEquals(expected, actual.toArray());
    }

    @Test
    public void saveNotExist() throws Exception {
        storage.save(resume4);
        Assert.assertEquals(resume4, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(resume1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("not_exist");
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExist() throws Exception {
        storage.delete(UUID_1);
        storage.get(UUID_1);
    }

    @Test
    public void getExist() throws Exception {
        Assert.assertEquals(resume1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}