package com.autocopy.util;

import com.autocopy.exception.FolderNotFoundException;

import java.io.File;

public class Utility {
    public static void checkDirectory(String directory) throws FolderNotFoundException {
        File file = new File(directory);
        if (!file.isDirectory()) {
            throw new FolderNotFoundException(directory + "는 올바른 디렉토리 경로가 아닙니다");
        }
    }
}
