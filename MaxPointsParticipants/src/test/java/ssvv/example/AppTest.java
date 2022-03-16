package ssvv.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.Test;
import repository.NotaXMLRepository;
import repository.StudentFileRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        String filename = "./studenti.xml";
        Validator<Student> validator = new StudentValidator();
        StudentXMLRepository studentXMLRepository = new StudentXMLRepository(validator, filename);

    }

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
        NotaXMLRepository notaXMLRepository = new NotaXMLRepository(validatorNota, notaFileName); // a problem if file does not exist. todo: add checks
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
    public void ec1() {

        // todo: elevate to fixture.
        ArrayList<Integer> invalidStreamStudentId = new ArrayList<>();
        ArrayList<Integer> validStreamStudentId = new ArrayList<>();

        ArrayList<Integer> validStreamStudentGroup = new ArrayList<>();
        ArrayList<Integer> invalidStreamStudentGroup = new ArrayList<>();

        invalidStreamStudentGroup.add(1);
        validStreamStudentGroup.add(511);


        invalidStreamStudentId.add(-1); // add values to test
        invalidStreamStudentId.add(0);
        validStreamStudentId.add(1);

        Predicate<Integer> testStudentConstructorPredicate1 = (arg1) -> {
            try {
                new Student(arg1.toString(), "a", 500);
                Validator<Student> studentValidator = new StudentValidator();
                studentValidator.validate(new Student(arg1.toString(), "a", 500));
            } catch(Exception exception)  {
                return false;
            }

            return true;
        };

        // todo: merge args? optional args? specify? (refactor)
        Predicate<Integer> testStudentConstructorPredicate2 = (arg1) -> {
            try {
                new Student("13", "a", arg1);
                Validator<Student> studentValidator = new StudentValidator();
                studentValidator.validate(new Student("13", "a", arg1));
            } catch(Exception exception)  {
                return false;
            }
            return true;
        };

        invalidStreamStudentId.forEach((Integer e) -> {
            assertFalse(testStudentConstructorPredicate1.test(e));
        });

        validStreamStudentId.forEach((Integer e) -> {
            assertTrue(testStudentConstructorPredicate1.test(e));
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
