package com.example.calculator;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = (TextView) findViewById(R.id.tv_expression);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);
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
                    oppositeLastNum();
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

    public void updateFormula() {
        input = output;
        formula = answer;
    }

    public void solve() {
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

    public void oppositeLastNum() {
        boolean found = false;
        for (int i = formula.length()-1; i >= 0; i--) {
            switch (formula.charAt(i)) {
                case '+':
                    found = true;
                    formula = formula.substring(0, i) + "-" + formula.substring(i+1);
                    break;
                case '-':
                    found = true;
                    formula = formula.substring(0, i) + "+" + formula.substring(i+1);
                    break;
                case '*' :
                case '/':
                    found = true;
                    formula = formula.substring(0, i + 1) + "-" + formula.substring(i+1);
                    break;
                default:
                    break;
            }
            if (found)
                break;
        }

        if (!found)
            formula = "-" + formula;

        found = false;
        for (int i = input.length()-1; i >= 0; i--) {
            switch (input.charAt(i)) {
                case '+':
                    found = true;
                    input = input.substring(0, i) + "-" + input.substring(i+1);
                    break;
                case '-':
                    found = true;
                    input = input.substring(0, i) + "+" + input.substring(i+1);
                    break;
                case '×' :
                case '÷':
                    found = true;
                    input = input.substring(0, i + 1) + "-" + input.substring(i+1);
                    break;
                default:
                    break;
            }
            if (found)
                break;
        }
        if (!found)
            input = "- " + input;
    }
}
