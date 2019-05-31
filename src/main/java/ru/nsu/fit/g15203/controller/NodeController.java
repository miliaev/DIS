package ru.nsu.fit.g15203.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.g15203.dto.NodeDto;
import ru.nsu.fit.g15203.service.NodeService;

import static ru.nsu.fit.g15203.converter.NodeConverter.fromApi;
import static ru.nsu.fit.g15203.converter.NodeConverter.toApi;

@RestController
@RequestMapping("/node")
public class NodeController {
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/{id}")
    public NodeDto read(@Valid @PathVariable("id") long id) {
        return toApi(nodeService.read(id));
    }

    @PostMapping
    public NodeDto create(@Valid @RequestBody NodeDto node) {
        return toApi(nodeService.create(fromApi(node)));
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @PathVariable("id") long id) {
        nodeService.delete(id);
    }

    @PatchMapping("/{id}")
    public NodeDto update(@Valid @PathVariable("id") long id, @Valid @RequestBody NodeDto node) {
        return toApi(nodeService.update(id, fromApi(node)));
    }
}
