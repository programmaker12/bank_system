package com.project.bank_system.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class HttpServletResponseCopier extends HttpServletResponseWrapper {
    private ByteArrayOutputStream copy = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public HttpServletResponseCopier(HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("Writer already obtained");
        if (outputStream == null) {
            outputStream = new ServletOutputStreamCopier(super.getOutputStream(), copy);
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) throw new IllegalStateException("OutputStream already obtained");
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(copy, getCharacterEncoding()), true);
        }
        return writer;
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }
}

