package com.vishal.lld.designpattern.behavioural.command;

import com.vishal.lld.designpattern.behavioural.command.interfaces.Command;

public class DeleteCommand implements Command {

    private TextEditor editor;
    private String deletedWord;

    public DeleteCommand(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Performing Delete command...");
            deletedWord = editor.delete();
        } catch (RuntimeException e) {
            System.out.println("Empty editor cannot perform delete operation");
        }
    }

    @Override
    public void undo() {
        if (deletedWord == null)
            return;
        System.out.println("Performing undo Delete command...");
        editor.write(deletedWord);
    }
}
