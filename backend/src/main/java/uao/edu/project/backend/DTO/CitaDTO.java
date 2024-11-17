package uao.edu.project.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CitaDTO {
    private String id;
    private String fecha;
    private String hora;
    private PacienteDTO paciente;
    private MedicoDTO medico;
    private String notas;
    
}