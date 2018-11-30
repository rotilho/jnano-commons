package com.rotilho.jnano.commons;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NanoBaseAccountType implements NanoAccountType {
    NANO("nano_", "^(xrb_|nano_)[13456789abcdefghijkmnopqrstuwxyz]{60}$"),
    BANANO("ban_", "^(ban_)[13456789abcdefghijkmnopqrstuwxyz]{60}$");

    private final String prefix;
    private final String regex;

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
        if (prefix.equals("nano_")) {
            return account.startsWith("nano_") ? account.substring(5, 57) : account.substring(4, 56);
        }
        return account.substring(4, 56);
    }
}
