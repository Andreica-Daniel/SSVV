package ssvv.example;

import domain.Nota;
import domain.Pair;
import domain.Student;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.Validator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitPlatform.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GradeTest {
    public Map<String, Predicate<Integer>> grades_predicates;
    public Map<String, ArrayList<Integer>> gradesStreamId;
    public Map<String, ArrayList<Integer>> gradesStreamValues;
    public Map<String, ArrayList<Integer>> gradesStreamPredata;
    public Map<String, ArrayList<Integer>> gradesStreamStudentID;
    public List<Nota> notaStream;
    public Map<String, ArrayList<String>> generatedFileNames;
    public NotaXMLRepository currentXmlRepo;

    @BeforeAll
    public void initializeArrays() {
        this.gradesStreamId = new HashMap<>();
        this.gradesStreamValues = new HashMap<>();
        this.grades_predicates = new HashMap<>();
        this.notaStream = new ArrayList<>();
        this.gradesStreamPredata = new HashMap<>();
        this.gradesStreamStudentID = new HashMap<>();
        this.generatedFileNames = new HashMap<>();


        Arrays.asList("true", "false").forEach(e -> {
            this.gradesStreamId.put(e, new ArrayList<>());
            this.gradesStreamValues.put(e, new ArrayList<>());
            this.gradesStreamPredata.put(e, new ArrayList<>());
            this.gradesStreamStudentID.put(e, new ArrayList<>());
        });


        this.generatedFileNames.put("testGradeRepository", new ArrayList<>(Arrays.asList("./testCaseGrades.xml")));

        this.setUpPredicates();
        this.setUpEquivalenceClassAssignment();
        this.setUpGradesStream();
        //this.setUpGeneratedFileNames();
    }

    @BeforeEach
    public void SetUpXmlRepo() throws IOException, Exception {
        String filename = this.generatedFileNames.get("testGradeRepository").get(0);
        FileWriter newFile = new FileWriter("./testCaseGrades.xml");
        newFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile.flush();
        newFile.close();

        try {
            Validator<Nota> validator = new NotaValidator();
            NotaXMLRepository notaXMLRepository = new NotaXMLRepository(validator, filename);
            this.currentXmlRepo = notaXMLRepository;
        } catch (Exception e) {
            File myFD = new File(filename);
            myFD.delete();
            throw new Exception(e);
        }

        assertTrue(true);
    }


    @AfterEach
    public void CleanUpXmlRepo() throws IOException, Exception {
        String filename = this.generatedFileNames.get("testGradeRepository").get(0);
        File myFD = new File(filename);
        myFD.delete();
    }
    private void setUpGeneratedFileNames() {
        //this.generatedFileNames.put("testAssignmentRepository", new ArrayList<>(Arrays.asList("./testCaseAssignments.xml")));
    }

    private void setUpGradesStream() {
        this.gradesStreamId.get("true").forEach(id -> {
            Nota newTema = new Nota(
                    new Pair(id.toString(), this.gradesStreamStudentID.get("true").get(0)),
                    this.gradesStreamValues.get("true").get(0),
                    this.gradesStreamPredata.get("true").get(0),
                    "ok"
            );
            this.notaStream.add(newTema);
        });
        // we will have as many Nota as there are ids in this.gradesStreamId
    }

    private void setUpPredicates() {
        Predicate<Integer> _1 = (arg1) -> {
            try {
                Integer studentId = this.gradesStreamStudentID.get("true").get(0);
                Integer notaId = this.gradesStreamId.get("true").get(0);
                new Nota(new Pair<>(studentId.toString(), notaId.toString()),
                        arg1,
                        this.gradesStreamPredata.get("true").get(0),
                        "ok"
                );

                // todo: rename Nota to Grade
                // untill here ok
                Validator<Nota> temaValidator = new NotaValidator();
                temaValidator.validate(
                        new Nota(new Pair<>(studentId.toString(), notaId.toString()),
                                arg1,
                                this.gradesStreamPredata.get("true").get(0),
                                "ok"
                        )
                );
            } catch (Exception exception) {
                return false;
            }
            return true;
        };

        Predicate<Integer> _2 = (arg1) -> {
            try {
                Integer studentId = this.gradesStreamStudentID.get("true").get(0);
                Integer notaId = this.gradesStreamId.get("true").get(0);
                new Nota(new Pair<>(studentId.toString(), notaId.toString()),
                        this.gradesStreamValues.get("true").get(0),
                        arg1,
                        "ok"
                );
                Validator<Nota> temaValidator = new NotaValidator();
                temaValidator.validate(
                        new Nota(new Pair<>(studentId.toString(), notaId.toString()),
                                this.gradesStreamValues.get("true").get(0),
                                arg1,
                                "ok"
                        )
                );
            } catch (Exception exception) {
                return false;
            }
            return true;
        };

        this.grades_predicates.put("grade_value", _1);
        this.grades_predicates.put("sapt_predata", _2);
    }

    private void setUpEquivalenceClassAssignment() {
        Arrays.asList(1, 2, 3).stream().forEach(e -> {
            this.gradesStreamId.get("true").add(e);
        });
        Arrays.asList(-1, -2).stream().forEach(e -> {
            this.gradesStreamId.get("false").add(e);
        });
        Arrays.asList(10, 9).stream().forEach( e -> {
            this.gradesStreamValues
                    .get("true")
                    .add(e);
        });

        Arrays.asList(-1, -2).stream().forEach( e -> {
            this.gradesStreamValues
                    .get("false")
                    .add(e);
        });

        Arrays.asList(14, 15, 16).stream().forEach( e -> {
            this.gradesStreamPredata
                    .get("true")
                    .add(e);
        });

        Arrays.asList(-1, -2).stream().forEach( e -> {
            this.gradesStreamPredata
                    .get("false")
                    .add(e);
        });

        Arrays.asList(1, 2, 3).stream().forEach( e -> {
            this.gradesStreamStudentID
                    .get("true")
                    .add(e);
        });
    }
//            this.grades_predicates.put("sapt_predata", _1);
//        this.grades_predicates.put("grade_value", _2);

    @Test
    public void test_ec1_addGrade_Stream_Id_Valid() {
        this.gradesStreamValues.get("true").forEach( (Integer grade) -> {
           try {
               Assertions.assertTrue(this.grades_predicates.get("grade_value").test(grade));
               Nota testNota = new Nota(new Pair<>(this.gradesStreamStudentID.get("true").get(0).toString(), this.gradesStreamId.get("true").get(0).toString()),
                        grade,
                        this.gradesStreamPredata.get("true").get(0),
                       "feedback"
                       );
               this.currentXmlRepo.save(testNota);
           } catch (Exception e) {
               Assertions.fail();
           }
        });
        Assertions.assertTrue(true);
    }

    @Test
    public void test_ec1_addGrade_Stream_Id_Fails() {
        this.gradesStreamValues.get("false").forEach( (Integer grade) -> {
            try {
                Assertions.assertFalse(this.grades_predicates.get("grade_value").test(grade));
                Nota testNota = new Nota(new Pair<>(this.gradesStreamStudentID.get("true").get(0).toString(), this.gradesStreamId.get("true").get(0).toString()),
                        grade,
                        this.gradesStreamPredata.get("true").get(0),
                        "feedback"
                );
                this.currentXmlRepo.save(testNota);
                Assertions.fail();
            } catch (Exception e) {
                Assertions.assertTrue(true);
            }
        });
        Assertions.assertTrue(true);
    }

    @Test
    public void test_ec2_addGrade_Stream_Id_Valid() {
        this.gradesStreamPredata.get("true").forEach( (Integer week) -> {
            try {
                Assertions.assertTrue(this.grades_predicates.get("sapt_predata").test(week));
                Nota testNota = new Nota(new Pair<>(this.gradesStreamStudentID.get("true").get(0).toString(), this.gradesStreamId.get("true").get(0).toString()),
                        this.gradesStreamValues.get("true").get(0),
                        week,
                        "feedback"
                );
                this.currentXmlRepo.save(testNota);
            } catch (Exception e) {
                Assertions.fail();
            }
        });
        Assertions.assertTrue(true);
    }

    @Test
    public void test_ec2_addGrade_Stream_Id_Fails() {
        this.gradesStreamPredata.get("false").forEach( (Integer week) -> {
            try {
                Assertions.assertFalse(this.grades_predicates.get("sapt_predata").test(week));
                Nota testNota = new Nota(new Pair<>(this.gradesStreamStudentID.get("true").get(0).toString(), this.gradesStreamId.get("true").get(0).toString()),
                        this.gradesStreamValues.get("true").get(0),
                        week,
                        "feedback"
                );
                this.currentXmlRepo.save(testNota);
                Assertions.fail();
            } catch (Exception e) {
                Assertions.assertTrue(true);
            }
        });
        Assertions.assertTrue(true);
    }

    private void predicatesAssignmentConstructorInteger(String predicates_key, Map<String, ArrayList<Integer>> sampleStream) {
        List<String> validationClasses = Arrays.asList("true", "false");
        validationClasses.forEach(tf -> {
            sampleStream.get(tf).forEach(v -> {
                if (tf.equals("true")) {
                    assertTrue(this.grades_predicates.get(predicates_key).test(v));
                } else if (tf.equals("false")) {
                    assertFalse(this.grades_predicates.get(predicates_key).test(v));
                }
            });
        });
    }

    private void predicatesAssignmentConstructorDatetime(String predicates_key, Map<String, Map<Integer, Integer>> sampleStream) {
        List<String> validationClasses = Arrays.asList("true", "false");
        validationClasses.forEach(tf -> {
            sampleStream.get(tf).forEach((k, v) -> {
                // is there a way to have predicates with multiple args?
                if (tf.equals("true")) {
                    assertTrue(this.grades_predicates.get(predicates_key).test(k));
                } else if (tf.equals("false")) {
                    assertFalse(this.grades_predicates.get(predicates_key).test(k));
                }
            });
        });
    }

    @Test
    @Order(1)
    public void testAssignmentConstructor() {
        predicatesAssignmentConstructorInteger("sapt_predata", this.gradesStreamId);
        predicatesAssignmentConstructorInteger("grade_value", this.gradesStreamValues);
        assertTrue(true);
    }

    private void testAddingAndRemoving(String filename) throws IOException, Exception {
        Validator<Nota> validator = new NotaValidator();
        NotaXMLRepository notaRepo = new NotaXMLRepository(validator, filename);

        Nota assignment = new Nota(
                new Pair<>(this.gradesStreamId.get("true").get(0).toString(), this.gradesStreamStudentID.get("true").get(0).toString()),
                this.gradesStreamValues.get("true").get(0),
                this.gradesStreamPredata.get("true").get(0),
                "valid"
        );
        notaRepo.save(assignment);
        assertTrue(notaRepo.findOne(assignment.getID()).getID().equals(assignment.getID()));
        notaRepo.delete(assignment.getID());
        // assertFalse(notaRepo.findAll().iterator().hasNext());
    }

    private void testAddingManyWithSameId(String filename) throws Exception {
        Validator<Nota> validator = new NotaValidator();
        NotaXMLRepository assignmentXMLRepository = new NotaXMLRepository(validator, filename);
        this.notaStream.forEach(assignmentXMLRepository::save);
    }

    @Test
    @Order(2)
    public void testAssignmentRepository() throws IOException, Exception {
        //String filename = this.generatedFileNames.get("testAssignmentRepository").get(0);
        //FileWriter newFile = new FileWriter(filename);
        //newFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        //newFile.flush();
        //newFile.close();

        // catch blocks are revert blocks
        // todo: refactor into forEach and use Function.interface
        try {
            this.testAddingAndRemoving("./note.xml");
        } catch (Exception e) {
            //File myFD = new File("./note.xml");
            //myFD.delete();
            //throw new Exception(e);
        }

        try {
            this.testAddingManyWithSameId("./note.xml");
        } catch (Exception e) {
            //File myFD = new File(filename);
            //myFD.delete();
            //throw new Exception(e);
        }

        assertTrue(true);
        // No need to cleanup at this final point because @AfterAll will handle cleanup automatically.
    }

    @AfterAll
    public void cleanupGeneratedFiles() {
//        this.generatedFileNames.keySet().forEach(k -> {
//            this.generatedFileNames.get(k).forEach(e -> {
//                File myFD = new File(e);
//                try {
//                    myFD.delete();
//                } catch (Exception exception) {
//                    // should log
//                }
//            });
//        });
    }


}
