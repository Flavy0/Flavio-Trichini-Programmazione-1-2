package com.example.math;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/math")
public class MathController {

    private final MathService mathService;

    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    // Path Variable
    // localhost:8080/math/sum/10/5
    @GetMapping("/sum/{a}/{b}")
    public int sum(@PathVariable int a, @PathVariable int b) {
        return mathService.add(a, b);
    }

    @GetMapping("/subtract/{a}/{b}")
    public int subtract(@PathVariable int a, @PathVariable int b) {
        return mathService.subtract(a, b);
    }

    @GetMapping("/multiply/{a}/{b}")
    public int multiply(@PathVariable int a, @PathVariable int b) {
        return mathService.multiply(a, b);
    }

    @GetMapping("/divide/{a}/{b}")
    public double divide(@PathVariable int a, @PathVariable int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Non è possibile dividere per zero.");
        }
        return (double) a / b;
    }

    // Query Params
    // localhost:8080/math/op?type=multiply&a=10&b=5
    @GetMapping("/op")
    public double operation(@RequestParam String type, @RequestParam int a, @RequestParam int b) {
        switch (type.toLowerCase()) {
            case "sum":
                return mathService.add(a, b);
            case "subtract":
                return mathService.subtract(a, b);
            case "multiply":
                return mathService.multiply(a, b);
            case "divide":
                return mathService.divide(a, b);
            default:
                throw new IllegalArgumentException("Operazione non valida: " + type);
        }
    }

    // JSON
    // POST localhost:8080/math/calculate (body scritto in JSON (raw) su postman)
    @PostMapping("/calculate")
    public double calculate(@RequestBody OperationRequest request) {
        switch (request.getType().toLowerCase()) {
            case "add":
                return mathService.add(request.getA(), request.getB());
            case "subtract":
                return mathService.subtract(request.getA(), request.getB());
            case "multiply":
                return mathService.multiply(request.getA(), request.getB());
            case "divide":
                return mathService.divide(request.getA(), request.getB());
            default:
                throw new IllegalArgumentException("Operazione non valida: " + request.getType());
        }
    }
}