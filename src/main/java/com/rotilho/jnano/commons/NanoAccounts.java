package com.rotilho.jnano.commons;

import java.util.function.Function;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.rotilho.jnano.commons.NanoHelper.leftPad;
import static com.rotilho.jnano.commons.NanoHelper.reverse;
import static com.rotilho.jnano.commons.NanoHelper.toBinary;
import static com.rotilho.jnano.commons.NanoHelper.toByteArray;
import static com.rotilho.jnano.commons.NanoHelper.toHex;

public final class NanoAccounts {
    public final static String MAIN_NET_GENESIS_ACCOUNT = "xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3";

    private NanoAccounts() {
    }

    @NonNull
    public static String createAccount(@NonNull byte[] publicKey) {
        Preconditions.checkKey(publicKey);

        String binaryPublicKey = leftPad(toBinary(toHex(publicKey)), 260);
        String encodedChecksum = calculateEncodedChecksum(publicKey);
        String encodedPublicKey = NanoAccountEncodes.encode(binaryPublicKey);
        return "nano_" + encodedPublicKey + encodedChecksum;
    }

    @NonNull
    public static byte[] toPublicKey(@NonNull String account) {
        return toPublicKey(NanoAccounts.BaseNanoAccountType.NANO, account);
    }

    @NonNull
    public static byte[] toPublicKey(@NonNull NanoAccountType type, @NonNull String account) {
        Preconditions.checkArgument(isValid(type, account), () -> "Invalid account " + account);
        return extractPublicKey(type, account);
    }

    public static boolean isValid(@NonNull String account) {
        return isValid(NanoAccounts.BaseNanoAccountType.NANO, account);
    }

    public static boolean isValid(@NonNull NanoAccountType type, @NonNull String account) {
        if (!account.matches(type.regex())) {
            return false;
        }
        String expectedEncodedChecksum = account.substring(account.length() - 8);
        String encodedChecksum = calculateEncodedChecksum(extractPublicKey(type, account));
        return expectedEncodedChecksum.equals(encodedChecksum);
    }

    private static byte[] extractPublicKey(NanoAccountType type, String account) {
        String encodedPublicKey = type.extractEncodedPublicKey(account);
        String binaryPublicKey = NanoAccountEncodes.decode(encodedPublicKey).substring(4);
        String hexPublicKey = leftPad(toHex(binaryPublicKey), 64);
        return toByteArray(hexPublicKey);
    }

    private static String calculateEncodedChecksum(byte[] publicKey) {
        byte[] checksum = reverse(Hashes.digest(5, publicKey));
        String binaryChecksum = leftPad(toBinary(toHex(checksum)), checksum.length * 8);
        return NanoAccountEncodes.encode(binaryChecksum);
    }

    @RequiredArgsConstructor
    public enum BaseNanoAccountType implements NanoAccountType {
        NANO("^(xrb_|nano_)[13456789abcdefghijkmnopqrstuwxyz]{60}$", account -> account.startsWith("nano_") ? account.substring(5, 57) : account.substring(4, 56)),
        BANANO("^(ban_)[13456789abcdefghijkmnopqrstuwxyz]{60}$", account -> account.substring(4, 56));

        private final String regex;
        private final Function<String, String> accountExtractor;

        @Override
        public String extractEncodedPublicKey(String account) {
            return accountExtractor.apply(account);
        }

        @Override
        public String regex() {
            return regex;
        }
    }

    public interface NanoAccountType {
        String extractEncodedPublicKey(String account);

        String regex();
    }

}
