package com.vishal.lld.designpattern.behavioural.command;

import java.util.List;

import com.vishal.lld.designpattern.behavioural.command.interfaces.Command;

public class FormatCommand implements Command {

    private TextEditor editor;
    private List<String> image = null;

    public FormatCommand(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        image = editor.getText();
        System.out.println("Performing Format command...");
        editor.format();
    }

    @Override   
    public void undo() {
        if (image == null)
            return;
        System.out.println("Performing undo Format command");
        editor.unformat(image);
    }

}
