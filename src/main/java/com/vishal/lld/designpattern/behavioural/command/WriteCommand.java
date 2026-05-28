package com.vishal.lld.designpattern.behavioural.command;

import com.vishal.lld.designpattern.behavioural.command.interfaces.Command;

public class WriteCommand implements Command {
    private TextEditor editor;
    private String wordToAdd;

    public WriteCommand(TextEditor editor, String wordToAdd) {
        this.editor = editor;
        this.wordToAdd = wordToAdd;
    }

    @Override
    public void execute() {
        System.out.println("Performing write command...");
        editor.write(wordToAdd);
    }

    @Override
    public void undo() {
        System.out.println("Performing undo write command");
        editor.delete();
    }
}
