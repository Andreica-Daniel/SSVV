package ssvv.example;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitPlatform.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest 
{
    public HashMap<String, Predicate<Integer>> int_predicates;

    @BeforeAll
    public void setUpPredicates() {

        // question: how to have predicates with multiple args? Maybe Function.interface instead.
        // region predicates
        Predicate<Integer> _1 = (arg1) -> {
            try {
                new Student(arg1.toString(), "a", 500);
                Validator<Student> studentValidator = new StudentValidator();
                studentValidator.validate(new Student(arg1.toString(), "a", 500));
            } catch(Exception exception)  {
                return false;
            }

            return true;
        };

        Predicate<Integer> _2 = (arg1) -> {
            try {
                new Student("13", "a", arg1);
                Validator<Student> studentValidator = new StudentValidator();
                studentValidator.validate(new Student("13", "a", arg1));
            } catch(Exception exception)  {
                return false;
            }
            return true;
        };
        // endregion
        int_predicates.put("constructor_student_id", _1);
        int_predicates.put("constructor_student_group", _2);
    }


    // priority 2
    @Test
    public void shouldAnswerWithTrue()
    {
        String filename = "./studenti.xml";
        Validator<Student> validator = new StudentValidator();
        StudentXMLRepository studentXMLRepository = new StudentXMLRepository(validator, filename);
    }

    // priority 3
    @Test
    public void testAddStudent()
    {
        String studentFileName = "./studenti.xml";
        String notaFileName = "./note.xml";
        String temaFileName = "./teme.xml";

        Validator<Student> validatorStudent = new StudentValidator();
        Validator<Nota> validatorNota = new NotaValidator();
        Validator<Tema> validatorTema = new TemaValidator();
        StudentXMLRepository studentXMLRepository = new StudentXMLRepository(validatorStudent, studentFileName);
        NotaXMLRepository notaXMLRepository = new NotaXMLRepository(validatorNota, notaFileName);
        TemaXMLRepository temaXMLRepository = new TemaXMLRepository(validatorTema, temaFileName);

        Service service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);

        Student student = new Student("1234", "Abcd", 199);
        Nota nota = new Nota(new Pair<>("123", "456"), 9.1, 5, "Ok.");

        service.saveStudent(student.getID(), student.getNume(), student.getGrupa());
        service.saveNota(nota.getID().getObject1(), nota.getID().getObject2(), nota.getNota(), nota.getSaptamanaPredare(), nota.getFeedback());
        service.saveTema("123", "Abcd", 5, 3);

        try {
            service.deleteStudent(student.getID());
        } catch (Exception e)
        {
            // if student can't be deleted, it wasn't added. ^
            assertFalse(true);
        }
    }


    // todo: pre and post conditions: assert add + delete is effective before running the rest
    // todo: move in other test class (new TC)
    // todo: create fixtures.
    // equivalence class: tests constructor() of Student.class
    @Test
    public void test_student_constructor() {

        //
        ArrayList<Integer> invalidStreamStudentId = new ArrayList<>();
        ArrayList<Integer> validStreamStudentId = new ArrayList<>();

        ArrayList<Integer> validStreamStudentGroup = new ArrayList<>();
        ArrayList<Integer> invalidStreamStudentGroup = new ArrayList<>();

        invalidStreamStudentGroup.add(1);
        validStreamStudentGroup.add(511);

        invalidStreamStudentId.add(-1); // add values to test
        invalidStreamStudentId.add(0);
        validStreamStudentId.add(1);

        invalidStreamStudentId.forEach((Integer e) -> {
            assertFalse(this.int_predicates.get("constructor_student_id").test(e));
        });

        validStreamStudentId.forEach((Integer e) -> {
            assertTrue(this.int_predicates.get("constructor_student_group").test(e));
        });

    }

    @Test
    public void testStudentConstructor() {

    }

    @Test
    public void testSaveStudent() {

    }
    @Test
    public void tc_3() {

    }
}
