package com.example.rxjava_lab1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.rxjava3.core.Observable;

public class MainActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btn_Array;
    Button btn_Iterable;
    TextAdapter adapter;
    ArrayList<String> list = new ArrayList<>();
    private Integer[] arrays = {0, 1, 2, 3, 4, 5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleView);
        btn_Array = findViewById(R.id.button_array);
        btn_Iterable = findViewById(R.id.button_iterable);
        adapter = new TextAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        btn_Array.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Observable observable = Observable.intervalRange(
                        1,
                        5,
                        0,
                        1,
                        java.util.concurrent.TimeUnit.SECONDS
                );
                observable.subscribe(
                        item -> {
                            list.add("intervalRange onNext: " + item);
                            adapter.notifyDataSetChanged();
                        },
                        error -> {
                            list.add("intervalRange onError");
                            adapter.notifyDataSetChanged();
                        },
                        () -> {
                            list.add("intervalRange onComplete");
                            adapter.notifyDataSetChanged();
                        }
                );
            }
        });
        btn_Iterable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable<Integer> observable = io.reactivex.rxjava3.core.Observable.fromIterable(Arrays.asList(arrays));
                observable.subscribe(
                        item -> {
                            list.add("From Iterable: onNext: " + item);
                            adapter.notifyDataSetChanged();
                        },
                        error -> {
                            list.add("From Iterable: onError: " + error.getMessage());
                            adapter.notifyDataSetChanged();
                        },
                        () -> {
                            list.add("From Iterable: onComplete");
                            adapter.notifyDataSetChanged();
                        }
                );
            }
        });

    }
}