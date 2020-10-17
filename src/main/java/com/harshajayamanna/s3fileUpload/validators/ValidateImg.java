package com.harshajayamanna.s3fileUpload.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImgValidator.class)
@Documented
public @interface ValidateImg {
    String message() default "Check width and Height of the image (width and height should be equal to 1024px)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
