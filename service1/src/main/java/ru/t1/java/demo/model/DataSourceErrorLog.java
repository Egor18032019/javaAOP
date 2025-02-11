package ru.t1.java.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "datasource_error_log")
public class DataSourceErrorLog extends AbstractPersistable<Long> {
    /**
     * Текст стектрейса исключения в котором произошла ошибка
     */
    @Column(name = "stack_trace", length = 10000)
    private String stackTrace;
    /**
     * Текст сообщения исключения
     */
    @Column(name = "message")
    private String message;
    /**
     *  Сигнатура метода
     */
    @Column(name = "method_signature")
    private String methodSignature;
}
