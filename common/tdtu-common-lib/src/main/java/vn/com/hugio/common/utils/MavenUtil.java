package vn.com.hugio.common.utils;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MavenUtil {

    public static Model getProjectInfo() {
        try {
            String userDirectory = System.getProperty("user.dir");
            MavenXpp3Reader reader = new MavenXpp3Reader();
            File initialFile = new File(userDirectory + File.separator + "pom.xml");
            InputStream inputStream = new FileInputStream(initialFile);
            Model model = reader.read(new InputStreamReader(inputStream));
            inputStream.close();
            return model;
        } catch (Exception e) {
            return new Model();
        }
    }


}
