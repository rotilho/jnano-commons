package com.rotilho.jnano.commons;

import javax.annotation.Nonnull;
import java.math.BigInteger;

public final class NanoBlocks {
    public final static String MAIN_NET_GENESIS = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";

    private static final String HASH_REGEX = "([0-9A-Z]){64}";
    private final static byte[] STATE_BLOCK_PREAMBLE = DataUtils.toByteArray("0000000000000000000000000000000000000000000000000000000000000006");

    private NanoBlocks() {
    }


    @Nonnull
    public static String hashOpenBlock(@Nonnull String source, @Nonnull String representative, @Nonnull String account) {
        return hash(DataUtils.toByteArray(source), NanoAccounts.toPublicKey(representative), NanoAccounts.toPublicKey(account));
    }

    @Nonnull
    public static String hashSendBlock(@Nonnull String previous, @Nonnull String destination, @Nonnull BigInteger balance) {
        return hash(DataUtils.toByteArray(previous), NanoAccounts.toPublicKey(destination), DataUtils.toByteArray(DataUtils.radix(balance)));
    }

    @Nonnull
    public static String hashReceiveBlock(@Nonnull String previous, @Nonnull String source) {
        return hash(DataUtils.toByteArray(previous), DataUtils.toByteArray(source));
    }

    @Nonnull
    public static String hashChangeBlock(@Nonnull String previous, @Nonnull String representative) {
        return hash(DataUtils.toByteArray(previous), NanoAccounts.toPublicKey(representative));
    }

    @Nonnull
    public static String hashStateBlock(@Nonnull String account, @Nonnull String previous, @Nonnull String representative, @Nonnull BigInteger balance, @Nonnull String link) {
        return hash(
                STATE_BLOCK_PREAMBLE,
                NanoAccounts.toPublicKey(account),
                DataUtils.toByteArray(StringUtils.leftPad(previous, 64)),
                NanoAccounts.toPublicKey(representative),
                DataUtils.toByteArray(DataUtils.radix(balance)),
                link.startsWith("xrb_") ? NanoAccounts.toPublicKey(link) : DataUtils.toByteArray(link)
        );
    }

    private static String hash(byte[]... byteArrays) {
        return DataUtils.toHex(Hashes.digest256(byteArrays));
    }

    public static boolean isValid(@Nonnull String hash) {
        return hash.matches(HASH_REGEX);
    }


}
