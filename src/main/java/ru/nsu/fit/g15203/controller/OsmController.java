package ru.nsu.fit.g15203.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.g15203.service.OsmService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OsmController {
    private final OsmService service;

    @PostMapping("/internal/fill")
    public String read() {
        log.info("Database filling initialized");
        service.trigger();
        return "Database filling initialized\n";
    }
}
