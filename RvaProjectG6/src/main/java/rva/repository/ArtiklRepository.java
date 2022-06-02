package rva.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import rva.model.Artikl;

public interface ArtiklRepository extends JpaRepository<Artikl,Integer> {

	Collection<Artikl> findByNazivContainingIgnoreCase(String naziv);
}
