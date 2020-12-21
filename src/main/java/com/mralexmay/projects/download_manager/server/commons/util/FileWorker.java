package com.mralexmay.projects.download_manager.server.commons.util;

import java.io.File;
import java.io.IOException;

public interface FileWorker {
    void createDirectory(String dir) throws IOException;

    void writeOnDisk(File fullPath, byte[] content) throws IOException;

    byte[] readFromDisk(File fullPath) throws IOException;

    void appendToFile(File fullPath, byte[] content) throws IOException;

    long writeOnDiskWithCheckSum(File fullPath, byte[] content) throws IOException;

    long writeOnDiskWithCheckSum(File fullPath, Byte[] content) throws IOException;
}
