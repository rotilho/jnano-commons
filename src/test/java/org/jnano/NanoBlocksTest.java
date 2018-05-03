package org.jnano;

import org.junit.Test;

import java.math.BigInteger;

import static junit.framework.TestCase.assertEquals;


public class NanoBlocksTest {


    @Test
    public void shouldHashOpenBlock() {
        // given
        String source = "E89208DD038FBB269987689621D52292AE9C35941A7484756ECCED92A65093BA";
        String representative = "xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3";
        String account = "xrb_3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xtoncuohr3";

        //when
        String hash = NanoBlocks.hashOpenBlock(source, representative, account);

        // then
        String expectedHash = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashSendBlock() {
        // given
        String previous = "991CF190094C00F0B68E2E5F75F6BEE95A2E0BD93CEAA4A6734DB9F19B728948";
        String destination = "xrb_13ezf4od79h1tgj9aiu4djzcmmguendtjfuhwfukhuucboua8cpoihmh8byo";
        BigInteger balance = new BigInteger("337010421085160209006996005437231978653");

        // when
        String hash = NanoBlocks.hashSendBlock(previous, destination, balance);

        //then
        String expectedHash = "A170D51B94E00371ACE76E35AC81DC9405D5D04D4CEBC399AEACE07AE05DD293";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashReceiveBlock() {
        // given
        String previous = "67022ABE190D5C1E42F32F14EEBD3E25A36CA26B00BF1D038AE86055CCD105F5";
        String source = "ED9554BD4D0F3BDCC8A7BDED5DEF09C503F4E94EF3698047441EFBBCC0D241F7";

        // when
        String hash = NanoBlocks.hashReceiveBlock(previous, source);

        // then
        String expectedHash = "49876A7B00159C2F6EEB33BEAEE07FC637F4A29DD9631DFFD3DA015DE2165FE6";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashChangeBlock() {
        // given
        String previous = "F958305C0FF0551421D4ABEDCCF302079D020A0A3833E33F185E2B0415D4567A";
        String representative = "xrb_18gmu6engqhgtjnppqam181o5nfhj4sdtgyhy36dan3jr9spt84rzwmktafc";

        // when
        String hash = NanoBlocks.hashChangeBlock(previous, representative);

        // then
        String expectedHash = "654FA425CEBFC9E7726089E4EDE7A105462D93DBC915FFB70B50909920A7D286";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashOpenStateBlock() {
        // given
        String account = "xrb_3igf8hd4sjshoibbbkeitmgkp1o6ug4xads43j6e4gqkj5xk5o83j8ja9php";
        String previous = "0";
        String representative = "xrb_3p1asma84n8k84joneka776q4egm5wwru3suho9wjsfyuem8j95b3c78nw8j";
        BigInteger balance = new BigInteger("1");
        String link = "1EF0AD02257987B48030CC8D38511D3B2511672F33AF115AD09E18A86A8355A8";

        // when
        String hash = NanoBlocks.hashStateBlock(account, previous, representative, balance, link);

        // then
        String expectedHash = "FC5A7FB777110A858052468D448B2DF22B648943C097C0608D1E2341007438B0";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashReceiveStateBlock() {
        // given
        String account = "xrb_3igf8hd4sjshoibbbkeitmgkp1o6ug4xads43j6e4gqkj5xk5o83j8ja9php";
        String previous = "FC5A7FB777110A858052468D448B2DF22B648943C097C0608D1E2341007438B0";
        String representative = "xrb_3p1asma84n8k84joneka776q4egm5wwru3suho9wjsfyuem8j95b3c78nw8j";
        BigInteger balance = new BigInteger("5000000000000000000000000000001");
        String link = "B2EC73C1F503F47E051AD72ECB512C63BA8E1A0ACC2CEE4EA9A22FE1CBDB693F";

        // when
        String hash = NanoBlocks.hashStateBlock(account, previous, representative, balance, link);

        // then
        String expectedHash = "597395E83BD04DF8EF30AF04234EAAFE0606A883CF4AEAD2DB8196AAF5C4444F";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashSendStateBlock() {
        // given
        String account = "xrb_3igf8hd4sjshoibbbkeitmgkp1o6ug4xads43j6e4gqkj5xk5o83j8ja9php";
        String previous = "597395E83BD04DF8EF30AF04234EAAFE0606A883CF4AEAD2DB8196AAF5C4444F";
        String representative = "xrb_3p1asma84n8k84joneka776q4egm5wwru3suho9wjsfyuem8j95b3c78nw8j";
        BigInteger balance = new BigInteger("3000000000000000000000000000001");
        String link = "xrb_1q3hqecaw15cjt7thbtxu3pbzr1eihtzzpzxguoc37bj1wc5ffoh7w74gi6p";

        // when
        String hash = NanoBlocks.hashStateBlock(account, previous, representative, balance, link);

        // then
        String expectedHash = "128106287002E595F479ACD615C818117FCB3860EC112670557A2467386249D4";
        assertEquals(expectedHash, hash);
    }

    @Test
    public void shouldHashChangeStateBlock() {
        // given
        String account = "xrb_3igf8hd4sjshoibbbkeitmgkp1o6ug4xads43j6e4gqkj5xk5o83j8ja9php";
        String previous = "128106287002E595F479ACD615C818117FCB3860EC112670557A2467386249D4";
        String representative = "xrb_1anrzcuwe64rwxzcco8dkhpyxpi8kd7zsjc1oeimpc3ppca4mrjtwnqposrs";
        BigInteger balance = new BigInteger("3000000000000000000000000000001");
        String link = "0000000000000000000000000000000000000000000000000000000000000000";

        // when
        String hash = NanoBlocks.hashStateBlock(account, previous, representative, balance, link);

        // then
        String expectedHash = "2A322FD5ACAF50C057A8CF5200A000CF1193494C79C786B579E0B4A7D10E5A1E";
        assertEquals(expectedHash, hash);
    }


    @Test
    public void shouldHashChangeAndSendStateBlock() {
        // given
        String account = "xrb_3igf8hd4sjshoibbbkeitmgkp1o6ug4xads43j6e4gqkj5xk5o83j8ja9php";
        String previous = "2A322FD5ACAF50C057A8CF5200A000CF1193494C79C786B579E0B4A7D10E5A1E";
        String representative = "xrb_3p1asma84n8k84joneka776q4egm5wwru3suho9wjsfyuem8j95b3c78nw8j";
        BigInteger balance = new BigInteger("1");
        String link = "xrb_1q3hqecaw15cjt7thbtxu3pbzr1eihtzzpzxguoc37bj1wc5ffoh7w74gi6p";

        // when
        String hash = NanoBlocks.hashStateBlock(account, previous, representative, balance, link);

        // then
        String expectedHash = "9664412A834F0C27056C7BC4A363FBAE86DF8EF51341A5A5EA14061727AE519F";
        assertEquals(expectedHash, hash);
    }

}