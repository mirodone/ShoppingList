package ro.mirodone.shoppinglist;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText quantityAdd;

    private DatabaseHandler mDatabaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHandler = new DatabaseHandler(this);

        bypassActivity();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();


               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    private void bypassActivity() {

        if (mDatabaseHandler.getItemsCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }


    private void saveItem(View view) {

        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        int quantity = Integer.parseInt(quantityAdd.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemQuantity(quantity);

        mDatabaseHandler.addItem(item);

        Snackbar.make(view, getText(R.string.btn_item_saved) , Snackbar.LENGTH_LONG)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.dismiss();
                // move to the next screen
                startActivity(new Intent(MainActivity.this, ListActivity.class));

            }
        }, 1200); // delAY 1 SEC


    }


    public void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        babyItem = view.findViewById(R.id.babyItem);
        quantityAdd = view.findViewById(R.id.itemQuantity);

        saveButton = view.findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!babyItem.getText().toString().isEmpty() &&
                        !quantityAdd.getText().toString().isEmpty()) {
                    saveItem(view);
                } else {
                    Snackbar.make(view, getString(R.string.btn_fill_fields), Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        builder.setView(view);
        dialog = builder.create(); // create dialog object
        dialog.show();

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
        if (id == R.id.action_settings) {
            Intent mainIntent = new Intent(MainActivity.this, About.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
