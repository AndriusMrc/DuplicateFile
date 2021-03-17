package com.duplicatefile;

import java.nio.file.Path;
import java.util.List;

public class DuplicateFile {
    private List<Path> duplicateFiles;
    private int countOfDuplicateFiles;

    public DuplicateFile(List<Path> paths) {
        this.duplicateFiles = paths;
        this.countOfDuplicateFiles = paths.size();
    }

    public List<Path> getDuplicateFiles() {
        return duplicateFiles;
    }

    public int getCountOfDuplicateFiles() {
        return countOfDuplicateFiles;
    }

    @Override
    public String toString() {
        String pluralOrSingular = (this.countOfDuplicateFiles - 1) > 1 ? " duplicates" : " duplicate";
        return "File [ " + duplicateFiles.get(0).getFileName() + " ] has " + (countOfDuplicateFiles - 1) + pluralOrSingular + ": " + duplicateFiles.subList(1, this.duplicateFiles.size());
    }
}