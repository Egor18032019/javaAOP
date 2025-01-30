package ru.t1.java.demo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public  @interface  LogDataSourceError {
}
/*
 Аспект @LogDataSourceError логирующий сообщения об исключениях в проекте
  путем создания в БД новой записи DataSourceErrorLog в случае,
 если в результате CRUD-операций над сущностями возникла ошибка
 */