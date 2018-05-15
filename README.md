[![Build Status](https://travis-ci.org/rotilho/jnano-commons.svg?branch=master)](https://travis-ci.org/rotilho/jnano-commons)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9aba7b2a36f54a7689f7ffb798fb708c)](https://www.codacy.com/app/rotilho/jnano-commons?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rotilho/jnano-commons&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/9aba7b2a36f54a7689f7ffb798fb708c)](https://www.codacy.com/app/rotilho/jnano-commons?utm_source=github.com&utm_medium=referral&utm_content=rotilho/jnano-commons&utm_campaign=Badge_Coverage)

# JNano Commons
JNano provides a set of low level Nano operations that includes signing, seed generation, block hashing and account creation.

**IMPORTANT!** `NanoPOWs` is not implemented yet.

## How to use it?

`compile 'com.rotilho.jnano:jnano-commons:1.0.1`

All low level operations are handled by `NanoSeeds`, `NanoKeys`, `NanoAccounts`, `NanoBlocks`, `NanoPOWs`, and `NanoSignatures`.

```java
// create seed
String seed = NanoSeeds.generateSeed();
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


#### Special thanks to 
- [Harry](https://github.com/thehen101) and his [Rain](https://github.com/thehen101/Rain) project which were used in the first implementation
- [Scott Lanoue](https://github.com/schott12521) for all help