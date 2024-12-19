package com.example.gestionhuman.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gestionhuman.databinding.FragmentHomeBinding;
import com.example.gestionhuman.models.Tarea;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private TareaAdapter tareaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicializar el ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Configurar el RecyclerView
        binding.recyclerViewTareas.setLayoutManager(new LinearLayoutManager(getContext()));
        tareaAdapter = new TareaAdapter();
        binding.recyclerViewTareas.setAdapter(tareaAdapter);

        // Detectar toques fuera del RecyclerView para deshabilitar los EditText
        binding.getRoot().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tareaAdapter.deshabilitarTodosLosEditText();
            }
            return false;
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observar cambios en las tareas del ViewModel
        homeViewModel.getTareas().observe(getViewLifecycleOwner(), this::actualizarTareas);

        // Configurar clic en el botón "Editar" de cada ítem
        tareaAdapter.setOnEditButtonClickListener((tarea, editTextTitulo) -> {
            editTextTitulo.setEnabled(true);  // Habilitar la edición
            editTextTitulo.requestFocus();   // Enfocar el campo de texto
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextTitulo, InputMethodManager.SHOW_IMPLICIT); // Mostrar el teclado
            }
        });

        // Configurar clic en el botón "Eliminar"
        tareaAdapter.setOnDeleteButtonClickListener(tarea -> {
            homeViewModel.eliminarTarea(tarea.getId());  // Pasar solo el `id` de la tarea
        });

        // Cargar las tareas desde el ViewModel (backend)
        homeViewModel.cargarTareas();
    }

    private void actualizarTareas(List<Tarea> tareas) {
        if (tareas != null) {
            tareaAdapter.setTareas(tareas);
        } else {
            // Maneja el caso cuando no haya tareas
            Toast.makeText(getContext(), "No se pudieron cargar las tareas", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
