package ssvv.example;

import org.junit.jupiter.api.TestInstance;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import org.junit.jupiter.api.*;

@RunWith(JUnitPlatform.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {
    public GradeTest gT = new GradeTest();
    public StudentTest sT = new StudentTest();
    public AssignmentsTest aT = new AssignmentsTest();

    @Test
    public void test_1() {
        gT.test_ec1_addGrade_Stream_Id_Valid();
    }

    @Test
    public void test_2() {
        sT.test_ec1_addStudent_Stream_Group_Valid();
    }
    @Test
    public void test_3() throws Exception {
        aT.WhiteBoxTest1_Valid();
    }

    @Test
    public void test_all() throws Exception {
        gT.initializeArrays();
        sT.initializeArrays();
        aT.initializeArrays();

        gT.SetUpXmlRepo();
        gT.test_ec1_addGrade_Stream_Id_Valid();
        gT.CleanUpXmlRepo();
        sT.SetUpXmlRepo();
        sT.test_ec1_addStudent_Stream_Group_Valid();
        sT.CleanUpXmlRepo();

        aT.WhiteBoxTest1_Valid();
        aT.cleanupGeneratedFiles();
    }
}
