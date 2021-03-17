package com.duplicatefile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateFileSearchTest {
    @TempDir
    File tempDirectoryA;
    private DuplicateFileSearch duplicateFileSearch;

    @BeforeEach
    void setUp() {
        duplicateFileSearch = new DuplicateFileSearch();
    }

    @Test
    @DisplayName("Test program with data set provided by Cogent - \"Code_Test.zip\"")
    void duplicateFileSearchWithProvidedDataSet() throws IOException {
        String path = "./src/main/resources/Code Test";

        List<DuplicateFile> duplicateFiles = duplicateFileSearch.getDuplicateFiles(path);

        String outputMessage = duplicateFiles.toString();
        int expectedCountOfDuplicateRecords = 13;
        assertAll (
                () -> assertEquals(expectedCountOfDuplicateRecords, duplicateFiles.size()),
                () -> assertTrue(outputMessage.contains("incredible.jpg")),
                () -> assertTrue(outputMessage.contains("low tide.jpg")),
                () -> assertTrue(outputMessage.contains("sunset.jpg")),
                () -> assertTrue(outputMessage.contains("castle from drone.jpg")),
                () -> assertTrue(outputMessage.contains("quite a view.jpg")),
                () -> assertTrue(outputMessage.contains("mew.jpg")),
                () -> assertTrue(outputMessage.contains("laptop does run pretty hot.jpg")),
                () -> assertTrue(outputMessage.contains("s-01324.jpg")),
                () -> assertTrue(outputMessage.contains("matin.jpg")),
                () -> assertTrue(outputMessage.contains("tree.jpg")),
                () -> assertTrue(outputMessage.contains("coves.jpg")),
                () -> assertTrue(outputMessage.contains("that starfish again.jpg")),
                () -> assertTrue(outputMessage.contains("ilusion.jpg"))
        );
    }

    @Test
    @DisplayName("Test program with temporary files which will only exist for this test case")
    void duplicateFileSearchWithTempDataSet() throws IOException {
        // Create temp directory and files - all of it will be deleted after test is executed
        assertTrue(Files.isDirectory(this.tempDirectoryA.toPath()), "Temp Directory was not created");

        File photo1 = new File(tempDirectoryA, "photo1.jpg");
        byte[] data1 = {12, 22, 10};

        File photo2 = new File(tempDirectoryA, "photo2.jpg");
        byte[] data2 = {12, 22, 10, 5, 22};

        File photo3 = new File(tempDirectoryA, "photo3.jpg");
        byte[] data3 = {12, 22, 10};

        Files.write(photo1.toPath(), data1);
        Files.write(photo2.toPath(), data2);
        Files.write(photo3.toPath(), data3);

        String path = tempDirectoryA.getPath();

        List<DuplicateFile> duplicateFiles = duplicateFileSearch.getDuplicateFiles(path);
        String outputMessage = duplicateFiles.toString();
        int expectedCountOfDuplicateRecords = 1;
        String expectedDupFile = "photo1.jpg";
        String expectedDupFileWith = "photo3.jpg";
        String notDupFile = "photo2.jpg";
        assertAll(
                () -> assertEquals(expectedCountOfDuplicateRecords, duplicateFiles.size()),
                () -> assertTrue(outputMessage.contains(expectedDupFile)),
                () -> assertTrue(outputMessage.contains(expectedDupFileWith)),
                () -> assertFalse(outputMessage.contains(notDupFile))
        );
    }

    @Test
    void pathProvidedDuringApplicationStart() {
        String[] args = new String[1];
        args[0] = "./User/Photos";
        String expectedPath = "./User/Photos";
        assertEquals(expectedPath, duplicateFileSearch.setPathToResourcesIfNotProvided(args));
    }

    @Test
    void pathNotProvidedDuringApplicationStart() {
        String[] args = new String[0];
        String expectedPath = "./src/main/resources/Code Test";
        assertEquals(expectedPath, duplicateFileSearch.setPathToResourcesIfNotProvided(args));
    }

    @Test
    void pathWithNoDuplicateFiles() throws IOException {
        String path = "./src/test";
        List<DuplicateFile> duplicateFiles = duplicateFileSearch.getDuplicateFiles(path);
        assertTrue(duplicateFiles.isEmpty());
    }

    @Test
    void pathAsNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> duplicateFileSearch.getDuplicateFiles(null));
        assertEquals("Please specify location where program should start searching for duplicate files", exception.getMessage());
    }

    @Test
    void pathInvalid() {
        String path = "InvalidPath";
        assertThrows(IOException.class, () -> duplicateFileSearch.getDuplicateFiles(path));
    }

    @Test
    void outputResultWithDuplicateFiles() {
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        List<Path> paths = new ArrayList<>();
        paths.add(Path.of("./resource/TestData/photo1.jpg"));
        paths.add(Path.of("./resource/TestData/summer/photo2.jpg"));
        DuplicateFile duplicateFile = new DuplicateFile(paths);

        List<DuplicateFile> duplicateFiles = new ArrayList<>();
        duplicateFiles.add(duplicateFile);

        System.setOut(new PrintStream(outputStreamCaptor));
        duplicateFileSearch.outputResult(duplicateFiles);
        String expectedOutput = "File [ photo1.jpg ] has 1 duplicate: [.\\resource\\TestData\\summer\\photo2.jpg]";
        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
    }

    @Test
    void outputResultWithoutDuplicateFiles() {
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        List<DuplicateFile> duplicateFiles = new ArrayList<>();
        System.setOut(new PrintStream(outputStreamCaptor));
        duplicateFileSearch.outputResult(duplicateFiles);
        String expectedOutput = "No duplicate files in specified location";
        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
    }
}
