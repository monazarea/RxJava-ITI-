package com.example.rxjavalab3;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainActivity2 extends AppCompatActivity {
    private Disposable disposable;
    EditText names, ages;
    Button btn_display;
    TextView result;

    private BehaviorSubject<String[]> namesSubject;
    private BehaviorSubject<String[]> agesSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        names = findViewById(R.id.etNames);
        ages = findViewById(R.id.etAges);
        btn_display = findViewById(R.id.btnDisplay);
        result = findViewById(R.id.tvResult);

        namesSubject = BehaviorSubject.create();
        agesSubject  = BehaviorSubject.create();

        disposable = Observable.zip(namesSubject.distinctUntilChanged(), agesSubject.distinctUntilChanged(), (names, ages) -> {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < names.length && i < ages.length; i++){
                        if(!names[i].isEmpty() && !ages[i].isEmpty())
                            sb.append(names[i]).append(" - ").append(ages[i]).append("\n");
                    }
                    return sb.toString();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> result.setText(item));

        btn_display.setOnClickListener(v -> {
            namesSubject.onNext(names.getText().toString().split("\n"));
            agesSubject.onNext(ages.getText().toString().split("\n"));
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
