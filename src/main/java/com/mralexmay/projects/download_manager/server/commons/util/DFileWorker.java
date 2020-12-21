package com.mralexmay.projects.download_manager.server.commons.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;

public class DFileWorker implements FileWorker {
    private static String address = "hdfs://localhost:7000";


    @Override
    public void createDirectory(String dir) throws IOException {
        Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.defaultFS", address);

        org.apache.hadoop.fs.FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new org.apache.hadoop.fs.Path(dir);
        fileSystem.mkdirs(path);
    }

    @Override
    public void writeOnDisk(File fullPath, byte[] content) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", address);
        FileSystem fileSystem = FileSystem.get(configuration);

        //Create a path
        Path hdfsWritePath = new Path(fullPath.getPath());

        try (FSDataOutputStream fsdos = fileSystem.create(hdfsWritePath, true);
             BufferedOutputStream bos = new BufferedOutputStream(fsdos);) {
            bos.write(content);
            bos.flush();
        }
    }

    @Override
    public void appendToFile(File fullPath, byte[] content) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", address);
        FileSystem fileSystem = FileSystem.get(configuration);

        //Create a path
        Path hdfsWritePath = new Path(fullPath.getPath());
        try (FSDataOutputStream fsdos = fileSystem.append(hdfsWritePath);
             BufferedOutputStream bos = new BufferedOutputStream(fsdos);) {
            bos.write(content);
            bos.flush();
        }
    }

    @Override
    public long writeOnDiskWithCheckSum(File fullPath, byte[] content) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", address);
        FileSystem fileSystem = FileSystem.get(configuration);
        Checksum checkSum = new Adler32();

        //Create a path
        Path hdfsWritePath = new Path(fullPath.getPath());
        try (FSDataOutputStream fsdos = fileSystem.append(hdfsWritePath);
             BufferedOutputStream bos = new BufferedOutputStream(fsdos);
             CheckedOutputStream cos = new CheckedOutputStream(bos, checkSum);) {
            cos.write(content);
            cos.flush();

            return checkSum.getValue();
        }
    }

    @Override
    public long writeOnDiskWithCheckSum(File fullPath, Byte[] content) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", address);
        FileSystem fileSystem = FileSystem.get(configuration);
        Checksum checkSum = new Adler32();

        //Create a path
        Path hdfsWritePath = new Path(fullPath.getPath());
        try (FSDataOutputStream fsdos = fileSystem.append(hdfsWritePath);
             BufferedOutputStream bos = new BufferedOutputStream(fsdos);
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
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", address);
        FileSystem fileSystem = FileSystem.get(configuration);

        //Create a path
        Path hdfsWritePath = new Path(fullPath.getPath());
        try (FSDataInputStream fsdos = fileSystem.open(hdfsWritePath);
             BufferedInputStream bos = new BufferedInputStream(fsdos);) {
            return bos.readAllBytes();
        }
    }
}
