package com.example.gestionhuman.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {

    private static Retrofit retrofit = null;

    // Método para obtener la instancia de Retrofit y la API
    public static ApiService getApi() {
        if (retrofit == null) {
            // Configuración de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:4200/") // Cambiamos esta URL por la de nuestro servidor
                    .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson para convertir el JSON
                    .build();
        }
        return retrofit.create(ApiService.class); // Regresa la interfaz de la API
    }
}
