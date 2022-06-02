package rva.ctrl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import rva.model.Dobavljac;
import rva.repository.DobavljacRepository;

@CrossOrigin
@RestController
public class DobavljacController {

	@Autowired
	private DobavljacRepository dobavljacRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/dobavljac")
	public Collection<Dobavljac> getAllDobavljac() {
		return dobavljacRepository.findAll();
	}

	@GetMapping("/dobavljac/{id}")
	public Dobavljac getDobavljacById(@PathVariable int id) {
		return dobavljacRepository.getById(id);
	}

	@GetMapping("/dobavljac/naziv/{naziv}")
	public Collection<Dobavljac> getDobavljacByNaziv(@PathVariable String naziv) {
		return dobavljacRepository.findByNazivContainingIgnoreCase(naziv);
	}

	@PostMapping("/dobavljac")
	public ResponseEntity<Dobavljac> createDobavljac(@RequestBody Dobavljac dobavljac) {
		if (dobavljacRepository.existsById(dobavljac.getId())) {
			return new ResponseEntity<Dobavljac>(HttpStatus.CONFLICT);
		} else {
			dobavljacRepository.save(dobavljac);
			return new ResponseEntity<Dobavljac>(HttpStatus.CREATED);
		}
	}

	@PutMapping("/dobavljac")
	public ResponseEntity<Dobavljac> updateDobavljac(@RequestBody Dobavljac dobavljac) {
		if (dobavljacRepository.existsById(dobavljac.getId())) {
			Dobavljac updated = dobavljacRepository.save(dobavljac);
			return new ResponseEntity<Dobavljac>(updated, HttpStatus.OK);
		} else {
			return new ResponseEntity<Dobavljac>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/dobavljac/{id}")
	public ResponseEntity<Dobavljac> deleteDobavljac(@PathVariable int id) {
		if (dobavljacRepository.existsById(id)) {
			if(id == -100) {
				dobavljacRepository.deleteById(id);
				jdbcTemplate.execute("INSERT INTO DOBAVLJAC(id, \"adresa\", \"kontakt\", \"naziv\")"
						+ " VALUES(-100, 'Test adresa', 'Test kontakt', 'Test naziv')");
				return new ResponseEntity<Dobavljac>(HttpStatus.OK);
			}else {
				jdbcTemplate.execute("delete from Porudzbina where dobavljac = " + id);
				dobavljacRepository.deleteById(id);
				return new ResponseEntity<Dobavljac>(HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<Dobavljac>(HttpStatus.NOT_FOUND);
		}

	}

}
