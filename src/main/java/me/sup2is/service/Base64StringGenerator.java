package me.sup2is.service;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Base64;

@Service
public class Base64StringGenerator implements StringGenerator<Long> {

    @Override
    public String generate(Long key) {
        String str = Base64.getEncoder().encodeToString(longToBytes(key));
        return str.substring(str.length() - 8, str.length() - 1);
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}
