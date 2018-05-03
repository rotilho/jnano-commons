package org.jnano;

import javax.annotation.Nonnull;
import java.math.BigInteger;

import static org.jnano.DataUtils.radix;
import static org.jnano.DataUtils.toByteArray;

public final class NanoBlocks {
    private NanoBlocks() {
    }


    @Nonnull
    public static String hashOpenBlock(@Nonnull String source, @Nonnull String representative, @Nonnull String account) {
        return hash(toByteArray(source), NanoAccounts.toPublicKey(representative), NanoAccounts.toPublicKey(account));
    }

    @Nonnull
    public static String hashSendBlock(@Nonnull String previous, @Nonnull String destination, @Nonnull BigInteger balance) {
        String radixBalance = radix(balance);
        return hash(toByteArray(previous), NanoAccounts.toPublicKey(destination), toByteArray(radixBalance));
    }

    @Nonnull
    public static String hashReceiveBlock(@Nonnull String previous, @Nonnull String source) {
        return hash(toByteArray(previous), toByteArray(source));
    }

    @Nonnull
    public static String hashChangeBlock(@Nonnull String previous, @Nonnull String representative) {
        return hash(toByteArray(previous), NanoAccounts.toPublicKey(representative));
    }

    @Nonnull
    public static String hashStateBlock(@Nonnull String account, @Nonnull String previous, @Nonnull String representative, @Nonnull BigInteger balance, @Nonnull String link) {
        String radixBalance = radix(balance);
        return hash(
                NanoAccounts.toPublicKey(account),
                toByteArray(previous),
                NanoAccounts.toPublicKey(representative),
                toByteArray(radixBalance),
                link.startsWith("xrb_") ? NanoAccounts.toPublicKey(link) : toByteArray(link)
        );
    }

    private static String hash(byte[]... byteArrays) {
        return DataUtils.toHex(Hashes.digest256(byteArrays));
    }


}
