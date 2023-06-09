package main.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CaseRepository extends CrudRepository<CriminalCase, Integer> {

    Iterable<CriminalCase> findByDescription(String description);
}