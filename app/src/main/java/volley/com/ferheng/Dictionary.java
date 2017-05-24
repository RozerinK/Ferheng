package volley.com.ferheng;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedHashSet;
import java.util.Set;

public class Dictionary {
    public SQLiteDatabase database;

    public Dictionary(SQLiteDatabase database) {
        this.database = database;
    }

    // Generating Random query from Database
    public String getRandomWord() {
        Cursor records = database.rawQuery(
                "select * from eng2te where rowid = (abs(random()) % (select max(rowid)+1 from eng2te))", null);
        records.moveToFirst();
        int word_index = records.getColumnIndex("eng_word");
        String word = records.getString(word_index);
        return word;
    }

    // Getting Meaning for Input query
    public String getTrMeaning(String query) {
        try {
            Cursor trMeaning = database.rawQuery("Select * from zazaki where name='" + query + "'"
                    + "COLLATE NOCASE", null);
            boolean recordsExist = trMeaning.moveToFirst();
            if (recordsExist) {

               //int index = trMeaning.getColumnIndex("name");
                String key = trMeaning.getString(trMeaning.getColumnIndex("key"));
                Cursor findByKey = database.rawQuery("Select * from tr where key='" + key + "'"
                        + "COLLATE NOCASE", null);
                findByKey.moveToFirst();
                String meaning = findByKey.getString(findByKey.getColumnIndex("name"));


                return meaning;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getZazakiMeaning(String query) {
        try {
            Cursor zazakiMeaning = database.rawQuery("Select key from tr where name='" + query + "'"
                    + "COLLATE NOCASE", null);
            boolean recordsExist = zazakiMeaning.moveToFirst();
            if (recordsExist) {

                //int index = trMeaning.getColumnIndex("name");
                String key = zazakiMeaning.getString(zazakiMeaning.getColumnIndex("key"));
                Cursor findByKey = database.rawQuery("Select * from zazaki where key='" + key + "'"
                        + "COLLATE NOCASE", null);
                findByKey.moveToFirst();
                String meaning = findByKey.getString(findByKey.getColumnIndex("name"));


                return meaning;
            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void storeRecentWord(SharedPreferences recentQueries, String word) {
        String meaning = getTrMeaning(word);
        if (meaning != null) {
            Set recents = recentQueries.getStringSet("recentValues", new LinkedHashSet<String>());
            recents.add(word);
            SharedPreferences.Editor recentEditor = recentQueries.edit();
            recentEditor.putStringSet("recentValues", recents);
            recentEditor.commit();
        }
    }
}