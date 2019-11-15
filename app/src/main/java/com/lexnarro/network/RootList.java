package com.lexnarro.network;

import org.simpleframework.xml.Root;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by saurabh on 06-02-2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RootList {

    Root [] value() default {};
}
