package com.example.calculator;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class MainActivity extends AppCompatActivity {
    private TextView tvExpression, tvAnswer;
    private String
            input = "",
            output = "",
            formula = "",
            answer = "";
    private char lastChar;
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

    static final String STATE_INPUT = "input";
    static final String STATE_OUTPUT = "output";
    static final String STATE_FORMULA = "formula";
    static final String STATE_ANSWER = "answer";
    static final String STATE_LAST_CHAR = "lastChar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvExpression = (TextView) findViewById(R.id.tv_expression);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);

        tvExpression.setText(input);
        tvAnswer.setText(output);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_INPUT, input);
        savedInstanceState.putString(STATE_OUTPUT, output);
        savedInstanceState.putString(STATE_FORMULA, formula);
        savedInstanceState.putString(STATE_ANSWER, answer);
        savedInstanceState.putChar(STATE_LAST_CHAR, lastChar);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        input = savedInstanceState.getString(STATE_INPUT);
        output = savedInstanceState.getString(STATE_OUTPUT);
        formula = savedInstanceState.getString(STATE_FORMULA);
        answer = savedInstanceState.getString(STATE_ANSWER);
        lastChar = savedInstanceState.getChar(STATE_LAST_CHAR);
    }

    public void buttonClick(View view) {
        Button button = (Button) view;
        String data = button.getText().toString();

        switch (data) {
            case "C":
                input = "";
                formula = "";
                output = "";
                answer = "";
                lastChar = '\0';
                break;
            case "±":
                if (lastChar != '\0') {
                    if (lastChar == '=')
                        updateFormula();
                    formula = oppositeLastNum(formula, "-");
                    input = oppositeLastNum(input, "- ");
                    lastChar = '±';
                }
                break;
            case "%":
                if (lastChar!='\0' && lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                    if (lastChar == '=')
                        updateFormula();
                    input += "% ";
                    formula += "/100";
                    lastChar = '%';
                }
                break;
            case "÷":
                if (lastChar!='\0' && lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                    if (lastChar == '=')
                        updateFormula();
                    input+=" ÷ ";
                    formula += "/";
                    lastChar = '/';
                }
                break;
            case "×":
                if (lastChar!='\0' && lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                    if (lastChar == '=')
                        updateFormula();
                    formula += "*";
                    input += " × ";
                    lastChar = '*';
                }
                break;
            case "-":
                if (lastChar != '-')
                {
                    if (lastChar == '=')
                        updateFormula();
                    input+=" - ";
                    formula += "-";
                    lastChar = '-';
                }
                break;
            case "+":
                if (lastChar!='\0' && lastChar != '*' && lastChar != '/' && lastChar != '+')
                {
                    if (lastChar == '=')
                        updateFormula();
                    input+=" + ";
                    formula += "+";
                    lastChar = '+';
                }
                break;
            case "=":
                solve();
                lastChar = '=';
                break;
            default:
                if (lastChar == '=') {
                    formula = "";
                    input = "";
                    output = "";
                    answer = "";
                }
                input +=data;
                formula += data;
                lastChar = data.charAt(0);
                break;
        }

        tvExpression.setText(input);
        tvAnswer.setText(output);
        //button.setBackgroundColor(Color.GRAY);
    }

    private void updateFormula() {
        input = output;
        formula = answer;
    }

    private void solve() {
        try {
            double res = (double) engine.eval(formula);
            answer = res + "";
            if (answer.contains("Inf")) {
                output = "Error";
                answer = "";
                input = "";
                formula = "";
                return;
            }

            res = Math.round(res*1000000)/1000000.0;
            output = res+"";
            if (Math.ceil(res) == Math.floor(res)) {
                int temp = (int) res;
                answer = temp + "";
                output = answer;
            }
        } catch (ScriptException e) {
            output = "Error";
            answer = "";
            input = "";
            formula = "";
        }
    }

    private String oppositeLastNum(String s, String prefix) {
        for (int i = s.length()-1; i >= 0; i--)
            switch (s.charAt(i)) {
                case '+':
                    s = s.substring(0, i) + "-" + s.substring(i+1);
                    return s;
                case '-':
                    s = s.substring(0, i) + "+" + s.substring(i+1);
                    return s;
                case '*' :
                case '/':
                    s = s.substring(0, i + 1) + "-" + s.substring(i+1);
                    return s;
            }
        s = prefix + s;
        return s;
    }
}