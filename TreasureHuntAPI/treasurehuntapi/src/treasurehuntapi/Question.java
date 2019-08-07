/*
Author: Ben Gerard
Date: 17/3/17
Purpose: Question object, allows access to the different characteristics of a question.
*/
package treasurehuntapi;
import java.util.*;

public class Question {
	private List<String> clues = new ArrayList<String>();
	private String question;
	private String answer;
	
	public void addClues(ArrayList<String> clues) {
		this.clues.addAll(clues);
	}
	
	public String getClue() {
		Random random = new Random();
		return clues.get(random.nextInt(getNumberOfClues()));
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public int getNumberOfClues() {
		return clues.size();
	}
	
	public Question(ArrayList<String> clues, String answer, String question) {
		addClues(clues);
		setAnswer(answer);
		setQuestion(question);
	}
	
	

}
