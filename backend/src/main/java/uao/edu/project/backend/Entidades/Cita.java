package uao.edu.project.backend.Entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Cita")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cita {
    @Id
    private String id;
    private String fecha;
    private String hora;
    private Paciente paciente;  // Incluye toda la información del paciente
    private Medico medico;      // Incluye toda la información del médico
    private String notas;
}
