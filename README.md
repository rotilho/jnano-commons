[![Build Status](https://travis-ci.org/rotilho/jnano-commons.svg?branch=master)](https://travis-ci.org/rotilho/jnano-commons)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9aba7b2a36f54a7689f7ffb798fb708c)](https://www.codacy.com/app/rotilho/jnano-commons?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rotilho/jnano-commons&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/9aba7b2a36f54a7689f7ffb798fb708c)](https://www.codacy.com/app/rotilho/jnano-commons?utm_source=github.com&utm_medium=referral&utm_content=rotilho/jnano-commons&utm_campaign=Badge_Coverage)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.rotilho.jnano/jnano-commons/badge.svg)](https://mvnrepository.com/artifact/com.rotilho.jnano/jnano-commons)

# JNano Commons
JNano provides a set of low level Nano operations that includes signing, seed generation, block hashing and account creation.

## How to use it?

**Gradle**
`compile 'com.rotilho.jnano:jnano-commons:1.3.0`

**Maven**
```xml
<dependency>
    <groupId>com.rotilho.jnano</groupId>
    <artifactId>jnano-commons</artifactId>
    <version>1.3.0</version>
</dependency>
```

All low level operations are handled by `NanoSeeds`, `NanoKeys`, `NanoAccounts`, `NanoBlocks`, `NanoWorks`*, `NanoSignatures` and `NanoMnemonics`.

```java
// create seed
byte[] seed = NanoSeeds.generateSeed();
assertTrue(NanoSeeds.isValid(seed));

// create private key
byte[] privateKey = NanoKeys.createPrivateKey(seed, 0);
byte[] publicKey = NanoKeys.createPublicKey(privateKey);

// create account
String account = NanoAccounts.createAccount(publicKey);
assertTrue(NanoAccounts.isValid(account));

// convert account to publicKey
assertTrue(Arrays.equals(NanoAccounts.toPublicKey(account), publicKey));

// create block hash
String hash = NanoBlocks.hashStateBlock(
        account, // account
        NanoBlocks.MAIN_NET_GENESIS, //previous block
        account, // representative
        BigInteger.ONE, // balance
        NanoAccounts.MAIN_NET_GENESIS_ACCOUNT // link: target address in this case
);
assertTrue(NanoBlocks.isValid(hash));

// sign a block hash
String signature = NanoSignatures.sign(privateKey, hash);
assertTrue(NanoSignatures.isValid(account, hash, signature));
```

\* Java use just CPU and calculate PoW with CPU is bloody slow. For production ready application please use a [work server](https://github.com/nanocurrency/nano-work-server).

## Why not use string for Seed and Keys?
As stated by [Baeldung](https://www.baeldung.com/java-storing-passwords), "strings in Java are immutable which means that we cannot change them using any high-level APIs. Any change on a String object will produce a new String, keeping the old one in memory.
                                                                          
Therefore, the password stored in a String will be available in memory until Garbage Collector clears it. We cannot control when it happens, but this period can be significantly longer than for regular objects since Strings are kept in a String Pool for re-usability purpose".

Most of the time work with byte array will be enough but if there is the need to convert it to String you can easily achieve it using ``NanoHelper.toHex``.

If you want to reduce the time the privets keys or seed stay in memory you can use ```NanoHelper.wipe``` and clear out the byte array content.

#### Special thanks to 
- [Harry](https://github.com/thehen101) and his [Rain](https://github.com/thehen101/Rain) project which were used in the first implementation
- [Scott Lanoue](https://github.com/schott12521) for all help
- [NovaCrypto](https://github.com/NovaCrypto) and it [BIP39](https://github.com/NovaCrypto/BIP39) which were used as inspiration