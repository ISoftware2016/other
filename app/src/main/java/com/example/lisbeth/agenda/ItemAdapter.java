package com.example.lisbeth.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.uagrm.agenda.Contacto;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

/**
 * Created by Lisbeth on 19/05/2016.
 */
public class ItemAdapter extends ArrayAdapter<Contacto> implements Filterable {
    //private
    private ArrayList<Contacto> list;
    private ArrayList<Contacto> listContactos;
    private Context context;
    public Filter filterContacto;

    public ItemAdapter(Context context, ArrayList<Contacto> arrayList) {
        super(context, R.layout.item_contacto, arrayList);
        this.context = context;
        this.list = arrayList;
        this.listContactos = arrayList;
    }

    public void resetData() {
        listContactos = list;
    }

    @Override
    public int getCount() {
        return listContactos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ContactoHolder holder = new ContactoHolder();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_contacto, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView_item);
            TextView nombre = (TextView) view.findViewById(R.id.text_item_nombre);
            TextView telefono = (TextView) view.findViewById(R.id.text_item_telefono);

            holder.imageView = imageView;
            holder.textNombre = nombre;
            holder.textTelefono=telefono;
            view.setTag(holder);

        } else {
            holder=(ContactoHolder)view.getTag();
        }

        Contacto contacto=listContactos.get(position);

        if (contacto.imagen == null) {
            holder.imageView.setImageResource(R.drawable.nuevo_contacto);
        } else {

            holder.imageView.setImageBitmap(contacto.getImagen());
        }
        holder.textNombre.setText(contacto.getNombreCompleto());
        holder.textTelefono.setText(contacto.getTelefono());

        /*View view = LayoutInflater.from(context).inflate(R.layout.item_contacto, null);
        Contacto contacto = list.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_item);
        TextView nombre = (TextView) view.findViewById(R.id.text_item_nombre);
        TextView telefono = (TextView) view.findViewById(R.id.text_item_telefono);
        if (contacto.imagen == null) {
            imageView.setImageResource(R.drawable.nuevo_contacto);
        } else {

            imageView.setImageBitmap(contacto.getImagen());
        }
        nombre.setText(contacto.getNombreCompleto());
        telefono.setText(contacto.getTelefono());
        return view;*/
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filterContacto == null) {
            filterContacto = new ContactoFilter();

        }

        return filterContacto;
    }

    public  ArrayList<Contacto> getLista(){
        return  listContactos;
    }
    public class ContactoFilter extends Filter {

        public ArrayList<Contacto> lContacto;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<Contacto> listaFiltrada = new ArrayList<Contacto>();
                for (Contacto contacto : listContactos) {
                    if (contacto.getNombre().toUpperCase().startsWith(constraint.toString().toUpperCase()) ||
                            contacto.getApellido().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        listaFiltrada.add(contacto);
                }
                results.values = listaFiltrada;
                results.count = listaFiltrada.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                notifyDataSetInvalidated();
                listContactos = new ArrayList<Contacto>();
                Toast.makeText(getContext(),"No se han encontrado resultados",Toast.LENGTH_SHORT).show();
            } else {
                listContactos = (ArrayList<Contacto>) results.values;
                notifyDataSetChanged();
            }
            lContacto=listContactos;
        }


    }

    private class ContactoHolder {
        ImageView imageView;
        TextView textNombre;
        TextView textTelefono;
    }
}
