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

//    @Test
//    public void shouldHashStateBlock() {
//        // given
//        String account = "xrb_1eebu11bdt3j55jbuy8tmz1seu6tz1rdocg4isur3a15nsdpjck6w31hagjs";
//        String previous = "BE20FF44AF3F271E5BEDEA37AC103B651179E7795072BA4EA6590E7420668DC7";
//        String representative = "xrb_1i9ugg14c5sph67z4st9xk8xatz59xntofqpbagaihctg6ngog1f45mwoa54";
//        BigInteger balance = new BigInteger("1240000000000000000000000000000");
//        String link = "AAAB4AEC6CE72307A0264EA1AC252FC2FECB6A91836DA4D5C0696E7BD6D6E657";
//
//        // when
//        String hash = NanoBlocks.hashStateBlock(account, previous, representative, balance, link);
//
//        // then
//        String expectedHash = "87DB846B0CF844D605EAE06B79D3F9C0C4BEF8DC685CF8916C6A38E1E8CC9896";
//        assertEquals(expectedHash, hash);
//    }

}