package com.mralexmay.projects.download_manager.server.commons.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

public class BFileWorker implements FileWorker {
    private static String address = "hdfs://localhost:7000";


    @Override
    public void createDirectory(String dir) throws IOException {
        File file = new File(dir);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void writeOnDisk(File fullPath, byte[] content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fullPath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);) {
            bos.write(content);
            bos.flush();
        }
    }

    @Override
    public long writeOnDiskWithCheckSum(File fullPath, byte[] content) throws IOException {
        Checksum checkSum = new Adler32();

        try (FileOutputStream fos = new FileOutputStream(fullPath);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             CheckedOutputStream cos = new CheckedOutputStream(bos, checkSum);) {
            cos.write(content);
            cos.flush();

            return checkSum.getValue();
        }
    }

    @Override
    public long writeOnDiskWithCheckSum(File fullPath, Byte[] content) throws IOException {
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

    @Override
    public byte[] readFromDisk(File fullPath) throws IOException {
        List<Byte> bytes = new ArrayList<>();

        // Create FileReader and FileWriter
        try (InputStream is = new FileInputStream(fullPath.getCanonicalFile());
             BufferedInputStream bis = new BufferedInputStream(is);) {
            byte[] buffer = new byte[1024 * 4];

            // Read all bytes from file
            for (int read = 0; (read = bis.read(buffer, 0, buffer.length)) != -1; ) {
                // Read bytes and add
                bytes.addAll(Arrays.asList(toObjects(buffer)));
            }
        }

        // Return file content
        return toPrimitives(bytes.toArray(new Byte[0]));
    }

    @Override
    public void appendToFile(File fullPath, byte[] content) throws IOException {
    }

    private Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }

    private byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }
}
