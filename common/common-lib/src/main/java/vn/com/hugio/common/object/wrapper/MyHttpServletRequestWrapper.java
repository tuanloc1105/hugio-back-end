package vn.com.hugio.common.object.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayOutputStream byteArrayOutputStream;
    private HttpServletRequest request;
    private ResettableServletInputStream servletInput;

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.byteArrayOutputStream = new ByteArrayOutputStream(1024);
        this.servletInput = new ResettableServletInputStream();

        DataInputStream in;
        try {
            in = new DataInputStream(request.getInputStream());
            byte[] buffer = new byte[4096];
            int nRead = -1;

            while ((nRead = in.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, nRead);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        servletInput.stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return servletInput;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (byteArrayOutputStream.size() == 0) {
            DataInputStream in = new DataInputStream(request.getInputStream());
            byte[] buffer = new byte[4096];
            int nRead = -1;

            while ((nRead = in.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, nRead);
            }
        }
        servletInput.stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return new BufferedReader(new InputStreamReader(servletInput));
    }

    public byte[] getAllByteInRequest() {
        return byteArrayOutputStream.toByteArray();
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public ResettableServletInputStream getServletInput() {
        return servletInput;
    }

    public void setServletInput(ResettableServletInputStream servletInput) {
        this.servletInput = servletInput;
    }

    private static class ResettableServletInputStream extends ServletInputStream {

        private InputStream stream;

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }
    }
}
