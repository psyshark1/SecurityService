package utils;

import java.io.IOException;
import java.util.Properties;
/*
 * Класс с данными таблицы БД
 * */

public class Props {

    private static volatile Props instance;
    private static String DbName;

    private Props() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("../config/props.properties"));

        DbName = props.getProperty("dbName");
    }

    public static Props getInstance() throws IOException {
        if (instance == null) {
            synchronized (Props.class){
                if (instance == null) {
                    instance = new Props();
                }
            }
        }
        return instance;
    }

    public String getDbName(){
        return DbName;
    }
}

