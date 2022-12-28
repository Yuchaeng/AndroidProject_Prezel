
package com.example.graduproject;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendFragment extends Fragment implements OnItemClick {
    //recyclerview
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    UserAdapter mAdapter;
    UserProfileListAdapter mAdapter2;

    //firebase
    FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //arraylist
    ArrayList<userProfile> userArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        View v = inflater.inflate(R.layout.fragment_friend, container, false);

        userArrayList = new ArrayList<>();

        RecyclerView recyclerView = v.findViewById(R.id.my_recycler_view);
        RecyclerView recyclerView2 = v.findViewById(R.id.my_recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);

        //firebase에서 data 읽어오기
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                userProfile user = dataSnapshot.getValue(userProfile.class);
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser muser = mAuth.getCurrentUser();
                //내 자신은 제외하기 위하여
                if (user.getUid().equals(muser.getUid())){

                } else {
                    userArrayList.add(user);
                }

                mAdapter.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                userProfile user = dataSnapshot.getValue(userProfile.class);

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference databaseReference = database.getReference("userInfo");
        databaseReference.addChildEventListener(childEventListener);

        //recyclerview adapter
        mAdapter = new UserAdapter(userArrayList, this);
        mAdapter2 = new UserProfileListAdapter(userArrayList);
        recyclerView.setAdapter(mAdapter);
        recyclerView2.setAdapter(mAdapter2);





        TabHost tabHost1 = v.findViewById(R.id.tabHost1);
        tabHost1.setup();

        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("추천 친구");
        tabHost1.addTab(ts1);

        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("친구 목록");
        tabHost1.addTab(ts2);
        return v;
    }

    @Override
    public void onClick(String value) {


        Bundle bundle = new Bundle();
        bundle.putString("nick2", value);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        TipFragment fragment = new TipFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.home_cs, fragment);
        transaction.commit();
    }
}
