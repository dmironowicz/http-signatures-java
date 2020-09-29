/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tomitribe.auth.signatures;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EcTest extends Assert {

    private final Provider SUN_EC_PROVIDER = Security.getProvider("SunEC");

    private final String privateKeyPem = "-----BEGIN EC PRIVATE KEY-----\n" +
            "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAwMH6qcFB3MyllyHKe\n" +
            "4mqAFWS2gbD4XWzKtCnSmj2b1A==\n" +
            "-----END EC PRIVATE KEY-----\n";

    private final String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE/Z8eXF+0+qeCmXxNfF1jV1H9fui6\n" +
            "8wcd+wkoCIbghCPGVzf+EIYnvWh+UPWC7G0O6yft6S5v0WnXUhxY8r0sXg==\n" +
            "-----END PUBLIC KEY-----\n";

    private final String badSignature = "" +
            "MEUCIQD+HOBW6FrOPBZlh8LecJ5CYmJoSWu0b0w2N+dQovIFtgIgL0YPPp/Au0eZ" +
            "2utjs9QZ2nkl8toSLWVJfE6w+VXm/hA=";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final String method = "POST";
    private final String uri = "/foo?param=value&pet=dog";
    private final Map<String, String> headers = new HashMap<String, String>();

    {
        headers.put("Host", "example.org");
        headers.put("Date", "Thu, 05 Jan 2012 21:31:40 GMT");
        headers.put("Content-Type", "application/json");
        headers.put("Digest", "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=");
        headers.put("Accept", "*/*");
        headers.put("Content-Length", "18");
    }


    public EcTest() throws Exception {
        privateKey = PEM.readPrivateKey(new ByteArrayInputStream(privateKeyPem.getBytes()));
        publicKey = PEM.readPublicKey(new ByteArrayInputStream(publicKeyPem.getBytes()));
    }

    @Test
    public void ecdsaSha1() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA1;

        assertSignature(algorithm, "MEUCICRsOW+Ej5aesqMvfF4V9r7C9" +
                        "OzTWX5ZNybszCZozb3DAiEAkKEhBvuwiAunI5hA/TlDrVMk8" +
                        "ujrGN83VWDnqXSmGH8=",
                "date");

        assertSignature(algorithm, "MEQCIDsx+SKkBXkgd4p0Z5RNXi7C6" +
                        "irHRmJIgRQe67zwmLNLAiB0HVrjaqaxWjFNyIUWkg3FlKa08" +
                        "q0tvNwJQzVWDmoakg==",
                "(request-target)", "host", "date");
    }


    @Test
    public void ecdsaSha256() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA256;

        assertSignature(algorithm, "MEYCIQCM1tbbIk0looNj8v3+8N6H0" +
                        "z1v5glKM5PuF4VDOZEnUgIhAOrhZzpfcWwpFFGaexRPpDW4r" +
                        "zvEAgFpKFeSo+W09CUt",
                "date");

        assertSignature(algorithm, "MEUCIAbJDay2s2c22GN3B39xqtsTJ" +
                        "9yXKIOVriDivegH3UFaAiEAw5cJdN5h6znVw8kH7CD93mOQM" +
                        "L63p3baeAr/T4BbYXQ=",
                "(request-target)", "host", "date");
    }


    @Test
    public void ecdsaSha384() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA384;

        assertSignature(algorithm, "MEUCIQDAZTnleHTG+Ev2qzrmRauZr" +
                        "irDJK9gIYImMzMtnicFZQIgXh80xV8JYkNmnH6SfxgGcnbxp" +
                        "FwHGREE2eTONtWuTvQ="
                , "date");

        assertSignature(algorithm, "MEUCIQCMm5SnD+oZRlTzs3PoTEIQd" +
                        "gcZ0qHtG7iftbHJE2S0TgIgf0RVZvOUfAxEJI0WEcUZMzi0N" +
                        "n2h6PCxxI1sQ+29lpI="
                , "(request-target)", "host", "date");
    }

    @Test
    public void ecdsaSha512() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA512;

        assertSignature(algorithm, "MEQCIG53Je1U1Tk7jeTJN9BVpbTwK" +
                        "yDk3FyJsMH6MUQAzws2AiAN9BQVnb7sjTxOZKZVf+rb3TepU" +
                        "dEhZtyGsJMf4WGcWQ=="
                , "date");

        assertSignature(algorithm, "MEQCIGtGuZGma1YR0hc1rCHnNCb1v" +
                        "IpuN89r25wWqgPTV3v5AiBsQPeh8dUCirt/pCShTkbKVqo6l" +
                        "S1AweWe4EMi3x7Ung=="
                , "(request-target)", "host", "date");
    }

    @Test
    public void ecdsaSha256_P1363() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA256_P1363;
        final String signature = generateSignature(algorithm, "date");
        verifySignature(algorithm, signature, true, "date");
    }


    @Test
    public void ecdsaSha384_P1363() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA384_P1363;
        final String signature = generateSignature(algorithm, "date");
        verifySignature(algorithm, signature, true, "date");
    }

    @Test
    public void ecdsaSha512_P1363() throws Exception {
        final Algorithm algorithm = Algorithm.ECDSA_SHA512_P1363;
        final String signature = generateSignature(algorithm, "date");
        verifySignature(algorithm, signature, true, "date");
    }

    private void assertSignature(final Algorithm algorithm, final String expected, final String... sign) throws Exception {
        final String generated = generateSignature(algorithm, sign);

        // We can't directly compare signature strings because EC signatures
        // use nonces, but we can verify that the one we generate and the
        // hard-coded one in the test are both valid.

        verifySignature(algorithm, expected, true, sign);
        verifySignature(algorithm, generated, true, sign);
        verifySignature(algorithm, badSignature, false, sign);
    }

    private String generateSignature(final Algorithm algorithm, final String... sign) throws Exception {

        final Signer signer = new Signer(privateKey,
                new Signature("some-key-1", SigningAlgorithm.HS2019, algorithm, null, null, Arrays.asList(sign)), SUN_EC_PROVIDER);

        final Signature signed = signer.sign(method, uri, headers);

        return signed.getSignature();
    }

    private void verifySignature(final Algorithm algorithm, final String signature, final boolean expected, final String... sign) throws Exception {

        final Verifier verifier = new Verifier(publicKey,
                new Signature("some-key-1", SigningAlgorithm.HS2019, algorithm, null, signature, Arrays.asList(sign)), SUN_EC_PROVIDER);

        assertEquals(expected, verifier.verify(method, uri, headers));
    }

}
