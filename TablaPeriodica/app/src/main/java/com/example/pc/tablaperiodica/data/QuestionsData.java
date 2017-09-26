package com.example.pc.tablaperiodica.data;

import android.text.Html;

/**
 * Created by pc on 24/09/2017.
 */

public class QuestionsData {

    private static final String[] questionList = {
            "Elementos metálicos en el grupo 14",
            "Radio atómico más grande en el grupo 17 A",
            "Elemento de mayor electronegatividad",
            "La menor Energía de Ionización 1 en el periodo 4",
            "Elemento metaloide del grupo VI A",
            "Elemento del periodo 5 que forma un ión 1+ y es el mejor conductor de la electricidad",
            "Elementos no metálicos del grupo V A",
            "Elemento con configuración electrónica condensada: [Xe] 6s1 4f14 5d10",
            "Elemento del grupo 3A que tiene propiedades metálicas y no metálicas",
            "Grupo de transición que presenta diamagnetismo",
            "Radio atómico más pequeño en el periodo 6",
            "Elemento de transición del periodo 4 que forma un ión +2 con el subnivel d lleno a la mitad",
            "Elemento metálico menos electronegativo",
            "Elemento con configuración electrónica condensada: [Ar] 4s2 3d10 4p6",
            "La menor Energía de Ionización en el grupo 2"
    };

    private static final String[] answerList = {
            "50,82,114",
            "87",
            "9",
            "35",
            "52",
            "47",
            "7,15",
            "79",
            "5",
            "30,48,80,112",
            "86",
            "27",
            "87",
            "36",
            "88"
    };

    public static int[] getAnswer(int index){
        String[] temp_answer = answerList[index].split(",");
        int[] answer = new int[temp_answer.length];
        for(int i=0; i<temp_answer.length; i++){
            answer[i] = Integer.parseInt(temp_answer[i]);
        }
        return answer;
    }

    public static final int QUESTION_COUNT = questionList.length;

    public static String getQuestion(int questionIndex){
        if(questionIndex<0 || questionIndex >= QUESTION_COUNT)
            return null;
        return questionList[questionIndex];
    }

}
