package at.univie.server.abstractions;

import at.univie.server.models.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {
    Optional<Patient> findById(UUID id);

    List<Patient> findAll();

    Patient create(Patient patient);

    Optional<Patient> update(UUID id, Patient patient);

    void delete(UUID id);
}
