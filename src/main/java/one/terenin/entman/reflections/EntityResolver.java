package one.terenin.entman.reflections;


import one.terenin.entman.annotations.Entity;
import one.terenin.entman.annotations.Table;

import java.util.ArrayList;
import java.util.List;

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
                if (entity.isAnnotationPresent(Table.class)){
                    String tableName = entity.getAnnotation(Table.class).tableName();

                }
            }
        }
    }

    public EntityScanner getScanner() {
        return scanner;
    }
}
