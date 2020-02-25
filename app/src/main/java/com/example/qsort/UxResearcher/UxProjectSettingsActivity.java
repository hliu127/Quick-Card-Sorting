package com.example.qsort.UxResearcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qsort.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UxProjectSettingsActivity extends AppCompatActivity {

    String categories, labels;
    EditText categoriesTextView,labelsTextView,projectTitleTextView;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ux_project_settings);

        categoriesTextView = findViewById(R.id.categoryTextView);
        labelsTextView = findViewById(R.id.labelTextView);
        projectTitleTextView = findViewById(R.id.projectTitleTextView);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        categories = intent.getExtras().getString("Categories");
        labels = intent.getExtras().getString("Labels");

        categoriesTextView.setText(categories);
        labelsTextView.setText(labels);
    }

    public void submitProject(View view){

        String[] categoriesArray = categoriesTextView.getText().toString().split("\n");
        String[] labelsArray = labelsTextView.getText().toString().split("\n");
        String projectTitle = projectTitleTextView.getText().toString();
        String timestamp = String.valueOf(Timestamp.now().getSeconds());

        for (int i=0; i<labelsArray.length;i++){
            Map<String, Integer> label = new HashMap<>();

            for (int j=0; j<categoriesArray.length;j++){
                label.put(categoriesArray[j],0);
            }
            firebaseFirestore.collection(timestamp).document(labelsArray[i])
                    .set(label)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMessage("Project successfully created!");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error writing document", e);
                        }
                    });
        }

        Map<String, String> project = new HashMap<>();
        project.put("Project Name",projectTitle);
        project.put("Project ID",timestamp);

        firebaseFirestore.collection("projects").document(timestamp).set(project)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(),UxMainActivity.class));
                finish();
            }
        });
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

}
