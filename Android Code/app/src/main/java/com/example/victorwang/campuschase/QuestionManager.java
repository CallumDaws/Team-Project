/*
Author: Ben Gerard
Date: 17/3/17
Purpose: Download the questions from the server and create question objects. 
*/
package com.example.victorwang.campuschase;

import java.io.*;
import java.util.*;

import com.google.gson.*;

public class QuestionManager {
    private static QuestionManager instance = null;
    private int currentQuestionNumber = -1; // Must be -1 as getNextQuestion
    // will increment
    // currentQuestionNumber to 0 in the
    // event that the next question is
    // the first question
    private List<Question> questions = new ArrayList<Question>();

    protected QuestionManager() {
        // Download questions from server
        TreasureHuntAPI treasureHuntAPI = TreasureHuntAPI.getInstance();
        JsonArray jsonQuestions;
        try {
            jsonQuestions = treasureHuntAPI.downloadQuestions();

            if (jsonQuestions != null) {
                // Parse questions into Question objects
                for (int i = 0; i < jsonQuestions.size(); i++) { // Loop through json
                    // array
                    ArrayList<String> clues = new ArrayList<String>();
                    JsonElement question = jsonQuestions.get(i); // Get question from
                    // array
                    // Add data to question object
                    clues.add(question.getAsJsonObject().get("clue1").getAsString());
                    clues.add(question.getAsJsonObject().get("clue2").getAsString());
                    clues.add(question.getAsJsonObject().get("clue3").getAsString());
                    String questionString = question.getAsJsonObject().get("question").getAsString();
                    String answerString = question.getAsJsonObject().get("answer").getAsString();
                    Question tempQuestion = new Question(clues, answerString, questionString);
                    questions.add(tempQuestion); // Add question object to array of
                }
                    // questions
            } else {
                    System.out.println("ERROR COULD NOT DOWNLOAD QUESTIONS");
                }
        } catch (JsonIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    public int getNumberOfQuestions() {
        return questions.size();
    }

    public Question getQuestion(int questionNumber) {
        currentQuestionNumber = questionNumber;
        return questions.get(questionNumber);
    }

    public Question getNextQuestion() {
        currentQuestionNumber++;
        return questions.get(currentQuestionNumber);
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }

}
