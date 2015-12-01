package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {
    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox addWhipCream = (CheckBox) findViewById(R.id.Whip_cream_checkbox);
        CheckBox addChocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        EditText username = (EditText) findViewById(R.id.user_name);
        boolean hasWhip = addWhipCream.isChecked();
        boolean hasChocolate = addChocolate.isChecked();
        String name = username.getText().toString();
        int price = calculatePrice(hasWhip, hasChocolate);
        generateOrderSuammaryEmail(createOrderSummary(price, name, hasWhip, hasChocolate), name);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculatePrice(boolean addedWhip, boolean addedChocolate) {
        int basePrice = 5;
        if (addedWhip){
            basePrice = basePrice + 1;
        }
        if (addedChocolate) {
            basePrice = basePrice + 2;
        }
        return basePrice * quantity;

    }

    /**
     * Creates a summary of the order taken
     * @endPrice price passed in
     */

    private String createOrderSummary(int endPrice, String name, boolean addedWhip, boolean addedChocolate) {
        String priceMessage = getString(R.string.name_java) + " " + name;
        priceMessage += "\n" + getString(R.string.add_whip) + " " + addedWhip;
        priceMessage += "\n" + getString(R.string.add_chocolate) + " " + addedChocolate;
        priceMessage += "\n"+ getString(R.string.quantity_java) + " " +  quantity;
        priceMessage += "\n" + getString(R.string.total)+ endPrice;
        priceMessage += "\n" + getString(R.string.thank_you);
        return(priceMessage);
    }

    //this method will increase the order number
    public void increment(View view) {
        if (quantity == 100 ) {
            Toast.makeText(this, "I'm sorry, you can't buy more than 100 cups of coffee.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    //this method will decrease the order number
    public void decrement(View view) {
        if (quantity <= 1 ) {
            Toast.makeText(this, "I'm sorry, you can't buy less than 1 coffee.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * this method will generate an order summary email
     */

    private void generateOrderSuammaryEmail (String orderSummary, String name) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "JustJava Order Summary for" + " " + name);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        intent.setData(Uri.parse("mailto:"));

        //do not attempt to launch email if none exists
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "No email client on this device.", Toast.LENGTH_SHORT).show();
        }

    }
}