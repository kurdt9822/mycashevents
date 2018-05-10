package com.example.mycashcalc;

import android.util.Log;

import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

final class CalcValue {

    private static final String OPERATORS = "*/+-";
//    private static final String NUMBERS = "0123456789.,";
//    private static final ArrayList<String> output = new ArrayList<>();

    private CalcValue() {
    }

    private static boolean isFloat(String token) throws NumberFormatException {
        try {
            Float.parseFloat(token);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private static boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private static int getPriority(String token) {
        int i = -1;
        switch (token) {
            case "+": i = 1; break;
            case "-": i = 1; break;
            case "*": i = 2; break;
            case "/": i = 2; break;
            case "(": i = 0; break;
            case ")": i = 0; break;
        }
//        if (token.equals("+") || token.equals("-")) {
//            return 1;
//        }
//        return 2;
        return i;
    }

    private static Stack<String> getPolishForm (String input) {
        Stack<String> res = new Stack<>();
//        Stack<String> res = new Stack<>();
        Stack<String> operators = new Stack<>();
        StringTokenizer st = new StringTokenizer(input, OPERATORS, true);
        while(st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isFloat(token)) {
                res.push(token);
            }
            else {
                if (isOperator(token)) {
                    while (!operators.isEmpty() && isOperator(operators.lastElement()) && getPriority(operators.lastElement()) >= getPriority(token)) {
                        res.push(operators.pop());
                    }
                    operators.push(token);
                }
            }
        }
        while (!operators.isEmpty()){
            res.push(operators.pop());
        }
        Collections.reverse(res);
        return res;
    }

    private static Float execOperation(String op1, String op2, String operator){
        Float res = null;
        try {
        switch (operator){
            case "+":
                res = Float.parseFloat(op1) + Float.parseFloat(op2);
                break;
            case "-":
                res = Float.parseFloat(op1) - Float.parseFloat(op2);
                break;
            case "*":
                res = Float.parseFloat(op1) * Float.parseFloat(op2);
                break;
            case "/":
                res = Float.parseFloat(op1) / Float.parseFloat(op2);
                break;
        }
        }
        catch (NumberFormatException e) {
            Log.e(Main.LOG_TAG, e.getMessage());
            return null;
        }
        return res;
    }

    public static Float calcPolishForm (String input) {
        String clearString = input.replaceAll("[^0-9\\*\\+\\-\\/]", "");
        Stack<String> pForm = getPolishForm(clearString);
        Stack<String> res = new Stack<>();
        if (!pForm.isEmpty())
        {
            Float tmp;
            while (!pForm.isEmpty()) {
                String token = pForm.pop();
                if (isOperator(token)){
                    String op2 = res.pop();
                    String op1 = res.pop();
                    tmp = execOperation(op1, op2, token);
                    res.push(tmp.toString());
                } else {
                    res.push(token);
                }
            }
        }
        try {
            return Float.parseFloat(res.lastElement());
        } catch (NumberFormatException e) {
            Log.e(Main.LOG_TAG, e.getMessage());
            return null;
        }
    }

}
