package com.rotilho.jnano.commons;

import lombok.NonNull;

public final class NanoSignatures {
    private static final String SIGNATURE_REGEX = "([0-9A-Z]){128}";

    private NanoSignatures() {
    }

    @NonNull
    public static String sign(@NonNull byte[] privateKey, @NonNull String hash) {
        Preconditions.checkKey(privateKey);
        byte[] signature = ED25519.sign(NanoHelper.toByteArray(hash), privateKey);
        return NanoHelper.toHex(signature);
    }

    public static boolean isValid(@NonNull String account, @NonNull String hash, @NonNull String signature) {
        checkHash(hash);
        byte[] publicKey = NanoAccounts.toPublicKey(account);
        return signature.matches(SIGNATURE_REGEX) && isValid(publicKey, NanoHelper.toByteArray(hash), NanoHelper.toByteArray(signature));
    }

    public static boolean isValid(@NonNull byte[] publicKey, @NonNull byte[] hash, @NonNull byte[] signature) {
        return ED25519.verify(signature, hash, publicKey);
    }

    private static void checkHash(String hash) {
        Preconditions.checkArgument(NanoBlocks.isValid(hash), () -> "Invalid hash " + hash);
    }
}
