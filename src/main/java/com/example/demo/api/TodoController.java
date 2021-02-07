package com.example.demo.api;


import com.example.demo.model.Status;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;


    @GetMapping("/list")
    public List<Todo> getAllTodos(){
        return todoRepository.findAll();
    }

    @PostMapping("/add-todo")
        public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) throws URISyntaxException {
        java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        todo.setDate(currentDate);
        todo.setStatus(Status.ACTIVE);
        Todo result = todoRepository.save(todo);
        return ResponseEntity.created(new URI("/api/add-todo" + result.getId())).body(result);
    }

    @PutMapping("/update-todo/{id}")
    private ResponseEntity<Todo> updateTodo(@PathVariable UUID id){
        Todo todoToUpdate = todoRepository.findById(id).get();
        todoToUpdate.setStatus(Status.COMPLETE);
        todoRepository.save(todoToUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-todo/{id}")
    public ResponseEntity<Todo> removeTodo(@PathVariable UUID id){
        todoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
