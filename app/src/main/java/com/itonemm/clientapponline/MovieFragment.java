package com.itonemm.clientapponline;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_movie, container, false);
        final RecyclerView recyclerView=view.findViewById(R.id.newmoives);

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference Ref=db.collection("movies");
        Ref.whereEqualTo("movieCategory","NewMovies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    movieModels.add(snapshot.toObject(MovieModel.class));
                }
                MovieRecyclerAdapter adapter=new MovieRecyclerAdapter(movieModels,getContext(),getLayoutInflater());
                recyclerView.setAdapter(adapter);
                LinearLayoutManager lm=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                recyclerView.setLayoutManager(lm);
            }
        });

        final RecyclerView allmovies=view.findViewById(R.id.allmoives);
        Ref.whereEqualTo("movieCategory","AllMovies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MovieModel> movieModels=new ArrayList<MovieModel>();
                for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    movieModels.add(snapshot.toObject(MovieModel.class));
                }
                MovieRecyclerAdapter adapter=new MovieRecyclerAdapter(movieModels,getContext(),getLayoutInflater());
                allmovies.setAdapter(adapter);
                LinearLayoutManager lm=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                allmovies.setLayoutManager(lm);
            }
        });
        return  view;
    }

}
