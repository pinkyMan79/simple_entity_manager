package one.terenin.entman.reflections;

import java.io.IOException;
import java.util.Properties;

public class Test {

    public static void main(String[] args) {
        EntityScanner scanner = new EntityScanner("");
        scanner.scan();
    }


    public static Properties getProperties(){

        Properties properties = new Properties();
        try {
            properties.load(Test.class.getResourceAsStream("/db.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return properties;

    }

}
