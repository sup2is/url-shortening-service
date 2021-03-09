package me.sup2is.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Base62Test {

    @Test
    public void encode() throws Exception {
        Set<String> set = new HashSet<>();
        int count = 10000;
        for (long i = 0; i < count; i++) {
            String encoded = Base62.encode(i);
            set.add(encoded);
        }

        assertEquals(count, set.size());
    }

    @Test
    public void encode_expect_encoded_str_length_lte_8_when_10_trillion_value() {
        String encode = Base62.encode(10000000000000L);
        assertTrue(8 <= encode.length());
    }

    @Test
    public void decode() {
        long expect = 10000000000000L;
        String encode = Base62.encode(expect);
        long decode = Base62.decode(encode);
        assertEquals(expect, decode);
    }

}