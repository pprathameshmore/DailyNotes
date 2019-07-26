package com.prathameshmore.dailynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.prathameshmore.dailynotes.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view,parent,false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewTime.setText(note.getTime());
        holder.textDescription.setText(note.getDescription());
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewTime;
        private TextView textDescription;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewTime = itemView.findViewById(R.id.tv_time);
            textDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
