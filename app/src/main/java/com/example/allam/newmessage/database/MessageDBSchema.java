package com.example.allam.newmessage.database;



public class MessageDBSchema {
    public static final String DATABASE_NAME = "messages.db";

    public static final class MessagesTable{
        public static final String NAME = "message";

        public static final class Cols {
            public static final String USER = "user";
            public static final String BODY = "body";
            public static final String DATE = "date";
        }
    }
}
