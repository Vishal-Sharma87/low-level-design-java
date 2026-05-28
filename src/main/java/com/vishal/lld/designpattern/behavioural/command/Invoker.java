package com.vishal.lld.designpattern.behavioural.command;

import java.util.Deque;
import java.util.LinkedList;

import com.vishal.lld.designpattern.behavioural.command.interfaces.Command;

public class Invoker {
    private Deque<Command> history;
    private Deque<Command> redo;

    public Invoker() {
        history = new LinkedList<>();
        redo = new LinkedList<>();
    }

    public void executeCommand(Command command) {
        redo.clear();
        command.execute();
        history.push(command);
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command undoCommand = history.pop();
            undoCommand.undo();
            redo.push(undoCommand);
            return;
        }
        // Must throw an exception, printing to keep the program running
        System.out.println("No commands to undo");
    }

    public void redo() {
        if (!redo.isEmpty()) {
            Command redoCommand = redo.pop();
            redoCommand.execute();
            history.push(redoCommand);
        }
    }
}
