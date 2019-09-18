package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serializer.JsonStreamIOStrategyImpl;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamIOStrategyImpl()));
    }
}