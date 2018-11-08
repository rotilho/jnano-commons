package com.rotilho.jnano.commons;

public interface NanoAccountType {
    String extractEncodedPublicKey(String account);

    String regex();
}
