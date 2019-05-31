package ru.nsu.fit.g15203.exception;

public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(long id) {
        super(String.format("Node with id = [%d] not found", id));
    }
}
