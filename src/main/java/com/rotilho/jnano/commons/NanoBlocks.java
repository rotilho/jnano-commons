package com.rotilho.jnano.commons;

import java.math.BigInteger;

import javax.annotation.Nonnull;

import static com.rotilho.jnano.commons.NanoHelper.leftPad;

public final class NanoBlocks {
    public final static String MAIN_NET_GENESIS = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";

    private static final String HASH_REGEX = "([0-9A-Z]){64}";
    private final static byte[] STATE_BLOCK_PREAMBLE = NanoHelper.toByteArray("0000000000000000000000000000000000000000000000000000000000000006");

    private NanoBlocks() {
    }


    @Nonnull
    public static String hashOpenBlock(@Nonnull String source, @Nonnull String representative, @Nonnull String account) {
        return hash(NanoHelper.toByteArray(source), NanoAccounts.toPublicKey(representative), NanoAccounts.toPublicKey(account));
    }

    @Nonnull
    public static String hashSendBlock(@Nonnull String previous, @Nonnull String destination, @Nonnull BigInteger balance) {
        return hash(NanoHelper.toByteArray(previous), NanoAccounts.toPublicKey(destination), NanoHelper.toByteArray(NanoHelper.radix(balance)));
    }

    @Nonnull
    public static String hashReceiveBlock(@Nonnull String previous, @Nonnull String source) {
        return hash(NanoHelper.toByteArray(previous), NanoHelper.toByteArray(source));
    }

    @Nonnull
    public static String hashChangeBlock(@Nonnull String previous, @Nonnull String representative) {
        return hash(NanoHelper.toByteArray(previous), NanoAccounts.toPublicKey(representative));
    }

    @Nonnull
    public static String hashStateBlock(@Nonnull String account, @Nonnull String previous, @Nonnull String representative, @Nonnull BigInteger balance, @Nonnull String link) {
        return hash(
                STATE_BLOCK_PREAMBLE,
                NanoAccounts.toPublicKey(account),
                NanoHelper.toByteArray(leftPad(previous, 64)),
                NanoAccounts.toPublicKey(representative),
                NanoHelper.toByteArray(NanoHelper.radix(balance)),
                link.startsWith("xrb_") || link.startsWith("nano_") ? NanoAccounts.toPublicKey(link) : NanoHelper.toByteArray(link)
        );
    }

    private static String hash(byte[]... byteArrays) {
        return NanoHelper.toHex(Hashes.digest256(byteArrays));
    }

    public static boolean isValid(@Nonnull String hash) {
        return hash.matches(HASH_REGEX);
    }


}
