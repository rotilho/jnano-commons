package com.rotilho.jnano.commons;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NanoBaseAccountType implements NanoAccountType {
    NANO("nano_", "^(xrb_|nano_)[13456789abcdefghijkmnopqrstuwxyz]{60}$", account -> account.startsWith("nano_") ? account.substring(5, 57) : account.substring(4, 56)),
    BANANO("ban_", "^(ban_)[13456789abcdefghijkmnopqrstuwxyz]{60}$", account -> account.substring(4, 56));

    private final String prefix;
    private final String regex;
    private final Function<String, String> accountExtractor;

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public String regex() {
        return regex;
    }

    @Override
    public String extractEncodedPublicKey(String account) {
        return accountExtractor.apply(account);
    }
}
