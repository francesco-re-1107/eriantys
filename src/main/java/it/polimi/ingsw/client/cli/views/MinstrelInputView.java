package it.polimi.ingsw.client.cli.views;

import it.polimi.ingsw.server.model.Student;
import it.polimi.ingsw.server.model.StudentsContainer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This input view is specific for the Minstrel card.
 * It asks the user to choose 2 students to add to school and 2 students to remove from school.
 */
public class MinstrelInputView extends BaseView{

    /**
     * SimpleInputView used interally.
     */
    private final SimpleInputView inputView;

    /**
     * Holds the students to remove from school.
     */
    private final StudentsContainer studentsToRemove = new StudentsContainer();

    /**
     * The entrance of the player
     */
    private final StudentsContainer entrance;

    /**
     * The school of the player
     */
    private final StudentsContainer school;

    /**
     * Listener called when the user has chosen the students to remove from school.
     */
    private final Consumer<StudentsContainer> toRemoveListener;

    /**
     * Listener called when the user has chosen the students to add to school.
     * In other words, when the whole process is completed.
     */
    private final BiConsumer<StudentsContainer, StudentsContainer> completedListener;

    /**
     * Current phase of the process.
     */
    private Phase phase = Phase.REMOVE;

    /**
     * Message displayed to the user when removing students.
     */
    private static final String REMOVE_STUDENTS_MESSAGE = "Quali studenti vuoi rimuovere dalla sala? (e.g. 'y r')";

    /**
     * Message displayed to the user when adding students.
     */
    private static final String ADD_STUDENTS_MESSAGE = "Quali studenti vuoi aggiungere alla sala? (e.g. 'y r')";

    /**
     * Message displayed to the user when a wrong input is given.
     */
    private static final String WRONG_COMMAND_MESSAGE = "Comando errato (esempi 'y r', 'b b')";

    /**
     * Message displayed to the user when the selected students are not in the available.
     */
    private static final String NOT_ENOUGH_STUDENTS_ERROR_MESSAGE = "Non possiedi questi studenti";


    /**
     * Create a MinstrelInputView with the given parameters.
     * @param toRemoveListener listener called when the user has chosen the students to remove from school.
     * @param completedListener listener called when the user has chosen the students to add to school, hence the process is completed.
     * @param entrance entrance of the player
     * @param school school of the player
     */
    public MinstrelInputView(Consumer<StudentsContainer> toRemoveListener,
                             BiConsumer<StudentsContainer, StudentsContainer> completedListener,
                             StudentsContainer entrance,
                             StudentsContainer school) {
        this.toRemoveListener = toRemoveListener;
        this.completedListener = completedListener;
        this.entrance = entrance;
        this.school = school;

        this.inputView = new SimpleInputView(REMOVE_STUDENTS_MESSAGE);
    }

    @Override
    public void draw() {
        if(phase == Phase.REMOVE) {
            inputView.setMessage(REMOVE_STUDENTS_MESSAGE);
            inputView.setListener(input -> {
                var parsedStudents = parseInput(input);

                if (parsedStudents.getSize() != 2) {
                    inputView.showError(WRONG_COMMAND_MESSAGE);
                } else {
                    if(!school.contains(parsedStudents)) {
                        inputView.showError(NOT_ENOUGH_STUDENTS_ERROR_MESSAGE);
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
            inputView.setMessage(ADD_STUDENTS_MESSAGE);
            inputView.setListener(input -> {
                var parsedStudents = parseInput(input);

                if (parsedStudents.getSize() != 2) {
                    inputView.showError(WRONG_COMMAND_MESSAGE);
                } else {
                    if(!entrance.contains(parsedStudents)) {
                        inputView.showError(NOT_ENOUGH_STUDENTS_ERROR_MESSAGE);
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

    /**
     * Parse the given input to a StudentsContainer.
     * @param input input string to parse
     * @return StudentsContainer parsed from the input
     */
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

    /**
     * Phases of the process
     */
    private enum Phase {
        REMOVE,
        ADD
    }
}
