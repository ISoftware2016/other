package com.edu.uagrm.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Lisbeth on 21/05/2016.
 */
public class DataBaseContacto extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Contacto.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLA_NAME = "Contacto";
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    public static final String ID = "id";
    public static final String IMAGEN = "imagen";
    public static final String NOMBRE = "nombre";
    public static final String APELLIDO = "apellido";
    public static final String TELEFONO = "telefono";
    public static final String DIRECCION = "direccion";
    public static final String EMAIL = "email";

    public DataBaseContacto(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_NAME + " (" + ID + " " + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," + IMAGEN + " " + STRING_TYPE + "," + NOMBRE + " " + STRING_TYPE + "," + APELLIDO + " " + STRING_TYPE + "," + TELEFONO + " " + STRING_TYPE + "," + DIRECCION + " " + STRING_TYPE + "," + EMAIL + " " + STRING_TYPE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_NAME);
        onCreate(db);
    }

    public ArrayList<Contacto> getListaContantos() {
        SQLiteDatabase database = getWritableDatabase();
        ArrayList<Contacto> contactoArrayList = new ArrayList<Contacto>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLA_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id;
                String imagen, nombre, apellido, telefono, direccion, email;
                id = cursor.getInt(0);
                imagen = cursor.getString(1);
                nombre = cursor.getString(2);
                apellido = cursor.getString(3);
                telefono = cursor.getString(4);
                direccion = cursor.getString(5);
                email = cursor.getString(6);
                Contacto contacto = new Contacto(id, imagen, nombre, apellido, telefono, direccion, email);
                contactoArrayList.add(contacto);
            } while (cursor.moveToNext());
        }
        return contactoArrayList;
    }

    public void insertarContacto(Contacto contacto) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGEN, contacto.getImagenString());
        contentValues.put(NOMBRE, contacto.getNombre());
        contentValues.put(APELLIDO, contacto.getApellido());
        contentValues.put(TELEFONO, contacto.getTelefono());
        contentValues.put(DIRECCION, contacto.getDireccion());
        contentValues.put(EMAIL, contacto.getEmail());
        database.insert(TABLA_NAME, null, contentValues);
    }

    public int modificarContacto(Contacto contacto) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGEN, contacto.getImagenString());
        contentValues.put(NOMBRE, contacto.getNombre());
        contentValues.put(APELLIDO, contacto.getApellido());
        contentValues.put(TELEFONO, contacto.getTelefono());
        contentValues.put(DIRECCION, contacto.getDireccion());
        contentValues.put(EMAIL, contacto.getEmail());

        return database.update(TABLA_NAME, contentValues, ID + " = ?", new String[]{String.valueOf(contacto.getId())});
    }

    public void eliminarContacto(Contacto contacto) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLA_NAME, ID + " = ?", new String[]{String.valueOf(contacto.getId())});
    }
}
