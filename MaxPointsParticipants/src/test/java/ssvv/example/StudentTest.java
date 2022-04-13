package ssvv.example;

import domain.Student;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import repository.StudentXMLRepository;
import validation.StudentValidator;
import validation.Validator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitPlatform.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentTest {

    public HashMap<String, ArrayList<Integer>> studentStreamId;
    public HashMap<String, ArrayList<Integer>> studentStreamGroup;
    public ArrayList<Student> studentsStream;
    public HashMap<String, ArrayList<String>> generatedFileNames;
    public HashMap<String, Predicate<Integer>> int_predicates;

    @BeforeAll
    public void initializeArrays() {
        this.studentStreamId = new HashMap<>();
        this.studentStreamGroup = new HashMap<>();
        this.int_predicates = new HashMap<>();
        this.studentsStream = new ArrayList<>();
        this.generatedFileNames = new HashMap<>();


        Arrays.asList("true", "false").forEach(e -> {
            this.studentStreamId.put(e, new ArrayList<>());
            this.studentStreamGroup.put(e, new ArrayList<>());
        });

        this.setUpPredicates();
        this.setUpEquivalenceClassStudent();
        this.setUpStudentsStream();
        this.setUpGeneratedFileNames();
    }

    private void setUpGeneratedFileNames() {
        this.generatedFileNames.put("testStudentRepository", new ArrayList<>(Arrays.asList("./testCaseStudents.xml")));
    }

    private void setUpStudentsStream() {
        this.studentStreamId.get("true").forEach(id -> {
            this.studentsStream.add(new Student(id.toString(), "asdf", this.studentStreamGroup.get("true").get((int) this.studentStreamGroup.get("true").stream().count()-1)));
        });
    }

    private void setUpPredicates() {
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

    private void setUpEquivalenceClassStudent() {
        Arrays.asList(511, 512, 513).stream().forEach(e -> {
            this.studentStreamGroup.get("true").add(e);
        });

        Arrays.asList(1, 2, 3).stream().forEach(e -> {
            this.studentStreamGroup.get("false").add(e);
        });

        Arrays.asList(-1, 0).stream().forEach(e -> {
            this.studentStreamId.get("false").add(e);
        });

        Arrays.asList(1, 2, 3).stream().forEach(e -> {
            this.studentStreamId.get("true").add(e);
        });
    }

    private void predicatesStudentConstructorInteger(String predicates_key, HashMap<String, ArrayList<Integer>> sampleStream) {
        List<String> validationClasses = Arrays.asList("true", "false");
        validationClasses.forEach(tf -> {
            sampleStream.get(tf).forEach(v -> {
                if (tf.equals("true")) {
                    assertTrue(this.int_predicates.get(predicates_key).test(v));
                } else if (tf.equals("false")) {
                    assertFalse(this.int_predicates.get(predicates_key).test(v));
                }
            });
        });
    }

    @Test
    @Order(1)
    public void testStudentConstructor() {
        predicatesStudentConstructorInteger("constructor_student_id", this.studentStreamId);
        predicatesStudentConstructorInteger("constructor_student_group", this.studentStreamGroup);
        assertTrue(true);
    }


    private void testAddingAndRemoving(String filename) throws IOException, Exception {
        Validator<Student> validator = new StudentValidator();
        StudentXMLRepository studentXMLRepository = new StudentXMLRepository(validator, filename);

        Student student = new Student(
                studentStreamId.get("true").get(0).toString(),
                "asdf",
                this.studentStreamGroup.get("true").get(0)
        );
        studentXMLRepository.save(student);
        assertTrue(studentXMLRepository.findOne(student.getID()).getID().equals(student.getID()));
        studentXMLRepository.delete(student.getID());
        assertFalse(studentXMLRepository.findAll().iterator().hasNext());

    }

    private void testAddingManyWithSameId(String filename) throws Exception {
        Validator<Student> validator = new StudentValidator();
        StudentXMLRepository studentXMLRepository = new StudentXMLRepository(validator, filename);
        this.studentsStream.forEach(studentXMLRepository::save);
    }

    @Test
    @Order(2)
    public void testStudentRepository() throws IOException, Exception {
        String filename = this.generatedFileNames.get("testStudentRepository").get(0);
        FileWriter newFile = new FileWriter("./testCaseStudents.xml");
        newFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile.flush();
        newFile.close();

        // catch blocks are revert blocks
        // todo: refactor into forEach and use Function.interface
        try {
            this.testAddingAndRemoving(filename);
        } catch (Exception e) {
            File myFD = new File(filename);
            myFD.delete();
            throw new Exception(e);
        }

        try {
            this.testAddingManyWithSameId(filename);
        } catch (Exception e) {
            File myFD = new File(filename);
            myFD.delete();
            throw new Exception(e);
        }

        assertTrue(true);
        // No need to cleanup at this final point because @AfterAll will handle cleanup automatically.
    }

    @AfterAll
    public void cleanupGeneratedFiles() {
        this.generatedFileNames.keySet().forEach(k -> {
            this.generatedFileNames.get(k).forEach(e -> {
                File myFD = new File(e);
                try {
                    myFD.delete();
                } catch (Exception exception) {
                    // should log
                }
            });
        });
    }
}