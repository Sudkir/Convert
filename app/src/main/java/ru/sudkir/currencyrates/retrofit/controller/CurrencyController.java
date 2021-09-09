package ru.sudkir.currencyrates.retrofit.controller;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.sudkir.currencyrates.retrofit.model.ServerResponse;

/**
 * Контроллер для получения курсов валют
 */
public interface CurrencyController {

    @GET("daily_json.js")
    Call<ServerResponse> getAllCurrency();
}
