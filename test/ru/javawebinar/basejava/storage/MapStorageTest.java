package ru.javawebinar.basejava.storage;

import org.junit.Ignore;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;

public class MapStorageTest extends AbstractStorageTest {

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Ignore
    @Test(expected = StorageException.class)
    public void saveStorageFull() throws Exception {}
}