package ru.nsu.fit.g15203.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.nsu.fit.g15203.model.NodeEntity;
import ru.nsu.fit.g15203.model.TagEntity;
import ru.nsu.fit.g15203.model.generated.Node;
import ru.nsu.fit.g15203.model.generated.Node.Tag;
import ru.nsu.fit.g15203.repository.NodeRepository;
import ru.nsu.fit.g15203.repository.TagRepository;

import static java.lang.ClassLoader.getSystemResourceAsStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OsmService {

    private static final String NODE = "node";
    private static final String NAME_EN = "name:en";

    private final NodeRepository nodeRepository;
    private final TagRepository tagRepository;
    private final TransactionTemplate transactionTemplate;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void trigger() {
        executor.execute(() -> parse("osm/RU-NVS_osm.xml"));
    }

    public void parse(String filepath) {
        try (BufferedReader bufferedReader = getReader(filepath)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(bufferedReader);

            while (reader.hasNext() && !reader.isStartElement()) {
                reader.next();
            }

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT && NODE.equals(reader.getLocalName())) {
                    parseNode(reader, jaxbContext);
                }
            }
            executor.shutdown();
        } catch (Exception e) {
            log.warn(e.getMessage(), e.getCause());
        }

    }

    private void parseNode(XMLStreamReader reader, JAXBContext jaxbContext) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Node node = (Node) unmarshaller.unmarshal(reader);

        Collection<Tag> tagCollection = CollectionUtils.emptyIfNull(node.getTag());

        String name = tagCollection
            .stream()
            .filter(tag -> tag.getK().equalsIgnoreCase(NAME_EN))
            .map(Tag::getV)
            .findFirst()
            .orElse(null);

        transactionTemplate.execute((status) -> {
            NodeEntity dbNode = nodeRepository.save(
                new NodeEntity()
                    .setLatitude(node.getLat())
                    .setLongitude(node.getLon())
                    .setName(name)
            );

            tagRepository.saveAll(
                tagCollection.stream().map(tag ->
                    new TagEntity()
                        .setKey(tag.getK())
                        .setValue(tag.getV())
                        .setNode(dbNode)
                ).collect(Collectors.toList())
            );
            return null;
        });
    }

    private BufferedReader getReader(String filepath) {
        InputStream fileInputStream = getSystemResourceAsStream(filepath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(fileInputStream));
        return new BufferedReader(new InputStreamReader(bufferedInputStream));
    }
}
