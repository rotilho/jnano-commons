package org.jnano;

import javax.annotation.Nonnull;
import java.math.BigInteger;

import static org.jnano.Hexes.toByteArray;
import static org.jnano.Hexes.toHex;

public final class NanoBlocks {


    private NanoBlocks() {
    }


    @Nonnull
    public static String hashOpenBlock(@Nonnull String source, @Nonnull String representative, @Nonnull String account) {
        return hash(toByteArray(source), NanoAccounts.toPublicKey(representative), NanoAccounts.toPublicKey(account));
    }

    @Nonnull
    public static String hashSendBlock(@Nonnull String previous, @Nonnull String destination, @Nonnull BigInteger balance) {
        String hexBalance = toHex(balance);
        return hash(toByteArray(previous), NanoAccounts.toPublicKey(destination), toByteArray(hexBalance));
    }

    @Nonnull
    public static String hashReceiveBlock(@Nonnull String previous, @Nonnull String source) {
        return hash(toByteArray(previous), toByteArray(source));
    }

    @Nonnull
    public static String hashChangeBlock(@Nonnull String previous, @Nonnull String representative) {
        return hash(toByteArray(previous), NanoAccounts.toPublicKey(representative));
    }

//    @Nonnull
//    public static String hashStateBlock(@Nonnull String account, @Nonnull String previous, @Nonnull String representative, @Nonnull BigInteger balance, @Nonnull String link) {
//        String hexBalance = toHex(balance);
//        return hash(
//                NanoAccounts.toPublicKey(account),
//                toByteArray(previous),
//                NanoAccounts.toPublicKey(representative),
//                toByteArray(hexBalance),
//                link.startsWith("xrb_") ? NanoAccounts.toPublicKey(link) : toByteArray(link)
//        );
//    }

    private static String hash(byte[]... byteArrays) {
        return Hexes.toHex(Hashes.digest256(byteArrays));
    }

}
