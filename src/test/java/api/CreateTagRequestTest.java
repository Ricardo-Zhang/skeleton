package api;


import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
public class CreateTagRequestTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testValid() {
        CreateTagRequest tag = new CreateTagRequest();
        //tag.tagName = "Red";
        tag.id = new Integer(1);
        assertThat(validator.validate(tag), empty());
    }

    @Test
    public void testMissingId() {
        CreateTagRequest tag = new CreateTagRequest();
        assertThat(validator.validate(tag), hasSize(1));
    }}