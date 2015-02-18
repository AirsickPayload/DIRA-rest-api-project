package pl.edu.uam.restapi.storage.database;

import com.google.common.collect.Lists;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.*;
import pl.edu.uam.restapi.storage.entity.*;
import pl.edu.uam.restapi.storage.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Alan Matuszczak on 19.12.14.
 */
public class MysqlDB implements StudentDatabase, TeacherDatabase, SchoolClassDatabase, StudentClassAssignmentDatabase, SubjectDatabase, TeacherClassSubjectAssignmentDatabase {


    private static final String HOST = "localhost";
    private static final int PORT = 8889;
    private static final String DATABASE = "restprojekt";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";

    private static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            String dbUrl = "jdbc:mysql://" + HOST + ':' + PORT + "/" + DATABASE;

            Map<String, String> properties = new HashMap<String, String>();

            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
            properties.put("hibernate.connection.url", dbUrl);
            properties.put("hibernate.connection.username", USER_NAME);
            properties.put("hibernate.connection.password", PASSWORD);
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
            properties.put("hibernate.hbm2ddl.auto", "update");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit", properties);
            entityManager = emf.createEntityManager();
        }

        return entityManager;
    }

    /*OVERRIDE METOD STUDENTA
    *
    *
    *
    * */

    @Override
    public Student getStudent(String pesel) {
        StudentEntity studentEntity = getEntityManager().find(StudentEntity.class, pesel);

        if(studentEntity != null){
            return buildStudentResponse(studentEntity);
        }
        return null;
    }

    @Override
    public Student createOrUpdateStudent(Student student) {
        StudentEntity studentEntityNew = buildStudentEntity(student);
        if(getEntityManager().find(StudentEntity.class, student.getPesel()) != null){
            databaseTransactionOperation(studentEntityNew,"update");
        } else {
            databaseTransactionOperation(studentEntityNew, "create");
        }

        return buildStudentResponse(studentEntityNew);
    }

    @Override
    public Student removeStudent(String pesel) {
        StudentEntity studentEntity = getEntityManager().find(StudentEntity.class, pesel);

        databaseTransactionOperation(studentEntity, "remove");
        return buildStudentResponse(studentEntity);
    }

    @Override
    public Collection<Student> getStudents() {
        Query query = getEntityManager().createNamedQuery("students.findAll");
        List<StudentEntity> entityList = query.getResultList();

        List<Student> list  = Collections.emptyList();

        if (entityList != null && !entityList.isEmpty()) {
            list = Lists.newArrayListWithCapacity(entityList.size());

            for (StudentEntity studentEntity : entityList) {
                list.add(buildStudentResponse(studentEntity));
            }
        }
        return list;
    }

    private Student buildStudentResponse(StudentEntity studentEntity){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return new Student(
                studentEntity.getPesel(),
                studentEntity.getName(),
                studentEntity.getLastname(),
                formatter.format(studentEntity.getBirthdate()),
                studentEntity.getCity()
        );
    }

    private StudentEntity buildStudentEntity(Student student){
        Date dbBirthdate = null;
        try {
            dbBirthdate = new SimpleDateFormat("yyyy-MM-dd").parse(student.getBirthdate());
        } catch (ParseException e) {
            throw new StudentException("Student with wrong date format", "Student posiada zly format daty", "http://docu.pl/errors/wrong-date-format");
        }

        return new StudentEntity(
                student.getPesel(),
                student.getName(),
                student.getLastname(),
                dbBirthdate,
                student.getCity()
        );
    }

    /*OVERRIDE METOD NAUCZYCIELA
    *
    *
    *
    * */
    private TeacherEntity updateTeacherEntity(String id, Teacher teacher){
        Date dbBirthdate = null;
        try {
            dbBirthdate = new SimpleDateFormat("yyyy-MM-dd").parse(teacher.getEmploymentdate());
        } catch (ParseException e) {
            throw new StudentException("Teacher with wrong date format", "Nauczyciel posiada zly format daty", "http://docu.pl/errors/wrong-date-format");
        }

        return new TeacherEntity(
                convertStringToLong(id),
                teacher.getName(),
                teacher.getLastname(),
                teacher.getQualifications(),
                dbBirthdate
        );
    }

     private TeacherEntity buildTeacherEntity(Teacher teacher){
        Date dbBirthdate = null;
        try {
            dbBirthdate = new SimpleDateFormat("yyyy-MM-dd").parse(teacher.getEmploymentdate());
        } catch (ParseException e) {
            throw new StudentException("Teacher with wrong date format", "Nauczyciel posiada zly format daty", "http://docu.pl/errors/wrong-date-format");
        }

        return new TeacherEntity(
                teacher.getName(),
                teacher.getLastname(),
                teacher.getQualifications(),
                dbBirthdate
        );
    }

    private Teacher buildTeacherResponse(TeacherEntity teacherEntity){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");

        return new Teacher(
                teacherEntity.getName(),
                teacherEntity.getId().toString(),
                teacherEntity.getLastname(),
                formatter.format(teacherEntity.getEmploymentdate()),
                teacherEntity.getQualifications()
        );
    }

    @Override
    public Teacher getTeacher(String id) {
        Long dbId = convertStringToLong(id);

        TeacherEntity dbTeacherEntity = getEntityManager().find(TeacherEntity.class, dbId);

        if(dbTeacherEntity != null){
            return buildTeacherResponse(dbTeacherEntity);
        }
        return null;
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        TeacherEntity teacherEntity = buildTeacherEntity(teacher);

        databaseTransactionOperation(teacherEntity,"create");

        return buildTeacherResponse(teacherEntity);
    }

    @Override
    public Teacher removeTeacher(String id) {
        Long dbId = convertStringToLong(id);

        TeacherEntity teacherEntity = getEntityManager().find(TeacherEntity.class,dbId);
        databaseTransactionOperation(teacherEntity, "remove");

        return buildTeacherResponse(teacherEntity);
    }

    @Override
    public Teacher updateTeacher(String id, Teacher teacher) {
        TeacherEntity teacherEntityNew = updateTeacherEntity(id, teacher);

        if(getEntityManager().find(TeacherEntity.class, teacherEntityNew.getId()) == null){
            throw new TeacherException("Teacher not found", "Taki nauczyciel nie istnieje", "http://docu.pl/errors/teacher-not-found");
        }

        databaseTransactionOperation(teacherEntityNew, "update");

        return buildTeacherResponse(teacherEntityNew);
    }

    @Override
    public Collection<Teacher> getTeachers() {
        Query query = getEntityManager().createNamedQuery("teachers.findAll");
        Collection<TeacherEntity> teacherEntityCollection = query.getResultList();
        Collection<Teacher> iterationCollection = Collections.emptyList();

        if(teacherEntityCollection != null && !teacherEntityCollection.isEmpty()){
            iterationCollection = Lists.newArrayListWithCapacity(teacherEntityCollection.size());

            for(TeacherEntity teacher : teacherEntityCollection){
                iterationCollection.add(buildTeacherResponse(teacher));
            }
        }
        return iterationCollection;
    }

   /*OVERRIDE METOD KLAS
    *
    *
    *
    * */


    private SchoolClassEntity buildNewClassEntity(SchoolClass newclass){
        return new SchoolClassEntity(
                newclass.getProfile(),
                newclass.getShortcut(),
                newclass.getYear()
        );
    }

    private SchoolClassEntity returnUpdatedClassEntity(String id, SchoolClass updatedclass){
        return new SchoolClassEntity(
                Long.valueOf(id),
                updatedclass.getProfile(),
                updatedclass.getShortcut(),
                updatedclass.getYear()
        );
    }

    private SchoolClass buildClassResponse(SchoolClassEntity classEntity){
        return new SchoolClass(
                classEntity.getId().toString(),
                classEntity.getProfile(),
                classEntity.getShortcut(),
                classEntity.getYear()
        );
    }

    @Override
    public SchoolClass getClass(String id) {
        SchoolClassEntity classEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(id));

        if(classEntity != null) {return buildClassResponse(classEntity);}

        return null;
    }

    @Override
    public SchoolClass createClass(SchoolClass rclass) {
        SchoolClassEntity classEntity = buildNewClassEntity(rclass);

        databaseTransactionOperation(classEntity,"create");
        return buildClassResponse(classEntity);
    }

    @Override
    public SchoolClass removeClass(String id) {
        SchoolClassEntity classEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(id));

        databaseTransactionOperation(classEntity,"remove");

        return buildClassResponse(classEntity);
    }

    @Override
    public SchoolClass updateClass(String id, SchoolClass rclass) {
        SchoolClassEntity updatedClassEntity = returnUpdatedClassEntity(id, rclass);

        if(getEntityManager().find(SchoolClassEntity.class, convertStringToLong(id)) == null){
            throw new SchoolClassException("Class not found", "Taka klasa nie istnieje", "http://docu.pl/errors/schoolclass-not-found");
        }

        databaseTransactionOperation(updatedClassEntity,"update");

        return buildClassResponse(updatedClassEntity);
    }

    @Override
    public Collection<SchoolClass> getClasses() {
        Query query = getEntityManager().createNamedQuery("classes.findAll");

        Collection<SchoolClassEntity> classEntityCollection = query.getResultList();
        Collection<SchoolClass> iterationCollection = Collections.emptyList();

        if(classEntityCollection != null && !classEntityCollection.isEmpty()){
            iterationCollection = Lists.newArrayListWithCapacity(classEntityCollection.size());

            for(SchoolClassEntity classr : classEntityCollection){
                iterationCollection.add(buildClassResponse(classr));
            }
        }
        return iterationCollection;
    }

    /*OVERRIDE METOD STUDENTCLASSASSIGNMENT
    *
    *
    *
    * */

    @Override
    public Collection<StudentClassAssignment> getSCAssignments() {
        Query query = getEntityManager().createNamedQuery("studentclassassignment.findAll");

        Collection<StudentClassAssignmentEntity> assignmentsCollection = query.getResultList();
        Collection<StudentClassAssignment> iterationCollection = Collections.emptyList();

        if(assignmentsCollection != null && !assignmentsCollection.isEmpty()){
            iterationCollection = Lists.newArrayListWithCapacity(assignmentsCollection.size());

            for(StudentClassAssignmentEntity studentClassAssignmentEntity : assignmentsCollection){
                iterationCollection.add(buildSCAssignmentResponse(studentClassAssignmentEntity));
            }
        }

        return iterationCollection;
    }

    @Override
    public Collection<StudentClassAssignment> searchSCAssignments(String pesel, String classid) {
        Query query = null;
        StudentEntity studentEntity;
        SchoolClassEntity schoolClassEntity;

        if(pesel != null && !pesel.equals("undefined")){
            studentEntity = getEntityManager().find(StudentEntity.class, pesel);
            if(studentEntity != null){
                query = getEntityManager().createNamedQuery("studentclassassignment.findStudent");
                query.setParameter("pesel", studentEntity);
            }
        }

        if(classid != null && !classid.equals("undefined")){
            schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(classid));
            if(schoolClassEntity != null){
                query = getEntityManager().createNamedQuery("studentclassassignment.findClass");
                query.setParameter("classid", schoolClassEntity);
            }
        }

        if(pesel != null && !pesel.equals("undefined") && classid != null && !classid.equals("undefined")){
            studentEntity = getEntityManager().find(StudentEntity.class, pesel);
            schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(classid));
            if(studentEntity != null && schoolClassEntity != null){
                query = getEntityManager().createNamedQuery("studentclassassignment.findSC");
                query.setParameter("pesel", studentEntity);
                query.setParameter("classid", schoolClassEntity);
            }
        }

        if(query != null){
            Collection<StudentClassAssignmentEntity> assignmentsCollection = query.getResultList();
            Collection<StudentClassAssignment> iterationCollection = Collections.emptyList();

            if( assignmentsCollection != null && !assignmentsCollection.isEmpty()){
                iterationCollection = Lists.newArrayListWithCapacity(assignmentsCollection.size());

                for (StudentClassAssignmentEntity  studentClassAssignmentEntity : assignmentsCollection){
                    iterationCollection.add(buildSCAssignmentResponse(studentClassAssignmentEntity));
                }
            }
            return iterationCollection;
        }

        return null;
    }

    @Override
    public StudentClassAssignment createSCAssignment(StudentClassAssignment studentClassAssignment) {
        StudentClassAssignmentEntity studentClassAssignmentEntity = buildSCAssignmentEntity(studentClassAssignment);

        databaseTransactionOperation(studentClassAssignmentEntity,"create");

        return buildSCAssignmentResponse(studentClassAssignmentEntity);
    }

    @Override
    public StudentClassAssignment getSCAssignment(String id) {
        StudentClassAssignmentEntity studentClassAssignmentEntity = getEntityManager().find(StudentClassAssignmentEntity.class, convertStringToLong(id));

        if(studentClassAssignmentEntity != null){
            return buildSCAssignmentResponse(studentClassAssignmentEntity);
        }
        return null;
    }

    @Override
    public StudentClassAssignment removeSCAssignment(String id) {
        StudentClassAssignmentEntity studentClassAssignmentEntity = getEntityManager().find(StudentClassAssignmentEntity.class,convertStringToLong(id));

        databaseTransactionOperation(studentClassAssignmentEntity, "remove");

        return buildSCAssignmentResponse(studentClassAssignmentEntity);
    }

    @Override
    public StudentClassAssignment updateSCAssignment(String id, StudentClassAssignment studentClassAssignment) {
        StudentClassAssignmentEntity updated = returnUpdatedSCAssignmentEntity(id, studentClassAssignment);
        databaseTransactionOperation(updated, "update");
        return buildSCAssignmentResponse(updated);
    }

    private StudentClassAssignment buildSCAssignmentResponse(StudentClassAssignmentEntity studentClassAssignmentEntity){
        return new StudentClassAssignment(
                studentClassAssignmentEntity.getId().toString(),
                studentClassAssignmentEntity.getStudentEntity().getPesel(),
                studentClassAssignmentEntity.getSchoolClassEntity().getId().toString()
        );
    }

    private StudentClassAssignmentEntity buildSCAssignmentEntity(StudentClassAssignment studentClassAssignment){
        StudentEntity studentEntity = getEntityManager().find(StudentEntity.class,studentClassAssignment.getPesel());
        SchoolClassEntity schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(studentClassAssignment.getClassid()));

        if(studentEntity == null){
            throw new StudentClassAssignmentException("Student with supplied PESEL does not exist", "Uczen z podanym nr. PESEL nie istnieje", "http://docu.pl/error/sca-nonexistent-student");
        }
        if(schoolClassEntity == null){
            throw new StudentClassAssignmentException("Class with supplied id does not exist", "Klasa z podaneym nr. ID nie istnieje!", "http://docu.pl/errors/sca-nonexistent-schoolclass");
        }

        return new StudentClassAssignmentEntity(
                convertStringToLong(studentClassAssignment.getId()),
                studentEntity,
                schoolClassEntity
        );
    }

    private StudentClassAssignmentEntity returnUpdatedSCAssignmentEntity(String id, StudentClassAssignment studentClassAssignment){
        StudentClassAssignmentEntity scaEntity = getEntityManager().find(StudentClassAssignmentEntity.class, convertStringToLong(id));

        if(scaEntity == null){
            throw new StudentClassAssignmentException("Student-Class Assignment with supplied id does not exist", "Przypisanie Uczen-Klasa z podanym nr. ID nie istnieje!", "http://docu.pl/errors/sca-put-does-not-exist");
        }

        StudentEntity studentEntity = getEntityManager().find(StudentEntity.class,studentClassAssignment.getPesel());
        SchoolClassEntity schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(studentClassAssignment.getClassid()));

        if(studentEntity == null){
            throw new StudentClassAssignmentException("Student with supplied PESEL does not exist", "Uczen z podanym nr. PESEL nie istnieje", "http://docu.pl/error/sca-nonexistent-student");
        }
        if(schoolClassEntity == null){
            throw new StudentClassAssignmentException("Class with supplied id does not exist", "Klasa z podaneym nr. ID nie istnieje!", "http://docu.pl/errors/sca-nonexistent-schoolclass");
        }

        return new StudentClassAssignmentEntity(
                convertStringToLong(id),
                studentEntity,
                schoolClassEntity
        );

    }

    @Override
    public Subject getSubject(String id) {
        SubjectEntity dbSubjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(id));

        if(dbSubjectEntity != null){
            return buildSubjectResponse(dbSubjectEntity);
        }
        return null;
    }

    @Override
    public Subject createSubject(Subject subject) {
        SubjectEntity subjectEntity = buildSubjectEntity(subject);
        databaseTransactionOperation(subjectEntity, "create");
        return buildSubjectResponse(subjectEntity);
    }

    @Override
    public Subject updateSubject(String id, Subject subject) {
        SubjectEntity updatedSubjectEntity = returnUpdatedSubjectEntity(id, subject);

        if(getEntityManager().find(SubjectEntity.class, convertStringToLong(id)) == null){
            throw new SubjectException("Subject not found", "Taki przedmiot nie istnieje", "http://docu.pl/errors/subject-not-found");
        }

        databaseTransactionOperation(updatedSubjectEntity,"update");

        return buildSubjectResponse(updatedSubjectEntity);
    }

    @Override
    public Subject removeSubject(String id) {
        SubjectEntity dbSubjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(id));
        databaseTransactionOperation(dbSubjectEntity,"remove");
        return buildSubjectResponse(dbSubjectEntity);
    }

    @Override
    public Collection<Subject> getSubjects() {
        Query query = getEntityManager().createNamedQuery("subjects.findAll");

        Collection<SubjectEntity> subjectEntityCollection = query.getResultList();
        Collection<Subject> iterationCollection = Collections.emptyList();

        if(subjectEntityCollection != null && !subjectEntityCollection.isEmpty()){
            iterationCollection = Lists.newArrayListWithCapacity(subjectEntityCollection.size());

            for(SubjectEntity sb : subjectEntityCollection){
                iterationCollection.add(buildSubjectResponse(sb));
            }
        }
        return iterationCollection;
    }

    private Subject buildSubjectResponse(SubjectEntity subjectEntity){
        return new Subject(
                subjectEntity.getId().toString(),
                subjectEntity.getName(),
                subjectEntity.getShortname()
        );
    }

    private SubjectEntity buildSubjectEntity(Subject subject){
        return new SubjectEntity(
                convertStringToLong(subject.getId()),
                subject.getName(),
                subject.getShortname()
        );
    }

    private SubjectEntity returnUpdatedSubjectEntity(String id, Subject updatedsubject){
        return new SubjectEntity(
                convertStringToLong(id),
                updatedsubject.getName(),
                updatedsubject.getShortname()
        );
    }


    @Override
    public Collection<TCSAssignment> getTCSAssignments() {
        Query query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findAll");

        Collection<TCSAssignmentEntity> assignmentsCollection = query.getResultList();
        Collection<TCSAssignment> iterationCollection = Collections.emptyList();

        if( assignmentsCollection != null && !assignmentsCollection.isEmpty()){
            iterationCollection = Lists.newArrayListWithCapacity(assignmentsCollection.size());

            for ( TCSAssignmentEntity tcsAssignmentEntity : assignmentsCollection){
                iterationCollection.add(buildTCSAssingmentResponse(tcsAssignmentEntity));
            }
        }
        return iterationCollection;
    }

    @Override
    public Collection<TCSAssignment> searchTCSAssignments(String teacherid, String classid, String subjectid) {

        Query query = null;
        TeacherEntity teacherEntity;
        SchoolClassEntity schoolClassEntity;
        SubjectEntity subjectEntity;


        if(teacherid != null && !teacherid.equals("undefined")){
            teacherEntity = getEntityManager().find(TeacherEntity.class, convertStringToLong(teacherid));
            if(teacherEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findTeacher");
                query.setParameter("teacherid", teacherEntity);
            }
        }

        if(classid != null && !classid.equals("undefined")){
            schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(classid));
            if(schoolClassEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findClass");
                query.setParameter("classid", schoolClassEntity);
            }
        }

        if(subjectid != null && !subjectid.equals("undefined")){
            subjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(subjectid));
            if(subjectEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findSubject");
                query.setParameter("subjectid", subjectEntity);
            }
        }


        if(teacherid != null && !teacherid.equals("undefined") && classid != null && !classid.equals("undefined")){
            teacherEntity = getEntityManager().find(TeacherEntity.class, convertStringToLong(teacherid));
            schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(classid));

            if(teacherEntity != null && schoolClassEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findTeacherAndClass");
                query.setParameter("teacherid", teacherEntity);
                query.setParameter("classid", schoolClassEntity);
            }
        }

        if(teacherid != null && !teacherid.equals("undefined") && subjectid != null && !subjectid.equals("undefined")){
            teacherEntity = getEntityManager().find(TeacherEntity.class, convertStringToLong(teacherid));
            subjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(subjectid));

            if(teacherEntity != null && subjectEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findTeacherAndSubject");
                query.setParameter("teacherid", teacherEntity);
                query.setParameter("subjectid", subjectEntity);
            }
        }


        if(classid != null && !classid.equals("undefined") && subjectid != null && !subjectid.equals("undefined")){
            schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(classid));
            subjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(subjectid));

            if(schoolClassEntity != null && subjectEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findClassAndSubject");
                query.setParameter("classid", schoolClassEntity);
                query.setParameter("subjectid", subjectEntity);
            }
        }

        if(teacherid != null && !teacherid.equals("undefined") && classid != null && !classid.equals("undefined") && subjectid != null && !subjectid.equals("undefined")){
            teacherEntity = getEntityManager().find(TeacherEntity.class, convertStringToLong(teacherid));
            schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(classid));
            subjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(subjectid));

            if(teacherEntity != null && schoolClassEntity != null && subjectEntity != null){
                query = getEntityManager().createNamedQuery("teacherclasssubjectassignment.findTCS");
                query.setParameter("teacherid", teacherEntity);
                query.setParameter("classid", schoolClassEntity);
                query.setParameter("subjectid", subjectEntity);
            }
        }

        if(query != null){
            Collection<TCSAssignmentEntity> assignmentsCollection = query.getResultList();
            Collection<TCSAssignment> iterationCollection = Collections.emptyList();

            if( assignmentsCollection != null && !assignmentsCollection.isEmpty()){
                iterationCollection = Lists.newArrayListWithCapacity(assignmentsCollection.size());

                for ( TCSAssignmentEntity tcsAssignmentEntity : assignmentsCollection){
                    iterationCollection.add(buildTCSAssingmentResponse(tcsAssignmentEntity));
                }
            }
            return iterationCollection;
        }

        return null;
    }

    @Override
    public TCSAssignment createTCSAssignment(TCSAssignment tcsAssignment) {
        TCSAssignmentEntity tcsAssignmentEntity = buildTCSAssignmentEntity(tcsAssignment);
        databaseTransactionOperation(tcsAssignmentEntity, "create");
        return buildTCSAssingmentResponse(tcsAssignmentEntity);
    }

    @Override
    public TCSAssignment removeTCSAssignment(String id) {
        TCSAssignmentEntity tcsAssignmentEntity = getEntityManager().find(TCSAssignmentEntity.class, convertStringToLong(id));
        databaseTransactionOperation(tcsAssignmentEntity, "remove");
        return buildTCSAssingmentResponse(tcsAssignmentEntity);
    }

    @Override
    public TCSAssignment getTCSAssignment(String id) {
        TCSAssignmentEntity tcsAssignmentEntity = getEntityManager().find(TCSAssignmentEntity.class, convertStringToLong(id));

        if(tcsAssignmentEntity != null ){
            return buildTCSAssingmentResponse(tcsAssignmentEntity);
        }

        return null;
    }

    @Override
    public TCSAssignment updateTCSAssignment(String id, TCSAssignment tcsAssignment) {
        TCSAssignmentEntity updated = returnUpdatedTCSAssignmentEntity(id, tcsAssignment);
        databaseTransactionOperation(updated, "update");
        return buildTCSAssingmentResponse(updated);
    }

    private TCSAssignment buildTCSAssingmentResponse(TCSAssignmentEntity tcsAssignmentEntity){
        return new TCSAssignment(
                tcsAssignmentEntity.getId().toString(),
                tcsAssignmentEntity.getTeacherEntity().getId().toString(),
                tcsAssignmentEntity.getSchoolClassEntity().getId().toString(),
                tcsAssignmentEntity.getSubjectEntity().getId().toString()
        );
    }

    private TCSAssignmentEntity buildTCSAssignmentEntity(TCSAssignment tcsAssignment){
        TeacherEntity teacherEntity = getEntityManager().find(TeacherEntity.class, convertStringToLong(tcsAssignment.getTeacherid()));
        SchoolClassEntity schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(tcsAssignment.getClassid()));
        SubjectEntity subjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(tcsAssignment.getSubjectid()));

        if(teacherEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Teacher with supplied ID does not exist",
                    "[Przyporządkowanie Nauczyciel - Klasa - Przedmiot] Nauczyciel z podanym nr. ID nie istnieje!", "http://docu.pl/errors/tcsa-nonexistent-teacher");
        }
        if(schoolClassEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Class with supplied id does not exist",
                    "[Przyporządkowanie Nauczyciel - Klasa - Przedmiot] Klasa z podana nr. ID nie istnieje!", "http://docu.pl/errors/sca-nonexistent-schoolclass");
        }
        if(subjectEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Subject with supplied id does not exist",
                    "[Przyporządkowanie Nauczyciel - Klasa - Przedmiot] Przedmiot z podanym nr. ID nie istnieje!", "http://docu.pl/errors/sca-nonexistent-schoolclass");
        }


        return new TCSAssignmentEntity(
                convertStringToLong(tcsAssignment.getId()),
                teacherEntity,
                schoolClassEntity,
                subjectEntity
        );
    }

    private TCSAssignmentEntity returnUpdatedTCSAssignmentEntity(String id, TCSAssignment tcsAssignment){
        TCSAssignmentEntity tcsaEntity = getEntityManager().find(TCSAssignmentEntity.class, convertStringToLong(id));

        if(tcsaEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Assignment with supplied ID does not exist",
                    "[Przyporzadkowanie Nauczyciel - Klasa - Przedmiot] Przyporządkowanie z zadanym nr. ID nie istnieje!", "http://docu.pl/errors/tcsa-nonexistent-assignment");
        }

        TeacherEntity teacherEntity = getEntityManager().find(TeacherEntity.class, convertStringToLong(tcsAssignment.getId()));
        SchoolClassEntity schoolClassEntity = getEntityManager().find(SchoolClassEntity.class, convertStringToLong(tcsAssignment.getId()));
        SubjectEntity subjectEntity = getEntityManager().find(SubjectEntity.class, convertStringToLong(tcsAssignment.getId()));

        if(teacherEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Teacher with supplied ID does not exist",
                    "[Przyporządkowanie Nauczyciel - Klasa - Przedmiot] Nauczyciel z podanym nr. ID nie istnieje!", "http://docu.pl/errors/tcsa-nonexistent-teacher");
        }
        if(schoolClassEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Class with supplied id does not exist",
                    "[Przyporządkowanie Nauczyciel - Klasa - Przedmiot] Klasa z podana nr. ID nie istnieje!", "http://docu.pl/errors/sca-nonexistent-schoolclass");
        }
        if(subjectEntity == null){
            throw new TCSAssignmentException("[Teacher - Class - Subject Assignment] Subject with supplied id does not exist",
                    "[Przyporządkowanie Nauczyciel - Klasa - Przedmiot] Przedmiot z podanym nr. ID nie istnieje!", "http://docu.pl/errors/sca-nonexistent-schoolclass");
        }


        return new TCSAssignmentEntity(
                tcsaEntity.getId(),
                teacherEntity,
                schoolClassEntity,
                subjectEntity
        );
    }

    private void databaseTransactionOperation(Object o, String type){
        try{
            getEntityManager().getTransaction().begin();
            if(type.equals("create")){
                getEntityManager().persist(o);
            } else if(type.equals("remove")){
                getEntityManager().remove(o);
            } else if(type.equals("update")){
                getEntityManager().merge(o);
            }
            getEntityManager().getTransaction().commit();
        } finally{
            if(getEntityManager().getTransaction().isActive()){
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    private Long convertStringToLong(String sid){
        Long id;
        try{
            id = Long.valueOf(sid);
        } catch(NumberFormatException e){
            return null;
        }
        return id;
    }


}
