package ssvv.example;

import org.junit.After;
import org.junit.Before;
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

    @BeforeEach
    public void initFields() throws Exception {
        this.gT = new GradeTest();
        this.sT = new StudentTest();
        this.aT = new AssignmentsTest();

        this.gT.initializeArrays();
        this.sT.initializeArrays();
        this.aT.initializeArrays();

        this.gT.SetUpXmlRepo();
        this.sT.SetUpXmlRepo();
    }

    @AfterEach
    public void cleanupFiles() throws Exception {
        this.sT.CleanUpXmlRepo();
        this.gT.CleanUpXmlRepo();
        this.aT.cleanupGeneratedFiles();
    }

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
        gT.test_ec1_addGrade_Stream_Id_Valid();
        sT.test_ec1_addStudent_Stream_Group_Valid();
        aT.WhiteBoxTest1_Valid();
    }
}
