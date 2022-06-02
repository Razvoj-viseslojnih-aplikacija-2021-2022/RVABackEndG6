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
import rva.model.Porudzbina;
import rva.repository.PorudzbinaRepository;

@CrossOrigin
@RestController
public class PorudzbinaController {

	@Autowired
	private PorudzbinaRepository porudzbinaRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/porudzbina")
	public Collection<Porudzbina> getAllPorudzbina() {
		return porudzbinaRepository.findAll();
	}

	@GetMapping("/porudzbina/{id}")
	public Porudzbina getPorudzbinaById(@PathVariable int id) {
		return porudzbinaRepository.getById(id);
	}

	@GetMapping("/porudzbina/placeno")
	public Collection<Porudzbina> getPorudzbinaByPlaceno() {
		return porudzbinaRepository.findByPlacenoTrue();
	}

	@PostMapping("/porudzbina")
	public ResponseEntity<Porudzbina> createPorudzbina(@RequestBody Porudzbina porudzbina) {
		if (porudzbinaRepository.existsById(porudzbina.getId())) {
			return new ResponseEntity<Porudzbina>(HttpStatus.CONFLICT);
		} else {
			porudzbinaRepository.save(porudzbina);
			return new ResponseEntity<Porudzbina>(HttpStatus.CREATED);
		}
	}

	@PutMapping("/porudzbina")
	public ResponseEntity<Porudzbina> updatePorudzbina(@RequestBody Porudzbina porudzbina) {
		if (porudzbinaRepository.existsById(porudzbina.getId())) {
			Porudzbina updated = porudzbinaRepository.save(porudzbina);
			return new ResponseEntity<Porudzbina>(updated, HttpStatus.OK);
		} else {
			return new ResponseEntity<Porudzbina>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/porudzbina/{id}")
	public ResponseEntity<Porudzbina> deletePorudzbina(@PathVariable int id) {
		if (porudzbinaRepository.existsById(id)) {
			if (id == -100) {
				porudzbinaRepository.deleteById(id);
				jdbcTemplate.execute("INSERT INTO PORUDZBINA(id, \"datum\", \"isporuceno\", \"iznos\", \"placeno\", \"dobavljac\")"
						+ " VALUES(-100, to_date('10.04.2022' , 'dd.mm.yyyy'), to_date('12.04.2022' , 'dd.mm.yyyy'),"
						+ "15000 , true, 2)");
				return new ResponseEntity<Porudzbina>(HttpStatus.OK);
			} else {
				jdbcTemplate.execute("delete from stavka_porudzbine where porudzbina = " + id);
				porudzbinaRepository.deleteById(id);
				return new ResponseEntity<Porudzbina>(HttpStatus.OK);
			}

		} else {
			return new ResponseEntity<Porudzbina>(HttpStatus.NOT_FOUND);
		}

	}

}
