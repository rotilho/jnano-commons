package com.rotilho.jnano.commons;

import lombok.NonNull;

public final class NanoSignatures {
    private static final String SIGNATURE_REGEX = "([0-9A-Z]){128}";

    private NanoSignatures() {
    }

    @NonNull
    public static String sign(@NonNull byte[] privateKey, @NonNull String hash) {
        Preconditions.checkKey(privateKey);
        return NanoHelper.toHex(sign(privateKey, NanoHelper.toByteArray(hash)));
    }

    @NonNull
    public static byte[] sign(@NonNull byte[] privateKey, @NonNull byte[] hash) {
        Preconditions.checkKey(privateKey);
        return ED25519.sign(hash, privateKey);
    }

    public static boolean isValid(@NonNull String account, @NonNull String hash, @NonNull String signature) {
        return isValid(NanoBaseAccountType.NANO, account, hash, signature);
    }

    public static boolean isValid(@NonNull NanoAccountType type, @NonNull String account, @NonNull String hash, @NonNull String signature) {
        checkHash(hash);
        byte[] publicKey = NanoAccounts.toPublicKey(type, account);
        return signature.matches(SIGNATURE_REGEX) && isValid(publicKey, NanoHelper.toByteArray(hash), NanoHelper.toByteArray(signature));
    }

    // hash should moved to last parameter and receive a varargs. Maybe rename it to something else?
    public static boolean isValid(@NonNull byte[] publicKey, @NonNull byte[] hash, @NonNull byte[] signature) {
        return ED25519.verify(signature, hash.length == 32 ? hash : Hashes.digest256(hash), publicKey);
    }

    private static void checkHash(String hash) {
        Preconditions.checkArgument(NanoBlocks.isValid(hash), "Invalid hash " + hash);
    }
}
