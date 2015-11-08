package main.java.dao.interfaces;

import java.util.List;

import main.java.model.Note;

public interface NoteDao {
	Note insert(Note note);
    Note getById(Long notesId);
    List<Note> getAll(Long userId);
    void update(Note note);
    void delete(Note note);

}
