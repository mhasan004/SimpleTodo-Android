package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
view    - its that rectangle area on the screen that responsible for drawing and event handling. View is the base class for widgets, which are used to create interactive UI components (buttons, text fields, etc.)

adapter - responsible for making a 'view' for each item in the data set
        - "responsible for taking a data at a particular position and putting it into a view holder"
        - An 'adapter object' is like a bridge between an 'AdapterView' and the data for that view
        - provides access to data items*/

//1) ViewHolder - creates each view. Will create a new view and will wrap it inside a ViewHolder
//2) onBindViewHolder + data transfer through adapter setup + adding
//3) removing item: made a longClickListener
//step 4) using apache library to store data when app closes to a file in MainActivity.java
//step 5) continued from EditActivity.java to make a edit window for the item in the list that i clicked once on (need to make a one click listener just like long click)
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    public interface OnLongClickListener{                                                               // 3.0)* This interface will be used for delete items. MainActivity will implement it, this way info will be passed to ItemsAdapter
        void onItemLongClicked(int position);                                                                           //BASICALLY 'MAINACTIVITY' IS CHECKING POSITION OF LONG PRES AND TELLS ADAPTER TO DELETE THAT ITEM. we are passing in the position so that the class that's implementing this method (MainActivity) will need to know where we did the long press so that it can notify the adapter that is the position that should be deleted
    }
    public interface OnClickListener{                                                                   //  5.3) making an interface just like longclick to pass the POSITION OF THE ITEM WHERE I TAPPED
        void onItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;                                                                      // 5.4)

                                                                                                        // CONSTRUCTOR To fill up the adapter, need some info that will be passed in from MainActivity.java (this constructs the item adapter)
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {                         // the main data we need is the 'model' data which is a list of strings
        this.items = items;
        this.longClickListener = longClickListener;                                                     // 3.1)* made and passed longClickListener
        this.clickListener = clickListener;                                                             // 5.5)
    }


    //  These 3 methods were implemented when i made class and then i changed them
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {                     // 1) ViewHolder - creates each view. Will create a new view and will wrap it inside a ViewHolder
        View todoView;
        todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);        // Use the layout inflator to inflate a view
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {                            // 2) onBindViewHolder - responsible for binding data to a particular view holder. takes data at a particular position and puts it into the view holder
        String item = items.get(position);                                                                          //GIVEN THE POSITION (parameter 2), GET THE CORRESPONDING ITEM. this line gets the position. We will pass whatever is in the postition to the ViewHolder
        holder.bind(item);                                                                                          //2.a) BIND THE ITEM INTO THE SPECIFIED VIEW HOLDER (parameter 1)
    }

    @Override
    public int getItemCount() {                                                                         // # of items available in data
        return items.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {                                                  // This subclass provides easy access to the views that represent each row in the list
        TextView tvItem;                                                                                            // 2.b) 'simple_list_item_1' has a single text view with the id 'view1' so we will refer to it here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);                                                     // 2.c) getting that 'text1' item

        }

        public void bind(String item) {                                                                 // UPDATED THE VIEW INSIDE THE VIEW HOLDER. this was made when i did 'holder.bind(item)'
            tvItem.setText(item);                                                                                   // 2.d) now that we have the TextView, we can now set the contents of the TextView to be whatever we pass into 'item'
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {                                          // 3.1)* a way to delete items off list, long press to delete item off the recycler view. ISSUE: need a way to pass "user long pressed" from MainActivity to the ItemsAdapter. Smart: will use an interface that mainactivity will implement
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClicked(getAdapterPosition());                                      // 3.2)* Notify the listener which item was long pressed
                    return true;                                                                                                // changed to true means that the caller is consuming tHE long cliCk"
                }
            });
            tvItem.setOnClickListener(new View.OnClickListener() {                                                  //5.2) one click listener for the edit window. This will inform MainActivity which position was tapped by using another interface that the MainActivity can implement
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());                                              //5.6) when android notifies us that the TextView (tvItem) has been clicked, we can apply this method on the interface
                }
            });
        }


    }
}
                                                                                                                    // 2.e) go to 'MainActivity.java'
                                                                                                                    // 3.3) go to 'MainActivity.java' (deleting items via long press)
                                                                                                                    // 5.7) go to 'MainActivity.java' (editing items via tap)