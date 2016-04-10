package pl.planta.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // Wszystkie statyczne pola
    // Wersja bazy danych
    private static final int DATABASE_VERSION = 2;

    // Nazwa bazy danych
    private static final String DATABASE_NAME = "planta.db";

    // Nazwa tabel
    private static final String TABLE_USER = "user";

    // Nazwy kolumn w tabeli "user"
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_NICK = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COAL_SCORE = "coal_score";
    private static final String KEY_COAL_HIGHSCORE = "coal_highscore";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_UID + " TEXT,"
            + KEY_NICK + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE,"
            + KEY_COAL_SCORE + " INTEGER,"
            + KEY_COAL_HIGHSCORE + " INTEGER,"
            + KEY_CREATED_AT + " TEXT" + ")";

    /**
     * Konstruktor
     *
     * @param context context
     */
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Tworzenie bazy danych
     *
     * @param db database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        Log.d(TAG, "Tabela utworzona.");
    }

    /**
     * Aktualizacja SQLite
     *
     * @param db         database object
     * @param oldVersion database's old version
     * @param newVersion database's new version2
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Usuń starą tabelę, jeśli istnieje
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Utworzenie ponownie tabeli
        onCreate(db);
    }

    /**
     * Zapis użytkownika do SQLite
     *
     * @param uid            unique id
     * @param name           user's name
     * @param email          user's email
     * @param coal_score     user's coal score
     * @param coal_highscore user's best score
     * @param created_at     time when user has been created
     */
    public void addUser(String uid, String name, String email, int coal_score, int coal_highscore, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UID, uid);
        contentValues.put(KEY_NICK, name);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_COAL_SCORE, coal_score);
        contentValues.put(KEY_COAL_HIGHSCORE, coal_highscore);
        contentValues.put(KEY_CREATED_AT, created_at);

        // Dodawanie wiersza
        long id = db.insert(TABLE_USER, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Nowy uzytkownik dodany do bazy SQLite: " + id);
    }

    /**
     * Pobieranie z SQLite tylko UID gracza
     *
     * @return user UID - unique id
     */
    public HashMap<String, String> getUserUid() {
        HashMap<String, String> userUID = new HashMap<>();
        String selectQuery = "SELECT uid FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userUID.put("uid", cursor.getString(1));
        }
        cursor.close();
        db.close();

        // Zwracanie user'a
        Log.d(TAG, "Pobieranie UID uzytkownika z SQLite: " + userUID.toString());
        return userUID;
    }

    /**
     * Pobranie z SQLite uid, name, email, created_at
     *
     * @return user
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT uid, name, email, created_at FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Przejdź do pierwszego wiersza
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("uid", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();

        // Zwracanie user'a
        Log.d(TAG, "Pobieranie uzytkownika z SQLite: " + user.toString());

        return user;
    }

    /**
     * Pobranie z SQLite coal_score & coal_highscore
     *
     * @return userCoal
     */
    public HashMap<String, Integer> getUserCoalScores() {
        HashMap<String, Integer> userCoal = new HashMap<>();
        String selectQuery = "SELECT coal_score, coal_highscore FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userCoal.put("coal_score", cursor.getInt(4));
            userCoal.put("coal_highscore", cursor.getInt(5));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Pobieranie wyników użytkownika z SQLite: " + userCoal.toString());

        return userCoal;
    }

    /**
     * Aktualizacja wiersza z userID = 1
     *
     * @param userID         user's unique id
     * @param coal_score     user's coal score
     * @param coal_highscore user's best score
     * @return return true if id > 0 else return false
     */
    public boolean updateCoalScores(long userID, int coal_score, int coal_highscore) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_SCORE, coal_score);
        contentValues.put(KEY_COAL_HIGHSCORE, coal_highscore);

        long id = db.update(TABLE_USER, contentValues, KEY_ID + "=" + userID, null);
        if (id > 0) return true;
        else return false;
    }

    /**
     * Usuwanie wszystkich informacji o użytkowniku - tylko podczas wylogowania
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Usuwanie wszystkich wierszy
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Usuwanie wszystkich informacji o uzytkowniku z SQLite.");
    }
}
