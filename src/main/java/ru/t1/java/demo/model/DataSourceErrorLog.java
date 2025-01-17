package ru.t1.java.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DataSourceErrorLog {
    /**
     * Текст стектрейса исключения в котором произошла ошибка
     */
    private String stackTrace;
    /**
     * Текст сообщения исключения
     */
    private String message;
    /**
     *  Сигнатура метода
     */
    private String methodSignature; //todo он же должен быть в стектрейсе. Проверить


}
