package com.example.gestionhuman.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionhuman.R;
import com.example.gestionhuman.models.Tarea;
import com.example.gestionhuman.network.ApiService;
import com.example.gestionhuman.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private List<Tarea> tareas = new ArrayList<>();
    private OnEditButtonClickListener onEditButtonClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    // Método para deshabilitar todos los EditText en el adaptador
    public void deshabilitarTodosLosEditText() {
        for (int i = 0; i < tareas.size(); i++) {
            notifyItemChanged(i);  // Notificar que se debe actualizar cada item
        }
    }

    // Método para eliminar una tarea
    public void eliminarTarea(int position, Context context) {
        Tarea tarea = tareas.get(position);
        ApiService apiService = RetrofitInstance.getApi();
        apiService.eliminarTarea(tarea.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                } else {
                    // Maneja el caso de error en la eliminación
                    Toast.makeText(context, "Tarea Completada", Toast.LENGTH_SHORT).show();
                    // Eliminar la tarea de la lista
                    tareas.remove(position);
                    // Notificar que el item fue removido
                    notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error en la conexión
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);
        holder.editTextTitulo.setText(tarea.getTitulo());

        // Configurar el botón de editar
        holder.buttonEditar.setOnClickListener(v -> {
            if (onEditButtonClickListener != null) {
                onEditButtonClickListener.onEditButtonClick(tarea, holder.editTextTitulo);
            }
        });

        // Configurar el botón de eliminar
        holder.buttonEliminar.setOnClickListener(v -> {
            if (onDeleteButtonClickListener != null) {
                onDeleteButtonClickListener.onDeleteButtonClick(tarea);
                eliminarTarea(position, holder.itemView.getContext());  // Llamar al método de eliminación y pasar el contexto
            }
        });

        // Guardar cambios y deshabilitar el EditText cuando pierde el foco
        holder.editTextTitulo.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                tarea.setTitulo(holder.editTextTitulo.getText().toString());
                actualizarTareaEnServidor(tarea);
                holder.editTextTitulo.setEnabled(false);
                esconderTeclado(v, holder.itemView.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.onEditButtonClickListener = listener;
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(Tarea tarea, EditText editTextTitulo);
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(Tarea tarea);
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        EditText editTextTitulo;
        Button buttonEditar;
        Button buttonEliminar;

        public TareaViewHolder(View itemView) {
            super(itemView);
            editTextTitulo = itemView.findViewById(R.id.editTextTitulo);
            buttonEditar = itemView.findViewById(R.id.buttonEditar);
            buttonEliminar = itemView.findViewById(R.id.buttonEliminar);
        }
    }

    private void actualizarTareaEnServidor(Tarea tarea) {
        ApiService apiService = RetrofitInstance.getApi();
        apiService.actualizarTarea(tarea.getId(), tarea).enqueue(new Callback<Tarea>() {
            @Override
            public void onResponse(Call<Tarea> call, Response<Tarea> response) {
                if (response.isSuccessful()) {
                    // Éxito
                }
            }

            @Override
            public void onFailure(Call<Tarea> call, Throwable t) {
                // Error en la conexión
            }
        });
    }

    private void esconderTeclado(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
