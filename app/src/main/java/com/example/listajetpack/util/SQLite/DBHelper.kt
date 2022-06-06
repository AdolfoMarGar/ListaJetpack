package com.example.listajetpack.util.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val BD = "BASE DATOS"
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_NOM + " TEXT PRIMARY KEY, " +
                ROL_COL + " TEXT," +
                REG_COL + " TEXT," +
                URL_COL + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int){}

    //Corecto
    fun insert(nom : String, rol : String , reg : String , url : String ){
        try {
            val values = ContentValues()

            values.put(ID_NOM, nom)
            values.put(ROL_COL, rol)
            values.put(REG_COL, reg)
            values.put(URL_COL, url)

            val db = this.writableDatabase

            db.insert(TABLE_NAME, null, values)

            db.close()
            Log.v(BD,"insert realizado")

        }catch (e:Exception){
            Log.d(BD,"Error al insert")
        }

    }

    fun getNom(): ArrayList<String> {
        val db = this.readableDatabase
        val users = ArrayList<String>()
        val cols = arrayOf<String>(ID_NOM)
        val c = db.query(TABLE_NAME, cols, null, null, null, null, null)

        if (c.moveToFirst()) {
            do {
                val user = c.getString(0)
                users.add(user)
            } while (c.moveToNext())
        }
        return users
    }
    fun getRol(): ArrayList<String> {
        val db = this.readableDatabase
        val users = ArrayList<String>()
        val cols = arrayOf<String>(ROL_COL)
        val c = db.query(TABLE_NAME, cols, null, null, null, null, null)

        if (c.moveToFirst()) {
            do {
                val user = c.getString(0)
                users.add(user)
            } while (c.moveToNext())
        }
        return users
    }
    fun getReg(): ArrayList<String> {
        val db = this.readableDatabase
        val users = ArrayList<String>()
        val cols = arrayOf<String>(REG_COL)
        val c = db.query(TABLE_NAME, cols, null, null, null, null, null)

        if (c.moveToFirst()) {
            do {
                val user = c.getString(0)
                users.add(user)
            } while (c.moveToNext())
        }
        return users
    }
    fun getUrl(): ArrayList<String> {
        val db = this.readableDatabase
        val users = ArrayList<String>()
        val cols = arrayOf<String>(URL_COL)
        val c = db.query(TABLE_NAME, cols, null, null, null, null, null)

        if (c.moveToFirst()) {
            do {
                val user = c.getString(0)
                users.add(user)
            } while (c.moveToNext())
        }
        return users
    }

    fun delete(id: String) {
        val db = this.writableDatabase
        val delete =
            "DELETE FROM " + TABLE_NAME + " WHERE "+ ID_NOM+" like '" + id+"'"
        db.execSQL(delete)
        db.close()
    }

    companion object{
        private val DATABASE_NAME = "BD_SQLITE"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "campeones_tabla"

        val ID_NOM = "nom"
        val ROL_COL = "rol"
        val REG_COL = "reg"
        val URL_COL = "url"
    }
}