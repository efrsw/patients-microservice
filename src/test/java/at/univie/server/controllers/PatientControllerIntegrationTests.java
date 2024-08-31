package at.univie.server.controllers;

import at.univie.server.data.PatientRepository;
import at.univie.server.models.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PatientControllerIntegrationTests {

    private UUID patientId;
    private Patient patient;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        patientRepository.deleteAll();
        patientId = UUID.randomUUID();
        patient = new Patient(patientId,
                "First",
                "Second",
                new Date(2024, Calendar.JUNE, 1),
                123,
                "first@email.com",
                "male");

        patientRepository.save(patient);
    }

    @Test
    public void canAddNewPatient() {
        ResponseEntity<Patient> response = restTemplate.postForEntity("/api/patients", patient, Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(patientRepository.findById(patientId).get().getId()).isEqualTo(patientId);
    }

    @Test
    public void canRetrieveWhenIdExists() {
        ResponseEntity<Patient> response = restTemplate.getForEntity("/api/patients/" + patientId, Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void canUpdateExistingPatient() {
        var updatePatient = new Patient("Second",
                "First",
                new Date(2024, Calendar.JUNE, 01),
                123,
                "first@email.com",
                "male");

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(updatePatient, httpHeaders);

        var response = restTemplate.exchange("/api/patients/" + patientId, HttpMethod.PUT, httpEntity, Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}