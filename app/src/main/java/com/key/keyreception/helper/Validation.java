package com.key.keyreception.helper;

/**
 * Created by Ravi Birla on 17,December,2018
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import com.key.keyreception.R;

import java.io.File;


public class Validation {
    private Context context;

    public Validation(Context context) {
        this.context = context;
    }

    private String getString(EditText editText){
        return editText.getText().toString();
    }

    /*public boolean isFullNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
            editText.setError(context.getString(R.string.fullNameEmptyError));
            editText.requestFocus();
            return false;
        } else if (!(editText.length() >= 3)) {
            editText.setError(context.getString(R.string.fullNameLengthError));
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }*/

    //New Code
    public boolean isFirstNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.firstNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.firstNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(editText.length() >= 3)) {
//            editText.setError(context.getString(R.string.firstNameLengthError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.firstNameLengthError), Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }
    }


    public boolean iscoNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.firstNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.coNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }

    public boolean isSocialValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.firstNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.ssnEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }

    public boolean isCategoryValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.firstNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.categoryEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean isDriverImageUpload(Bitmap profileImageBitmap){
        if (profileImageBitmap==null){
            // Utility.showToast(context,"Please select profile picture",0);
            Toast.makeText(context, context.getString(R.string.dlEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }



    // New Code
    public boolean isLastNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.lastNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(editText.length() >= 3)) {
            editText.setError(context.getString(R.string.lastNameLengthError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.lastNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


//    public boolean isFullNValid(EditText editText,Animation shake) {
//        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.fullNameEmptyError));
//            //editText.requestFocus();
//            editText.startAnimation(shake);
//            return false;
//        } else if (!(editText.length() >= 3)) {
//            editText.setError(context.getString(R.string.fullNameLengthError));
//            //editText.requestFocus();
//            editText.startAnimation(shake);
//            return false;
//        } else {
//            return true;
//        }
//
//    }


    public boolean isLastNValid(EditText editText,Animation shake) {
        if (getString(editText).isEmpty()) {
            editText.setError(context.getString(R.string.lastNameEmptyError));
            //editText.requestFocus();
            editText.startAnimation(shake);
            Toast.makeText(context, context.getString(R.string.lastNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(editText.length() >= 3)) {
            editText.setError(context.getString(R.string.lastNameLengthError));
            //editText.requestFocus();
            editText.startAnimation(shake);
            Toast.makeText(context, context.getString(R.string.lastNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    public boolean isEmailValid(EditText editText) {
        if (getString(editText).isEmpty()){
//            editText.setError(context.getString(R.string.emailEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.emailEmptyError), Toast.LENGTH_SHORT).show();

            return false;
        }else{
            boolean bool = android.util.Patterns.EMAIL_ADDRESS.matcher(getString(editText)).matches();
            if (!bool) {
//                editText.setError(context.getString(R.string.emailInvalidError));
//                editText.requestFocus();
                Toast.makeText(context, context.getString(R.string.emailInvalidError), Toast.LENGTH_SHORT).show();

            }
            return bool;
        }
    }

    public boolean isaddNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.addNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean isPasswordValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.passEmptyError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.passEmptyError), Toast.LENGTH_SHORT).show();

            return false;
        }
       /* else if (editText.getText().toString().contains(" ")) {
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.passspace), Toast.LENGTH_SHORT).show();
            return false;
        }*/
        else if (editText.getText().length() >= 6) {
            editText.requestFocus();
            return true;
        }
        else {
//            editText.setError(context.getString(R.string.passLengthError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.passLengthError), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isImageUpload(Bitmap profileImageBitmap){
        if (profileImageBitmap==null){
           // Utility.showToast(context,"Please select profile picture",0);
            Toast.makeText(context, "Please select profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }


    /*.................................isEmpty......................................*/
    public boolean isEmpty(String textView) {
        if (textView.equals("") || textView.length() == 0) {
            return false;
        }
        return true;
    }


    public boolean isoldPasswordValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.passEmptyError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.opassEmptyError), Toast.LENGTH_SHORT).show();

            return false;
        } else if (editText.getText().length() >= 6) {
            editText.requestFocus();
            return true;
        } else {
//            editText.setError(context.getString(R.string.passLengthError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.passLengthError), Toast.LENGTH_SHORT).show();
            return false;
        }
    } public boolean isnewPasswordValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.passEmptyError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.npassEmptyError), Toast.LENGTH_SHORT).show();

            return false;
        } else if (editText.getText().length() >= 6) {
            editText.requestFocus();
            return true;
        } else {
//            editText.setError(context.getString(R.string.passLengthError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.passLengthError), Toast.LENGTH_SHORT).show();
            return false;
        }
    } public boolean isconfirmPasswordValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.passEmptyError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.cpassEmptyError), Toast.LENGTH_SHORT).show();

            return false;
        } else if (editText.getText().length() >= 6) {
            editText.requestFocus();
            return true;
        } else {
//            editText.setError(context.getString(R.string.passLengthError));
            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.passLengthError), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean ispriceValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.priceEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isdateValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.dateEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isdesValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.desEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean ispropsizeValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.psEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isimagefileValid(File s) {
        if (s == null) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.imageEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isuploadImageModalArrayListValid(int s) {
        if (s == 0) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.imageEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    public boolean isbedroomValid(String s) {
        if (s.isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.bedEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    public boolean isbathroomValid(String s) {
        if (s.isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.bathEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isptypeValid(String s) {
        if (s.isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.ptypeEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean isserviceValid(String s) {
        if (s.isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.serEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean isselectpropValid(String s) {
        if (s.isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.spEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

   /* public boolean istitleNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.titleNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean isaddNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.addNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public boolean isdateNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.dateNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }

    public boolean isdesNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.desNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean iscatNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.catNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }


    public boolean isphoneNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.catNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }


    public boolean iszipNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.zipNameEmptyError), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }



    public boolean isbankNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.pbname), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }

    public boolean isahNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.pahname), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean isahLNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.pahlname), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean isahdobNameValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.pdob), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean isaccNumberValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.panumber), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean isrouNumberValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.prnumber), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean ispcodeValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.ppcode), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }
    public boolean isahtypeValid(EditText editText) {
        if (getString(editText).isEmpty()) {
//            editText.setError(context.getString(R.string.lastNameEmptyError));
//            editText.requestFocus();
            Toast.makeText(context, context.getString(R.string.pahtype), Toast.LENGTH_SHORT).show();
            return false;
        }  else {
            return true;
        }
    }*/

}
