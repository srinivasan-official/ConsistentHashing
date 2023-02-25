package com.localproj.hash.impl;

import com.localproj.hash.HashFunction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash implements HashFunction {

    private static final String HASH_ALGORITHM = "MD5";

    private MessageDigest messageDigest;

    public MD5Hash() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
    }

    @Override
    public Long hash(String key) {
        messageDigest.reset();
        messageDigest.update(key.getBytes());
        byte[] digest = messageDigest.digest();
        long hash = 0;
        for(int i=0; i<4; i++) {
            hash <<= 8;
            hash |= (int)digest[i] & 255;
        }
        return hash;
    }
}
