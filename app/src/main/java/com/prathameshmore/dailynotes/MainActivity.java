package com.prathameshmore.dailynotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.prathameshmore.dailynotes.models.Note;
import com.prathameshmore.toastylibrary.Toasty;

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

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String title, description, time;
    private NotesDao notesDao;
    private TextInputLayout layoutTitle, layoutDescription;
    private TextInputEditText inputEditTextTitle, inputEditTextDescription;
    private NoteViewModel noteViewModel;
    private Vibrator vibrator;
    private AlertDialog.Builder deleteBuilder;
    private Toasty toasty;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        toasty = new Toasty(this);

        MobileAds.initialize(this, "ca-app-pub-9370043571819984~8956283764");
        adView = findViewById(R.id.adView);
        adView.setAdUnitId("ca-app-pub-9370043571819984/8381568698");


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        deleteBuilder = new AlertDialog.Builder(MainActivity.this);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);


        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Collections.sort(notes, new Comparator<Note>() {
                    @Override
                    public int compare(Note note, Note t1) {
                        return note.getId() > t1.getId() ?  -1  : (note.getId() < t1.getId()) ? 1 : 0;
                    }
                });
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                deleteBuilder.setMessage(R.string.delete_title).setTitle(R.string.warning_delete).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                        //Toast.makeText(MainActivity.this, R.string.toast_delete, Toast.LENGTH_SHORT).show();
                        toasty.dangerToasty(MainActivity.this,"Note deleted",Toasty.LENGTH_LONG,Toasty.BOTTOM);
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(MainActivity.this, R.string.cancel_btn, Toast.LENGTH_SHORT).show();

                        toasty.infoToasty(MainActivity.this,"Cancel",Toasty.LENGTH_LONG,Toasty.BOTTOM);
                    }
                });

                AlertDialog alertDialog = deleteBuilder.create();
                alertDialog.show();
                noteViewModel.getAllNotes().observe(MainActivity.this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        adapter.setNotes(notes);
                    }
                });

               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(1);
                } else {
                    vibrator.vibrate(VibrationEffect.DEFAULT_AMPLITUDE);
                } */
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                showDialog(note);
                //Toast.makeText(MainActivity.this, note.getId(), Toast.LENGTH_SHORT).show();
            }
        });

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

        switch (id) {
            case R.id.action_delete_all:
                final AlertDialog.Builder delete = new AlertDialog.Builder(this);
                delete.setMessage(R.string.delete_title).setTitle(R.string.warning_delete_all).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noteViewModel.deleteAll();
                        //Toast.makeText(MainActivity.this, R.string.toast_all_delete, Toast.LENGTH_SHORT).show();
                        toasty.dangerToasty(MainActivity.this,"All notes deleted",Toasty.LENGTH_LONG,Toasty.BOTTOM);
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = delete.create();
                alertDialog.show();
                return true;

            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;

            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Daily Notes - Super Simple Clean Note Taking App \n ");
                startActivity(Intent.createChooser(shareIntent, "Send to your friend"));
                return true;

            case R.id.action_help:
                showHelpDialog();
                return true;

            case R.id.action_report_bug:
                links("https://github.com/pprathameshmore/DailyNotes/issues");
                toasty.darkToasty(this, "Thank you for reporting bug! :) ", Toasty.LENGTH_LONG, Toasty.CENTER);
                return true;

            case R.id.action_privacy_policy:
                links("https://github.com/pprathameshmore/DailyNotes/blob/master/privacy_policy.md");
                return true;

        }



        return super.onOptionsItemSelected(item);
    }

    public void links(String link) {
        Uri uriLink = Uri.parse(link);
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, uriLink);
        startActivity(linkIntent);
    }

    private void showHelpDialog() {
        ViewGroup viewGroup = findViewById(R.id.content);
        View helpDialog = LayoutInflater.from(this).inflate(R.layout.alert_help, viewGroup, false);
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setView(helpDialog).setTitle("How to use?").setCancelable(true).setNeutralButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = helpBuilder.create();
        alertDialog.show();

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
                String currentDateTimeS = DateFormat.getDateTimeInstance().format(new Date());

                if (title.trim().isEmpty() || description.trim().isEmpty()) {
                    //Toast.makeText(MainActivity.this, "Blank", Toast.LENGTH_SHORT).show();
                    showDialog();
                    toasty.warningToasty(MainActivity.this,"Enter required fields", Toasty.LENGTH_LONG, Toasty.BOTTOM);
                } else {
                    NoteViewModel noteViewModel = new NoteViewModel(getApplication());
                    noteViewModel.insert(new Note(title, currentDateTimeS, description, 1));
                    toasty.successToasty(MainActivity.this,"Note saved", Toasty.LENGTH_LONG, Toasty.BOTTOM);

                }

            }
        }).setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(MainActivity.this, R.string.note_discarded, Toast.LENGTH_SHORT).show();
                toasty.infoToasty(MainActivity.this,"Note discarded",Toasty.LENGTH_LONG,Toasty.BOTTOM);
                dialogInterface.dismiss();
            }
        });
        alertBuilder.setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void showDialog(final Note noteData) {
        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_note, viewGroup, false);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        layoutTitle = dialogView.findViewById(R.id.textInputTitle);
        layoutDescription = dialogView.findViewById(R.id.textInputDescription);

        inputEditTextTitle = dialogView.findViewById(R.id.editTextTitle);
        inputEditTextDescription = dialogView.findViewById(R.id.editTextDescription);

        inputEditTextTitle.setText(noteData.getTitle());
        inputEditTextDescription.setText(noteData.getDescription());
        final int id = noteData.getId();
        //etTitle.addTextChangedListener(new NoteValidator(etTitle));
        //etDescription.addTextChangedListener(new NoteValidator(etDescription));

        alertBuilder.setView(dialogView).setPositiveButton(R.string.save_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(MainActivity.this, "Save", Toast.LENGTH_SHORT).show();
                title = inputEditTextTitle.getText().toString().trim();
                description = inputEditTextDescription.getText().toString().trim();
                String currentDateTimeS = DateFormat.getDateTimeInstance().format(new Date());

                if (title.trim().isEmpty() || description.trim().isEmpty()) {
                    showDialog();
                } else {
                    NoteViewModel noteViewModel = new NoteViewModel(getApplication());
                    Note note = new Note(title, currentDateTimeS,description,1);
                    note.setId(id);
                    noteViewModel.update(note);
                    //Toast.makeText(MainActivity.this, R.string.toast_note_updated, Toast.LENGTH_SHORT).show();
                    toasty.successToasty(MainActivity.this,"Note updated", Toasty.LENGTH_LONG, Toasty.BOTTOM);
                }

            }
        }).setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(MainActivity.this, R.string.toast_no_changes, Toast.LENGTH_SHORT).show();
                toasty.infoToasty(MainActivity.this,"No changes",Toasty.LENGTH_LONG,Toasty.BOTTOM);
                dialogInterface.dismiss();
            }
        });
        alertBuilder.setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();
        alertBuilder.setTitle("Add new note");
        alertDialog.show();
    }
}
