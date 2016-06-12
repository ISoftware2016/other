package com.edu.uagrm.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Lisbeth on 19/05/2016.
 */
public class Contacto implements Parcelable {
    private static final long serialVersionUID = 46543445;

    public int id;
    public Bitmap imagen;
    public String nombre;
    public String apellido;
    public String telefono;
    public String direccion;
    public String email;
    public boolean tieneImagen;

    public Contacto() {
    }

    public Contacto(int id, String nombre, String apellido, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Contacto(int id, Bitmap imagen, String nombre, String apellido, String telefono) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Contacto(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Contacto(int id, Bitmap imagen, String nombre, String apellido, String telefono, boolean tieneImagen) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.tieneImagen = tieneImagen;
    }

    public Contacto(int id, Bitmap imagen, String nombre, String apellido, String telefono, String direccion, String email, boolean tieneImagen) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.tieneImagen = tieneImagen;
    }

    public Contacto(int id, String imagen, String nombre, String apellido, String telefono, String direccion, String email) {
        this.id = id;
        this.imagen = stringToBitmap(imagen);
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;

        this.direccion = direccion;
        this.email = email;
    }

    static Bitmap stringToBitmap(String imagen) {
        try {
            byte[] bytes = Base64.decode(imagen, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public String getImagenString() {
        if (imagen == null) {
            return "";
        }
        return bitmapToString(imagen);
    }

    static String bitmapToString(Bitmap imagen) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isTieneImagen() {
        return tieneImagen;
    }

    public void setTieneImagen(boolean tieneImagen) {
        this.tieneImagen = tieneImagen;
    }


    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public Contacto(Parcel parcel) {
        this.id = parcel.readInt();
        this.imagen = (Bitmap) parcel.readValue(Bitmap.class.getClassLoader());

        this.nombre = parcel.readString();
        this.apellido = parcel.readString();
        this.telefono = parcel.readString();
        this.direccion = parcel.readString();
        this.email = parcel.readString();
    }

    public static final Parcelable.Creator<Contacto> CREATOR = new Parcelable.Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel source) {
            return new Contacto(source);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }

    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(imagen);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(telefono);
        dest.writeString(direccion);
        dest.writeString(email);

    }
}
