package com.example.pc.tablaperiodica.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.pc.tablaperiodica.R;

/**
 * Created by pc on 22/09/2017.
 */

public class TableElements {


    private static String[] elementNames = {

            "", "Hidrógeno", "Helio", "Litio", "Berilio", "Boro", "Carbono",
            "Nitrógeno", "Oxígeno", "Flúor", "Neón", "Sodio", "Magnesio",
            "Aluminio", "Silicio", "Fósforo", "Azufre", "Cloro", "Argón", "Potasio",
            "Calcio", "Escandio", "Titanio", "Vanadio", "Cromo", "Manganeso",
            "Hierro", "Cobalto", "Niquel", "Cobre", "Zinc", "Galio",
            "Germanio", "Arsénico", "Selenio", "Bromo", "Kriptón", "Rubidio",
            "Estroncio", "Itrio", "Circonio", "Niobio", "Molibdeno", "Tecnecio",
            "Rutenio", "Rodio", "Paladio", "Plata", "Cadmio", "Indio",
            "Estaño", "Antimonio", "Telurio", "Yodo", "Xenón", "Cesio",
            "Bario", "Lantano", "Cerio", "Praseodimio", "Neodimio", "Prometio",
            "Samario", "Europio", "Gadolinio", "Terbio", "Disprosio", "Holmio",
            "Erbio", "Tulio", "Iterbio", "Lutecio", "Hafnio", "Tántalo",
            "Wolframio", "Renio", "Osmio", "Iridio", "Platino", "Oro",
            "Mercurio", "Talio", "Plomo", "Bismuto", "Polonio", "Astato",
            "Radón", "Francio", "Radio", "Actinio", "Torio", "Protactinio",
            "Uranio", "Neptunio", "Plutonio", "Americio", "Curio", "Berkelio",
            "Californio", "Einstenio", "Fermio", "Mendelevio", "Nobelio", "Lawrencio",
            "Rutherfordio", "Dubnio", "Seaborgio", "Bohrio", "Hasio", "Meitnerio",
            "Darmstatio", "Roentgenio", "Copernicio", "Nihonio", "Flerovio", "Moscovio",
            "Livermorio", "Teneso", "Oganessón"

    };

    private static String[] elementSymbols = {

            "", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al",
            "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn",
            "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb",
            "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In",
            "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm",
            "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta",
            "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At",
            "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk",
            "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt",
            "Ds", "Rg", "Cn", "Nh", "Fl", "Mc", "Lv", "Ts", "Og"

    };

    public static final int ELEMENT_COUNT = elementNames.length;


    public static String getElementSymbol(int index){
        if(index<0 || index>=118)
            return null;
        return elementSymbols[index];
    }

    public static String getElementName(int index){
        if(index < 0 || 118 <= index)
            return null;
        return elementNames[index];


    }

    public static View noElement(Context context){
        View view;
        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.element_view, null);
        view.setVisibility(View.INVISIBLE);
        return view;
    }

    public static View elementView(Context context, int number){
        if(number<=0 || number>118)
            return noElement(context);

        View view;
//        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.element_view, null);
        view = View.inflate(context, R.layout.element_view, null);

        TextView elementNumber = (TextView) view.findViewById(R.id.tv_atomic_number);
        TextView elementSymbol = (TextView) view.findViewById(R.id.tv_element_symbol);

        elementNumber.setText(String.valueOf(number));
        elementSymbol.setText(elementSymbols[number]);
        return view;
    }

}
