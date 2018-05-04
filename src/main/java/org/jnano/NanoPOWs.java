package org.jnano;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public final class NanoPOWs {
    private NanoPOWs() {
    }

    public static String perform(String hash) {
        Random random = new Random();
        byte[] byteArray = DataUtils.toByteArray(hash);
        return Stream.generate(() -> perform(random, byteArray))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .map(DataUtils::toHex)
                .map(String::toLowerCase)
                .get();
    }

    public static boolean isValid(String hash, String pow) {
        return isValid(DataUtils.toByteArray(hash), DataUtils.reverse(DataUtils.toByteArray(pow)));
    }


    private static boolean isValid(byte[] byteArrayHash, byte[] byteArrayPOW) {
        byte[] work = Hashes.digest(8, byteArrayPOW, byteArrayHash);
        return overThreshold(work);
    }


    private static boolean overThreshold(byte[] work) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(work);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return Long.compareUnsigned(byteBuffer.getLong(), 0xFFFFFFC000000000L) >= 0;
    }

    private static Optional<byte[]> perform(Random random, byte[] byteArrayHash) {
        byte[] byteArrayPOW = new byte[8];
        random.nextBytes(byteArrayPOW);
        for (byte b = -128; b < 127; b++) {
            byteArrayPOW[7] = b;
            if (isValid(byteArrayHash, byteArrayPOW)) {
                return Optional.of(DataUtils.reverse(byteArrayPOW));
            }
        }
        return Optional.empty();
    }
}
