/*
Author: Ben Gerard
Date: 17/3/17
Purpose: Question object, allows access to the different characteristics of a question.
*/
package com.example.victorwang.campuschase;

import java.util.*;

public class Question {
    private List<String> clues = new ArrayList<String>();
    private List<String> usedClues = new ArrayList<String>();
    private String question;
    private String answer;
    private String currentClue;

    public void addClues(ArrayList<String> clues) {
        this.clues.addAll(clues);
    }

    public String getClue(int clueNumber) {
        return clues.get(clueNumber);
    }

    public List<String> getClues() {
        return clues;
    }

    public List<String> getUsedClues() {
        return usedClues;
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

    public void addUsedClue(String clue) {
        usedClues.add(clue);
    }

    public String getCurrentClue() {
        return currentClue;
    }

    public void setCurrentClue(String clue) {
        currentClue = clue;
    }


}
