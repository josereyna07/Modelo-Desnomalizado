package uao.edu.project.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PacienteDTO {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String genero;
    private String direccion;
    private int edad;
    private String telefono;
    private String eps;          // EPS del paciente
    private String prioridad;    // Prioridad de atención
    private String condicion;    // Condición médica
}
