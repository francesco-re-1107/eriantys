package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MinstrelInputView extends BaseView{
    private final SimpleInputView inputView;
    private final StudentsContainer studentsToRemove = new StudentsContainer();
    private final StudentsContainer entrance;
    private final StudentsContainer school;
    private final Consumer<StudentsContainer> toRemoveListener;
    private final BiConsumer<StudentsContainer, StudentsContainer> completedListener;
    private Phase phase = Phase.REMOVE;

    public MinstrelInputView(Consumer<StudentsContainer> toRemoveListener,
                             BiConsumer<StudentsContainer, StudentsContainer> completedListener,
                             StudentsContainer entrance,
                             StudentsContainer school) {
        this.toRemoveListener = toRemoveListener;
        this.completedListener = completedListener;
        this.entrance = entrance;
        this.school = school;

        this.inputView = new SimpleInputView("Quali studenti vuoi rimuovere dalla sala:");
    }

    @Override
    public void draw() {
        if(phase == Phase.REMOVE) {
            inputView.setMessage("Quali studenti vuoi rimuovere dalla sala:");
            inputView.setListener(input -> {
                var parsedStudents = parseInput(input);

                if (parsedStudents.getSize() != 2) {
                    inputView.showError("Comando errato (esempi 'y r', 'b b')");
                } else {
                    if(!school.contains(parsedStudents)) {
                        inputView.showError("Non possiedi questi studenti");
                    } else {
                        studentsToRemove.addAll(parsedStudents);
                        school.removeAll(parsedStudents);
                        entrance.addAll(parsedStudents);
                        phase = Phase.ADD;
                        toRemoveListener.accept(studentsToRemove);
                        draw();
                    }
                }
            });
            inputView.draw();
        } else if (phase == Phase.ADD) {
            inputView.setMessage("Quali studenti vuoi aggiungere alla sala:");
            inputView.setListener(input -> {
                var parsedStudents = parseInput(input);

                if (parsedStudents.getSize() != 2) {
                    inputView.showError("Comando errato (esempi 'y r', 'b b')");
                } else {
                    if(!entrance.contains(parsedStudents)) {
                        inputView.showError("Non possiedi questi studenti");
                    } else {
                        entrance.removeAll(parsedStudents);
                        school.addAll(parsedStudents);
                        completedListener.accept(studentsToRemove, parsedStudents);
                    }
                }
            });
            inputView.draw();
        }
    }

    private StudentsContainer parseInput(String input) {
        var students = new StudentsContainer();
        var args = input.split(" ");
        for(var a : args) {
            Student student = null;
            for(var s : Student.values())
                if(s.name().toLowerCase().startsWith(a))
                    student = s;

            if(student != null)
                students.addStudent(student);
        }

        return students;
    }

    private enum Phase {
        REMOVE,
        ADD
    }
}
