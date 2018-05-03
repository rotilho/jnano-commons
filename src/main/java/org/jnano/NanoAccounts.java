package org.jnano;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static org.jnano.DataUtils.*;
import static org.jnano.Preconditions.checkArgument;

public class NanoAccounts {
    public static final String ADDRESS_REGEX = "^(xrb_)[13456789abcdefghijkmnopqrstuwxyz]{60}$";

    /**
     * Deterministically create address from a seed in a given index
     *
     * @param seed
     * @param index
     * @return Nano address (xrb_1111111111111111111111111111111111111111111111111111hifc8npp)
     */
    @Nonnull
    public static String createAddress(@Nonnull String seed, int index) {
        checkArgument(NanoSeeds.isValid(seed), () -> "Invalid seed " + seed);
        checkArgument(index >= 0, () -> "Invalid index " + index);

        byte[] privateKey = Keys.generatePrivateKey(seed, index);
        byte[] publicKey = Keys.generatePublicKey(privateKey);
        return toAddress(publicKey);
    }

    /**
     * Extract public key from a Address
     *
     * @param address
     * @return public key
     */
    @Nonnull
    public static byte[] toPublicKey(@Nonnull String address) {
        checkArgument(isValid(address), () -> "Invalid address " + address);

        String encodedPublicKey = address.substring(4, 56);
        String encodedChecksum = address.substring(56);

        String binaryPublicKey = AddressEncodes.decode(encodedPublicKey).substring(4);

        String hexPublicKey = StringUtils.leftPad(DataUtils.toHex(binaryPublicKey), 64);

        byte[] publicKey = toByteArray(hexPublicKey);

        checkEncodedChecksum(encodedChecksum, publicKey);

        return publicKey;
    }

    /**
     * Create address to a given public key
     *
     * @param publicKey
     * @return address
     */
    @Nonnull
    public static String toAddress(@Nonnull byte[] publicKey) {
        checkArgument(publicKey.length == 32, () -> "Invalid public key" + Arrays.toString(publicKey));

        String binaryPublicKey = StringUtils.leftPad(toBinary(toHex(publicKey)), 260); //we get the address by picking

        String encodedChecksum = calculateEncodedChecksum(publicKey);
        String encodedPublicKey = AddressEncodes.encode(binaryPublicKey);

        //return the address prefixed with xrb_ and suffixed with
        return "xrb_" + encodedPublicKey + encodedChecksum;
    }

    public static boolean isValid(String address) {
        return address.matches(ADDRESS_REGEX);
    }

    private static void checkEncodedChecksum(String expectedEncodedChecksum, byte[] publicKey) {
        String encodedChecksum = calculateEncodedChecksum(publicKey);
        checkArgument(expectedEncodedChecksum.equals(encodedChecksum), () -> "Invalid checksum " + expectedEncodedChecksum);
    }

    private static String calculateEncodedChecksum(byte[] publicKey) {
        byte[] checksum = reverse(Hashes.digest(5, publicKey));
        String binaryChecksum = StringUtils.leftPad(toBinary(toHex(checksum)), checksum.length * 8);
        return AddressEncodes.encode(binaryChecksum);
    }

}
