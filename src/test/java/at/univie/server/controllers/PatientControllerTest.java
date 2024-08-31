package at.univie.server.controllers;

import at.univie.server.abstractions.PatientService;
import at.univie.server.models.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    private MockMvc mvc;
    private UUID patientId;
    private Patient patient;

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    private JacksonTester<Patient> jsonPatient;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        patientId = UUID.randomUUID();
        patient = new Patient(patientId,
                "First",
                "Second",
                new Date(2024, Calendar.JUNE, 1),
                123,
                "first@email.com",
                "male");

        mvc = MockMvcBuilders.standaloneSetup(patientController)
                .build();
    }

    @Test
    public void canRetrieveByPatientIdWhenExists() throws Exception {
        given(patientService.findById(patientId))
                .willReturn(Optional.of(patient));

        MockHttpServletResponse response = mvc.perform(get("/api/patients/" + patientId).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonPatient.write(patient).getJson());
    }

    @Test
    public void canRetrieveByPatientIdWhenNotExists() throws Exception {
        given(patientService.findById(patientId))
                .willReturn(Optional.empty());

        MockHttpServletResponse response = mvc.perform(get("/api/patients/" + patientId).accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void canCreatePatient() throws Exception {
        var savedPatient = new Patient(patientId, "First", "Second", new Date(2024, Calendar.JUNE, 1),
                123, "first@email.com", "male");

        given(patientService.create(any(Patient.class))).willReturn(savedPatient);

        MockHttpServletResponse response = mvc.perform(post("/api/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPatient.write(patient).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonPatient.write(savedPatient).getJson());
    }

    @Test
    public void canUpdatePatient() throws Exception {
        var updatedPatient = new Patient("Second", "First", new Date(2024, Calendar.JUNE, 1),
                123, "first@email.com", "male");

        given(patientService.update(eq(patientId), any(Patient.class))).willReturn(Optional.of(updatedPatient));

        MockHttpServletResponse response = mvc.perform(put("/api/patients/" + patientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPatient.write(updatedPatient).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonPatient.write(updatedPatient).getJson());
    }
}