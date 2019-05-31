package ru.nsu.fit.g15203.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.g15203.exception.NodeNotFoundException;
import ru.nsu.fit.g15203.model.NodeEntity;
import ru.nsu.fit.g15203.repository.NodeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NodeService {
    private final NodeRepository nodeRepository;

    public NodeEntity create(NodeEntity node) {
        return nodeRepository.save(node);
    }

    public NodeEntity read(long id) {
        return nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
    }

    public void delete(long id) {
        NodeEntity node = nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
        nodeRepository.delete(node);
    }

    public NodeEntity update(long id, NodeEntity incoming) {
        NodeEntity node = nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
        node.setLatitude(incoming.getLatitude());
        node.setLongitude(incoming.getLongitude());
        node.setName(incoming.getName());
        node.addAllTags(incoming.getTags());
        return nodeRepository.save(node);
    }
}
