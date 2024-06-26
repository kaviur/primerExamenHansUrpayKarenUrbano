package BackendC3.ClinicaOdontologica.service.impl;

import BackendC3.ClinicaOdontologica.dto.IDto;
import BackendC3.ClinicaOdontologica.dto.requestDtos.InputTurnoDto;
import BackendC3.ClinicaOdontologica.entity.Odontologo;
import BackendC3.ClinicaOdontologica.entity.Paciente;
import BackendC3.ClinicaOdontologica.entity.Turno;
import BackendC3.ClinicaOdontologica.exceptions.customExceptions.TurnoNotFoundException;
import BackendC3.ClinicaOdontologica.mappers.TurnoMapper;
import BackendC3.ClinicaOdontologica.repository.IOdontologoRepository;
import BackendC3.ClinicaOdontologica.repository.IPacienteRepository;
import BackendC3.ClinicaOdontologica.repository.ITurnoRepository;
import BackendC3.ClinicaOdontologica.service.ITurnoService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements ITurnoService {

    private final ITurnoRepository turnoRepository;
    private final IPacienteRepository pacienteRepository;
    private final IOdontologoRepository odontologoRepository;

    @Override
    public IDto buscar(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));

        return TurnoMapper.toDto(turno);
    }

    @Override
    public IDto guardar(IDto dto) {
        if(!(dto instanceof InputTurnoDto turnoDto)){
            throw new IllegalArgumentException("Entrada de datos incorrecta");
        }
        if(turnoDto.getIdPaciente() == null || turnoDto.getIdOdontologo() == null || turnoDto.getFecha() == null){
            throw new ServiceException("Faltan datos");
        }

        Paciente paciente = pacienteRepository.findById(turnoDto.getIdPaciente())
                .orElseThrow(() -> new TurnoNotFoundException("Paciente no encontrado"));

        Odontologo odontologo = odontologoRepository.findById(turnoDto.getIdOdontologo())
                .orElseThrow(() -> new TurnoNotFoundException("Odontólogo no encontrado"));

        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologo);
        turno.setFecha(LocalDateTime.parse(turnoDto.getFecha()));

        return TurnoMapper.toDto(turnoRepository.save(turno));
    }

    @Override
    public IDto actualizar(IDto dto, Long id) {
        if(!(dto instanceof InputTurnoDto turnoDto)){
            throw new IllegalArgumentException("Entrada de datos incorrecta");
        }

        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));

        if(!Objects.equals(turnoDto.getIdPaciente(), turno.getPaciente().getId())){
            Paciente paciente = pacienteRepository.findById(turnoDto.getIdPaciente())
                    .orElseThrow(() -> new TurnoNotFoundException("Paciente no encontrado"));
            turno.setPaciente(paciente);
        }

        if(!Objects.equals(turnoDto.getIdOdontologo(), turno.getOdontologo().getId())){
            Odontologo odontologo = odontologoRepository.findById(turnoDto.getIdOdontologo())
                    .orElseThrow(() -> new TurnoNotFoundException("Odontólogo no encontrado"));
            turno.setOdontologo(odontologo);
        }

        turno.setFecha(LocalDateTime.parse(turnoDto.getFecha()));

        return TurnoMapper.toDto(turnoRepository.save(turno));
    }

    @Override
    public void eliminar(Long id) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException("Turno no encontrado"));

        turnoRepository.delete(turno);
    }

    @Override
    public List<IDto> buscarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        return TurnoMapper.toDtoList(turnos);
    }

    @Override
    public List<IDto> buscarTurnosPorPaciente(Long idPaciente) {
        int pacienteId = Math.toIntExact(idPaciente);
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new TurnoNotFoundException("Paciente no encontrado"));
        List<Turno> turnos = turnoRepository.findAllByPacienteId(idPaciente);
        if(turnos.isEmpty()){
            throw new TurnoNotFoundException("El paciente no tiene turnos");
        }
        return TurnoMapper.toDtoList(turnos);
    }

    @Override
    public List<IDto> buscarTurnosPorOdontologo(Long idOdontologo) {
        int odontologoId = Math.toIntExact(idOdontologo);
        Odontologo odontologo = odontologoRepository.findById(odontologoId)
                .orElseThrow(() -> new TurnoNotFoundException("Odontólogo no encontrado"));
        List<Turno> turnos = turnoRepository.findAllByOdontologoId(idOdontologo);
        if(turnos.isEmpty()){
            throw new TurnoNotFoundException("El odontólogo no tiene turnos");
        }
        return TurnoMapper.toDtoList(turnos);
    }
}
