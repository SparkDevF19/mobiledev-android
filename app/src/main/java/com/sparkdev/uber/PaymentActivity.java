package com.sparkdev.uber;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity{

    public static final String VISA_PREFIX = "4";
    public static final String MASTERCARD_PREFIX = "51,52,53,54,55,";
    public static final String DISCOVER_PREFIX = "6011";
    public static final String AMEX_PREFIX = "34,37,";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button button = (Button)findViewById(R.id.addCardButt);
        button.setOnClickListener(buttListener);
    }

    private View.OnClickListener buttListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (validateCardName() && validateCardNum() && validateCSV() && validateExpireDate()) {
                //all fields pass, save data to database & continue onto next page
            }
        }
    };

    private boolean validateCardNum() { //checks if field is valid and makes cc type show
        EditText ccNumInput = (EditText) findViewById(R.id.ccNum);
        String ccNum = ccNumInput.getText().toString().trim();

        ImageView img= (ImageView) findViewById(R.id.cardType);

        if (ccNum.isEmpty()) {
            ccNumInput.setError("This field can't be empty.");
            return false;
        }
        else if (ccNum.length() != 16) {
            ccNumInput.setError("This field must be 16 characters long.");
            return false;
        }
        else {
            if(getCardType(ccNum).equals("visa")) { //shows visa picture
                img.setImageResource(R.drawable.ic_visa);
                img .setVisibility(View.VISIBLE);
                return true;
            }
            else if(getCardType(ccNum).equals("mastercard")) { //shows mastercard picture
                img.setImageResource(R.drawable.ic_mastercard);
                img .setVisibility(View.VISIBLE);
                return true;
            }
            else if(getCardType(ccNum).equals("amex")) { //shows amex picture
                img.setImageResource(R.drawable.ic_amex);
                img .setVisibility(View.VISIBLE);
                return true;
            }
            else if(getCardType(ccNum).equals("discover")) { //shows discover picture
                img.setImageResource(R.drawable.ic_discover);
                img .setVisibility(View.VISIBLE);
                return true;
            }
            else if(getCardType(ccNum).equals("none")) {
                img .setVisibility(View.GONE);
                ccNumInput.setError("Invalid credit card number.");
                return false;
            }
        }
        return true;
    }

    public static String getCardType(String cardNumber) { //returns type of credit card

        if (cardNumber.substring(0, 1).equals(VISA_PREFIX))
            return "visa";
        else if (MASTERCARD_PREFIX.contains(cardNumber.substring(0, 2) + ","))
            return "mastercard";
        else if (AMEX_PREFIX.contains(cardNumber.substring(0, 2) + ","))
            return "amex";
        else if (cardNumber.substring(0, 4).equals(DISCOVER_PREFIX))
            return "discover";
        return "none";
    }

    private boolean validateCardName() { //makes sure field is valid
        EditText ccNameInput = (EditText) findViewById(R.id.ccName);
        String ccName = ccNameInput.getText().toString().trim();

        if (ccName.isEmpty()) {
            ccNameInput.setError("This field can't be empty.");
            return false;
        }
        return true;
    }

    private boolean validateExpireDate() { //makes sure field is valid
        EditText ccExpireInput = (EditText) findViewById(R.id.expire);
        String expire = ccExpireInput.getText().toString().trim();

        if (expire.isEmpty()) {
            ccExpireInput.setError("This field can't be empty.");
            return false;
        }
        else if (expire.length() != 4) {
            ccExpireInput.setError("This field must be 4 characters long.");
            return false;
        }
        else {
            if (checkDate(expire).equals("invalid month")) {
                ccExpireInput.setError("This is an invalid month.");
                return false;
            } else if (checkDate(expire).equals("expired")) {
                ccExpireInput.setError("This card is expired.");
                return false;
            } else if (checkDate(expire).equals("valid")) {
                return true;
            }
        }
        return true; //not needed but to satisfy function
    }

    public String checkDate(String expireDate) { //checks if expiration date is active
        Calendar c = Calendar.getInstance();
        int mm = c.get(Calendar.MONTH);
        int yy = c.get(Calendar.YEAR);

        int userMM = Integer.parseInt(expireDate.substring(0, 2));
        int userYY = Integer.parseInt(expireDate.substring(2, 4));

        if (userMM > 12) { //only 12 months exist
            return "invalid month";
        }
        else if (userYY < yy) { //expired year
            return "expired";
        }
        else if (userYY == yy) { //same year
            if (userMM < mm) { //we check if expired month
                return "expired";
            }
        }
        return "valid";
    }

    private boolean validateCSV() { //checks if field is valid
        EditText csvInput = (EditText) findViewById(R.id.csv);
        String csv = csvInput.getText().toString().trim();

        if (csv.isEmpty()) {
            csvInput.setError("This field can't be empty.");
            return false;
        }
        else if (csv.length() != 3) {
            csvInput.setError("This field must be 3 characters long.");
            return false;
        }
        return true;
    }
}