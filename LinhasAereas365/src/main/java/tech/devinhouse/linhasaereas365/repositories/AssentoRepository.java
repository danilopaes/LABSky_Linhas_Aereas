package tech.devinhouse.linhasaereas365.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.devinhouse.linhasaereas365.models.AssentoJpa;

@Repository
public interface AssentoRepository extends JpaRepository<AssentoJpa, String> {

}
