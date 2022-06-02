package rva.ctrl;

import java.math.BigDecimal;
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
import rva.model.StavkaPorudzbine;
import rva.repository.PorudzbinaRepository;
import rva.repository.StavkaPorudzbineRepository;

@CrossOrigin
@RestController
public class StavkaPorudzbineController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private StavkaPorudzbineRepository stavkaPorudzbineRepository;
	
	@Autowired
	private PorudzbinaRepository porudzbinaRepository;
	
	
	@GetMapping("stavkaPorudzbine")
	public Collection<StavkaPorudzbine> getAllStavke(){
		return stavkaPorudzbineRepository.findAll();
	}
	
	@GetMapping("stavkaPorudzbine/{id}")
	public StavkaPorudzbine getStavkaById(@PathVariable int id) {
		return stavkaPorudzbineRepository.getById(id);
	}
	
	@GetMapping("stavkaPorudzbine/forPorudzbina/{id}")
	public Collection<StavkaPorudzbine> getStavkeByPorudzbina(@PathVariable int id){
		Porudzbina temp = porudzbinaRepository.getById(id);
		return stavkaPorudzbineRepository.findByPorudzbina(temp);
	}
	
	@GetMapping("stavkaPorudzbine/cena/{cena}")
	public Collection<StavkaPorudzbine> getStavkeByCenaLessThan(@PathVariable BigDecimal cena){
		return stavkaPorudzbineRepository.findByCenaLessThanOrderById(cena);
	}
	
	@PostMapping("stavkaPorudzbine")
	public ResponseEntity<StavkaPorudzbine> createStavkaPorudzbine(@RequestBody StavkaPorudzbine stavka){
		if(stavkaPorudzbineRepository.existsById(stavka.getId())) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}else {
			stavka.setRedniBroj(stavkaPorudzbineRepository.nextRbr(stavka.getPorudzbina().getId()));
			StavkaPorudzbine stavkaCreated = stavkaPorudzbineRepository.save(stavka);
			return new ResponseEntity<StavkaPorudzbine>(stavkaCreated, HttpStatus.CREATED);
		}
	}
	
	@PutMapping("stavkaPorudzbine")
	public ResponseEntity<StavkaPorudzbine> updateStavkaPorudzbine(@RequestBody StavkaPorudzbine stavka){
		if(stavkaPorudzbineRepository.existsById(stavka.getId())) {
			stavkaPorudzbineRepository.save(stavka);
			return new ResponseEntity<StavkaPorudzbine>(HttpStatus.OK);
		}else {
			return new ResponseEntity<StavkaPorudzbine>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("stavkaPorudzbine/{id}")
	public ResponseEntity<StavkaPorudzbine> deleteStavkaPorudzbine(@PathVariable int id){
		if(stavkaPorudzbineRepository.existsById(id)) {
			if(id == -100) {
				stavkaPorudzbineRepository.deleteById(id);
				//pravimo ovu torku kako bi nam test brisanja uvek radio kada ga testiramo
				jdbcTemplate.execute("insert into stavka_porudzbine(id, \"cena\", \"jedinica_mere\", \"kolicina\" , \"redni_broj\", \"artikl\", \"porudzbina\")"
						+ "values(-100, 1000, 'kg', 10, " + stavkaPorudzbineRepository.nextRbr(22) + ", 1, 22)");
				return new ResponseEntity<StavkaPorudzbine>(HttpStatus.OK);
			}else {
				stavkaPorudzbineRepository.deleteById(id);
				return new ResponseEntity<StavkaPorudzbine>(HttpStatus.OK);
			}
		}else {
			return new ResponseEntity<StavkaPorudzbine>(HttpStatus.NOT_FOUND);
		}
	}
}
