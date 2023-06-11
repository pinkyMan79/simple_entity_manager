package one.terenin.entman.reflections;

import lombok.Generated;
import lombok.SneakyThrows;
import one.terenin.entman.annotations.Column;
import one.terenin.entman.annotations.Entity;
import one.terenin.entman.annotations.GeneratedValue;
import one.terenin.entman.annotations.Id;
import one.terenin.entman.annotations.Strategies;
import one.terenin.entman.annotations.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityResolver {

    private final EntityScanner scanner;

    public EntityResolver(String basePackage) {
        this.scanner = new EntityScanner(basePackage);
    }

    public List<Class<?>> getEntitiesWithAnnotation(){
        List<? extends Class<?>> allEntities = scanner.scan();
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> entity : allEntities) {
            if (entity.isAnnotationPresent(Entity.class)){
                result.add(entity);
            }
        }
        return result;
    }

    @SneakyThrows
    public void generateEntities() {
        List<Class<?>> ableToGenerateEntities = getEntitiesWithAnnotation();
        for (Class<?> ableToGenerateEntity : ableToGenerateEntities) {
            if (ableToGenerateEntity.isAnnotationPresent(Table.class)) {
                Table tab = ableToGenerateEntity.getAnnotation(Table.class);
                String tableName = tab.tableName();
                String generate = tab.generateTable();
                if (generate != null && generate.equals("y")) {
                    Field[] fields = ableToGenerateEntity.getDeclaredFields();
                    boolean isIdPresent = false;
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].isAnnotationPresent(Id.class)) {
                            isIdPresent = true;
                            if (fields[i].isAnnotationPresent(GeneratedValue.class)) {
                                Strategies strategy = fields[i].getAnnotation(GeneratedValue.class).strategy();
                                fields[i].setAccessible(true);
                                switch (strategy) {
                                    case UUID -> fields[i].set(UUID.class, UUID.randomUUID());
                                    case INT_8 -> fields[i].setInt(Integer.class, (int)(Math.random()*1000));
                                    case INT_16 -> fields[i].setInt(Integer.class, (int)(Math.random()*10000));
                                    case INT_32 -> fields[i].setInt(Integer.class, (int)(Math.random()*100000));
                                }
                            }
                            // SQL code here
                            continue;
                        }
                        if (isIdPresent) {
                            if (fields[i].isAnnotationPresent(Id.class)) {
                                throw new RuntimeException("cannot parse entity with 2 or more id fields");
                            }
                            if (fields[i].isAnnotationPresent(Column.class)) {
                                String columnName = fields[i].getAnnotation(Column.class).name();
                                // here is SQL code
                            }
                        } else {
                            throw new RuntimeException("cannot generate entity without id");
                        }
                    }
                }
            }
        }
    }

    public EntityScanner getScanner() {
        return scanner;
    }
}
