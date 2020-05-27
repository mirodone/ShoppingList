package ro.mirodone.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    Button saveButton;
    EditText babyItem;
    EditText quantityAdd;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler mDatabaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        mRecyclerView = findViewById(R.id.rview);
        fab = findViewById(R.id.fab_list);

        mDatabaseHandler = new DatabaseHandler(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        //get items from db
        itemList = mDatabaseHandler.getAllItems();

        for (Item item : itemList) {
            Log.d(TAG, "onCreate: " + item.getItemName());
        }

        mRecyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.notifyDataSetChanged();// used when the view is refreshed and need to show the new changes

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopDialog();
            }
        });


    }

    private void createPopDialog() {

        mBuilder = new AlertDialog.Builder(this);
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
                    Snackbar.make(view, "Fill all the fields!", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        mBuilder.setView(view);
        mAlertDialog = mBuilder.create();
        mAlertDialog.show();
    }

    private void saveItem(View view) {

        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        int quantity = Integer.parseInt(quantityAdd.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemQuantity(quantity);

        mDatabaseHandler.addItem(item);

        Snackbar.make(view, "Item saved", Snackbar.LENGTH_LONG)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mAlertDialog.dismiss();
                // move to the next screen
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();

            }
        }, 1200); // delAY 1 SEC

    }
}
