package factory.repo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TransField {
    // ADD THIS ANNOTATION TO HAVE FIELD DO NOT SAVED INTO DATABASE OR INDEX INTO ELASTICSEARCH
    // giapdepchai do something handsomely
}

