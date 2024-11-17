package uao.edu.project.backend.Entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Paciente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String genero;
    private String direccion;
    private int edad;
    private String telefono;
    private String eps;          // EPS del paciente
    private String prioridad;    // Prioridad de atención: "Alta", "Normal", etc.
    private String condicion;    // Condición médica: "hipertensión", "diabetes", etc.
}
