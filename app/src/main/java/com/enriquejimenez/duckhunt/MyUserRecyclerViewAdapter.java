package com.enriquejimenez.duckhunt;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enriquejimenez.duckhunt.models.User;

import java.util.List;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.positionTextView.setText(String.valueOf(position));
        holder.nameTextView.setText(mValues.get(position).getName());
        holder.ducksTextView.setText(String.valueOf(mValues.get(position).getDucks()));
        Typeface typeface = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "pixel.ttf");
        holder.nameTextView.setTypeface(typeface);
        holder.positionTextView.setTypeface(typeface);
        holder.ducksTextView.setTypeface(typeface);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTextView;
        public final TextView positionTextView;
        public final TextView ducksTextView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = view.findViewById(R.id.textViewNickName);
            positionTextView = view.findViewById(R.id.textViewPosition);
            ducksTextView = view.findViewById(R.id.textViewDucks);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameTextView.getText().toString() + "'";
        }
    }
}
