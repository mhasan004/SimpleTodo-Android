package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//5.1) now that i made the layout in the activity_edit.xml, i now declare the components and find them (A and B)
public class EditActivity extends AppCompatActivity {
    EditText editTextItem;                                                                  //A) declare components
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextItem = findViewById(R.id.editTextItem);                                     //B) find components
        buttonSave = findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit item");                                                //change the name of the activity windows so users aren't confused as to where they are

        editTextItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));       //5.13) So once i click on an item to edit it, it will go to this activity window where the 'editTextItem" component is filled with the item name so we know what we are changing

        buttonSave.setOnClickListener(new View.OnClickListener() {                          //5.14) adding a listener to the button so when user clicked the button to save we will know
            @Override
            public void onClick(View v) {                                                           //when the user taps the 'Save' button, we want to go back to the MainActivity using Intents once again!
                Intent intent = new Intent();                                               //5.15) create an intent that will contain all the results. this time we will leave an empty contructor because we are only using the shell to pass on the info

                intent.putExtra(MainActivity.KEY_ITEM_TEXT,     editTextItem.getText().toString()); //5.16) pass the data (results of the editing). Will reuse the same key (KEY_ITEM_TEXT) and set it to whatever the new stuff in the edit text is!
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                setResult(RESULT_OK, intent);                                               //5.17) set the results of the intent. RESULT_OK is predetermined in android sys
                finish();                                                                   //5.18) finish activity, close the screen and go back
            }
        });
    }
}

// go to itemsAdapter 5.2 will need to make something like a OneClickListener
// come back to this from MainActivity for 5.13)
// go back to MainActivity for 5.19)
