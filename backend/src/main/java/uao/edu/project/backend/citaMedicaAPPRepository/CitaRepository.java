package uao.edu.project.backend.citaMedicaAPPRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import uao.edu.project.backend.Entidades.Cita;

@Repository
public interface CitaRepository extends MongoRepository<Cita, String> {
    // Agrega métodos personalizados si es necesario
}
