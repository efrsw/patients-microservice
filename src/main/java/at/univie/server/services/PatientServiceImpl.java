package at.univie.server.services;

import at.univie.server.abstractions.PatientService;
import at.univie.server.data.PatientRepository;
import at.univie.server.models.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> findById(UUID id) {
        return patientRepository.findById(id);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> update(UUID id, Patient patient) {
        var currentPatient = patientRepository.findById(id);

        if (currentPatient.isPresent()) {
            currentPatient.get().setFirstName(patient.getFirstName());
            currentPatient.get().setLastName(patient.getLastName());
            currentPatient.get().setContactNumber(patient.getContactNumber());
            currentPatient.get().setDateOfBirth(patient.getDateOfBirth());
            currentPatient.get().setEmailAddress(patient.getEmailAddress());
            currentPatient.get().setGender(patient.getGender());
            patientRepository.save(currentPatient.get());
        }

        return currentPatient;
    }

    public void delete(UUID id) {
        patientRepository.deleteById(id);
    }
}
