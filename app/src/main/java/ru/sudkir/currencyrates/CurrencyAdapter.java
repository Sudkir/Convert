package ru.sudkir.currencyrates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import ru.sudkir.currencyrates.retrofit.model.Currency;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.MyViewHolder> {

    private List<Currency> currencyList;
    private LayoutInflater mInflater;
    private String amountString = "";

    CurrencyAdapter(Context context, List<Currency> currencyList) {
        this.mInflater = LayoutInflater.from(context);
        this.currencyList = currencyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.currency_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Currency currency = currencyList.get(position);

        holder.title.setText(currency.getName());
        //валюта не перевода
        holder.course.setText(currency.getCharCode().concat("/RUB: ").concat(currency.getValue().toString()));
        if (amountString.length() != 0) {
            BigDecimal finalAmountDecimal = new BigDecimal(amountString);
            BigDecimal course = currency.getValue();
            //RoundingMode.HALF_EVEN округление
            holder.finalAmount.setText((finalAmountDecimal.divide(course, 2, RoundingMode.HALF_EVEN).toString()));
        } else {
            holder.finalAmount.setText("");
        }
    }

    //подсчет курса при изменени числа рублей
    public void calculateAmount(String amountString) {
        this.amountString = amountString;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, course, finalAmount;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_currency_adapter);//название валюты
            course = view.findViewById(R.id.course_currency_adapter);//курс к рублю
            finalAmount = view.findViewById(R.id.final_amount);//финальное кол-во валюты
        }
    }

}