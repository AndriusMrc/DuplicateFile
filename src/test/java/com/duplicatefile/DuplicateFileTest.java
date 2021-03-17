package com.duplicatefile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateFileTest {
    private DuplicateFile duplicateFile;

    @BeforeEach
    void setUp() {
        List<Path> paths = new ArrayList<>();
        paths.add(Path.of("./resource/TestData/photo1.jpg"));
        paths.add(Path.of("./resource/TestData/summer/photo2.jpg"));
        duplicateFile = new DuplicateFile(paths);
    }

    @Test
    void getDuplicateFiles() {
        List<Path> expectedPaths = new ArrayList<>();
        expectedPaths.add(Path.of("./resource/TestData/photo1.jpg"));
        expectedPaths.add(Path.of("./resource/TestData/summer/photo2.jpg"));
        assertAll (
                () -> assertEquals(expectedPaths.get(0), duplicateFile.getDuplicateFiles().get(0)),
                () -> assertEquals(expectedPaths.get(1), duplicateFile.getDuplicateFiles().get(1))
        );
    }

    @Test
    void getCountOfDuplicateFiles() {
        int expectedCount = 2;
        assertEquals(expectedCount, duplicateFile.getCountOfDuplicateFiles());
    }

    @Test
    void testToString() {
        String expectedDupFile = "photo1.jpg";
        String expectedDupFileWith = "photo2.jpg";
        String result = duplicateFile.toString();
        assertAll (
                () -> assertTrue(result.contains(expectedDupFile)),
                () -> assertTrue(result.contains(expectedDupFileWith))
        );
    }
}