package com.mralexmay.projects.download_manager.server.commons.util;

import java.io.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

public class FileUtil {


    public static long saveContentToFile(File fullPath, byte[] content) throws IOException {
        Checksum checkSum = new Adler32();

        try (FileOutputStream fos = new FileOutputStream(fullPath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             CheckedOutputStream cos = new CheckedOutputStream(bos, checkSum);) {
            cos.write(content);
            cos.flush();

            return checkSum.getValue();
        }
    }

    public static long saveContentToFile(File fullPath, Byte[] content) throws IOException {
        Checksum checkSum = new Adler32();

        try (FileOutputStream fos = new FileOutputStream(fullPath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             CheckedOutputStream cos = new CheckedOutputStream(bos, checkSum);) {
            for (int i = 0; i < content.length; i++) {
                cos.write(content[i]);
            }

            bos.flush();

            return checkSum.getValue();
        }
    }

}
