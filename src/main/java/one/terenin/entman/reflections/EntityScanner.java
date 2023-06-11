package one.terenin.entman.reflections;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class EntityScanner {

    private final String basePackage;

    public EntityScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<? extends Class<?>> scan(){
        InputStream stream = ClassLoader
                .getSystemClassLoader()
                .getResourceAsStream(basePackage.replaceAll("[.]", "/"));
        assert stream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        return reader.lines()
                .filter(l -> l.endsWith(".class") || l.endsWith(".java"))
                .map(li -> getClassByLine(li))
                .toList();
    }

    private Class<?> getClassByLine(String classname){
        try {
            return Class.forName(basePackage + "." + classname
                    .substring(0, classname.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBasePackage() {
        return basePackage;
    }
}
