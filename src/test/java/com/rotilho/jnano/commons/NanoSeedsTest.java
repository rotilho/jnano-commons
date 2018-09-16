package com.rotilho.jnano.commons;

import com.rotilho.jnano.commons.exception.ActionNotSupportedException;

import org.junit.After;
import org.junit.Test;

import java.security.Security;

import static org.junit.Assert.assertTrue;

public class NanoSeedsTest {
    private final static String strongAlgorithms = Security.getProperty("securerandom.strongAlgorithms");

    @After
    public void tearDown() {
        Security.setProperty("securerandom.strongAlgorithms", strongAlgorithms);
    }


    @Test
    public void shouldGenerateSeedWhenAlgorithmIsAvailable() {
        assertTrue(NanoSeeds.isValid(NanoSeeds.generateSeed()));
    }

    @Test(expected = ActionNotSupportedException.class)
    public void shouldNotGenerateSeedWhenAlgorithmIsNotAvailable() {
        Security.setProperty("securerandom.strongAlgorithms", "");

        NanoSeeds.generateSeed();
    }
}