package com.prathameshmore.dailynotes;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prathameshmore.dailynotes.models.Note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String title, description, time;
    private NotesDao notesDao;
    private TextInputLayout layoutTitle, layoutDescription;
    private TextInputEditText inputEditTextTitle, inputEditTextDescription;
    private NoteViewModel noteViewModel;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(1);
                } else {
                    vibrator.vibrate(VibrationEffect.DEFAULT_AMPLITUDE);
                } */
            }
        }).attachToRecyclerView(recyclerView);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_delete_all) {

            if (noteViewModel.getAllNotes() == null) {
                item.setEnabled(false);
            } else {
                noteViewModel.deleteAll();
                Toast.makeText(this, R.string.toast_all_delete, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {

        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_note, viewGroup, false);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        layoutTitle = dialogView.findViewById(R.id.textInputTitle);
        layoutDescription = dialogView.findViewById(R.id.textInputDescription);

        inputEditTextTitle = dialogView.findViewById(R.id.editTextTitle);
        inputEditTextDescription = dialogView.findViewById(R.id.editTextDescription);

        //etTitle.addTextChangedListener(new NoteValidator(etTitle));
        //etDescription.addTextChangedListener(new NoteValidator(etDescription));

        alertBuilder.setView(dialogView).setPositiveButton(R.string.save_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(MainActivity.this, "Save", Toast.LENGTH_SHORT).show();
                title = inputEditTextTitle.getText().toString().trim();
                description = inputEditTextDescription.getText().toString().trim();

                if (title.trim().isEmpty() || description.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter required fields", Toast.LENGTH_SHORT).show();
                } else {
                    NoteViewModel noteViewModel = new NoteViewModel(getApplication());
                    noteViewModel.insert(new Note(title, "1", description, 1));
                }

            }
        }).setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, R.string.note_discarded, Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        alertBuilder.setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
