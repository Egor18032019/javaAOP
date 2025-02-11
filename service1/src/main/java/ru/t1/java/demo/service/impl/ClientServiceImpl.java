package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.util.ClientMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final ClientMapper clientMapper;
    private final KafkaProducer kafkaProducer;
    @Value("${t1.kafka.topic.client_registration}")
    private String topic;


    @Override
    public List<Client> registerClients(List<Client> clients) {
        return null;
    }

    @Override
    public Client registerClient(ClientDto clientDto) {
        Client client = clientMapper.toEntityWithId(clientDto);

        repository.save(client);
        boolean result = kafkaProducer.send(client, topic);
        return client;
    }



}
