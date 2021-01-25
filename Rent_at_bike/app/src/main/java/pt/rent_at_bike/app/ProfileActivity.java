package pt.rent_at_bike.app;

import android.content.Intent;
import android.os.Bundle;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private EditText email;
    private FloatingActionButton logout;
    private FirebaseAuth mAuth;
    public RecyclerView rvDetails;
    public HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = findViewById(R.id.username);
        logout = findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();

        email.setText(mAuth.getCurrentUser().getEmail());
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
}