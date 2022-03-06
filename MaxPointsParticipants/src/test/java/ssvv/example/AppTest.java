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

}
