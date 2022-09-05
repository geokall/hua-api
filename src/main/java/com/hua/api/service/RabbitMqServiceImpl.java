package com.hua.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hua.api.dto.RabbitMqDTO;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqServiceImpl.class);

    private final static String HOST = "huademo.ddns.net";
    private final static String QUEUE_REGISTRATIONS = "registrations";
    private final static String QUEUE_CHANGE_PASSWORD = "password";
    private final static String QUEUE_UPDATE_STUDENT = "update";

    public RabbitMqServiceImpl() {
    }

    @Override
    public void queueRegistrationStudent(RabbitMqDTO dto) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_REGISTRATIONS, false, false, false, null);
            ObjectMapper mapper = new ObjectMapper();

            String student = mapper.writeValueAsString(dto);
            channel.basicPublish("", QUEUE_REGISTRATIONS, null, student.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("Sending student registration to rabbit queue");
        }
    }

    @Override
    public void queueChangePassword(RabbitMqDTO dto) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_CHANGE_PASSWORD, false, false, false, null);
            ObjectMapper mapper = new ObjectMapper();

            String passwordChanged = mapper.writeValueAsString(dto);
            channel.basicPublish("", QUEUE_CHANGE_PASSWORD, null, passwordChanged.getBytes(StandardCharsets.UTF_8));
            LOGGER.info("Sending change password to rabbit queue");
        }
    }
}
