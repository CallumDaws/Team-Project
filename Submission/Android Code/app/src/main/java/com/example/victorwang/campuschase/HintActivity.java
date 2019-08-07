package com.example.victorwang.campuschase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class HintActivity extends AppCompatActivity {
    private QuestionManager questionManager = QuestionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hint);

        Question currentQuestion = questionManager.getQuestion(questionManager.getCurrentQuestionNumber());
        List<String> usedClues = currentQuestion.getUsedClues();

        TextView clueHistory = (TextView) findViewById(R.id.tx_hcd2);
        for (int i = 0; i < usedClues.size(); i++) {
            clueHistory.setText(clueHistory.getText().toString() + "\n" + usedClues.get(i));
        }

        TextView currentClue = (TextView) findViewById(R.id.tx_rchc);
        currentClue.setText(currentQuestion.getCurrentClue());

    }
}
