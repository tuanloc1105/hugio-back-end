package vn.com.hugio.order;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import vn.com.hugio.common.constant.ConsoleColors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(value = {"vn.com"})
public class OrderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        //Model model = MavenUtil.getProjectInfo();
        System.setProperty("LOG_FILE_NAME", "order_application");
        System.setProperty("APPLICATION_NAME", "order_application");
        //MDC.put("appName", model.getArtifactId());
        SpringApplication.run(OrderApplication.class, args);
    }

    public static String getCharacterEncoding() {

        // Creating an array of byte type chars and
        // passing random  alphabet as an argument.abstract
        // Say alphabet be 'w'
        byte[] byte_array = {'w'};

        // Creating an object of InputStream
        InputStream instream = new ByteArrayInputStream(byte_array);

        // Now, opening new file input stream reader
        InputStreamReader streamreader = new InputStreamReader(instream);
        String defaultCharset = streamreader.getEncoding();

        // Returning default character encoding
        return defaultCharset;
    }

    @Override
    public void run(String... args) throws Exception {
        String defaultEncoding = System.getProperty("file.encoding");

        System.out.printf(
                "%s: %s%n",
                ConsoleColors.printYellow("Default Charset"),
                ConsoleColors.printRed(defaultEncoding)
        );

        System.out.printf(
                "%s: %s%n",
                ConsoleColors.printYellow("Default Charset encoding by java.nio.charset"),
                ConsoleColors.printRed(Charset.defaultCharset().name())
        );

        System.out.printf(
                "%s: %s%n",
                ConsoleColors.printYellow("Default Charset by InputStreamReader"),
                ConsoleColors.printRed(getCharacterEncoding())
        );

        // Getting character encoding by InputStreamReader
        // System.out.println("Default Charset by InputStreamReader: " + getCharacterEncoding());

        // Getting character encoding by java.nio.charset
        // System.out.println("Default Charset: " + Charset.defaultCharset());
    }

}
