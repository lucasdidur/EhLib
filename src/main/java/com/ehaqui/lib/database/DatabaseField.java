package com.ehaqui.lib.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseField
{
    /**
     * Define o nome do campo
     * @return
     */
    String value();

    /**
     * Define o campo como id e auto increment
     * @return
     */
    boolean id() default false;

    /**
     * Define o campo como auto increment
     * @return
     */
    boolean autoincrement() default false;

    /**
     * Define o tipo do campo
     * @return
     */
    String type() default "";

    /**
     * Define o valor padrão
     * @return
     */
    String defaultValue() default "";

    /**
     * Cria o banco de dados com o timestamp atual
     * @return
     */
    boolean now() default false;

    /**
     * Define o campo atual como nulo
     * @return
     */
    boolean nullField() default false;

    /**
     * Define o campo como unico
     * @return
     */
    boolean unique() default false;

    /**
     * Define o campo como uma key
     * @return
     */
    boolean key() default false;

}
