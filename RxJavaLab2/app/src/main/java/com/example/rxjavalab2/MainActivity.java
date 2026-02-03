package com.example.rxjavalab2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity  {

    List<String> names = List.of("Patrick Ross","Kelly Wood","James Moore","Janice Coleman", "Mary Carter");

    List<String> namesList = Arrays.asList(
            "Mona Zarea",
            "Mohamed Ali",
            "Ahmed Hassan",
            "Sara Ibrahim",
            "Youssef Adel",
            "Nour Mohamed",
            "Hany Fathy",
            "Salma Khaled",
            "Omar Samir",
            "Aya Mostafa"
    );
EditText editText;
TextView textView;

    CompositeDisposable disposable = new CompositeDisposable();
    PublishSubject<String> searchSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Lab1
        long count = names.stream().filter(e ->
                e.length() < 12 && e.startsWith("J")
        ).count();
        Log.d("asd->", count + "");

        // Lab2
        Stream<String> stream = Stream.of("one", "two", "three", "four");
        boolean match = stream.anyMatch(s -> s.contains("four"));
        Log.d("asd->", match + "");

        // Lab3
        Observable<Long> observable = Observable.create(emitter -> {
            emitter.onNext(1L);
            Thread.sleep(100);
            emitter.onNext(2L);
            Thread.sleep(100);
            emitter.onNext(3L);
            Thread.sleep(600);
            emitter.onNext(4L);
            Thread.sleep(100);
            emitter.onNext(5L);
            Thread.sleep(100);
            emitter.onNext(6L);
            Thread.sleep(100);
            emitter.onNext(7L);
            emitter.onComplete();
        });
        Disposable subscribe = observable
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(
                        item -> Log.d("asd->", "onNext: " + item),
                        error -> Log.d("asd->", "onError"),
                        () -> Log.d("asd->", "onComplete")
                );

        // Lab 4
        editText = findViewById(R.id.etSearch);
        textView = findViewById(R.id.tvResults);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchSubject.onNext(charSequence.toString());
            }


        });

        disposable.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(query -> {
                            StringBuilder result = new StringBuilder();
                            String input = query.toLowerCase();

                            for (String name : namesList) {
                                if (name.toLowerCase().contains(input)) {
                                    result.append(name).append("\n");
                                }
                            }

                            if (result.length() == 0) {
                                textView.setText("No results");
                            } else {
                                textView.setText(result.toString());
                            }
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

}