package br.com.arthivia.api.services;

import br.com.arthivia.api.models.dtos.MeasureDto;
import br.com.arthivia.api.models.entities.MeasureEntity;
import br.com.arthivia.api.repositories.MeasureRepository;
import br.com.arthivia.api.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeasureService {
    private final MeasureRepository measureRepository;

    public String createMeasure(String name) {
        measureRepository.findByName(name).ifPresent(m -> {
            throw new RuntimeException("The measure with name " + m.getName() + "' already exists. if you not found contact the support.");
        });

        var measure = new MeasureEntity();
        measure.setName(Util.normalizeText(name));
        measure.setActive(true);
        measureRepository.save(measure);
        return "Measure created successfully.";
    }

    public String updateMeasure(UUID id, String name) {
        var measure = measureRepository.findById(id).orElseThrow(() -> new RuntimeException("Measure with id '" + id + "' not found."));

        measure.setName(Util.normalizeText(name));
        measureRepository.save(measure);
        return "Measure updated successfully.";
    }

    public String deleteMeasure(UUID id) {
        measureRepository.findById(id).orElseThrow(() -> new RuntimeException("Measure with id '" + id + "' not found."));
        measureRepository.disableMeasure(id);
        return "Measure deleted successfully.";
    }

    public List<MeasureDto> getAllMeasures() {
        return measureRepository.findByActiveTrue().stream()
                .map(m -> new MeasureDto(m.getId(), m.getName())).toList();
    }
}
