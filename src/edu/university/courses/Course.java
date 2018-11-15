package edu.university.courses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.university.professors.Professor;
import edu.university.students.Student;

/**
 * Course class to add register students, print list of students registered, set
 * coursework components and more.
 */
public class Course implements Serializable {

	private static final long serialVersionUID = 4324846353865727203L;
	private String courseName;
	private Professor courseCoordinator;
	private ArrayList<CourseComponent> courseComponents = new ArrayList<CourseComponent>();
	private HashMap<String, Integer> assessmentComponents = new HashMap<>();
	private HashMap<String, Integer> courseworkComponents = new HashMap<>();

	/**
	 * Contructor to create Course object based on parameters given. Creates
	 * lectures for the course as all course should have lectures.
	 * 
	 * @param courseName
	 * @param courseCoordinator
	 * @param lectureVacancies
	 */
	public Course(String courseName, Professor courseCoordinator, int lectureVacancies) {
		// Since all courses have lectures.
		// Before constructing the Course object, ask whether there is tutorials or labs
		// and if there is, how many tutorials/labs groups per course.
		this.courseName = courseName;
		this.courseCoordinator = courseCoordinator;

		CourseComponent lecture = new Lecture(lectureVacancies);
		courseComponents.add(lecture);
	}

	/**
	 * Constructor to create Course object based on parameters given. Creates
	 * tutorials for the course if they exist.
	 * 
	 * @param courseName
	 * @param courseCoordinator
	 * @param lectureVacancies
	 * @param numberOfTutorials
	 * @param slotsPerTutGroup
	 */
	public Course(String courseName, Professor courseCoordinator, int lectureVacancies, int numberOfTutorialGroups,
			int slotsPerTutGroup) {
		this(courseName, courseCoordinator, lectureVacancies);

		CourseComponent tutorial = new Tutorial(numberOfTutorialGroups, slotsPerTutGroup);
		courseComponents.add(tutorial);

	}

	/**
	 * Constructor to create Course object based on parameters given. Creates labs
	 * for the course if they exist.
	 * 
	 * @param courseName
	 * @param courseCoordinator
	 * @param lectureVacancies
	 * @param numberOfTutorials
	 * @param slotsPerTutGroup
	 * @param numberOfLabGroups
	 * @param slotsPerLabGroup
	 */
	public Course(String courseName, Professor courseCoordinator, int lectureVacancies, int numberOfTutorialGroups,
			int slotsPerTutGroup, int numberOfLabGroups, int slotsPerLabGroup) {
		this(courseName, courseCoordinator, lectureVacancies, numberOfTutorialGroups, slotsPerTutGroup);

		CourseComponent lab = new Laboratory(numberOfLabGroups, slotsPerLabGroup);
		courseComponents.add(lab);
	}

	public void registerStudent(Student s) {
		this.courseComponents.get(0).registerStudent(s, 1); // The second argument can be any number since it is not
															// used.
	}

	public void registerStudent(Student s, int tutGroup) {
		this.registerStudent(s);
		this.courseComponents.get(1).registerStudent(s, tutGroup);
	}

	public void registerStudent(Student s, int tutGroup, int labGroup) {
		this.registerStudent(s, tutGroup);
		this.courseComponents.get(2).registerStudent(s, labGroup);
	}

	public void printStudents(int option) {
		// index = 0 : print by lecture, index = 1 : print by tutorial, index = 2: print
		// by lab
		this.courseComponents.get(option).printStudentList();

	}

	public boolean haveVacancies() {
		boolean bn = true;
		for (CourseComponent cc : this.courseComponents) {
			bn &= cc.haveVacancies();
		}
		return bn;
	}

	public boolean hasWeightageInfo() {
		return !this.assessmentComponents.isEmpty();
	}

	public boolean hasCourseworkSubcomponents() {
		return !this.courseworkComponents.isEmpty();
	}

	public HashMap<String, Integer> getAssessmentComponents() {
		return this.assessmentComponents;
	}

	public void setAssessmentComponents(HashMap<String, Integer> assessmentComponents) {
		this.assessmentComponents = assessmentComponents;
	}

	/**
	 * This method is used to get the weigtage of coursework subcomponents.
	 * @return a hashmap with key being the name of each coursework subcomponnet, value being their correspinding weightage (upon 100).
	 */
	public HashMap<String, Integer> getCourseworkComponents() {
		return this.courseworkComponents;
	}

	/**
	 * This method is used to set the weightage of subcomponents under the
	 * assessment component coursework.
	 * 
	 * @param courseworkComponents A hashmap with key being the name of each
	 *                             coursework subcomponent, value being their
	 *                             corresponding weightage (upon 100).
	 */
	public void setCourseworkSubcomponents(HashMap<String, Integer> courseworkComponents) {
		this.courseworkComponents = courseworkComponents;
	}

	/**
	 * This method is used to print the formatted head of table for ease of reading
	 * the output.
	 */
	public static void printCourseSlotsHead() {
		System.out.println("+-------------+-----------------+-----------+");
		System.out.println("| Course Name |      Group      | Vacancies |");
		System.out.println("|             |                 | /Slots    |");
		System.out.println("+-------------+-----------------+-----------+");
	}

	/**
	 * This method is used to print the slots available for a course.
	 * 
	 * Depending on whether a course has lecture, tutorial or lab, the method will
	 * print the number of available slots left for every group.
	 */
	public void printCourseSlots() {
		System.out.printf("| %-11s | Lecture         | %4d/%-4d |\n", this.getCourseName(),
				this.getCourseComponents().get(0).getListOfGroups().get(0).getNumberOfVacancies(),
				this.getCourseComponents().get(0).getListOfGroups().get(0).getNumberOfSlots());
		if (this.getCourseComponents().size() == 1) {
			System.out.println("+-------------+-----------------+-----------+");
			return;
		}
		System.out.println("|             +-----------------+-----------+");

		if (this.getCourseComponents().size() >= 2) {
			// This means that the course has more than or equal to 2 components (i.e. lect
			// and tut or lect, tut and lab)
			int i = 1;
			for (Group group : this.getCourseComponents().get(1).getListOfGroups()) {
				System.out.printf("|             | Tutorial Grp %-2d | %4d/%-4d |\n", i, group.getNumberOfVacancies(),
						group.getNumberOfSlots());
				i++;
			}
			if (this.getCourseComponents().size() == 2) {
				System.out.println("+-------------+-----------------+-----------+");
				return;
			}
		}

		if (this.getCourseComponents().size() == 3) {
			// This means that the course has all 3 components
			System.out.println("|             +-----------------+-----------+");
			int i = 1;
			for (Group group : this.getCourseComponents().get(2).getListOfGroups()) {
				System.out.printf("|             | Lab Grp %-2d      | %4d/%-4d |\n", i, group.getNumberOfVacancies(),
						group.getNumberOfSlots());
				i++;
			}
			System.out.println("+-------------+-----------------+-----------+");
		}
	}

	/**
	 * This method is used to get an ArrayList of all students registered for a
	 * course.
	 * 
	 * @return an ArrayList of all students registered for a course.
	 */
	public ArrayList<Student> getRegisteredStudents() {
		ArrayList<Student> registeredStudents = new ArrayList<>();
		for (int i = 0; i < (this.courseComponents.get(0).getListOfGroups().get(0).getNumberOfSlots()
				- this.courseComponents.get(0).getListOfGroups().get(0).getNumberOfVacancies()); i++) {
			registeredStudents
					.add(this.courseComponents.get(0).getListOfGroups().get(0).getRegisteredStudents().get(i));
		}
		return registeredStudents;
	}

	/**
	 * This method is used to get the weightage of the examination
	 * 
	 * @return the weightage of the examination.
	 */
	public int getExamWeightage() {
		return this.assessmentComponents.get("Examination");
	}

	/**
	 * This method is used to get the weightage of the coursework
	 * 
	 * @return the weight of the coursework.
	 */
	public int getCourseworkWeightage() {
		return this.assessmentComponents.get("Coursework");
	}

	/**
	 * This method is used to get the name of the course.
	 * 
	 * @return the name of the course.
	 */
	public String getCourseName() {
		return this.courseName;
	}

	/**
	 * This method is used to access the name of the course coordinator.
	 * 
	 * @return the name of the course coordinator.
	 */
	public String getCourseCoordinatorName() {
		return this.courseCoordinator.getName();
	}

	/**
	 * The getter method to access the list of course components.
	 * 
	 * @return the list of course components.
	 */
	public ArrayList<CourseComponent> getCourseComponents() {
		return this.courseComponents;
	}
}
