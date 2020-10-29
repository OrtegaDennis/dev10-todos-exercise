package learn.todos.controllers;

import learn.todos.models.ToDo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDosController {
    private static int nextId = 11;
    private static List<ToDo> toDos = new ArrayList<>(List.of(
            new ToDo(1, "Buy milk."),
            new ToDo(2, "Walk the dog."),
            new ToDo(3, "Wash the car."),
            new ToDo(4, "Workout."),
            new ToDo(5, "Make dinner reservations."),
            new ToDo(6, "Cook dinner."),
            new ToDo(7, "Pack a lunch."),
            new ToDo(8, "Give the dog a bath"),
            new ToDo(9, "Change the oil."),
            new ToDo(10, "Get cash from the ATM.")
    ));

    @GetMapping
    public List<ToDo> get() {
        return toDos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        ToDo todo = toDos.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody(required = false) ToDo toDo) {
        List<String> errors = validateToDo(toDo);
        if (errors.size() > 0) {
            return ResponseEntity.badRequest().body(errors);
        }

        toDo.setId(nextId);
        nextId++;

        URI uri = URI.create(String.format("/api/todos/%s", toDo.getId()));

        return ResponseEntity.created(uri).body(toDo);
    }

//    @PutMapping

//    @DeleteMapping

    private List<String> validateToDo(ToDo toDo) {
        List<String> errors = new ArrayList<>();

        if (toDo == null) {
            errors.add("Please provide a TODO.");
            return errors;
        }

        if (toDo.getDescription() == null || toDo.getDescription().isBlank()) {
            errors.add("Please provide a description.");
        }

        return errors;
    }
}