package com.example.gestionhuman.ui.gallery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestionhuman.R;
import com.example.gestionhuman.models.Tarea;
import com.example.gestionhuman.network.ApiService;
import com.example.gestionhuman.network.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    private EditText editTextTitulo;
    private Button buttonGuardar;
    private ApiService apiService;
    private Long tareaId = null; // Para identificar si es edición o creación

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Inicializar vistas
        editTextTitulo = root.findViewById(R.id.editTextTitulo);
        buttonGuardar = root.findViewById(R.id.buttonGuardar);

        // Inicializar el servicio de API
        apiService = RetrofitInstance.getApi();

        // Verificar si hay argumentos para editar una tarea
        if (getArguments() != null) {
            Tarea tarea = (Tarea) getArguments().getSerializable("tarea");
            if (tarea != null) {
                tareaId = tarea.getId(); // Guardar el ID de la tarea
                editTextTitulo.setText(tarea.getTitulo()); // Mostrar el título en el campo de texto
            }
        }

        // Configurar el botón para guardar o actualizar la tarea
        buttonGuardar.setOnClickListener(v -> guardarTarea());

        return root;
    }

    private void guardarTarea() {
        String titulo = editTextTitulo.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            Toast.makeText(getContext(), "Por favor, ingresa un título", Toast.LENGTH_SHORT).show();
            return;
        }

        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);

        if (tareaId != null) {
            // Si hay un ID, se trata de una actualización
            apiService.actualizarTarea(tareaId, tarea).enqueue(new Callback<Tarea>() {
                @Override
                public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Tarea actualizada con éxito", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed(); // Volver atrás
                    } else {
                        Toast.makeText(getContext(), "Error al actualizar la tarea", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Tarea> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Si no hay un ID, se trata de una nueva tarea
            apiService.crearTarea(tarea).enqueue(new Callback<Tarea>() {
                @Override
                public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Tarea creada con éxito", Toast.LENGTH_SHORT).show();
                        editTextTitulo.setText(""); // Limpiar el campo de texto
                    } else {
                        Toast.makeText(getContext(), "Error al crear la tarea", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Tarea> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
