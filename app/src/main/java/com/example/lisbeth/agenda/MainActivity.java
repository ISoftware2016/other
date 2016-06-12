package com.example.lisbeth.agenda;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.edu.uagrm.agenda.Contacto;
import com.edu.uagrm.agenda.DataBaseContacto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 1010;
    private View view;
    private ArrayList<Contacto> listaContactos;
    private ArrayList<Contacto> listaContactosSearch;
    private ListView listView;
    private DataBaseContacto baseContacto;
    private ItemAdapter itemAdapter;


    private EditText editText;
    //private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivity();

    }

    private void startActivity() {
        listView = (ListView) findViewById(R.id.listView);
        baseContacto = new DataBaseContacto(this);
        listaContactos = baseContacto.getListaContantos();
        if (!listaContactos.isEmpty()) {
            itemAdapter = new ItemAdapter(this, listaContactos);
            listView.setAdapter(itemAdapter);
        }

        registerForContextMenu(listView);
        listView.setTextFilterEnabled(true);
  /*      searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(newText.toString());
                return false;
            }
        });*/
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    itemAdapter.resetData();
                }
                itemAdapter.getFilter().filter(s.toString());
                listView.setAdapter(itemAdapter);
                listaContactos = itemAdapter.getLista();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemAdapter.ContactoFilter x = (ItemAdapter.ContactoFilter) itemAdapter.filterContacto;
                if (x != null) {
                    listaContactos = x.lContacto;
                }
                //Contacto contactoSelected = (Contacto) listView.getAdapter().getItem(position);
                Contacto contactoSelected = listaContactos.get(position);
                Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contactoSelected.getTelefono()));
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intentLlamada);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ItemAdapter.ContactoFilter x = (ItemAdapter.ContactoFilter) itemAdapter.filterContacto;
                if (x != null) {
                    listaContactos = x.lContacto;
                }
                //Contacto contactoSelected = (Contacto) listView.getAdapter().getItem(position);
                Contacto contactoSelected = listaContactos.get(position);
                //Toast.makeText(getBaseContext(),contactoSelected.getTelefono(),Toast.LENGTH_SHORT).show();
                editarContacto(contactoSelected);
                return true;
            }
        });

    }

    private void editarContacto(final Contacto contacto) {
        try {
            String[] strings = new String[]{"Modificar Contacto", "Eliminar Contacto"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccione una opción");
            builder.setItems(strings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogInterface(which, contacto);
                }
            }).show();
        } catch (Exception e) {
        }

    }

    private void dialogInterface(int which, Contacto contacto) {
        if (which == 0) {
          /*  Intent intent = new Intent(this, ContactoActivity.class);
            intent.putExtra("ContactoSelected", contacto);
            startActivity(intent);*/
            Bundle bundle = new Bundle();
            bundle.putParcelable("Contacto", contacto);
            startActivity(new Intent(this, ContactoActivity.class).putExtras(bundle));
        } else {
            baseContacto.eliminarContacto(contacto);
            Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
            actualizarListView();
            itemAdapter.notifyDataSetChanged();
        }
    }

    private void actualizarListView() {
        listaContactos = baseContacto.getListaContantos();

        itemAdapter = new ItemAdapter(this, listaContactos);
        listView.setAdapter(itemAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_nuevo_contacto) {
            Intent intent = new Intent(this, ContactoActivity.class);
            startActivity(intent);
            return true;
        } else {
            if (id == R.id.action_adicionar_contacto) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case PICK_CONTACT_REQUEST:
                    Uri uri = data.getData();
                    Intent intent = new Intent(this, ContactoActivity.class);
                    intent.putExtra("uri", uri.toString());
                    startActivity(intent);
                    break;
            }
        }
    }


}
