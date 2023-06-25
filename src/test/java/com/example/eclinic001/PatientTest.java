//package com.example.eclinic001;
//
//import com.example.eclinic001.model.Patient;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//
//public class PatientTest  {
//
//    @Test
//    void testPatientBuilder(){
//      Patient patient = Patient.builder()
//              .patientId(1L)
//              .genotype("AA")
//              .bloodGroup("O+")
//              .build();
//
//
//
//      Assertions.assertNotNull(patient);
//      Assertions.assertEquals(1L, patient.getPatientId());
//      Assertions.assertEquals("AA", patient.getGenotype());
//      Assertions.assertEquals("O+", patient.getBloodGroup());
//
//
//    }
//
//}
