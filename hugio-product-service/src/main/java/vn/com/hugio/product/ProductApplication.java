package vn.com.hugio.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import vn.com.hugio.common.constant.ConsoleColors;
import vn.com.hugio.product.service.ProductService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(value = {"vn.com"})
public class ProductApplication implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    public static void main(String[] args) {
        //Model model = MavenUtil.getProjectInfo();
        System.setProperty("LOG_FILE_NAME", "product_application");
        System.setProperty("APPLICATION_NAME", "product_application");

        //MDC.put("appName", model.getArtifactId());
        SpringApplication.run(ProductApplication.class, args);
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

        productService.getProductDetail("e2f74144-597a-44e3-8aa3-938fcec63d20");

        // Getting character encoding by InputStreamReader
        // System.out.println("Default Charset by InputStreamReader: " + getCharacterEncoding());

        // Getting character encoding by java.nio.charset
        // System.out.println("Default Charset: " + Charset.defaultCharset());
    }

}
