package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    // Global vars

    int quantity = 0;

    // END Global vars

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //
    // Start user triggered events.
    //

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedTopping = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean orderedWhippedTopping = whippedTopping.isChecked();

        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean orderedChocolate = chocolate.isChecked();

        EditText userInputView = (EditText) findViewById(R.id.input_user_name);
        String userName = userInputView.getText().toString();

        //Log.v("MainActivity", "Has whipped cream: " + userName);
        String priceMessage = createOrderSummary(
                calculatePrice(orderedWhippedTopping, orderedChocolate),
                userName,
                orderedWhippedTopping,
                orderedChocolate
        );

        Intent summaryIntent = new Intent(Intent.ACTION_SENDTO);
        summaryIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        summaryIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.app_name)
                        + getString(R.string.summary_email_subject)
                        + userName
        );
        summaryIntent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (summaryIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(summaryIntent);
        }
    }

    /**
     * * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if(quantity == 100) {
            Toast.makeText(this, getString(R.string.too_many_cups), Toast.LENGTH_LONG).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }

    /**
     * * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if(quantity == 1) {
            Toast.makeText(this, getString(R.string.too_few_cups), Toast.LENGTH_LONG).show();
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    //
    // End user triggered events.
    //

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @param hasWhippedCream did the user order whipped cream?
     * @param hasChocolate did the user order chocolate?
     * @return the price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int pricePerCup = 5;

        if(hasWhippedCream) {
            pricePerCup += 1;
        }

        if(hasChocolate) {
            pricePerCup += 2;
        }
        return (quantity * pricePerCup);
    }

    /**
     * Create summary of order
     *
     * @param priceOfOrder price of the order
     * @param userName name of the user ordering coffee
     * @param orderedWhippedtopping whether or not user wants whipped cream
     * @param orderedChocolate whether or not user wants chocolate
     * @return summary string
     */
    private String createOrderSummary(int priceOfOrder,
                                      String userName,
                                      boolean orderedWhippedtopping,
                                      boolean orderedChocolate) {
        return "Name: " + getString(R.string.order_summary_name, userName)
                + "\n" + getString(R.string.add_whipped_topping_question) + orderedWhippedtopping
                + "\n" + getString(R.string.add_chocolate_question) + orderedChocolate
                + "\n" + getString(R.string.quantity_for_summary) + quantity
                + "\n" + getString(R.string.total_cost,
                                   NumberFormat.getCurrencyInstance().format(priceOfOrder))
                + "\n" + getString(R.string.thank_you);
    }
}