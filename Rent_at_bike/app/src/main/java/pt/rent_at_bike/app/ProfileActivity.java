package pt.rent_at_bike.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.rent_at_bike.app.detail.DetailBikeAdapter;
import pt.rent_at_bike.app.history.History;
import pt.rent_at_bike.app.history.HistoryAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText email;
    private FloatingActionButton logout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference colRefUsers;
    public RecyclerView rvDetails;
    public HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.username);
        logout = findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        colRefUsers = db.collection("/users");

        fetchCollection();

        name.setText("");
        name.setFocusable(false);
        surname.setText("");
        surname.setFocusable(false);
        email.setText("");
        email.setFocusable(false);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        rvDetails = (RecyclerView) findViewById(R.id.recyclerView);

        List history = new ArrayList<History>();
        history.add(new History(2, "rentbike@ua.pt",03,100, LocalDate.now(), LocalDate.now()));

        adapter = new HistoryAdapter(history);
        rvDetails.setAdapter(adapter);
        // Set layout manager to position the items
        rvDetails.setLayoutManager(new LinearLayoutManager(this));
        rvDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void fetchCollection() {
        colRefUsers.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> databaseUsers = document.getData();
                                if(mAuth.getCurrentUser().getEmail().equals(databaseUsers.get("email"))){
                                    name.setText((String)databaseUsers.get("name"));
                                    surname.setText((String)databaseUsers.get("surname"));
                                    email.setText((String)databaseUsers.get("email"));
                                }
                                System.out.println("Success");
                            }
                        } else {
                            System.out.println("Error");
                        }
                    }
                });
    }
}