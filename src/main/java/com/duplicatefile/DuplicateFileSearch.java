package com.duplicatefile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DuplicateFileSearch {

    public static void main(String[] args) {
        DuplicateFileSearch duplicateFileSearch = new DuplicateFileSearch();
        String path = duplicateFileSearch.setPathToResourcesIfNotProvided(args);
        try {
            List<DuplicateFile> duplicateFiles = duplicateFileSearch.getDuplicateFiles(path);
            duplicateFileSearch.outputResult(duplicateFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String setPathToResourcesIfNotProvided(String[] args) {
        // If location where to look for duplicates is not specified when application is started, use provided "Code Test" photos folder
        if (args.length == 0) {
            args = new String[1];
            args[0] = "./src/main/resources/Code Test";
        }
        return args[0];
    }

    public List<DuplicateFile> getDuplicateFiles(String filesFolderLocation) throws IOException {
        if (filesFolderLocation == null) {
            throw new RuntimeException("Please specify location where program should start searching for duplicate files");
        }
        Path path = Paths.get(filesFolderLocation).toAbsolutePath();
        Map<Integer, List<Path>> fileContentHashCodeAndPathMap = buildFileContentHashCodeAndPathMap(path);
        List<DuplicateFile> duplicateFiles = buildDuplicateFilesList(fileContentHashCodeAndPathMap);
        return duplicateFiles;
    }

    private Map<Integer, List<Path>> buildFileContentHashCodeAndPathMap(Path path) throws IOException {
        Map<Integer, List<Path>> fileContentHashCodeAndPathMap = new HashMap<>();
        try (Stream<Path> streamOfPaths = Files.walk(path)) {
            List<Path> paths = getPathsOfAllFiles(streamOfPaths);
            paths.forEach(p -> {
                try {
                    Integer fileContentHashCode = getFileContentHashCode(p);
                    if (!fileContentHashCodeAndPathMap.containsKey(fileContentHashCode)) {
                        fileContentHashCodeAndPathMap.put(fileContentHashCode, new ArrayList<>());
                    }
                    fileContentHashCodeAndPathMap.get(fileContentHashCode).add(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return fileContentHashCodeAndPathMap;
    }

    private List<Path> getPathsOfAllFiles(Stream<Path> streamOfPaths) {
        return streamOfPaths.filter(Files::isRegularFile).collect(Collectors.toList());
    }

    private int getFileContentHashCode(Path filePath) throws IOException {
        // Max file size can be 2147483647 bytes or ~2.147 GB
        return Arrays.hashCode(Files.readAllBytes(filePath));
    }

    private List<DuplicateFile> buildDuplicateFilesList(Map<Integer, List<Path>> fileContentHashCodeAndPathMap) {
        List<DuplicateFile> duplicateFileList = new ArrayList<>();
        fileContentHashCodeAndPathMap.forEach((key, value) -> {
            if (value.size() > 1) {
                duplicateFileList.add(new DuplicateFile(value));
            }
        });
        return duplicateFileList;
    }

    protected void outputResult(List<DuplicateFile> duplicateFiles) {
        if (duplicateFiles!= null && !duplicateFiles.isEmpty()) {
            duplicateFiles.forEach(System.out::println);
        } else {
            System.out.println("No duplicate files in specified location");
        }
    }
}