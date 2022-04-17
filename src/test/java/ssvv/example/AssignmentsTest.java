package ssvv.example;

import console.UI;
import domain.Tema;
import org.junit.jupiter.api.*;
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

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(JUnitPlatform.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssignmentsTest {

    public Map<String, Predicate<Integer>> assignment_predicates;
    public Map<String, ArrayList<Integer>> assignmentStreamId;
    public Map<String, Map<Integer, Integer>> assignmentStreamStartdate_Enddate;
//    public Map<String, Map<Integer, Integer>> assignmentStreamDeadlines;
    public List<Tema> assignmentsStream;
    public Map<String, ArrayList<String>> generatedFileNames;

    @BeforeAll
    public void initializeArrays() {
        this.assignmentStreamId = new HashMap<>();
        this.assignmentStreamStartdate_Enddate = new HashMap<>();
//        this.assignmentStreamDeadlines = new HashMap<>();
        this.assignment_predicates = new HashMap<>();
        this.assignmentsStream = new ArrayList<>();
        this.generatedFileNames = new HashMap<>();


        Arrays.asList("true", "false").forEach(e -> {
            this.assignmentStreamId.put(e, new ArrayList<>());
            this.assignmentStreamStartdate_Enddate.put(e, new HashMap<>());
        });

        this.setUpPredicates();
        this.setUpEquivalenceClassAssignment();
        this.setUpAssignmentsStream();
        this.setUpGeneratedFileNames();
    }

    private void setUpGeneratedFileNames() {
        this.generatedFileNames.put("testAssignmentRepository", new ArrayList<>(Arrays.asList("./testCaseAssignments.xml")));
    }

    private void WhiteBoxTest(String menuOption, String id, String description, String endWeek, String startWeek) throws Exception{
        byte[] i1 = String.format("%s %s\n%s\n%s\n%s\n0\n", menuOption, id, description, endWeek, startWeek).getBytes();
        InputStream in = new ByteArrayInputStream(i1);

        FileWriter newFile = new FileWriter("./mockFile1.txt");
        newFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile.flush();
        newFile.close();

        FileWriter newFile2 = new FileWriter("./mockFile2.txt");
        newFile2.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile2.flush();
        newFile2.close();

        FileWriter newFile3 = new FileWriter("./mockFile3.txt");
        newFile3.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile3.flush();
        newFile3.close();

        StudentValidator sv = new StudentValidator();
        StudentXMLRepository sr = new StudentXMLRepository(sv, "./mockFile1.txt");

        TemaValidator tv = new TemaValidator();
        TemaXMLRepository tr = new TemaXMLRepository(tv, "./mockFile2.txt");

        NotaValidator nv = new NotaValidator();
        NotaXMLRepository nr = new NotaXMLRepository(nv, "./mockFile3.txt");

        Service service = new Service(sr, tr, nr);
        UI ui = new UI(service);
        System.setIn(in);
        ui.run();

        if (!tr.findAll().iterator().hasNext()) {
            Assertions.fail();
        }

        File fd1 = new File("./mockFile1.txt");
        fd1.delete();
        File fd2 = new File("./mockFile2.txt");
        fd2.delete();
        File fd3 = new File("./mockFile3.txt");
        fd3.delete();
    }

    @Test
    public void WhiteBoxTest2_Valid_AddAndUpdate() throws Exception {
        byte[] i1 = String.format("22 1\nasdf\n12\n6\n22 1\nasdfg\n12\n6\n0\n").getBytes();
        InputStream in = new ByteArrayInputStream(i1);

        FileWriter newFile = new FileWriter("./mockFile1.txt");
        newFile.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile.flush();
        newFile.close();

        FileWriter newFile2 = new FileWriter("./mockFile2.txt");
        newFile2.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile2.flush();
        newFile2.close();

        FileWriter newFile3 = new FileWriter("./mockFile3.txt");
        newFile3.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<Entitati>\n</Entitati>\n");
        newFile3.flush();
        newFile3.close();

        StudentValidator sv = new StudentValidator();
        StudentXMLRepository sr = new StudentXMLRepository(sv, "./mockFile1.txt");

        TemaValidator tv = new TemaValidator();
        TemaXMLRepository tr = new TemaXMLRepository(tv, "./mockFile2.txt");

        NotaValidator nv = new NotaValidator();
        NotaXMLRepository nr = new NotaXMLRepository(nv, "./mockFile3.txt");

        Service service = new Service(sr, tr, nr);
        UI ui = new UI(service);
        System.setIn(in);
        ui.run();

        if (!tr.findAll().iterator().hasNext()) {
            Assertions.fail();
        }

        File fd1 = new File("./mockFile1.txt");
        fd1.delete();
        File fd2 = new File("./mockFile2.txt");
        fd2.delete();
        File fd3 = new File("./mockFile3.txt");
        fd3.delete();
    }

    @Test
    public void AllWhiteBoxTestsCoverage() throws IOException, Exception {
        this.WhiteBoxTest1_Fails();
        this.WhiteBoxTest1_Valid();
        this.WhiteBoxTest2_Valid_AddAndUpdate();
    }

    @Test
    public void WhiteBoxTest1_Valid() throws IOException, Exception {
        this.WhiteBoxTest("22", "1", "asdf", "12", "6");
    }

    @Test
    public void WhiteBoxTest1_Fails() throws IOException, Exception {
        try {
            this.WhiteBoxTest("22", "-1", "asdf", "12", "6");
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    private void setUpAssignmentsStream() {
        this.assignmentStreamId.get("true").forEach(id -> {
            Tema newTema = new Tema(
                    id.toString(),
                    "desc",
                    this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get(),
                    this.assignmentStreamStartdate_Enddate
                            .get("true")
                            .get(this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get())
            );
            this.assignmentsStream.add(newTema);
        });
    }

    private void setUpPredicates() {
        Predicate<Integer> _1 = (arg1) -> {
            try {
                new Tema(arg1.toString(),
                        "desc",
                        this.assignmentStreamStartdate_Enddate.get("true")
                                .get(this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get()),
                        this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get()
                );
                Validator<Tema> temaValidator = new TemaValidator();
                temaValidator.validate(
                        new Tema(arg1.toString(),
                                "desc",
                                this.assignmentStreamStartdate_Enddate.get("true")
                                        .get(this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get()),
                                this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get()
                        )
                );
            } catch (Exception exception) {
                return false;
            }
            return true;
        };
        Predicate<Integer> _2 = (arg1) -> {
            try {
                new Tema(arg1.toString(),
                        "desc",
                        this.assignmentStreamStartdate_Enddate.get("true")
                                .get(arg1),
                        arg1
                );
                Validator<Tema> temaValidator = new TemaValidator();
                temaValidator.validate(
                        new Tema(arg1.toString(),
                                "desc",
                                this.assignmentStreamStartdate_Enddate.get("true")
                                        .get(arg1),
                                arg1
                        )
                );
            } catch (Exception exception) {
                return false;
            }
            return true;
        };

        this.assignment_predicates.put("constructor_id", _1);
        this.assignment_predicates.put("constructor_date_interval", _2);
    }

    private void setUpEquivalenceClassAssignment() {
        Arrays.asList(1, 2, 3).stream().forEach(e -> {
            this.assignmentStreamId.get("true").add(e);
        });
        Arrays.asList(-1, -2).stream().forEach(e -> {
            this.assignmentStreamId.get("false").add(e);
        });
        Arrays.asList(3, 6).stream().forEach( e -> {
            this.assignmentStreamStartdate_Enddate
                    .get("true")
                    .put(
                        e,
                        e + 5
                    );
        });
//        Arrays.asList(9, 12).stream().forEach( e -> {
//            this.assignmentStreamDeadlines
//                    .get("true")
//                    .put(
//                            e,
//                            e + 5
//                    );
//        });
    }

    private void predicatesAssignmentConstructorInteger(String predicates_key, Map<String, ArrayList<Integer>> sampleStream) {
        List<String> validationClasses = Arrays.asList("true", "false");
        validationClasses.forEach(tf -> {
            sampleStream.get(tf).forEach(v -> {
                if (tf.equals("true")) {
                    assertTrue(this.assignment_predicates.get(predicates_key).test(v));
                } else if (tf.equals("false")) {
                    assertFalse(this.assignment_predicates.get(predicates_key).test(v));
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
                    assertTrue(this.assignment_predicates.get(predicates_key).test(k));
                } else if (tf.equals("false")) {
                    assertFalse(this.assignment_predicates.get(predicates_key).test(k));
                }
            });
        });
    }

    @Test
    @Order(1)
    public void testAssignmentConstructor() {
        predicatesAssignmentConstructorInteger("constructor_id", this.assignmentStreamId);
        predicatesAssignmentConstructorDatetime("constructor_date_interval", this.assignmentStreamStartdate_Enddate);
        assertTrue(true);
    }

    private void testAddingAndRemoving(String filename) throws IOException, Exception {
        Validator<Tema> validator = new TemaValidator();
        TemaXMLRepository assignmentXMLRepository = new TemaXMLRepository(validator, filename);

        Tema assignment = new Tema(
                this.assignmentStreamId.get("true").get(0).toString(),
                "asdf",
                this.assignmentStreamStartdate_Enddate.get("true").get(this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get()),
                this.assignmentStreamStartdate_Enddate.get("true").keySet().stream().findFirst().get()
        );
        assignmentXMLRepository.save(assignment);
        assertTrue(assignmentXMLRepository.findOne(assignment.getID()).getID().equals(assignment.getID()));
        assignmentXMLRepository.delete(assignment.getID());
        assertFalse(assignmentXMLRepository.findAll().iterator().hasNext());
    }

    private void testAddingManyWithSameId(String filename) throws Exception {
        Validator<Tema> validator = new TemaValidator();
        TemaXMLRepository assignmentXMLRepository = new TemaXMLRepository(validator, filename);
        this.assignmentsStream.forEach(assignmentXMLRepository::save);
    }

    @Test
    @Order(2)
    public void testAssignmentRepository() throws IOException, Exception {
        String filename = this.generatedFileNames.get("testAssignmentRepository").get(0);
        FileWriter newFile = new FileWriter(filename);
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
