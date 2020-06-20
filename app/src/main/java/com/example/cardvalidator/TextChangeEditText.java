package com.example.cardvalidator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import java.util.ArrayList;

import static com.example.cardvalidator.MainActivity.btn_submit;
import static com.example.cardvalidator.MainActivity.card_number_et;

public class TextChangeEditText implements TextWatcher {


    //Array Lists for storing codes of Master and American cards
    private ArrayList<Character> master_card_list=new ArrayList<>();
    private ArrayList<Character> american_card_list=new ArrayList<>();


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        //Master Card second digits
        master_card_list.add('1');
        master_card_list.add('2');
        master_card_list.add('3');
        master_card_list.add('4');
        master_card_list.add('5');


        //American card second digits
        american_card_list.add('4');
        american_card_list.add('7');

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {


        int length=editable.length();

        //Enabling button when size reaches 16 (adding spaces also)
        if (editable.length() >= 19) {
            btn_submit.setVisibility(View.VISIBLE);
            return;
        }


        //Setting by default image
        card_number_et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_card, 0);

        //Setting card image according to code
        setCardImages(length,editable);


        //Setting Submit button disable if length is less than 16
        btn_submit.setVisibility(View.GONE);


        //Setting spaces in edit text box
        for (int i = 4; i < editable.length(); i += 5) {
            if (editable.toString().charAt(i) != ' ') {
                editable.insert(i, " ");
            }
        }

    }




    //Setting card images according to number
    private void setCardImages(int length, Editable editable) {


        //If card is VISA
        if (length>0 && editable.toString().charAt(0)=='4'){
            card_number_et
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visa_card, 0);
        }


        //If card is master
        else if(length>1 && editable.toString().charAt(0)=='5'
                && master_card_list.contains(editable.charAt(1))){
            card_number_et
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.master_card, 0);

        }


        //If card is American Express
        else if (length>1 && editable.toString().charAt(0)=='3'
                && american_card_list.contains(editable.toString().charAt(1))){

            card_number_et
                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.american_card, 0);

        }


        //By default card
        else {
            card_number_et.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_card, 0);
        }


    }
}
