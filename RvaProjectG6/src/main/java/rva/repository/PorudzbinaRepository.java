package rva.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Porudzbina;

public interface PorudzbinaRepository extends JpaRepository<Porudzbina, Integer>{

	Collection<Porudzbina> findByPlacenoTrue();
}
