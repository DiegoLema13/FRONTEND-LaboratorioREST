package com.example.gestionhuman.network;

import com.example.gestionhuman.models.Tarea;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/tarea")
    Call<List<Tarea>> obtenerTareas();

    @GET("/tarea/{id}")
    Call<Tarea> obtenerTareaPorId(@Path("id") Long id);

    @POST("/tarea")
    Call<Tarea> crearTarea(@Body Tarea tarea);

    @PUT("/tarea/{id}")
    Call<Tarea> actualizarTarea(@Path("id") Long id, @Body Tarea tarea);

    @DELETE("/tarea/{id}")
    Call<Void> eliminarTarea(@Path("id") Long id);
}
