package com.example.gestionhuman.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionhuman.models.Tarea;
import com.example.gestionhuman.network.RetrofitInstance;
import com.example.gestionhuman.network.ApiService;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Tarea>> tareasLiveData;
    private final ApiService apiService;

    public HomeViewModel() {
        tareasLiveData = new MutableLiveData<>();
        apiService = RetrofitInstance.getApi();
    }

    // Método público para obtener las tareas como LiveData
    public LiveData<List<Tarea>> getTareas() {
        return tareasLiveData;
    }

    // Método privado para realizar la llamada a la API y obtener las tareas
    public void cargarTareas() {
        apiService.obtenerTareas().enqueue(new Callback<List<Tarea>>() {
            @Override
            public void onResponse(Call<List<Tarea>> call, Response<List<Tarea>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tareasLiveData.postValue(response.body()); // Actualiza el LiveData con las tareas recibidas
                } else {
                    // Manejar error en la respuesta
                    tareasLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Tarea>> call, Throwable t) {
                // Manejar error en la conexión
                tareasLiveData.postValue(null);
            }
        });
    }

    public void eliminarTarea(Long id) {
        apiService.eliminarTarea(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // La tarea fue eliminada exitosamente en el servidor
                } else {
                    // Manejar error si la eliminación no fue exitosa
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejar fallo en la conexión
            }
        });
    }
}




