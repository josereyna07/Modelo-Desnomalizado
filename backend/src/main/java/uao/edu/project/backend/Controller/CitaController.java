package uao.edu.project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uao.edu.project.backend.DTO.PacienteDTO;
import uao.edu.project.backend.Entidades.Cita;
import uao.edu.project.backend.Entidades.Medico;
import uao.edu.project.backend.Entidades.Paciente;
import uao.edu.project.backend.citaMedicaAPPRepository.CitaRepository;
import uao.edu.project.backend.citaMedicaAPPRepository.MedicoRepository;
import uao.edu.project.backend.citaMedicaAPPRepository.PacienteRepository;
import uao.edu.project.backend.DTO.CitaDTO;
import uao.edu.project.backend.DTO.PacienteDTO;
import uao.edu.project.backend.DTO.MedicoDTO;


import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Cita")
public class CitaController {

    private final CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    // Listar todas las citas
    // Listar todas las citas
@GetMapping
public List<CitaDTO> getAllCitas() {
    return citaRepository.findAll().stream()
        .map(cita -> new CitaDTO(
            cita.getId(),
            cita.getFecha(),
            cita.getHora(),
            new PacienteDTO(
                cita.getPaciente().getId(),
                cita.getPaciente().getNombre(),
                cita.getPaciente().getApellido(),
                cita.getPaciente().getEmail(),
                cita.getPaciente().getGenero(),
                cita.getPaciente().getDireccion(),
                cita.getPaciente().getEdad(),
                cita.getPaciente().getTelefono(),
                cita.getPaciente().getEps(),
                cita.getPaciente().getPrioridad(),
                cita.getPaciente().getCondicion()
            ),
            new MedicoDTO(
                cita.getMedico().getNombre(),
                cita.getMedico().getApellido(),
                cita.getMedico().getEspecialidad(),
                cita.getMedico().getTelefono(),
                cita.getMedico().getEmail()
            ),
            cita.getNotas()
        ))
        .toList();
}


    // Obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable("id") String id) {
        Optional<Cita> cita = citaRepository.findById(id);
        return cita.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva cita
    @PostMapping
public ResponseEntity<Cita> createCita(@RequestBody Cita cita) {
    try {
        if (cita.getPaciente().getId() == null || cita.getMedico().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Paciente paciente = pacienteRepository.findById(cita.getPaciente().getId()).orElse(null);
        Medico medico = medicoRepository.findById(cita.getMedico().getId()).orElse(null);

        if (paciente == null || medico == null) {
            return ResponseEntity.badRequest().body(null);
        }

        cita.setPaciente(paciente);
        cita.setMedico(medico);

        Cita nuevaCita = citaRepository.save(cita);
        return ResponseEntity.ok(nuevaCita);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}

    
    

    // Actualizar parcialmente una cita
    @PutMapping("/{id}")
public ResponseEntity<Cita> updatePartialCita(@PathVariable("id") String id, @RequestBody Map<String, Object> updates) {
    // Verifica si la cita existe
    Optional<Cita> optionalCita = citaRepository.findById(id);
    if (optionalCita.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Cita cita = optionalCita.get();

    // Verifica y actualiza los campos de la cita
    updates.forEach((key, value) -> {
        switch (key) {
            case "fecha":
                cita.setFecha((String) value);
                break;
            case "hora":
                cita.setHora((String) value);
                break;
            case "notas":
                cita.setNotas((String) value);
                break;
            case "paciente":
                Map<String, Object> pacienteUpdates = (Map<String, Object>) value;
                if (pacienteUpdates.containsKey("id")) {
                    String pacienteId = (String) pacienteUpdates.get("id");
                    Paciente paciente = pacienteRepository.findById(pacienteId)
                            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con ID: " + pacienteId));
                    pacienteUpdates.forEach((pacienteKey, pacienteValue) -> {
                        switch (pacienteKey) {
                            case "nombre":
                                paciente.setNombre((String) pacienteValue);
                                break;
                            case "direccion":
                                paciente.setDireccion((String) pacienteValue);
                                break;
                            // Otros campos del paciente
                        }
                    });
                    pacienteRepository.save(paciente);
                    cita.setPaciente(paciente);
                }
                break;
            case "medico":
                Map<String, Object> medicoUpdates = (Map<String, Object>) value;
                if (medicoUpdates.containsKey("id")) {
                    String medicoId = (String) medicoUpdates.get("id");
                    Medico medico = medicoRepository.findById(medicoId)
                            .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado con ID: " + medicoId));
                    medicoUpdates.forEach((medicoKey, medicoValue) -> {
                        switch (medicoKey) {
                            case "telefono":
                                medico.setTelefono((String) medicoValue);
                                break;
                            // Otros campos del médico
                        }
                    });
                    medicoRepository.save(medico);
                    cita.setMedico(medico);
                }
                break;
        }
    });

    // Guarda la cita actualizada
    citaRepository.save(cita);

    return ResponseEntity.ok(cita);
}



    // Eliminar una cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable String id) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

       // Consulta de todos los pacientes en un rango de edad adscritos a determinada EPS
       // Consulta de todos los pacientes en un rango de edad adscritos a determinada EPS
@GetMapping("/pacientes/por-edad-eps")
public ResponseEntity<List<Paciente>> getPacientesPorEdadYEps(
        @RequestParam(name = "edadMinima") int edadMinima,
        @RequestParam(name = "edadMaxima") int edadMaxima,
        @RequestParam(name = "eps") String eps) {
    try {
        List<Paciente> pacientes = pacienteRepository.findByEdadBetweenAndEpsIgnoreCase(edadMinima, edadMaxima, eps);
        return ResponseEntity.ok(pacientes);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}

       

   
       // Cambiar la prioridad a "Alta" para pacientes mayores de 65 años con condiciones especiales
       @PutMapping("/pacientes/prioridad/alta")
       public ResponseEntity<Void> cambiarPrioridadPacientesConCondicionesEspeciales() {
           List<Paciente> pacientes = pacienteRepository.findAll().stream()
                   .filter(paciente -> paciente.getEdad() > 65
                           && paciente.getCondicion() != null
                           && esCondicionEspecial(paciente.getCondicion()))
                   .toList();
   
           pacientes.forEach(paciente -> {
               paciente.setPrioridad("Alta");
               pacienteRepository.save(paciente);
           });
   
           return ResponseEntity.ok().build();
       }
   
       // Consulta de pacientes de una EPS mayores de 65 años
       @GetMapping("/pacientes/eps-mayores")
public List<Paciente> getPacientesPorEpsYEdadMayor(
        @RequestParam(name = "eps") String eps) {
    return pacienteRepository.findAll().stream()
            .filter(paciente -> paciente.getEdad() >= 65
                    && eps.equalsIgnoreCase(paciente.getEps()))
            .toList();
}


   
       // Método auxiliar para validar condiciones especiales
       private boolean esCondicionEspecial(String condicion) {
           List<String> condicionesEspeciales = List.of("hipertensión", "diabetes", "cáncer");
           return condicionesEspeciales.contains(condicion.toLowerCase());
       }
   
}
