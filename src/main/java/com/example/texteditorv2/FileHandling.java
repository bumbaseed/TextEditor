package com.example.texteditorv2;

import com.example.texteditorv2.Piece;
import com.example.texteditorv2.RedBlackTree;
import com.example.texteditorv2.RBTreeNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileHandling {
    // read from a file
    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }

    // write to a file
    public static void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.writeString(path, content);
    }
}
