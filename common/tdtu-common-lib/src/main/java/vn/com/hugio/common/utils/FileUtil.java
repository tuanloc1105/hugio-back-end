package vn.com.hugio.common.utils;

import vn.com.hugio.common.log.LOG;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileUtil {

    private static byte[] getFileFromLocation(String location) {
        try {
            ClassLoader classLoader = FileUtil.class.getClassLoader();
            URL url = classLoader.getResource(location);
            if (Optional.ofNullable(url).isEmpty()) {
                return null;
            }
            if(url.getPath() != null) {
                File file = new File(url.getPath());
                Path path = file.toPath();
                return Files.readAllBytes(path);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return null;
        }
        return null;
    }


}
