package com.example.ivosjatek;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayersAdapter  extends
        RecyclerView.Adapter<PlayersAdapter.ViewHolder> {

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View playerView = inflater.inflate(R.layout.item_player, parent, false);

        // Return a new holder instance
        PlayersAdapter.ViewHolder viewHolder = new PlayersAdapter.ViewHolder(playerView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PlayersAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Player player = mPlayers.get(position);

        // Set item views based on your views and data model
        EditText editText = holder.nameEditText;
        editText.setHint(player.getName());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                player.setName(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public EditText nameEditText;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameEditText = (EditText) itemView.findViewById(R.id.player_name);
        }
    }
    // Store a member variable for the contacts
    private List<Player> mPlayers;

    // Pass in the contact array into the constructor
    public PlayersAdapter(List<Player> players) {
        mPlayers = players;
    }

}
