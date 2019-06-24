package com.enriquejimenez.duckhunt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.enriquejimenez.duckhunt.models.User;
import com.enriquejimenez.duckhunt.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRankingFragment extends Fragment {

    private List<User> userList;
    private MyUserRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    private FirebaseFirestore db;

    private int mColumnCount = 1;


    public UserRankingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            db.collection(Constants.DB_USERS)
                    .orderBy("ducks", Query.Direction.DESCENDING)
                    .limit(25)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            userList = new ArrayList<>();
                            for(DocumentSnapshot  documentSnapshot : task.getResult()){
                                if (documentSnapshot.exists()) {
                                    // convert document to POJO
                                    User user = documentSnapshot.toObject(User.class);
                                    userList.add(user);
                                }

                            }
                            adapter = new MyUserRecyclerViewAdapter(userList);
                            recyclerView.setAdapter(adapter);
                        }
                    });

        }
        return view;
    }
}
