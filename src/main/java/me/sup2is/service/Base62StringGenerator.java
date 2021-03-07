package me.sup2is.service;

import me.sup2is.util.Base62;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Base64;

@Service
public class Base62StringGenerator implements StringGenerator<Long> {

    @Override
    public String generate(Long key) {
        String encode = Base62.encode(key);
        return encode;
    }

}
