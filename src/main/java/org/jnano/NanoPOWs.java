package org.jnano;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

public final class NanoPOWs {
//    private static final Long THRESHOLD = 0xFFFFFFC000000000L;

    private NanoPOWs() {
    }

    @Nonnull
    public static String perform(@Nonnull String hash) {
        Random random = new Random();
        byte[] byteArray = DataUtils.toByteArray(hash);
        return Stream.generate(() -> perform(random, byteArray))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .map(String::toLowerCase)
                .get();
    }

    public static boolean isValid(@Nonnull String hash, @Nonnull String pow) {
        return isValid(DataUtils.toByteArray(hash), DataUtils.reverse(DataUtils.toByteArray(pow)));
    }


    private static boolean isValid(@Nonnull byte[] byteArrayHash, @Nonnull byte[] byteArrayPOW) {
        throw new UnsupportedOperationException("Validation is not implemented yet");
//        byte[] work = Hashes.digest(8, byteArrayPOW, byteArrayHash);
//        long uWork = ByteBuffer.wrap(work).order(ByteOrder.LITTLE_ENDIAN).getLong();
//        return Long.compareUnsigned(uWork, THRESHOLD) >= 0;
    }

    private static Optional<String> perform(Random random, byte[] byteArrayHash) {
        byte[] byteArrayPOW = new byte[8];
        random.nextBytes(byteArrayPOW);
        for (byte b = -128; b < 127; b++) {
            byteArrayPOW[7] = b;
            if (isValid(byteArrayHash, byteArrayPOW)) {
                return Optional.of(DataUtils.toHex(DataUtils.reverse(byteArrayPOW)));
            }
        }
        return Optional.empty();
    }
}
