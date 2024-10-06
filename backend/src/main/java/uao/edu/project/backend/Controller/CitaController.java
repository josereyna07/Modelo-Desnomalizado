package uao.edu.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uao.edu.project.backend.Entidades.Cita;
import uao.edu.project.backend.Entidades.Medico;
import uao.edu.project.backend.Entidades.Paciente;
import uao.edu.project.backend.citaMedicaAPPRepository.CitaRepository;
import uao.edu.project.backend.citaMedicaAPPRepository.MedicoRepository;
import uao.edu.project.backend.citaMedicaAPPRepository.PacienteRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Cita")
public class CitaController {

    private final CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;  // Inyección del repositorio de Paciente

    @Autowired
    private MedicoRepository medicoRepository;      // Inyección del repositorio de Médico

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @GetMapping
    public List<Cita> getAllCitas() {
        return citaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable("id") String id) {
        Optional<Cita> cita = citaRepository.findById(id);
        return cita.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Cita> createCita(@RequestBody Cita cita) {
        try {
            Paciente paciente = pacienteRepository.findById(cita.getPaciente().getId()).orElse(null);
            Medico medico = medicoRepository.findById(cita.getMedico().getId()).orElse(null);
            
            if (paciente == null || medico == null) {
                return ResponseEntity.badRequest().body(null); // Manejar el error según sea necesario
            }
    
            // Establecer los objetos completos en la cita
            cita.setPaciente(paciente);
            cita.setMedico(medico);
    
            Cita nuevaCita = citaRepository.save(cita);
            return ResponseEntity.ok(nuevaCita);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> updateCita(@PathVariable String id, @RequestBody Cita cita) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cita.setId(id);
        Cita updatedCita = citaRepository.save(cita);
        return ResponseEntity.ok(updatedCita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable String id) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
