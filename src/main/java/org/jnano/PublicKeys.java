package org.jnano;

import com.rfksystems.blake2b.security.Blake2bProvider;

import java.security.Security;

final class PublicKeys {
    static {
        Security.addProvider(new Blake2bProvider());
    }

    private PublicKeys() {
    }

    public static byte[] generate(byte[] privateKey) {
        return ED25519.publickey(privateKey);
    }
}
