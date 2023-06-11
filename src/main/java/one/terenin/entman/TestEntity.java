package one.terenin.entman;

import one.terenin.entman.annotations.Column;
import one.terenin.entman.annotations.Entity;
import one.terenin.entman.annotations.GeneratedValue;
import one.terenin.entman.annotations.Id;
import one.terenin.entman.annotations.Strategies;
import one.terenin.entman.annotations.Table;

import java.util.UUID;

@Entity
@Table(tableName = "t_entity")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = Strategies.UUID)
    private UUID id;

    @Column(name = "test_column")
    private String test;

}
