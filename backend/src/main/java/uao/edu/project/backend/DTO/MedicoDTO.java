package uao.edu.project.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedicoDTO {
    private String nombre;
    private String apellido;
    private String especialidad;
    private String telefono;
    private String email;
}