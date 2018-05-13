package com.rotilho.jnano.commons;

import javax.annotation.Nonnull;

import static com.rotilho.jnano.commons.DataUtils.*;

public final class NanoAccounts {
    public final static String MAIN_NET_GENESIS_ACCOUNT = "xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3";

    private static final String ACCOUNT_REGEX = "^(xrb_|nano_)[13456789abcdefghijkmnopqrstuwxyz]{60}$";

    private NanoAccounts() {
    }

    @Nonnull
    public static String createAccount(@Nonnull byte[] publicKey) {
        Preconditions.checkKey(publicKey);

        String binaryPublicKey = StringUtils.leftPad(toBinary(toHex(publicKey)), 260);
        String encodedChecksum = calculateEncodedChecksum(publicKey);
        String encodedPublicKey = AccountEncodes.encode(binaryPublicKey);
        return "nano_" + encodedPublicKey + encodedChecksum;
    }

    /**
     * Extract public key from a account
     *
     * @return public key
     */
    @Nonnull
    public static byte[] toPublicKey(@Nonnull String account) {
        Preconditions.checkArgument(isValid(account), () -> "Invalid account " + account);
        return extractPublicKey(account);
    }

    public static boolean isValid(@Nonnull String account) {
        if (!account.matches(ACCOUNT_REGEX)) {
            return false;
        }
        String expectedEncodedChecksum = account.substring(account.length() - 8);
        String encodedChecksum = calculateEncodedChecksum(extractPublicKey(account));
        return expectedEncodedChecksum.equals(encodedChecksum);
    }

    private static byte[] extractPublicKey(String account) {
        String encodedPublicKey = account.startsWith("nano_") ? account.substring(5, 57) : account.substring(4, 56);
        String binaryPublicKey = AccountEncodes.decode(encodedPublicKey).substring(4);
        String hexPublicKey = StringUtils.leftPad(toHex(binaryPublicKey), 64);
        return toByteArray(hexPublicKey);
    }

    private static String calculateEncodedChecksum(byte[] publicKey) {
        byte[] checksum = reverse(Hashes.digest(5, publicKey));
        String binaryChecksum = StringUtils.leftPad(toBinary(toHex(checksum)), checksum.length * 8);
        return AccountEncodes.encode(binaryChecksum);
    }

}
