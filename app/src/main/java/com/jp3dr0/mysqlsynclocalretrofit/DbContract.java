package com.jp3dr0.mysqlsynclocalretrofit;

// ESSA CLASSE DEFINE ALGUMAS CONSTANTES QUE VAO SER UTILIZADAS NO BANCO DE DADOS DO SQLITE
public class DbContract {
    public static final int SYNC_STATUS_FAILED = 0;
    public static final int SYNC_STATUS_OK = 1;
    public static final String SERVER_URL = "http://contatinhos.000webhostapp.com/syncdemo/";

    public static final String UI_UPDATE_BROADCAST = "com.jp3dr0.mysqlsynclocalretrofit.uiupdatebroadcast";

    public static final String DATABASE_NAME = "contactdb"; // nome do banco de dados
    public static final String TABLE_NAME = "contactinfo"; // nome da tabela
    public static final String NAME = "name"; // nome da primeira coluna da tabela
    public static final String SYNC_STATUS = "syncstatus"; // nome da segundo coluna da tabela
    public static final String ID = "id";
}