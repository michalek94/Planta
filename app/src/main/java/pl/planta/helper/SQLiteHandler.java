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

    /**
     * DATABASE_VERSION
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * DATABASE_NAME
     */
    private static final String DATABASE_NAME = "planta.db";

    /**
     * DATABASE_TABLES_NAME
     */
    private static final String TABLE_USER = "user";
    private static final String TABLE_COAL = "coal";
    private static final String TABLE_PIPE = "pipe";
    private static final String TABLE_STOREROOM = "storeroom";
    private static final String TABLE_LEVELS = "levels";
    private static final String TABLE_PRICES = "prices";

    /**
     * ID_PRIMARY_KEY
     */
    private static final String KEY_ID = "id";

    /**
     * TALBE_USER_COLUMNS
     */
    private static final String KEY_UID = "uid";
    private static final String KEY_NICK = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MONEY = "money";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_CREATED_AT = "created_at";

    /**
     * TABLE_COAL_COLUMNS
     */
    private static final String KEY_COAL_HIGHSCORE = "coal_highscore";
    private static final String KEY_COAL_BONUS = "coal_bonus";
    private static final String KEY_COAL_PRICE = "coal_price";

    /**
     * TABLE_PIPE_COLUMNS
     */
    private static final String KEY_PIPE_HIGHSCORE = "pipe_highscore";
    private static final String KEY_PIPE_BONUS = "pipe_bonus";
    private static final String KEY_PIPE_PRICE = "pipe_price";

    /**
     * TABLE_LEVELS_COLUMNS
     */
    private static final String KEY_COMPUTER_LEVEL = "computer_level";
    private static final String KEY_HOOK_LEVEL = "hook_level";
    private static final String KEY_STOREROOM_LEVEL = "storeroom_level";
    private static final String KEY_FURNACE_LEVEL = "furnace_level";
    private static final String KEY_FACTORY_LEVEL = "factory_level";
    private static final String KEY_FLATS_LEVEL = "flats_level";
    private static final String KEY_PIPELINE_LEVEL = "pipeline_level";
    private static final String KEY_MINE_LEVEL = "mine_level";

    /**
     * TABLE_LEVELS_COLUMNS
     */
    private static final String KEY_COMPUTER_PRICE = "computer_price";
    private static final String KEY_HOOK_PRICE = "hook_price";
    private static final String KEY_STOREROOM_PRICE = "storeroom_price";
    private static final String KEY_FURNACE_PRICE = "furnace_price";
    private static final String KEY_FACTORY_PRICE = "factory_price";
    private static final String KEY_FLATS_PRICE = "flats_price";
    private static final String KEY_PIPELINE_PRICE = "pipeline_price";
    private static final String KEY_MINE_PRICE = "mine_price";


    /**
     * TABLE_STOREROOM_COLUMNS
     */
    private static final String KEY_COAL_AMOUNT = "coal_amount";
    private static final String KEY_COAL_MAX = "coal_max";
    private static final String KEY_PIPE_AMOUNT = "pipe_amount";
    private static final String KEY_PIPE_MAX = "pipe_max";
    private static final String KEY_ELECTRICITY_AMOUNT = "electricity_amount";
    private static final String KEY_ELECTRICITY_MAX = "electricity_max";

    /**
     * CREATE_TABLE_USER
     */
    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_UID + " TEXT,"
            + KEY_NICK + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE,"
            + KEY_MONEY + " INTEGER,"
            + KEY_LEVEL + " INTEGER,"
            + KEY_CREATED_AT + " TEXT" + ")";

    /**
     * CREATE_TABLE_COAL
     */
    private static final String CREATE_COAL_TABLE = "CREATE TABLE "
            + TABLE_COAL + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COAL_HIGHSCORE + " INTEGER,"
            + KEY_COAL_BONUS + " REAL,"
            + KEY_COAL_PRICE + " REAL" + ")";

    /**
     * CREATE_TABLE_PIPE
     */
    private static final String CREATE_PIPE_TABLE = "CREATE TABLE "
            + TABLE_PIPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PIPE_HIGHSCORE + " INTEGER,"
            + KEY_PIPE_BONUS + " REAL,"
            + KEY_PIPE_PRICE + " REAL" + ")";

    /**
     * CREATE_TABLE_LEVELS
     */
    private static final String CREATE_LEVELS_TABLE = "CREATE TABLE "
            + TABLE_LEVELS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COMPUTER_LEVEL + " INTEGER,"
            + KEY_HOOK_LEVEL + " INTEGER,"
            + KEY_STOREROOM_LEVEL + " INTEGER,"
            + KEY_FURNACE_LEVEL + " INTEGER,"
            + KEY_FACTORY_LEVEL + " INTEGER,"
            + KEY_FLATS_LEVEL + " INTEGER,"
            + KEY_PIPELINE_LEVEL + " INTEGER,"
            + KEY_MINE_LEVEL + " INTEGER" + ")";

    /**
     * CREATE_TABLE_LEVELS
     */
    private static final String CREATE_PRICES_TABLE = "CREATE TABLE "
            + TABLE_PRICES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COMPUTER_PRICE + " INTEGER,"
            + KEY_HOOK_PRICE + " INTEGER,"
            + KEY_STOREROOM_PRICE + " INTEGER,"
            + KEY_FURNACE_PRICE + " INTEGER,"
            + KEY_FACTORY_PRICE + " INTEGER,"
            + KEY_FLATS_PRICE + " INTEGER,"
            + KEY_PIPELINE_PRICE + " INTEGER,"
            + KEY_MINE_PRICE + " INTEGER" + ")";

    /**
     * CREATE_TABLE_STOREROOM
     */
    private static final String CREATE_STOREROOM_TABLE = "CREATE TABLE "
            + TABLE_STOREROOM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COAL_AMOUNT + " INTEGER,"
            + KEY_COAL_MAX + " INTEGER,"
            + KEY_PIPE_AMOUNT + " INTEGER,"
            + KEY_PIPE_MAX + "  INTEGER,"
            + KEY_ELECTRICITY_AMOUNT + " INTEGER,"
            + KEY_ELECTRICITY_MAX + " INTEGER" + ")";

    /**
     * CONSTRUCTOR
     *
     * @param context context of application
     */
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * CREATE SQLITE_DATABASE
     *
     * @param db database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        Log.d(TAG, "Tabela " + TABLE_USER + " zostala utworzona.");
        db.execSQL(CREATE_COAL_TABLE);
        Log.d(TAG, "Tabela " + TABLE_COAL + " zostala utworzona.");
        db.execSQL(CREATE_PIPE_TABLE);
        Log.d(TAG, "Tabela " + TABLE_PIPE + " zostala utworzona.");
        db.execSQL(CREATE_LEVELS_TABLE);
        Log.d(TAG, "Tabela " + TABLE_LEVELS + " zostala utworzona.");
        db.execSQL(CREATE_PRICES_TABLE);
        Log.d(TAG, "Tabela " + TABLE_PRICES + " zostala utworzona.");
        db.execSQL(CREATE_STOREROOM_TABLE);
        Log.d(TAG, "Tabela " + TABLE_STOREROOM + " zostala utworzona.");
    }

    /**
     * SQLITE_UPGRADE DATABASE
     *
     * @param db         database object
     * @param oldVersion database's old version
     * @param newVersion database's new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If old table exists, delete
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOREROOM);
        // Recreate tables
        onCreate(db);
    }

    /**
     * ADD USER TO SQLITE DATABASE
     *
     * @param uid        unique id
     * @param name       user's name
     * @param email      user's email
     * @param money      user's money
     * @param level      user's level
     * @param created_at time when user has been created
     */
    public void addUser(String uid, String name, String email, int money, int level, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UID, uid);
        contentValues.put(KEY_NICK, name);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_MONEY, money);
        contentValues.put(KEY_LEVEL, level);
        contentValues.put(KEY_CREATED_AT, created_at);

        // Dodawanie wiersza
        long id = db.insert(TABLE_USER, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Nowy uzytkownik dodany do bazy SQLite: " + id);
    }

    /**
     * ADD COAL VALUES TO SQLITE DATABASE
     *
     * @param coalHighScore user's coal highscore
     * @param coalBonus     coal bonus
     * @param coalPrice     coal price
     */
    public void addCoal(int coalHighScore, double coalBonus, double coalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_HIGHSCORE, coalHighScore);
        contentValues.put(KEY_COAL_BONUS, coalBonus);
        contentValues.put(KEY_COAL_PRICE, coalPrice);

        // Dodawanie wiersza
        long id = db.insert(TABLE_COAL, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Wartosci dla gry COAL zostaly dodane do SQLite: " + id);
    }

    /**
     * ADD PIPE VALUES TO SQLITE DATABASE
     *
     * @param pipeHighScore user's pipe highscore
     * @param pipeBonus     pipe bonus
     * @param pipePrice     pipe price
     */
    public void addPipe(int pipeHighScore, double pipeBonus, double pipePrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PIPE_HIGHSCORE, pipeHighScore);
        contentValues.put(KEY_PIPE_BONUS, pipeBonus);
        contentValues.put(KEY_PIPE_PRICE, pipePrice);

        // Dodawanie wiersza
        long id = db.insert(TABLE_PIPE, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Wartosci dla gry PIPE zostaly dodane do SQLite: " + id);
    }

    /**
     * ADD BUILDINGS LEVELS TO SQLITE DATABASE
     *
     * @param computerLevel  computer level
     * @param hookLevel      hook level
     * @param storeRoomLevel storeroom level
     * @param furnaceLevel   furnace level
     * @param factoryLevel   factory level
     * @param flatsLevel     flats level
     * @param pipelineLevel  pipeline level
     * @param mineLevel      mine level
     */
    public void addLevels(int computerLevel, int hookLevel, int storeRoomLevel, int furnaceLevel, int factoryLevel, int flatsLevel, int pipelineLevel, int mineLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMPUTER_LEVEL, computerLevel);
        contentValues.put(KEY_HOOK_LEVEL, hookLevel);
        contentValues.put(KEY_STOREROOM_LEVEL, storeRoomLevel);
        contentValues.put(KEY_FURNACE_LEVEL, furnaceLevel);
        contentValues.put(KEY_FACTORY_LEVEL, factoryLevel);
        contentValues.put(KEY_FLATS_LEVEL, flatsLevel);
        contentValues.put(KEY_PIPELINE_LEVEL, pipelineLevel);
        contentValues.put(KEY_MINE_LEVEL, mineLevel);

        // Dodawanie wiersza
        long id = db.insert(TABLE_LEVELS, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Wartosci dla poziomow budynkow zostaly dodane do SQLite: " + id);
    }

    /**
     * ADD VALUES TO STOREROOM TABLE TO SQLITE DATABASE
     *
     * @param coalAmount        coal amount
     * @param coalMax           coal max
     * @param pipeAmount        pipe amount
     * @param pipeMax           pipe max
     * @param electricityAmount electricity amount
     * @param electricityMax    electricity max
     */
    public void addStoreValues(int coalAmount, int coalMax, int pipeAmount, int pipeMax, int electricityAmount, int electricityMax) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_AMOUNT, coalAmount);
        contentValues.put(KEY_COAL_MAX, coalMax);
        contentValues.put(KEY_PIPE_AMOUNT, pipeAmount);
        contentValues.put(KEY_PIPE_MAX, pipeMax);
        contentValues.put(KEY_ELECTRICITY_AMOUNT, electricityAmount);
        contentValues.put(KEY_ELECTRICITY_MAX, electricityMax);

        // Dodawanie wiersza
        long id = db.insert(TABLE_STOREROOM, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Wartosci dla posiadanych surowcow zsotaly dodane do SQLite: " + id);
    }

    /**
     * ADD UPGRADE PRICES TO SQLITE DATABASE
     *
     * @param computerPrice  computer Price
     * @param hookPrice     hook Price
     * @param storeRoomPrice storeroom Price
     * @param furnacePrice   furnace Price
     * @param factoryPrice   factory Price
     * @param flatsPrice   flats Price
     * @param pipelinePrice pipeline Price
     * @param minePrice     mine Price
     */
    public void addPrices(int computerPrice, int hookPrice, int storeRoomPrice, int furnacePrice, int factoryPrice, int flatsPrice, int pipelinePrice, int minePrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMPUTER_PRICE, computerPrice);
        contentValues.put(KEY_HOOK_PRICE, hookPrice);
        contentValues.put(KEY_STOREROOM_PRICE, storeRoomPrice);
        contentValues.put(KEY_FURNACE_PRICE, furnacePrice);
        contentValues.put(KEY_FACTORY_PRICE, factoryPrice);
        contentValues.put(KEY_FLATS_PRICE, flatsPrice);
        contentValues.put(KEY_PIPELINE_PRICE, pipelinePrice);
        contentValues.put(KEY_MINE_PRICE, minePrice);

        // Dodawanie wiersza
        long id = db.insert(TABLE_PRICES, null, contentValues);
        db.close(); // Zamykanie bazy danych

        Log.d(TAG, "Wartosci dla kosztow rozbudowy budynkow zostaly dodane do SQLite: " + id);
    }

    /**
     * Pobranie z SQLite uid, name, email, created_at
     *
     * @return user
     */
    public HashMap<String, String> getUserInfo() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT name, email, created_at FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Przejdź do pierwszego wiersza
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(0));
            user.put("email", cursor.getString(1));
            user.put("created_at", cursor.getString(2));
        }
        cursor.close();
        db.close();

        // Zwracanie user'a
        Log.d(TAG, "Pobieranie uzytkownika z SQLite: " + user.toString());

        return user;
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
            userUID.put("uid", cursor.getString(0));
        }
        cursor.close();
        db.close();

        // Zwracanie user'a
        Log.d(TAG, "Pobieranie UID uzytkownika z SQLite: " + userUID.toString());
        return userUID;
    }

    /**
     * GET USER MONEY
     *
     * @return user's money
     */
    public HashMap<String, Integer> getUserMoney() {
        HashMap<String, Integer> userMoney = new HashMap<>();
        String selectQuery = "SELECT money FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userMoney.put("money", cursor.getInt(0));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik posiada na koncie: " + userMoney.toString());

        return userMoney;
    }

    /**
     * UPDATE USER'S MONEY
     *
     * @param money user's money
     * @return return true if id > 0 else return false
     */
    public boolean updateMoney(int money) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MONEY, money);

        long id = db.update(TABLE_USER, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * GET USER LEVEL
     *
     * @return user's level
     */
    public HashMap<String, Integer> getUserLevel() {
        HashMap<String, Integer> userLevel = new HashMap<>();
        String selectQuery = "SELECT level FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userLevel.put(KEY_LEVEL, cursor.getInt(0));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik ma: " + userLevel.toString() + " poziom doswiadczenia");

        return userLevel;
    }

    /**
     * UPDATE USER'S LEVEL
     *
     * @param level user's level
     * @return return true if id > 0 else return false
     */
    public boolean updateLevel(int level) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LEVEL, level);

        long id = db.update(TABLE_USER, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * GET USER'S COAL HIGHSCORE
     *
     * @return coal_highscore
     */
    public HashMap<String, Integer> getCoalHighScore() {
        HashMap<String, Integer> coalHighScore = new HashMap<>();
        String selectQuery = "SELECT coal_highscore FROM " + TABLE_COAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            coalHighScore.put(KEY_COAL_HIGHSCORE, cursor.getInt(0));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik posiada: " + coalHighScore.toString() + " highscore");

        return coalHighScore;
    }

    /**
     * UPDATE USER'S COAL HIGHSCORE
     *
     * @param coalHighScore coal_highscore
     * @return return true if id > 0 else return false
     */
    public boolean updateCoalHighScore(int coalHighScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_HIGHSCORE, coalHighScore);

        long id = db.update(TABLE_COAL, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * GET COAL BONUS AND COAL PRICE FROM SQLITE DATABASE
     *
     * @return coal bonus & price
     */
    public HashMap<String, Double> getCoalBonusPrice() {
        HashMap<String, Double> coalBonusPrice = new HashMap<>();
        String selectQuery = "SELECT coal_bonus, coal_price FROM " + TABLE_COAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            coalBonusPrice.put(KEY_COAL_BONUS, cursor.getDouble(0));
            coalBonusPrice.put(KEY_COAL_PRICE, cursor.getDouble(1));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Aktualny bonus i cena wegla to: " + coalBonusPrice.toString());

        return coalBonusPrice;
    }

    /**
     * UPDATE COAL BONUS
     *
     * @param coalBonus coal_bonus
     * @return return true if id > 0 else return false
     */
    public boolean updateCoalBonus(double coalBonus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_BONUS, coalBonus);

        long id = db.update(TABLE_COAL, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE COAL PRICE
     *
     * @param coalPrice coal_price
     * @return return true if id > 0 else return false
     */
    public boolean updateCoalPrice(double coalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_PRICE, coalPrice);

        long id = db.update(TABLE_COAL, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * GET PIPE BONUS AND PIPE PRICE FROM SQLITE DATABASE
     *
     * @return pipe bonus & price
     */
    public HashMap<String, Double> getPipeBonusPrice() {
        HashMap<String, Double> pipeBonusPrice = new HashMap<>();
        String selectQuery = "SELECT pipe_bonus, pipe_price FROM " + TABLE_PIPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            pipeBonusPrice.put(KEY_PIPE_BONUS, cursor.getDouble(0));
            pipeBonusPrice.put(KEY_PIPE_PRICE, cursor.getDouble(1));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Aktualny bonus i cena wegla to: " + pipeBonusPrice.toString());

        return pipeBonusPrice;
    }

    /**
     * UPDATE PIPE BONUS
     *
     * @param pipeBonus pipe_bonus
     * @return return true if id > 0 else return false
     */
    public boolean updatePipeBonus(double pipeBonus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PIPE_BONUS, pipeBonus);

        long id = db.update(TABLE_PIPE, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE PIPE PRICE
     *
   //  * @param pipePrice pipe_price
     * @return return true if id > 0 else return false
     */
    public boolean updatePipePrice() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT pipe_bonus, pipe_price FROM " + TABLE_PIPE;
        double temp = 0;

        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
           temp = cursor.getDouble(1) * 1.5;

        }
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PIPE_PRICE, temp);

        long id = db.update(TABLE_PIPE, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE PIPE HIGHSCORE
     *
     * @param pipeHighScore pipe_highscore
     * @return return true if id > 0 else return false
     */
    public boolean updatePipeHighScore(int pipeHighScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PIPE_HIGHSCORE, pipeHighScore);

        long id = db.update(TABLE_PIPE, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * GET USER'S PIPE HIGHSCORE
     *
     * @return pipe_highscore
     */
    public HashMap<String, Integer> getPipeHighScore() {
        HashMap<String, Integer> pipeHighScore = new HashMap<>();
        String selectQuery = "SELECT pipe_highscore FROM " + TABLE_PIPE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            pipeHighScore.put(KEY_PIPE_HIGHSCORE, cursor.getInt(0));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik posiada: " + pipeHighScore.toString() + " highscore");

        return pipeHighScore;
    }

    /**
     * GET USER'S BUILDINGS LEVELS
     *
     * @return buildings levels
     */
    public HashMap<String, Integer> getBuildingsLevels() {
        HashMap<String, Integer> buildingsLevels = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_LEVELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            buildingsLevels.put(KEY_COMPUTER_LEVEL, cursor.getInt(0));
            buildingsLevels.put(KEY_HOOK_LEVEL, cursor.getInt(1));
            buildingsLevels.put(KEY_STOREROOM_LEVEL, cursor.getInt(2));
            buildingsLevels.put(KEY_FURNACE_LEVEL, cursor.getInt(3));
            buildingsLevels.put(KEY_FACTORY_LEVEL, cursor.getInt(4));
            buildingsLevels.put(KEY_FLATS_LEVEL, cursor.getInt(5));
            buildingsLevels.put(KEY_PIPELINE_LEVEL, cursor.getInt(6));
            buildingsLevels.put(KEY_MINE_LEVEL, cursor.getInt(7));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik posiada: " + buildingsLevels.toString() + " poziomy budynkow");

        return buildingsLevels;
    }

    /**
     * UPDATE COMPUTER LEVEL
     *
     * @param computerLevel computer's level
     * @return return true if id > 0 else return false
     */
    public boolean updateComputerLevel(int computerLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMPUTER_LEVEL, computerLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE HOOK LEVEL
     *
     * @param hookLevel hook's level
     * @return return true if id > 0 else return false
     */
    public boolean updateHookLevel(int hookLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_HOOK_LEVEL, hookLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE STOREROOM LEVEL
     *
     * @param storeroomLevel storeroom's level
     * @return return true if id > 0 else return false
     */
    public boolean updateStoreroomLevel(int storeroomLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STOREROOM_LEVEL, storeroomLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE FURNACE LEVEL
     *
     * @param furnaceLevel furnace's level
     * @return return true if id > 0 else return false
     */
    public boolean updateFurnaceLevel(int furnaceLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FURNACE_LEVEL, furnaceLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE FACTORY LEVEL
     *
     * @param factoryLevel factory's level
     * @return return true if id > 0 else return false
     */
    public boolean updateFactoryLevel(int factoryLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FACTORY_LEVEL, factoryLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE FLATS LEVEL
     *
     * @param flatsLevel flats's level
     * @return return true if id > 0 else return false
     */
    public boolean updateFlatsLevel(int flatsLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FLATS_LEVEL, flatsLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE PIPELINE LEVEL
     *
     * @param pipelineLevel pipeline's level
     * @return return true if id > 0 else return false
     */
    public boolean updatePipelineLevel(int pipelineLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PIPELINE_LEVEL, pipelineLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE MINE LEVEL
     *
     * @param mineLevel mine's level
     * @return return true if id > 0 else return false
     */
    public boolean updateMineLevel(int mineLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MINE_LEVEL, mineLevel);

        long id = db.update(TABLE_LEVELS, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * GET USER'S AMOUNTS
     *
     * @return amounts
     */
    public HashMap<String, Integer> getAmounts() {
        HashMap<String, Integer> amounts = new HashMap<>();
        String selectQuery = "SELECT coal_amount, pipe_amount, electricity_amount FROM " + TABLE_STOREROOM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            amounts.put(KEY_COAL_AMOUNT, cursor.getInt(0));
            amounts.put(KEY_PIPE_AMOUNT, cursor.getInt(1));
            amounts.put(KEY_ELECTRICITY_AMOUNT, cursor.getInt(2));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik posiada: " + amounts.toString() + " surowcow");

        return amounts;
    }

    /**
     * GET USER'S MAX
     *
     * @return max
     */
    public HashMap<String, Integer> getMax() {
        HashMap<String, Integer> max = new HashMap<>();
        String selectQuery = "SELECT coal_max, pipe_max, electricity_max FROM " + TABLE_STOREROOM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            max.put(KEY_COAL_MAX, cursor.getInt(0));
            max.put(KEY_PIPE_MAX, cursor.getInt(1));
            max.put(KEY_ELECTRICITY_MAX, cursor.getInt(2));
        }
        cursor.close();
        db.close();

        // Zwracanie wyniku
        Log.d(TAG, "Uzytkownik posiada: " + max.toString() + " surowcow");

        return max;
    }

    /**
     * UPDATE COAL AMOUNT
     *
     * @param coalAmount coal's amount
     * @return return true if id > 0 else return false
     */
    public boolean updateCoalAmount(int coalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_AMOUNT, coalAmount);

        long id = db.update(TABLE_STOREROOM, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE COAL MAX
     *
     * @param coalMax coal's max
     * @return return true if id > 0 else return false
     */
    public boolean updateCoalMax(int coalMax) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COAL_MAX, coalMax);

        long id = db.update(TABLE_STOREROOM, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE PIPE AMOUNT
     *
     * @param pipeAmount pipe's amount
     * @return return true if id > 0 else return false
     */
    public boolean updatePipeAmount(int pipeAmount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PIPE_AMOUNT, pipeAmount);

        long id = db.update(TABLE_STOREROOM, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE PIPE MAX
     *
     * @param pipeMax pipe's max
     * @return return true if id > 0 else return false
     */
    public boolean updatePipeMax(int pipeMax) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STOREROOM_LEVEL, pipeMax);

        long id = db.update(TABLE_STOREROOM, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE ELECTRICITY AMOUNT
     *
     * @param electricityAmount electricity's amount
     * @return return true if id > 0 else return false
     */
    public boolean updateElectricityAmount(int electricityAmount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ELECTRICITY_AMOUNT, electricityAmount);

        long id = db.update(TABLE_STOREROOM, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * UPDATE ELECTRICITY MAX
     *
     * @param electricityMax electricity's max
     * @return return true if id > 0 else return false
     */
    public boolean updateElectricityMax(int electricityMax) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ELECTRICITY_MAX, electricityMax);

        long id = db.update(TABLE_STOREROOM, contentValues, KEY_ID + "=" + 1, null);
        return id > 0;
    }

    /**
     * Usuwanie wszystkich informacji o użytkowniku - tylko podczas wylogowania
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Usuwanie wszystkich wierszy
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_COAL, null, null);
        db.delete(TABLE_PIPE, null, null);
        db.delete(TABLE_LEVELS, null, null);
        db.delete(TABLE_STOREROOM, null, null);
        db.close();

        Log.d(TAG, "Usuwanie wszystkich informacji o uzytkowniku z SQLite.");
    }

    public int getPrice(int column)
    {
        int value=0;

        String selectQuery = "SELECT * FROM " + TABLE_PRICES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            value = cursor.getInt(column);
        }
        cursor.close();
        db.close();

        return value;
    }

    public void updatePrice(int column)
    {
        String selectQuery = "SELECT * FROM " + TABLE_PRICES;

        int temp = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            temp = (int) (cursor.getInt(column) * 1.5);
            ContentValues contentValues = new ContentValues();
            contentValues.put(cursor.getColumnName(column),temp);
        }
        cursor.close();
        db.close();
    }
}