package com.vishal.lld.designpattern.behavioural.command;

import java.util.ArrayList;
import java.util.List;

public class TextEditor {

    private List<String> editor;

    public TextEditor() {
        editor = new ArrayList<>();
    }

    public void write(String word) {
        if (word != null && !word.isEmpty()) {
            editor.add(word);
            return;
        }
        throw new IllegalArgumentException("Word cannot be null or empty");
    }

    public String delete() {
        if (!editor.isEmpty()) {
            return editor.removeLast();
        }
        throw new RuntimeException("Insert some words first inorder to delete");
    }

    public List<String> getText() {
        return editor;
    }

    public void format() {
        List<String> formattedEditor = editor.stream().map(s -> s.toUpperCase()).toList();
        editor = formattedEditor;
    }

    public void unformat(List<String> image) {
        editor = image;
    }

    public void showEditor() {
        editor.forEach(j -> System.out.print(j + " "));
        System.out.println();
    }

}
