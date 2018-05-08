package com.example.mycashcalc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

final class CalcValue {

    private static final String OPERATORS = "*/+-";
    private static final String NUMBERS = "0123456789.,";
    private static final ArrayList<String> output = new ArrayList<>();

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
        if (token.equals("+") || token.equals("-")) {
            return 1;
        }
        return 2;
    }

    private static Stack<String> getPolishForm (String input) {
        Stack<String> res = new Stack<>();
        Stack<String> operators = new Stack<>();
        StringTokenizer st = new StringTokenizer(input, OPERATORS, true);
        while(st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isFloat(token)) {
                res.push(token);
            }
            else {
                if (isOperator(token)) {
                    while (getPriority(operators.lastElement()) >= getPriority(token) && !operators.isEmpty() && isOperator(operators.lastElement())) {
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
        return res;
    }

    public static Float calcPolishForm (String input) {
        Stack<String> pForm = getPolishForm(input);
        Stack<String> res = new Stack<>();
//        String op2;
//        String op1;
        Float tmp;
        if (!pForm.isEmpty())
        {
            while (!pForm.isEmpty()){
                String token = pForm.pop();
                if (isOperator(token)){
//                    op2 = res.pop();
//                    op1 = res.pop();
                    tmp = execOperation(res.pop(), res.pop(), token);
                    res.push(tmp.toString());
                } else {
                    res.push(pForm.pop());
                }
            }
        }
        return null;
    }
}
