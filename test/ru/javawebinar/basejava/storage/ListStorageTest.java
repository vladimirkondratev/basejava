package ru.javawebinar.basejava.storage;

import org.junit.Ignore;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Ignore
    @Test(expected = StorageException.class)
    public void saveStorageFull() throws Exception {}
}