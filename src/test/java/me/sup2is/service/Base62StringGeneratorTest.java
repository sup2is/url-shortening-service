package me.sup2is.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Base62StringGenerator.class)
class Base62StringGeneratorTest {

    @Autowired
    Base62StringGenerator base62StringGenerator;

    @Test
    public void generate() {
        //given
        long id = 100000;

        //when
        String generatedKey = base62StringGenerator.generate(id);

        //then
        assertNotNull(generatedKey);
    }

}