package com.rotilho.jnano.commons;

import java.math.BigInteger;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import static com.rotilho.jnano.commons.NanoHelper.leftPad;
import static com.rotilho.jnano.commons.NanoHelper.toHex;

@UtilityClass
public final class NanoBlocks {
    public final static String MAIN_NET_GENESIS = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";

    private static final String HASH_REGEX = "([0-9A-Z]){64}";
    private final static byte[] STATE_BLOCK_PREAMBLE = NanoHelper.toByteArray("0000000000000000000000000000000000000000000000000000000000000006");

    @NonNull
    public static String hashOpenBlock(@NonNull String source, @NonNull String representative, @NonNull String account) {
        return toHex(hashOpenBlock(NanoHelper.toByteArray(source), NanoAccounts.toPublicKey(representative), NanoAccounts.toPublicKey(account)));
    }

    @NonNull
    public static byte[] hashOpenBlock(@NonNull byte[] source, @NonNull byte[] representative, @NonNull byte[] account) {
        return hash(source, representative, account);
    }

    @NonNull
    public static String hashSendBlock(@NonNull String previous, @NonNull String destination, @NonNull BigInteger balance) {
        return toHex(hashSendBlock(NanoHelper.toByteArray(previous), NanoAccounts.toPublicKey(destination), NanoHelper.toByteArray(NanoHelper.radix(balance))));
    }

    @NonNull
    public static byte[] hashSendBlock(@NonNull byte[] previous, @NonNull byte[] destination, @NonNull byte[] balance) {
        return hash(previous, destination, balance);
    }

    @NonNull
    public static String hashReceiveBlock(@NonNull String previous, @NonNull String source) {
        return toHex(hashReceiveBlock(NanoHelper.toByteArray(previous), NanoHelper.toByteArray(source)));
    }

    @NonNull
    public static byte[] hashReceiveBlock(@NonNull byte[] previous, @NonNull byte[] source) {
        return hash(previous, source);
    }

    @NonNull
    public static String hashChangeBlock(@NonNull String previous, @NonNull String representative) {
        return toHex(hashChangeBlock(NanoHelper.toByteArray(previous), NanoAccounts.toPublicKey(representative)));
    }

    @NonNull
    public static byte[] hashChangeBlock(@NonNull byte[] previous, @NonNull byte[] representative) {
        return hash(previous, representative);
    }

    @NonNull
    public static String hashStateBlock(@NonNull String account, @NonNull String previous, @NonNull String representative, @NonNull BigInteger balance, @NonNull String link) {
        return toHex(
                hashStateBlock(
                        NanoAccounts.toPublicKey(account),
                        NanoHelper.toByteArray(leftPad(previous, 64)),
                        NanoAccounts.toPublicKey(representative),
                        NanoHelper.toByteArray(NanoHelper.radix(balance)),
                        link.startsWith("xrb_") || link.startsWith("nano_") ? NanoAccounts.toPublicKey(link) : NanoHelper.toByteArray(link)
                )
        );
    }

    @NonNull
    public static byte[] hashStateBlock(@NonNull byte[] account, @NonNull byte[] previous, @NonNull byte[] representative, @NonNull byte[] balance, @NonNull byte[] link) {
        return hash(
                STATE_BLOCK_PREAMBLE,
                account,
                previous,
                representative,
                balance,
                link
        );
    }

    private static byte[] hash(byte[]... byteArrays) {
        return Hashes.digest256(byteArrays);
    }

    public static boolean isValid(@NonNull String hash) {
        return hash.matches(HASH_REGEX);
    }


}
