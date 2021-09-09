package ru.sudkir.currencyrates;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sudkir.currencyrates.popup.CustomPopUp;
import ru.sudkir.currencyrates.retrofit.model.Currency;
import ru.sudkir.currencyrates.retrofit.model.ServerResponse;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_layout)
    LinearLayout parent;

    @BindView(R.id.progress_bar_holder)
    FrameLayout progressBarHolder;

    @BindView(R.id.amount_from)
    EditText amountFrom;

    @BindView(R.id.currency_recycler)
    RecyclerView currencyRecycler;//отображение списка валют

    CustomPopUp customPopUp = new CustomPopUp();
    private List<Currency> currencyList;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Get the user input from EditText
        myEditText = findViewById(R.id.amount_from);
        //setting up not show the default soft keyboard
        myEditText.setShowSoftInputOnFocus(false);
        //created a softkeyboard, the numbers user input can ge from the string userInput
        CreateSoftKeyboard();

        //reset Button click switch to select country activity
        reset = findViewById(R.id.btnReset);









        progressBarHolder.setVisibility(View.VISIBLE);

        currencyList = new ArrayList<>();//список значений курса
        currencyRecycler.setLayoutManager(new LinearLayoutManager(this));
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(this, currencyList);

        currencyRecycler.setAdapter(currencyAdapter);//обновление данных
        currencyAdapter.notifyDataSetChanged();

        // Запускаем таймер для обращения на сервер раз в минуту
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, 60000);

        // Следим за обновлением вводимого текста в edit text
        amountFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currencyAdapter.calculateAmount(amountFrom.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // Change the label of the menu based on the state of the app.
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else{
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Check if the correct item was clicked
        if(item.getItemId()==R.id.night_mode) {
            // Get the night mode state of the app.
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            //Set the theme mode for the restarted activity
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
            }
// Recreate the activity for the theme change to take effect.
            recreate();
        }
        return true;
    }

    //обновление курса по таймеру
    private class RemindTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(MainActivity.this::getCurrency);
        }
    }

    public void getCurrency() {
        currencyList.clear();
        App.getCurrencyController().getAllCurrency().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (!response.isSuccessful()) {
                    hideProgress();
                    customPopUp.showFailedPopUp(MainActivity.this, parent, "Не удалось получить курсы валют");
                    return;
                }
                ServerResponse serverResponse = response.body();
                if (serverResponse == null) {
                    hideProgress();
                    customPopUp.showFailedPopUp(MainActivity.this, parent, "Не удалось получить курсы валют");
                    return;
                }
                for (Map.Entry<String, Currency> entry : serverResponse.getValute().entrySet()) {
                    Currency value = entry.getValue();
                    currencyList.add(value);
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable throwable) {
                progressBarHolder.setVisibility(View.GONE);
                customPopUp.showFailedPopUp(MainActivity.this, parent, "Не удалось получить курсы валют");
            }
        });
    }

    private void hideProgress() {
        if (progressBarHolder.getVisibility() == View.VISIBLE) {
            progressBarHolder.setVisibility(View.GONE);
        }
    }



    //new code keybord

    private final int LIMITUSERINPUT = 12;
    private String userInput = "";
    private EditText myEditText;

    //softkeyboard variables
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn0;
    Button btnPoint;
    Button btnDelete;
    ImageButton resCur;
    boolean addedPoint = false;

    //reset country variables
    Button reset;



    private void CreateSoftKeyboard(){
        resCur = findViewById(R.id.resCurBtn);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);
        btnPoint = findViewById(R.id.btnPoint);
        btnDelete = findViewById(R.id.btnDelete);
        myEditText = findViewById(R.id.amount_from);

        resCur.setOnClickListener(v -> {
            getCurrency();

        });

        btn1.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "1";
                myEditText.setText(userInput);
            }
        });

        btn2.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "2";
                myEditText.setText(userInput);
            }
        });

        btn3.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "3";
                myEditText.setText(userInput);
            }
        });

        btn4.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "4";
                myEditText.setText(userInput);
            }
        });

        btn5.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "5";
                myEditText.setText(userInput);
            }
        });

        btn6.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "6";
                myEditText.setText(userInput);
            }
        });

        btn7.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "7";
                myEditText.setText(userInput);
            }
        });

        btn8.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "8";
                myEditText.setText(userInput);
            }
        });

        btn9.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "9";
                myEditText.setText(userInput);
            }
        });

        btn0.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                userInput += "0";
                myEditText.setText(userInput);
            }
        });

        btnPoint.setOnClickListener(v -> {
            if(userInput.length()<LIMITUSERINPUT){
                if(!addedPoint){
                    userInput += ".";
                    myEditText.setText(userInput);
                }
                addedPoint = true;
            }
        });

        btnDelete.setOnClickListener(v -> {
            if(userInput.length()<=0){
                myEditText.setText("");
            }
            else
            {
                if(userInput.charAt(userInput.length()-1) == '.')
                    addedPoint = false;
                userInput = userInput.substring(0, userInput.length()-1);
                myEditText.setText(userInput);
            }
        });
    }

}
