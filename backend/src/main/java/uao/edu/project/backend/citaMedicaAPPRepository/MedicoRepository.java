package uao.edu.project.backend.citaMedicaAPPRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import uao.edu.project.backend.Entidades.Medico;

@Repository
public interface MedicoRepository extends MongoRepository<Medico, String> {
}
