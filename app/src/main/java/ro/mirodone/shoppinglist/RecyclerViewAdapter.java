package ro.mirodone.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;
    LayoutInflater mLayoutInflater;
    private Context context;
    private List<Item> itemList;


    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;

    }


    @NonNull
    @Override
    // will get our row that we created earlier
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Item item = itemList.get(position);

        holder.itemName.setText(item.getItemName());
        holder.quantity.setText(String.valueOf(item.getItemQuantity()));
        holder.dateAdded.setText(item.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView itemName;
         TextView quantity;
         TextView dateAdded;
        public int id;
         Button editButton;
         Button deleteButton;


         ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.list_item_name);
            quantity = itemView.findViewById(R.id.list_item_qty);
            dateAdded = itemView.findViewById(R.id.list_item_date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);

            switch (view.getId()) {
                case R.id.editButton:

                    editItem(item);
                    break;

                case R.id.deleteButton:

                    deleteItem(item.getId());
                    break;
            }
        }

        private void deleteItem(final int id) {

            alertDialog = new AlertDialog.Builder(context);
            final AlertDialog dialog;
            dialog = alertDialog.create();
            alertDialog.setMessage("Are you sure ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    DatabaseHandler databaseHandler = new DatabaseHandler(context);
                    databaseHandler.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();

                }
            });
            alertDialog.create().show();
        }

        private void editItem(final Item newItem) {

            //populate popup with curent object data

            alertDialog = new AlertDialog.Builder(context);
            mLayoutInflater = LayoutInflater.from(context);
            View view = mLayoutInflater.inflate(R.layout.popup, null);


            Button saveButton;
            final EditText babyItem;
            final EditText quantityItem;


            babyItem = view.findViewById(R.id.babyItem);
            quantityItem = view.findViewById(R.id.itemQuantity);
            saveButton = view.findViewById(R.id.btn_save);
            saveButton.setText(R.string.update_text);


            babyItem.setText((newItem.getItemName()));
            quantityItem.setText(String.valueOf(newItem.getItemQuantity()));

            alertDialog.setView(view);
            dialog = alertDialog.create();
            dialog.show();


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    newItem.setItemName(babyItem.getText().toString());
                    newItem.setItemQuantity(Integer.parseInt(quantityItem.getText().toString()));

                    if (!babyItem.getText().toString().isEmpty() &&
                            !quantityItem.getText().toString().isEmpty()) {
                        databaseHandler.updateItem(newItem);
                        //refresh the update
                        notifyItemChanged(getAdapterPosition(), newItem);
                    } else {
                        Snackbar.make(view, "Fill all the fields!", Snackbar.LENGTH_SHORT)
                                .show();
                    }

                    dialog.dismiss();
                }
            });

        }


    }
}