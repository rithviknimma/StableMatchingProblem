/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    private int getPreferenceIndex(ArrayList<Integer> preferenceList, int entity) {
        int index = -1;
        for (int i = 0; i < preferenceList.size(); i++) {
            if (preferenceList.get(i) == entity) {
                index = i;
                break;
            }
        }
        return index;
    }
    /**
     * Compute the preference lists for each internship, given weights and student metrics.
     * Return a ArrayList<ArrayList<Integer>> prefs, where prefs.get(i) is the ordered list of preferred students for
     * internship i, with length studentCount.
     */
    public static ArrayList<ArrayList<Integer>> computeInternshipPreferences(int internshipCount, int studentCount,
                                                                      ArrayList<ArrayList<Integer>>internship_weights,
                                                                      ArrayList<Double> student_GPA,
                                                                      ArrayList<Integer> student_months,
                                                                      ArrayList<Integer> student_projects){
        ArrayList<ArrayList<Integer>> internshipPref = new ArrayList<>(internshipCount);
        ArrayList<ArrayList<Integer>> studentScores = new ArrayList<>(internshipCount); // array to store studentScores for each internship
        int sScore; // student score
        
        
        // makes studentScores for each internship
        for(int i = 0; i < internshipCount; i++) {
        	studentScores.add(new ArrayList<Integer>()); // add arraylist to each internship to keep track of scores
        	for(int j = 0; j < studentCount; j++) {
        		sScore = (int) Math.round(computeInternshipStudentScore(student_GPA.get(j), student_months.get(j), student_projects.get(j),
        											   					internship_weights.get(i).get(0), internship_weights.get(i).get(1), 
        											   					internship_weights.get(i).get(2)));
        		studentScores.get(i).add(sScore);
            }
        }

        int maxScore;
        int studentNumber; // the student who has the max score
        
        // fill out internship Pref list
        for(int i = 0; i < internshipCount; i++) {
        	maxScore = Collections.max(studentScores.get(i)); // get max score of students for an internship
        	internshipPref.add(new ArrayList<Integer>()); // add arraylist to each internship to keep track of preferences
        	while(maxScore != -1) {
        		studentNumber = studentScores.get(i).indexOf(maxScore);
        		internshipPref.get(i).add(studentNumber);
        		studentScores.get(i).set(studentNumber, -1); // set student score to -1 after account for, for an internship
        		maxScore = Collections.max(studentScores.get(i));
        	}
        }
        
        return internshipPref;
    }
    
    private static Double computeInternshipStudentScore(double studentGPA, int studentExp, int studentProjects, int
                                                        weightGPA, int weightExp, int weightProjects){
        return studentGPA*weightGPA+studentExp*weightExp+studentProjects*weightProjects;
    }

    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
    	// return false if not all internships are filled up
    	if(matchedStudents(marriage.getStudentMatching()) < marriage.totalInternshipSlots()) {
    		return false;
    	}
    	
    	int internship;
    	int internshipPrime;
    	ArrayList<ArrayList<Integer>> studentPref = marriage.getStudentPreference();
    	ArrayList<ArrayList<Integer>> internshipPref = marriage.getInternshipPreference();
    	
    	// check for instabilities in matching
    	for(int student = 0; student < marriage.getStudentCount(); student++) {
    		for(int studentPrime = student + 1; studentPrime < marriage.getStudentCount(); studentPrime++) {
    			internship = marriage.getStudentMatching().get(student);
    			internshipPrime = marriage.getStudentMatching().get(studentPrime);
    			
    			if(internship != -1 && internshipPrime == -1) { // if studentPrime isn't matched but student is matched
    				// if internship prefers studentPrime more, instability found
    				if(internshipPref.get(internship).indexOf(studentPrime) < internshipPref.get(internship).indexOf(student)) {
    					return false;
    				}
    			}
    			else if(internship == -1 && internshipPrime != -1) { // if student isn't matched but student Prime is matched
    				// if internship prefers studentPrime more, instability found
    				if(internshipPref.get(internshipPrime).indexOf(student) < internshipPref.get(internshipPrime).indexOf(studentPrime)) {
    					return false;
    				}
    			}
    			else if(internship != -1) { // if both students are matched
    				
    				// if internship prefers studentprime over student and studentprime prefers internship over internshipPrime, found instability
    				if(internshipPref.get(internship).indexOf(studentPrime) < internshipPref.get(internship).indexOf(student)
    				   && studentPref.get(studentPrime).indexOf(internship) < studentPref.get(studentPrime).indexOf(internshipPrime)) {
    					return false;
    				}
    				// if intenrshipPrime prefers student over studentPrime and student prefers internshipPrime over internship, found instability
    				if(internshipPref.get(internshipPrime).indexOf(student) < internshipPref.get(internshipPrime).indexOf(studentPrime)
    				   && studentPref.get(student).indexOf(internshipPrime) < studentPref.get(student).indexOf(internship)) {
    					return false;
    				}
    				
    			}
    		}
    	}
        return true;
    }
    
    // finds number of matched students for a given matching
    public int matchedStudents(ArrayList<Integer> studentMatching) {
    	int numOfMatched = 0;
    	
    	for(int i = 0; i < studentMatching.size(); i++) {
    		if(studentMatching.get(i) != -1) {
    			numOfMatched++;
    		}
    	}
    	return numOfMatched;
    }


    /**
     * Determines a solution to the Stable Marriage problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_studentoptimal(Matching marriage) {
    	ArrayList<Integer> internshipToPropose = new ArrayList<Integer>(); // keeps track of all students number of proposal, index of this corresponds to student
    	Queue<Integer> students = new LinkedList<>(); // queue of students trying to propose
    	ArrayList<ArrayList<Integer>> internshipMatching = new ArrayList<ArrayList<Integer>>();
    	ArrayList<Integer> newStudentMatching = new ArrayList<Integer>();
    	
    	// initializing student proposals to zero
    	// adding students to queue
    	// initializing studentMatching to all students unmatched
    	for(int i = 0; i < marriage.getStudentCount(); i++){
    		internshipToPropose.add(0);
    		students.add(i);
    		newStudentMatching.add(-1);
    	}
    	
    	// initializing internshipMatching to all internships unmatched
    	for(int i = 0; i < marriage.getInternshipCount(); i++) {
    		internshipMatching.add(new ArrayList<Integer>());
    		for(int j = 0; j < marriage.getInternshipSlots().get(i); j++) {
    			internshipMatching.get(i).add(-1);
    		}
    	}
    	
    	int numberOfProposals = 0;
    	int totalNumberOfProposals = marriage.getStudentCount() * marriage.getInternshipCount();
    	
    	int internship; // internship being proposed to from student's preference list
    	int student; // student number
    	int sScore; // internship score for student
    	int proposal; // student's proposal number
    	int freeInternshipSlot; 
    	
    	int internshipPreference;
    	
    	// internships scores for each student
    	ArrayList<ArrayList<Integer>> studentScores = new ArrayList<ArrayList<Integer>>();
    	studentScores = computeStudentScores(marriage.getInternshipCount(), marriage.getStudentCount(),
    																		marriage.getInternshipWeights(), marriage.getStudentGPA(), 
    																		marriage.getStudentMonths(), marriage.getStudentProjects());
    	
    	while(numberOfProposals < totalNumberOfProposals && students.isEmpty() != true){ // while there exists student who hasn't proposed to internships
    		student = students.peek(); // get student in queue
    		proposal = internshipToPropose.get(student); // get student's number of proposals
    		
    		// reset what internship to propose to for student if student gets rejected by all
    		if(proposal == marriage.getInternshipCount()) {
    			internshipToPropose.set(student, 0); 
    			proposal = 0;
    		}
    		internship = marriage.getStudentPreference().get(student).get(proposal); // iterate through student's preferred internship
    		
    		if(internshipMatching.get(internship).contains(-1)) { // if internship is free (slots open)
    			newStudentMatching.set(student, internship); // matched student with internship
    			freeInternshipSlot = internshipMatching.get(internship).indexOf(-1); // get index of free internship slot
    			internshipMatching.get(internship).set(freeInternshipSlot, student); // matched internship with student
    			internshipToPropose.set(student, proposal + 1); // increments number of proposals made by student
    			numberOfProposals++; // also increments number of proposals for the while loop
    			students.remove(); // removes student from queue
    		}
    		else {
    			sScore = studentScores.get(internship).get(student); // gets score of student for internship
    			internshipPreference = internshipPrefersStudent(sScore, internshipMatching.get(internship), studentScores.get(internship));
    			
    			if(internshipPreference != -1) { // if internship prefers new student over current
    				newStudentMatching.set(student, internship); // internship matches with new student
    				newStudentMatching.set(internshipPreference, -1);
    				internshipMatching.get(internship).set(internshipMatching.get(internship).indexOf(internshipPreference), student); // replaces weakest student
    				internshipToPropose.set(student, proposal + 1); // increments number of proposals made by student
    				numberOfProposals++; // also increments number of proposals for the while loop
    				students.remove(); // removes newly matched student from queue
    				students.add(internshipPreference); // adds current student back onto queue as he is unmatched
    			}
    			else {
    				internshipToPropose.set(student, proposal + 1); // increments number of proposals made by student
    				numberOfProposals++; // also increments number of proposals for the while loop
    			}
    		}
    	}
    	
    	marriage.setStudentMatching(newStudentMatching);
    	
        return marriage;
    }
    
    // returns -1 if internship does not prefer student
    // returns weakest matched student if intenrship prefers new student
    public int internshipPrefersStudent(int sScore, ArrayList<Integer> internshipStudents, ArrayList<Integer> studentScores) {
    	int index = -1;
    	int sPrimeScore;
    	for(int i = 0; i < internshipStudents.size(); i++) {
    		sPrimeScore = studentScores.get(internshipStudents.get(i));
    		if(sPrimeScore < sScore) {
    			index = studentScores.indexOf(sPrimeScore);
    		}
    	}
    	
    	return index;
    }
    
    // computes StudentScores for each student for each internship
    public static ArrayList<ArrayList<Integer>> computeStudentScores(int internshipCount, int studentCount,
            ArrayList<ArrayList<Integer>>internship_weights,
            ArrayList<Double> student_GPA,
            ArrayList<Integer> student_months,
            ArrayList<Integer> student_projects){
    	
    	ArrayList<ArrayList<Integer>> studentScores = new ArrayList<ArrayList<Integer>>(internshipCount);
    	int sScore;

    	for(int i = 0; i < internshipCount; i++) {
        	studentScores.add(new ArrayList<Integer>()); // add arraylist to each internship to keep track of scores
        	for(int j = 0; j < studentCount; j++) {
        		sScore = (int) Math.round(computeInternshipStudentScore(student_GPA.get(j), student_months.get(j), student_projects.get(j),
        											   					internship_weights.get(i).get(0), internship_weights.get(i).get(1), 
        											   					internship_weights.get(i).get(2)));
        		studentScores.get(i).add(sScore);
            }
        }

    	return studentScores;
}

    private ArrayList<Matching> getAllStableMarriages(Matching marriage) {
        ArrayList<Matching> marriages = new ArrayList<>();
        int n = marriage.getStudentCount();
        int slots = marriage.totalInternshipSlots();

        Permutation p = new Permutation(n, slots);
        Matching matching;
        while ((matching = p.getNextMatching(marriage)) != null) {
            if (isStableMatching(matching)) {
                marriages.add(matching);
            }
        }

        return marriages;
    }

    @Override
    public Matching stableMarriageBruteForce_studentoptimal(Matching marriage) {
        ArrayList<Matching> allStableMarriages = getAllStableMarriages(marriage);
        Matching studentOptimal = null;
        int n = marriage.getStudentCount();
        int m = marriage.getInternshipCount();
        System.out.println("student" + n + "internship" + m);
        ArrayList<ArrayList<Integer>> student_preference = marriage.getStudentPreference();

        //Construct an inverse list for constant access time
        ArrayList<ArrayList<Integer>> inverse_student_preference = new ArrayList<ArrayList<Integer>>(0) ;
        for (Integer i=0; i<n; i++) {
            ArrayList<Integer> inverse_preference_list = new ArrayList<Integer>(m) ;
            for (Integer j=0; j<m; j++)
                inverse_preference_list.add(-1) ;
            ArrayList<Integer> preference_list = student_preference.get(i) ;

            for (int j=0; j<m; j++) {
                inverse_preference_list.set(preference_list.get(j), j) ;
            }
            inverse_student_preference.add(inverse_preference_list) ;
        }

        // bestStudentMatching stores the rank of the best Internship each student matched to
        // over all stable matchings
        int[] bestStudentMatching = new int[marriage.getStudentCount()];
        Arrays.fill(bestStudentMatching, -1);
        for (Matching mar : allStableMarriages) {
            ArrayList<Integer> student_matching = mar.getStudentMatching();
            for (int i = 0; i < student_matching.size(); i++) {
                if (student_matching.get(i) != -1 && (bestStudentMatching[i] == -1 ||
                        inverse_student_preference.get(i).get(student_matching.get(i)) < bestStudentMatching[i])) {
                    bestStudentMatching[i] = inverse_student_preference.get(i).get(student_matching.get(i));
                    studentOptimal = mar;
                }
            }
        }

        return studentOptimal;
    }
}
