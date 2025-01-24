package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> registerClients(List<Client> clients);

    Client registerClient(Client client);

    List<ClientDto> parseJson();

    void clearMiddleName(List<ClientDto> dtos);
}
