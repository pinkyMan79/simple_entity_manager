package one.terenin.entman.reflections;

import lombok.Generated;
import lombok.SneakyThrows;
import one.terenin.entman.annotations.Column;
import one.terenin.entman.annotations.Entity;
import one.terenin.entman.annotations.GeneratedValue;
import one.terenin.entman.annotations.Id;
import one.terenin.entman.annotations.Strategies;
import one.terenin.entman.annotations.Table;
import one.terenin.entman.reflections.ddl.SimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

    public DataSource getDataSource(){
        Properties properties = Test.getProperties();
        return new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
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
                    // language=SQL
                    String query = "CREATE TABLE " + tableName + " (";
                    Field[] fields = ableToGenerateEntity.getDeclaredFields();
                    boolean isIdPresent = false;
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].isAnnotationPresent(Id.class)) {
                            isIdPresent = true;
                            if (fields[i].isAnnotationPresent(GeneratedValue.class)) {
                                Strategies strategy = fields[i].getAnnotation(GeneratedValue.class).strategy();
                                fields[i].setAccessible(true);
                                switch (strategy) {
                                    case UUID -> {
                                        fields[i].set(UUID.class, UUID.randomUUID());
                                        query += fields[i].getName() + " uuid primary key";
                                    }
                                    case INT_8 -> fields[i].setInt(Integer.class, (int)(Math.random()*1000));
                                    case INT_16 -> fields[i].setInt(Integer.class, (int)(Math.random()*10000));
                                    case INT_32 -> fields[i].setInt(Integer.class, (int)(Math.random()*100000));
                                }
                            }
                            if (fields.length == 1){
                                query += " );";
                            }else {
                                query += ",";
                            }
                            continue;
                        }
                        if (isIdPresent) {
                            if (fields[i].isAnnotationPresent(Id.class)) {
                                throw new RuntimeException("cannot parse entity with 2 or more id fields");
                            }
                            if (fields[i].isAnnotationPresent(Column.class)) {
                                String columnName = fields[i].getAnnotation(Column.class).name();
                                Class<?> type = fields[i].getType();
                                if (type.equals(String.class)){
                                    query += columnName + " varchar(255) not null,";
                                } else if (type.equals(Integer.class)) {
                                    query += columnName + " int4 not null,";
                                } else if (type.equals(Long.class)) {
                                    query += columnName + " int8 not null,";
                                }
                                // here is SQL code
                            }
                        } else {
                            throw new RuntimeException("cannot generate entity without id");
                        }
                    }
                    try (Connection connection = getDataSource().getConnection();
                         Statement statement = connection.prepareStatement(query)){
                        ResultSet resultSet = statement.executeQuery(query);
                    }
                }
            }
        }
    }

    public EntityScanner getScanner() {
        return scanner;
    }
}
