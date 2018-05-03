package org.jnano;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Security;

import static org.junit.Assert.assertTrue;

public class NanoSeedsTest {
    private String strongAlgorithms;

    @Before
    public void setUp() {
        strongAlgorithms = Security.getProperty("securerandom.strongAlgorithms");
    }

    @After
    public void tearDown() {
        Security.setProperty("securerandom.strongAlgorithms", strongAlgorithms);
    }


    @Test
    public void shouldGenerateSeedWhenAlgorithmIsAvailable() {
        assertTrue(NanoSeeds.isValid(NanoSeeds.generateSeed()));
    }

    @Test(expected = NanoSeeds.ActionNotSupportedException.class)
    public void shouldNotGenerateSeedWhenAlogirithmIsNotAvailable() {
        Security.setProperty("securerandom.strongAlgorithms", "");

        NanoSeeds.generateSeed();
    }
}