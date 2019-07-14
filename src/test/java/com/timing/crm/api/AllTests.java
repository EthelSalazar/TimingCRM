package com.timing.crm.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.timing.crm.api.Helper.ValidatorHelper.keyNormalization;
import static com.timing.crm.api.Utils.Constants.ABORDAJE;
import static com.timing.crm.api.Utils.Constants.REFERIDO;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;

@RunWith(JUnit4.class)
public class AllTests {

    @Test
    public void keyNormalizeTest() {
        String expectedReferido = REFERIDO;
        String expectedAbordaje = ABORDAJE;

        String goodReferido = "Referido";
        String badReferido = "referidos";

        String goodAbordaje = "Abordaje";
        String badAbordaje = "abordajes";

        assertEquals(expectedReferido,keyNormalization(goodReferido));
        assertNotSame(expectedReferido, keyNormalization(badReferido));

        assertEquals(expectedAbordaje,keyNormalization(goodAbordaje));
        assertNotSame(expectedAbordaje, keyNormalization(badAbordaje));

    }


}
