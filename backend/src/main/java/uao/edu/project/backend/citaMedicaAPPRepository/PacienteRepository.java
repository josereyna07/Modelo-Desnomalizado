package uao.edu.project.backend.citaMedicaAPPRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import uao.edu.project.backend.Entidades.Paciente;

@Repository
public interface PacienteRepository extends MongoRepository<Paciente, String> {
}
