/*
Author: Ben Gerard
Date: 17/3/17
Purpose: Perform tests on the QuestionManager class
*/
package treasurehuntapi;

import java.io.IOException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class QuestionManagerTest {

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException {
		// TODO Auto-generated method stub
		
		QuestionManager manager = new QuestionManager();
		System.out.println(manager.getNextQuestion().getQuestion());
		System.out.println(manager.getNextQuestion().getQuestion());
		System.out.println(manager.getQuestion(0).getQuestion());
	}

}
