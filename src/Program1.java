/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

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
        /* TODO implement this function */
        return null; /* TODO remove this line */
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
