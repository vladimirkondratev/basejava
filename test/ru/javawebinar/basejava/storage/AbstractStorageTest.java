package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final Resume resume1 = ResumeTestData.getResume(UUID_1, "fullName1");
    private static final Resume resume2 = ResumeTestData.getResume(UUID_2, "fullName2");
    private static final Resume resume3 = ResumeTestData.getResume(UUID_3, "fullName3");
    private static final Resume resume4 = ResumeTestData.getResume(UUID_4, "fullName4");

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
        Resume resume = ResumeTestData.getResume(UUID_1, "newName");
        resume.addContact(ContactType.SKYPE, "skype.NEWskype");
        storage.update(resume);
        Assert.assertEquals(resume, storage.get(UUID_1));
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> expected = new ArrayList<>();
        expected.add(resume1);
        expected.add(resume2);
        expected.add(resume3);
        List<Resume> actual = storage.getAllSorted();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void saveNotExist() throws Exception {
        storage.save(resume4);
        Assert.assertEquals(resume4, storage.get(UUID_4));
        Assert.assertEquals(4, storage.size());
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
        Assert.assertEquals(2, storage.size());
        storage.get(UUID_1);
    }

    @Test
    public void getExist() throws Exception {
        Assert.assertEquals(resume2, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }
}