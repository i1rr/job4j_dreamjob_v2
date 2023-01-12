package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.io.IOException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oUserRepositoryTest {

    static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    static void initRepositories() throws IOException {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    void saveUserWithUniqueEmail() {
        User user = new User("Clark Simpson", "clark.simpson@gmail.com", "veryStrongPassword");
        var savedUser = sql2oUserRepository.save(user).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void findByEmailAndPassword() {
        User user = new User("Clark Simpson", "clark.simpson@gmail.com", "veryStrongPassword");
        sql2oUserRepository.save(user);

        assertThat(sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).get())
                .usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void saveUserWithNonUniqueEmail() {
        User user1 = new User("clark.simpson@gmail.com", "Clark Simpson", "veryStrongPassword");
        User user2 = new User("clark.simpson@gmail.com", "John Wayne", "notVeryStrongPassword");
        sql2oUserRepository.save(user1);

        assertThat(sql2oUserRepository.save(user2)).isEmpty();
    }
}