package com.rotilho.jnano.commons;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import lombok.NonNull;

import static java.util.function.Function.identity;

public final class NanoWorks {
    private static final Long THRESHOLD = 0xFFFFFFC000000000L;
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private NanoWorks() {
    }

    @NonNull
    public static String perform(@NonNull String hash) {
        byte[] byteArray = NanoHelper.toByteArray(hash);
        return Stream.generate(() -> perform(byteArray))
                .parallel()
                .flatMap(identity())
                .map(NanoHelper::reverse)
                .map(NanoHelper::toHex)
                .map(String::toLowerCase)
                .findAny()
                .get();
    }

    public static boolean isValid(@NonNull String hash, @NonNull String pow) {
        return isValid(NanoHelper.toByteArray(hash), NanoHelper.reverse(NanoHelper.toByteArray(pow)));
    }


    public static boolean isValid(@NonNull byte[] byteArrayHash, @NonNull byte[] byteArrayPOW) {
        byte[] work = Hashes.digest(8, byteArrayPOW, byteArrayHash);
        long uWork = ByteBuffer.wrap(work).order(ByteOrder.LITTLE_ENDIAN).getLong();
        return Long.compareUnsigned(uWork, THRESHOLD) >= 0;
    }

    private static Stream<byte[]> perform(byte[] byteArrayHash) {
        byte[] byteArrayPOW = new byte[8];
        RANDOM.nextBytes(byteArrayPOW);
        for (byte b = -128; b < 127; b++) {
            byteArrayPOW[7] = b;
            if (isValid(byteArrayHash, byteArrayPOW)) {
                return Stream.of(byteArrayPOW);
            }
        }
        return Stream.empty();
    }
}
