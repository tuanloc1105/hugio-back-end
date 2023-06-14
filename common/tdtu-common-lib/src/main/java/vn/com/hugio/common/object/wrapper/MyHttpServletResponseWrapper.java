package vn.com.hugio.common.object.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ResettableServletOutputStream servletOutput;

    public MyHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        byteArrayOutputStream = new ByteArrayOutputStream();
        servletOutput = new ResettableServletOutputStream();
        servletOutput.output = byteArrayOutputStream;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return servletOutput;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(byteArrayOutputStream);
    }

    public byte[] getAllByteInResponse() {
        return byteArrayOutputStream.toByteArray();
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public ResettableServletOutputStream getServletOutput() {
        return servletOutput;
    }

    public void setServletOutput(ResettableServletOutputStream servletOutput) {
        this.servletOutput = servletOutput;
    }

    private class ResettableServletOutputStream extends ServletOutputStream {
        private OutputStream output;

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

        @Override
        public void write(int b) throws IOException {
            output.write(b);
        }

    }

}
