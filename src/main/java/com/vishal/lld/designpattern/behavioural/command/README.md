# Command Pattern

## Problem It Solves

Operations need to be undoable and redoable. Without Command pattern, storing enough information to reverse each operation leads to messy flag-based or type-based logic.

**Without Command — mixed types, if-else on undo:**

```java
// what does this stack hold? different data for each operation
Stack<Object> history;

// on undo — if-else nightmare
Object last = history.pop();
if (last instanceof String) {
    // was a write — delete it
} else if (last instanceof Boolean) {
    // was a format — unformat
} else if ... // keeps growing
```

**What is wrong:**

- Each operation needs different data to undo — no uniform way to store
- Undo logic scattered across caller — not inside the operation itself
- Adding new operation requires adding new if-else branch — OCP violation
- No clean way to support redo without duplicating more logic

---

## Core Idea

> Encapsulate a request as an object.  
> The object knows how to execute itself and how to undo itself.  
> Caller just manages a stack of command objects — no knowledge of what each command does internally.

---

## The Four Roles

```
Command          → interface with execute() and undo()
ConcreteCommand  → implements Command, owns execute + undo logic, holds captured state
Receiver         → actual object being operated on (TextEditor) — exposes primitive operations
Invoker          → manages history and redo stacks, calls execute() and undo()
```

---

## Undo and Redo — Two Stack Approach

```
history stack  → commands that have been executed — available for undo
redo stack     → commands that have been undone — available for redo
```

**Execute new command:**

```
redo.clear()          ← new action invalidates redo history
command.execute()
history.push(command)
```

**Undo:**

```
command = history.pop()
command.undo()
redo.push(command)    ← available for redo
```

**Redo:**

```
command = redo.pop()
command.execute()
history.push(command) ← available for undo again
```

**Why redo.clear() on new command?**

```
write "Hello" → history: [write], redo: []
undo          → history: [],      redo: [write]
write "World" → redo.clear() — undone history gone
              → history: [write(World)], redo: []
```

Once you write something new after undoing — the undone history is gone. Same behavior as every text editor.

---

## The Implementation

**Command interface:**

```java
public interface Command {
    void execute();
    void undo();
}
```

**Receiver — TextEditor holds state, exposes primitive operations:**

```java
// TextEditor never knows about undo, redo, or commands
// it just does what it is told
public class TextEditor {

    private List<String> editor = new ArrayList<>();

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
        throw new RuntimeException("Insert some words first to delete");
    }

    public void format() {
        editor = editor.stream().map(String::toUpperCase).toList();
    }

    public void unformat(List<String> original) {
        editor = new ArrayList<>(original); // restore exact original state
    }

    public List<String> getText() {
        return new ArrayList<>(editor); // defensive copy — snapshot of current state
    }

    public void showEditor() {
        editor.forEach(w -> System.out.print(w + " "));
        System.out.println();
    }
}
```

**WriteCommand — captures word for undo:**

```java
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
        System.out.println("Performing undo write command...");
        editor.delete();
    }
}
```

**DeleteCommand — captures deleted word at execute time:**

```java
public class DeleteCommand implements Command {

    private TextEditor editor;
    private String deletedWord; // captured at execute time, not construction time

    public DeleteCommand(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Performing delete command...");
            deletedWord = editor.delete(); // capture before deleting
        } catch (RuntimeException e) {
            System.out.println("Empty editor — cannot perform delete");
        }
    }

    @Override
    public void undo() {
        if (deletedWord != null) { // null if execute() threw exception
            System.out.println("Performing undo delete command...");
            editor.write(deletedWord);
        }
    }
}
```

**FormatCommand — captures original state before formatting:**

```java
public class FormatCommand implements Command {

    private TextEditor editor;
    private List<String> image; // snapshot of state before format

    public FormatCommand(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        image = editor.getText(); // capture exact original state
        System.out.println("Performing format command...");
        editor.format();
    }

    @Override
    public void undo() {
        if (image == null) return;
        System.out.println("Performing undo format command...");
        editor.unformat(image); // restore exact original — not just lowercase
    }
}
```

**Invoker — pure command manager, zero knowledge of TextEditor:**

```java
public class Invoker {

    private Deque<Command> history = new LinkedList<>();
    private Deque<Command> redo = new LinkedList<>();

    public void executeCommand(Command command) {
        redo.clear();            // new action invalidates redo history
        command.execute();
        history.push(command);
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redo.push(command);
            return;
        }
        System.out.println("Nothing to undo");
    }

    public void redo() {
        if (!redo.isEmpty()) {
            Command command = redo.pop();
            command.execute();
            history.push(command); // back in history — can be undone again
            return;
        }
        System.out.println("Nothing to redo");
    }
}
```

---

## Usage

```java
TextEditor editor = new TextEditor();
Invoker invoker = new Invoker();

// Invoker knows nothing about TextEditor — caller wires editor to commands
invoker.executeCommand(new WriteCommand(editor, "Hello"));
invoker.executeCommand(new WriteCommand(editor, "I"));
invoker.executeCommand(new WriteCommand(editor, "am"));
invoker.executeCommand(new WriteCommand(editor, "Vishal"));
editor.showEditor(); // Hello I am Vishal

invoker.executeCommand(new DeleteCommand(editor));
invoker.executeCommand(new FormatCommand(editor));
editor.showEditor(); // HELLO I AM

invoker.undo();      // undo format
editor.showEditor(); // Hello I am

invoker.undo();      // undo delete
editor.showEditor(); // Hello I am Vishal

invoker.redo();      // redo delete
editor.showEditor(); // Hello I am
```

---

## Key Design Decisions

**Why capture state at execute() time and not construction time?**
Between construction and execution, the editor state may change. Capturing at execute() guarantees the snapshot reflects the actual state at the moment the operation ran.

**Why does FormatCommand store original state instead of just unformatting?**
`unformat()` via lowercase loses original casing — `"Hello"` becomes `"hello"` after format then undo. Storing the exact snapshot before format and restoring it preserves original casing perfectly.

**Why does Invoker not hold TextEditor reference?**
Invoker's job is managing command stacks — not wiring commands to receivers. Keeping TextEditor out of Invoker means Invoker works with any command on any receiver — pure SRP.

**Why clear redo stack on new command?**
Once a new action is taken after undoing, the undone future is invalidated. Keeping it would allow replaying a timeline that no longer matches current state — inconsistent behavior.

---

## Gain vs Loss

```
Gain:
  clean undo/redo — each command owns its own reversal logic
  OCP — new operations added without touching Invoker or existing commands
  SRP — TextEditor does operations, Commands capture state, Invoker manages stacks
  extensible — macro commands (batch execute) easy to add

Loss:
  more classes — one class per operation
  state capture complexity — each command must carefully decide what to snapshot
  memory — history stack grows with every operation, needs size limit in production
```

---

## Where Command Appears in LLD Case Studies

| Case Study            | Command Used For                          |
| --------------------- | ----------------------------------------- |
| Text editor           | Write, delete, format with undo/redo      |
| Database transactions | Each query as a command, rollback as undo |
| Game engine           | Player moves as commands, replay and undo |
| Task scheduler        | Each scheduled task as a command object   |

---

## Interview Version to Write

Show the mixed-type stack problem first — flags and if-else on undo.  
Introduce Command interface with execute() and undo().  
Show each command capturing its own state for undo — write captures word, delete captures deleted word, format captures snapshot.  
Show Invoker with two stacks — clean undo and redo flow.  
Explain why redo.clear() on new command — invalidates stale future.  
Mention memory concern — history stack needs size limit in production.
