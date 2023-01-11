package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    public MemoryVacancyRepository() {
        save(new Vacancy(
                "Intern Java Developer", "Intern Java Developer",
                LocalDateTime.of(2021, 1, 1, 1, 12, 0),
                true, 1));
        save(new Vacancy(
                "Junior Java Developer", "Junior Java Developer",
                LocalDateTime.of(2021, 1, 2, 2, 11, 0),
                true, 2));
        save(new Vacancy(
                "Junior+ Java Developer", "Junior+ Java Developer",
                LocalDateTime.of(2021, 1, 3, 3, 23, 0),
                true, 3));
        save(new Vacancy(
                "Middle Java Developer", "Middle Java Developer",
                LocalDateTime.of(2021, 1, 4, 4, 1, 0),
                true, 1));
        save(new Vacancy(
                "Middle+ Java Developer", "Middle+ Java Developer",
                LocalDateTime.of(2021, 1, 5, 5, 44, 0),
                true, 2));
        save(new Vacancy(
                "Senior Java Developer", "Senior Java Developer",
                LocalDateTime.of(2021, 1, 6, 6, 52, 0),
                true, 3));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.getAndIncrement());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(
                vacancy.getId(), (id, oldVacancy) -> new Vacancy(
                        oldVacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        vacancy.getCreationDate(),
                        vacancy.getVisible(),
                        vacancy.getCityId())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}