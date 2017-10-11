package com.jp3dr0.mysqlsynclocalretrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.jp3dr0.mysqlsynclocalretrofit.MainActivity.dbHelper;

public class DbHelper extends SQLiteOpenHelper {

    // Versão do banco de dados SQLite
    private static final int DATABASE_VERSION = 1;
    // Comando SQL para criar a tabela
    private static final String CREATE_TABLE = "CREATE TABLE " + DbContract.TABLE_NAME +
            "(" + DbContract.ID + " integer primary key autoincrement," + DbContract.NAME + " text," + DbContract.SYNC_STATUS +
            " integer);";
    // Comando SQL para deletar a tabela
    private static final String DROP_TABLE = "drop table if exists " + DbContract.TABLE_NAME;

    // CONSTRUTOR - só precisa passar de parametro o contexto, o resto vai pegar aqui dessa classe
    public DbHelper(Context context) {
        super(context, DbContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance(Context context) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (dbHelper == null) {
            dbHelper = new DbHelper(context);
        }
        return dbHelper;
    }

    // função de callback chamada quando o banco for criado
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE); // executa o comando sql de criar a tabela
    }

    // função de callback chamada quando o banco for atualizado, para "reiniciar" o banco
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // exclui e cria dnv a tabela
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    // metodo para salvar dados no banco local (CREATE)

    public boolean saveToLocalDatabase(SQLiteDatabase sqLiteDatabase, String name, int sync_status) {
        return saveToLocalDatabase(sqLiteDatabase, 0, name, sync_status);
    }

    public boolean saveToLocalDatabase (SQLiteDatabase sqLiteDatabase, int id, String name, int sync_status) {
        ContentValues contentValues = new ContentValues(); // objeto para aramazenar os valores, vai usando o put para colocar cada coluna e o seu valor
        contentValues.put(DbContract.ID, id);
        contentValues.put(DbContract.NAME, name); // coluna name, passando o valor
        contentValues.put(DbContract.SYNC_STATUS, sync_status); // coluna sync_status, passando o valor
        //sqLiteDatabase.insert(DbContract.TABLE_NAME, null, contentValues); // executando o insert no banco, passando como parametro a tabela e os valores
        String[] id_string = new String[]{ id + "" };
        return sqLiteDatabase.insert(DbContract.TABLE_NAME, null, contentValues) > 0;
//        if(id > 0)
//            return sqLiteDatabase.insert(DbContract.TABLE_NAME, contentValues, "ID=?", id_string) > 0;
//        else
//            return sqLiteDatabase.insert(DbContract.TABLE_NAME, null, contentValues) > 0;
    }

    // metodo para ler os dados do banco local (retorna um cursor com os dados) (READ)

    public Cursor readFromLocalDatabase (SQLiteDatabase sqLiteDatabase){
        String[] projection = {DbContract.ID, DbContract.NAME , DbContract.SYNC_STATUS}; // array com as colunas que queremos percorrer
        //Cursor cursor = sqLiteDatabase.query(DbContract.TABLE_NAME, projection, null, null, null, null, null); // passa como parametro o nome da tabela, as colunas (array) e percorrer
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbContract.TABLE_NAME, null);
        return cursor;

//        List<Cliente> clientes = new ArrayList<>();
//        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Clientes", null);
//        while(cursor.moveToNext()){
//            int id = cursor.getInt(cursor.getColumnIndex("ID"));
//            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
//            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
//            String uf = cursor.getString(cursor.getColumnIndex("UF"));
//            boolean vip = cursor.getInt(cursor.getColumnIndex("Vip")) > 0;
//            clientes.add(new Cliente(id, nome, sexo, uf, vip));
//        }
//        cursor.close();
//        return clientes;
    }

    public int retornarUltimoId(SQLiteDatabase sqLiteDatabase) {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbContract.TABLE_NAME + " ORDER BY " + DbContract.ID + " DESC", null);
        if(cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DbContract.ID));
            cursor.close();
            return id;
        }
        return 0;
    }

    // metodo para atualizar o banco local (UPDATE)

    public boolean updateLocalDatabase(SQLiteDatabase sqLiteDatabase, String name, int sync_status) {
        ContentValues contentValues = new ContentValues(); // objeto para aramazenar os valores, vai usando o put para colocar cada coluna e o seu valor
        contentValues.put(DbContract.SYNC_STATUS, sync_status); // coluna sync_status, passando o valor
        String selection = DbContract.NAME + " LIKE ?"; // coluna que terá o valor escolhido para atualizar (geralmente o id pk)
        String[] selection_args = {name}; // parametro do like do sql
        return sqLiteDatabase.update(DbContract.TABLE_NAME, contentValues, selection, selection_args) > 0; // executando o update no banco, passando como parametro a tabela e os valores
    }

    public boolean updateLocalDatabase(SQLiteDatabase sqLiteDatabase, int id, String name, int sync_status) {
        ContentValues contentValues = new ContentValues(); // objeto para aramazenar os valores, vai usando o put para colocar cada coluna e o seu valor
        contentValues.put(DbContract.SYNC_STATUS, sync_status); // coluna sync_status, passando o valor
        contentValues.put(DbContract.NAME, name);
        String selection = DbContract.ID + " LIKE ?"; // coluna que terá o valor escolhido para atualizar (geralmente o id pk)
        String id_string = id + "";
        String[] selection_args = {id_string}; // parametro do like do sql
        return sqLiteDatabase.update(DbContract.TABLE_NAME, contentValues, selection, selection_args) > 0; // executando o update no banco, passando como parametro a tabela e os valores
    }

    // metodo para deletar dados do banco local (DELETE)

    public boolean deleteContactFromLocalDatabase(SQLiteDatabase sqLiteDatabase, int id) {
        String[] id_string = new String[]{ id + "" };
        return sqLiteDatabase.delete(DbContract.TABLE_NAME, DbContract.ID + "=?", id_string) > 0;
    }

    public boolean deleteContactFromLocalDatabase(SQLiteDatabase sqLiteDatabase, String name) {
        String[] name_string = new String[]{ name + "" };
        return sqLiteDatabase.delete(DbContract.TABLE_NAME, DbContract.NAME + "=?", name_string) > 0;
    }

    // metodo para deletar todos os registros da tabela (truncate)
    public void truncateContacts(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

}