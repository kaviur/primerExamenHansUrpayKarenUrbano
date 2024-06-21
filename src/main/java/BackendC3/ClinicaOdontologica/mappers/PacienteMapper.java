package BackendC3.ClinicaOdontologica.mappers;

import BackendC3.ClinicaOdontologica.dto.PacienteDto;
import BackendC3.ClinicaOdontologica.entity.Paciente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PacienteMapper {

    public static PacienteDto toDto(Paciente paciente){
        return new PacienteDto(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getDomicilio()
        );
    }

    public static Paciente toEntity(PacienteDto pacienteDto){
        return Paciente.builder()
                .nombre(pacienteDto.getNombre())
                .apellido((pacienteDto.getApellido()))
                .dni(pacienteDto.getDni())
                .build();
    }

    public static List<PacienteDto> toDtoList(List<Paciente> pacientes) {
        return pacientes.stream().map(PacienteMapper::toDto).toList();
    }
}
