package com.example.pc.tablaperiodica.data;

import android.database.DatabaseUtils;
import android.provider.BaseColumns;

/**
 * Created by Hector on 26/09/2017.
 */

public class QuestionsContract {

    private QuestionsContract(){}

    public static class AnswersEntry implements BaseColumns{

        public static final String QUESTION_TABLE_NAME = "questions_table";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ANSWERED = "answered";
        public static final String COLUMN_USER_ANSWERS = "user_answers";

    }
}
