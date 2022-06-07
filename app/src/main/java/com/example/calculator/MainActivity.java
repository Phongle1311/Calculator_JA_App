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
    private String input, output, formula, answer;
    private char lastChar;
    private boolean newExp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvExpression = (TextView) findViewById(R.id.tv_expression);
        tvAnswer = (TextView) findViewById(R.id.tv_answer);
    }

    public void ButtonClick(View view) {
        Button button = (Button) view;
        String data = button.getText().toString();

        if (formula != null)
            if (formula.length()>0)
                lastChar = formula.charAt(formula.length() - 1);
        switch (data) {
            case "C":
                input = "";
                formula = "";
                output = "";
                answer = "";
                break;
            case "±":
                if (answer != null && answer != "") {
                    oppositeLastNum();
                }
                //newExp = false;
                break;
            case "%":
                if (formula != null && formula != "") {
                    if (output != "" && output != "Error" && output != null) {
                        input = output;
                        formula = answer;
                    }
                    input += "% ";
                    formula += "/100";
                }
                newExp = false;
                break;
            case "÷":
                if (formula != null && formula != "" ) {
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/')
                    {
                        if (output != "" && output != "Error" && output != null) {
                            input = output;
                            formula = answer;
                        }
                        input+=" ÷ ";
                        formula += "/";
                    }
                }
                newExp = false;
                break;
            case "×":
                if (formula != null && formula != "") {
                    if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/') {
                        if (output != "" && output != "Error" && output != null) {
                            input = output;
                            formula = answer;
                        }
                        formula += "*";
                        input += " × ";
                    }
                }
                newExp = false;
                break;
            case "-":
                if (lastChar != '-')
                {
                    if (output != "" && output != "Error" && output != null) {
                        input = output;
                        formula = answer;
                    }
                    input+=" - ";
                    formula += "-";
                }
                newExp = false;
                break;
            case "+":
                if (lastChar != '*' && lastChar != '/' && lastChar != '+')
                {
                    if (output != "" && output != "Error" && output != null) {
                        input = output;
                        formula = answer;
                    }
                    input+=" + ";
                    formula += "+";
                }
                newExp = false;
                break;
            case "=":
                solve();
                newExp = true;
                break;
            default:
                if (formula == null) {
                    formula = "";
                    input = "";
                }
                if (newExp) {
                    formula = "";
                    input = "";
                    output = "";
                    answer = "";
                }
                input +=data;
                formula += data;
                newExp = false;
                break;
        }

        tvExpression.setText(input);
        tvAnswer.setText(output);
        //button.setBackgroundColor(Color.GRAY);
    }

    public void solve() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        try {
            double res = (double) engine.eval(formula);
            answer = res + "";
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

    }
}