package com.example.lisbeth.agenda;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.edu.uagrm.agenda.Contacto;
import com.edu.uagrm.agenda.DataBaseContacto;

import java.io.IOException;
import java.io.InputStream;

public class ContactoActivity extends AppCompatActivity implements View.OnClickListener {

    private final int CAMARA_REQUEST_CODE = 2222;
    private final int GALERIA_REQUEST_CODE = 3333;


    private boolean modificar = false;
    private Contacto modificarContacto;


    private static DataBaseContacto dbContacto;

    private ImageButton bCapturarImagen;
    private ImageButton bEliminarImagen;
    private Bitmap bImagen;
    private Uri uri;

    private ImageView imageView;
    private EditText textNombre;
    private EditText textApellido;
    private EditText textTelefono;
    private EditText textDireccion;
    private EditText textEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        Bundle bundle = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inicializar();
        if (bundle != null && bundle.containsKey("uri")) {
            String string = bundle.getString("uri");
            uri = Uri.parse(string);
            contacto();
        } else {
            if (bundle != null && bundle.containsKey("Contacto")) {
                modificarContacto = bundle.getParcelable("Contacto");
                modificar = true;
                setTitle("Modificar Contacto");
                cargarDatosContacto();
            }
        }


        botonImagen();

    }

    private void cargarDatosContacto() {
        imageView.setImageBitmap(modificarContacto.getImagen());
        if (modificarContacto.getImagen() != null) {
            bCapturarImagen.setVisibility(View.INVISIBLE);
            bEliminarImagen.setVisibility(View.VISIBLE);
        }
        textNombre.setText(modificarContacto.getNombre());
        textApellido.setText(modificarContacto.getApellido());
        textTelefono.setText(modificarContacto.getTelefono());
        textDireccion.setText(modificarContacto.getDireccion());
        textEmail.setText(modificarContacto.getEmail());
    }

    private void botonImagen() {
        if (imageView.getDrawable() != null) {
            if (getImagen(imageView) == null) {
                bCapturarImagen.setVisibility(View.VISIBLE);
                bEliminarImagen.setVisibility(View.INVISIBLE);
            } else {
                bCapturarImagen.setVisibility(View.INVISIBLE);
                bEliminarImagen.setVisibility(View.VISIBLE);
            }
        }

    }

    private void contacto() {
        textNombre.setText(getName(uri));
        textTelefono.setText(getPhone(uri));
        imageView.setImageBitmap(getPhoto(uri));
    }

    private void inicializar() {
        bImagen = null;
        modificar = false;
        bCapturarImagen = (ImageButton) findViewById(R.id.button_capturar);
        bEliminarImagen = (ImageButton) findViewById(R.id.button_eliminar);
        imageView = (ImageView) findViewById(R.id.imageView);
        textNombre = (EditText) findViewById(R.id.editText_nombre);
        textApellido = (EditText) findViewById(R.id.editText_apellido);
        textTelefono = (EditText) findViewById(R.id.text_telefono);
        textDireccion = (EditText) findViewById(R.id.text_direccion);
        textEmail = (EditText) findViewById(R.id.text_email);
        dbContacto = new DataBaseContacto(this);

        bCapturarImagen.setOnClickListener(this);
        bEliminarImagen.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacto, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_capturar:
                capturarImagen();
                break;
            case R.id.button_eliminar:
                eliminarImagen();
                break;
        }
    }

    private void eliminarImagen() {
        imageView.setImageResource(R.drawable.nuevo_contacto);
        bCapturarImagen.setVisibility(View.VISIBLE);
        bEliminarImagen.setVisibility(View.INVISIBLE);
    }

    private void capturarImagen() {
        try {
            String[] strings = new String[]{"Cámara", "Galería"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar Imagen");
            builder.setItems(strings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogInterface(which);
                }
            }).show();
        } catch (Exception e) {
        }
    }

    private void dialogInterface(int which) {
        Intent intent;
        if (which == 1) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALERIA_REQUEST_CODE);

        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMARA_REQUEST_CODE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_guardar_contacto:
                guardarContacto();
                return true;
            case R.id.action_modificar_contacto:
                modificarContacto();
                return true;

            case R.id.action_cancelar:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void modificarContacto() {
        if (validarDatos()) {
            //Contacto contacto = new Contacto();
            modificarContacto.setNombre(getString(textNombre));
            modificarContacto.setApellido(getString(textApellido));
            modificarContacto.setTelefono(getString(textTelefono));
            modificarContacto.setDireccion(getString(textDireccion));
            modificarContacto.setEmail(getString(textEmail));
            modificarContacto.setImagen(getImagen(imageView));

            dbContacto.modificarContacto(modificarContacto);
            mensaje("Contacto modificado");
            onBackPressed();

        } else {
            mensaje("Ingrese todos los datos requeridos");
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (modificar) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(true);
        } else {
            menu.getItem(0).setEnabled(true);
            menu.getItem(1).setEnabled(false);
        }
        return true;
    }

    private void guardarContacto() {
        if (validarDatos()) {
            Contacto contacto = new Contacto();
            contacto.setNombre(getString(textNombre));
            contacto.setApellido(getString(textApellido));
            contacto.setTelefono(getString(textTelefono));
            contacto.setDireccion(getString(textDireccion));
            contacto.setEmail(getString(textEmail));
            contacto.setImagen(getImagen(imageView));

            dbContacto.insertarContacto(contacto);
            mensaje("Contacto guardado");
            limpiarCampos();

        } else {
            mensaje("Ingrese todos los datos requeridos");
        }
    }

    private Bitmap getImagen(ImageView imageView) {
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        return bitmap;
    }

    private void limpiarCampos() {
        textNombre.setText("");
        textApellido.setText("");
        textTelefono.setText("");
        textDireccion.setText("");
        textEmail.setText("");
        imageView.setImageResource(R.drawable.nuevo_contacto);
        bCapturarImagen.setVisibility(View.VISIBLE);
        bEliminarImagen.setVisibility(View.INVISIBLE);
    }

    private void mensaje(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private boolean validarDatos() {
        if (getString(textNombre).length() > 0 && getString(textApellido).length() > 0 && getString(textTelefono).length() > 0
                && getString(textDireccion).length() > 0 && getString(textEmail).length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getString(EditText editText) {
        return editText.getText().toString();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ContactoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case CAMARA_REQUEST_CODE:
                    bImagen = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bImagen);
                    botonImagen();
                    break;
                case GALERIA_REQUEST_CODE:
                    Uri uri = data.getData();
                    String[] stringPath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, stringPath, null, null, null);
                    cursor.moveToFirst();

                    int columnaIndex = cursor.getColumnIndex(stringPath[0]);
                    String filePath = cursor.getString(columnaIndex);
                    cursor.close();

                    bImagen = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(bImagen);
                    botonImagen();
                    break;
            }
        }
    }


    private String getPhone(Uri uri) {

        String id = null;
        String phone = null;
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);


        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + "= " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                selectionArgs,
                new String[]{id},
                null
        );
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }
        phoneCursor.close();

        return phone;
    }

    private String getName(Uri uri) {
        String name = null;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if (c.moveToFirst()) {
            name = c.getString(0);
        }
        c.close();
        return name;
    }

    private Bitmap getPhoto(Uri uri) {
        Bitmap photo = null;
        String id = null;
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();
        try {
            Uri contactUri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    Long.parseLong(id));

            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    getContentResolver(),
                    contactUri);

            if (input != null) {
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) {
        }

        return photo;
    }


}
