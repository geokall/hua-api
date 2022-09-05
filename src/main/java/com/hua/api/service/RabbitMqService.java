package com.hua.api.service;

import com.hua.api.dto.RabbitMqDTO;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface RabbitMqService {

    void queueRegistrationStudent(RabbitMqDTO dto) throws IOException, TimeoutException;

    void queueChangePassword(RabbitMqDTO dto) throws IOException, TimeoutException;
}
