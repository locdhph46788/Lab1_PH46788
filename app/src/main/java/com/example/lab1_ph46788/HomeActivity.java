package com.example.lab1_ph46788;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FloatingActionButton btnLogout;
    EditText edtName, edtPopulation, edtCountry;
    Button btnAdd;
    RecyclerView rcvCity;
    ArrayList<CityDTO> listCity = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();
        edtName = findViewById(R.id.edt_name);
        edtPopulation = findViewById(R.id.edt_population);
        edtCountry = findViewById(R.id.edt_country);
        btnAdd = findViewById(R.id.btn_add);
        rcvCity = findViewById(R.id.rcv_city);
        btnLogout = findViewById(R.id.btn_logout);
        FillCityToRecyclerView();
        ReadDataFormFirestore();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String population = edtPopulation.getText().toString().trim();
                String country = edtCountry.getText().toString().trim();
                if (name.isEmpty() || population.isEmpty() || country.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Không được để trống thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                WriteDataToFirestore(new CityDTO(UUID.randomUUID().toString(), name, Integer.parseInt(population), country));
            }
        });

    }

    private void FillCityToRecyclerView() {
        CityAdapter cityAdapter = new CityAdapter(this, listCity, this);
        rcvCity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvCity.setLayoutManager(new GridLayoutManager(this, 1));
        rcvCity.setAdapter(cityAdapter);

    }

    private void ReadDataFormFirestore() {
        db.collection("City")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (listCity != null) {
                                listCity.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CityDTO city = document.toObject(CityDTO.class);
                                listCity.add(city);
                            }
                            FillCityToRecyclerView();
                        } else {
                        }
                    }
                });
    }

    private void WriteDataToFirestore(CityDTO city) {
        db.collection("City")
                .add(city)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("documentReference Id", documentReference.getId());
                        Toast.makeText(HomeActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        edtName.setText("");
                        edtPopulation.setText("");
                        edtCountry.setText("");
                        ReadDataFormFirestore();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error documentReference getId ", e.toString());
                    }
                });
    }
}