package com.project.bank_system.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ServletOutputStreamCopier extends ServletOutputStream {
    private final ServletOutputStream outputStream;
    private final ByteArrayOutputStream copy;

    public ServletOutputStreamCopier(ServletOutputStream outputStream, ByteArrayOutputStream copy) {
        this.outputStream = outputStream;
        this.copy = copy;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
        copy.write(b);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
        copy.flush();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {}
}
