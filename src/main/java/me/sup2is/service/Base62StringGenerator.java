package me.sup2is.service;

import me.sup2is.util.Base62;
import org.springframework.stereotype.Service;

@Service
public class Base62StringGenerator implements StringGenerator<Long> {

    @Override
    public String generate(Long key) {
        String encode = Base62.encode(key);
        return encode;
    }

}
