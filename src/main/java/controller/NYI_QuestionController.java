/*
NYI = NOT YET IMPLEMENTED
 */

package main.java.controller;

import main.java.model.Question;

import java.util.ArrayList;

public class NYI_QuestionController {
    private ArrayList<Question> questions;
    private static NYI_QuestionController instance;

    private NYI_QuestionController(){

    }

    public static NYI_QuestionController getInstance(){
        if(instance == null){
            instance = new NYI_QuestionController();
        }
        return instance;
    }
}
