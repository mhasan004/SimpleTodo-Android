// view: res/layout/activity_main.xml
// Model + Controller: in java
// Persistence (load save) via file sys or DS, etc
// User actions (add, remove) implemented via handlers on Java->MainActivity

/*intent: CAN USE INTENTS TO PASS INFORMATION/DATA BETWEEN ACTIVITIES!!!
        - is like making a request to the android system
        - ex; intent to open up a browser on ur phone, intent to open up the camera, etc
        - Intent(where we calling the intent from, where do we want to go?)
*/

//A-D: basic setup for button component
//step 2) is continued from ItemsAdapter: this step is binding data to some view model + adding
//step 3) is continued from ItemsAdapter: deleting an item in the list
//step 4) using apache library to store data when app closes to a file
//step 5) will press once one item to edit them -> will lead to a new window ( so need to make a new activity: com.example.simpletodo -> new -. activity -> empty activity)
package com.example.simpletodo;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final String KEY_ITEM_TEXT = "item_text";                                     //5.10) variables im using the pass data with intent
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button buttonAdd;                                                                           //A) Declare components
    EditText editTextItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;                                                                  //3.4.1)* if ur adding or removing an item from the model (the RecyclerView) u have to let this guy know


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonAdd);                                               //B) Find Components on View
        editTextItem = findViewById(R.id.editTextItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();                                                                            //4.3) now that we made a loadItems func that populates 'items' with a array of strings read from a file, we don't need to manually add stuff with items.add("Buy milk");

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener()
        {                                                                                       //3.3)* implementing the interface
            @Override
            public void onItemLongClicked(int position) {                                       //3.4.2)* By this point we already have the position of the item that was long pressed. So will:
                items.remove(position);                                                                         // will remove the item that is in that position from the model
                itemsAdapter.notifyItemRemoved(position);                                                       // Notify the adapter that we deleted an item
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();        //notify item was removed
                saveItems();                                                                    //4.4) Removing an item so save it to a file
            }
        };
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener(){      //5.8) implementing the one click interface
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position "+ position);                         //just testing to see if its working (should say im clicking oat position # in 'logcat cat' at the bottom
                Intent i = new Intent(MainActivity.this, EditActivity.class);     //5.9) create a new activity using Intents. Intent(where we calling the intent from, where do we want to go?)
                i.putExtra(KEY_ITEM_TEXT, items.get(position))  ;                               //5.11) pass the data being edited into the Intent using putExtra(a String key, a value)
                i.putExtra(KEY_ITEM_POSITION, position)  ;

                startActivityForResult(i, EDIT_TEXT_CODE);                                      //5.12) Display the activity. We expect the updated todo item from EditActivity. startActivityForResult(intent defined above, request code for different activity); GO TO EDITACTIVITY
            }
        };


        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);           //2.e+5.7) construct the adapter. make an object for the itemAdapter class and pass the current items list into the ItemAdapter's items list.
        rvItems.setAdapter(itemsAdapter);                                                       //2.f) set up the adapter ('itemAdapter') to the RecyclerView component ('rvItems')
        rvItems.setLayoutManager(new LinearLayoutManager(this));                         //2.g) SET THE LAYOUT: will lay all out in a simple vertical way.

        buttonAdd.setOnClickListener(new View.OnClickListener()                                 //D) WHEN BUTTON IS PRESSED ADD ITEM TO LIST: take the stuff from 'editTextItem" component, turn it to string -> then add that item to the items list (ADD TO THE MODEL)-> finally notify the adapter that its in the last potiion in the model
        {
            @Override
            public void onClick(View v) {
                String todoItem = editTextItem.getText().toString();                                            // getting the stuff from the 'editTextItem' component and turning it to a string
                items.add(todoItem);                                                                            // add to the item
                itemsAdapter.notifyItemInserted(items.size()-1);                                        // notify the adapter that its in the last position in the model
                editTextItem.setText("");                                                                       // clearing the edit text
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                saveItems();                                                                    //4.4) adding to list to save to file
            }
        });
    }
    private File getDataFile(){                                                                 //4.0) Will return the file in which we will store the todo items
        return new File(getFilesDir(), "data.txt");
    }
    private void loadItems(){                                                                   //4.1) READ: This function will load items by reading every line of the data file. [SHOULD BE CALLED WHEN APP IS STARTED (SO CALLED ONCE)]
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset() ));             // read all of the lines out of the "getDataFile()" and populate push it into ArrayList, which will be 'items' (this is using the Apache Library)
        } catch (IOException e) {                                                                               // this is a input/out put exception. if there is an error with the input or output, it will run this
            Log.e("MainActivity", "Error reading items", e);                                          // Log is used by developers to find errors in the program
            items = new ArrayList<>();                                                                          // we we didn't get anything, set the items to a empty list that we can build the recyclerview off of
        }
    }
    private void saveItems(){                                                                   //4.2) WRITE: this function saves items by writing them into the data file
        try {
            FileUtils.writeLines(getDataFile(), items);                                                         // Using the apache library again to write to files. [ SHOULD BE CALLED WHENEVER WE MAKE A CHANGE TO THE TODO ITEMS (when we add or remove so two calls)]
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //5.19) HANDLE THE RESULT OF THE EDIT ACTIVITY: So i checked the item from EditActivity and will not use this function to overwrite the item
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){                                           // check if the RESULT CODE matches the code we sent in and there is no error
            String updatedText = data.getStringExtra(KEY_ITEM_TEXT);                            //5.20) retrieve the updated text value. What we passed in from EditActivity shows up in this intent data
            int passesPosition = data.getExtras().getInt(KEY_ITEM_POSITION);                    //5.21) get the original position of the edited item from the position key 9which was were we did the modification)
            items.set(passesPosition,updatedText);                                              //5.22) Update the model with the new item text AT THE RIGHT POSITION
            itemsAdapter.notifyItemChanged(passesPosition);                                     //5.22) NOTIFY THE ADAPTER so that the RecyclerView knows something has changed
            saveItems();                                                                        //5.23) save changes into the data.txt file
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.w("MainActivity", "Unknown call to OnActivityResult");                                // if these is an error log it
        }
    }
}



/* EXAMPLE:
Button buttonAdd;                                                   //1) Declare Button component
protected void onCreate(Bundle savedInstanceState) {
    buttonAdd = findViewById(R.id.buttonAdd);                       //2) Find it in the view
}

------------------------------------------------------------------------------------------------------------
RecyclerView rvItems;                                               //1) Declare recycle view component
protected void onCreate(Bundle savedInstanceState) {
    rvItems = findViewById(R.id.rvItems);                           //2) Find it in the view

    List<String> items;                                             //add some stuff to the items list
    items =  new ArrayList<>();
    items.add("Buy milk");

    ItemsAdapter itemsAdapter = new ItemsAdapter(items);            //3)* make an object for the ItemsAdapter and pass in the list to it
    rvItems.setAdapter(itemsAdapter);                               //4)* set up the adapter ('itemAdapter') to the RecyclerView component ('rvItems')
    rvItems.setLayoutManager(new LinearLayoutManager(this));        //5)* SET THE LAYOUT
}
 */
