package br.com.arthivia.api.controllers;

import br.com.arthivia.api.models.dtos.MeasureDto;
import br.com.arthivia.api.services.MeasureService;
import br.com.arthivia.api.util.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/measures")
public class MeasureController {
    private final MeasureService measureService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createMeasure(@PathVariable @Valid String name) {
        var result = measureService.createMeasure(name);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @PutMapping("/update")
    public ResponseEntity<SuccessResponse> updateMeasure(@PathVariable @Valid UUID id, @PathVariable @Valid String name) {
        var result = measureService.updateMeasure(id, name);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse> deleteMeasure(@PathVariable @Valid UUID id) {
        var result = measureService.deleteMeasure(id);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MeasureDto>> getAllMeasures() {
        var result = measureService.getAllMeasures();
        return ResponseEntity.ok(result);
    }
}
