/*
 * Copyright © 2024 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package com.io7m.ironsegment.tests;

import com.io7m.anethum.api.ParsingException;
import com.io7m.ironsegment.api.ISManifest;
import com.io7m.ironsegment.basic.ISManifestParsers;
import com.io7m.ironsegment.basic.ISManifestSerializers;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ISManifestParserTest
{
  private static final ISManifestParsers PARSERS =
    new ISManifestParsers();
  private static final ISManifestSerializers SERIALIZERS =
    new ISManifestSerializers();

  /**
   * Parsing and serialization are an identity operation.
   *
   * @param m0 The manifest
   *
   * @throws Exception On errors
   */

  @Property
  public void testIdentity(
    final @ForAll ISManifest m0)
    throws Exception
  {
    final ISManifest m1;
    try (var s0 = new ByteArrayOutputStream()) {
      SERIALIZERS.serialize(URI.create("urn:0"), s0, m0);
      m1 = PARSERS.parse(
        URI.create("urn:1"),
        new ByteArrayInputStream(s0.toByteArray()));
    }
    assertEquals(m0, m1);
  }

  @Test
  public void testError0()
  {
    assertThrows(ParsingException.class, () -> {
      PARSERS.parse(URI.create("urn:0"), streamOf("manifest-error0.xml"));
    });
  }

  @Test
  public void testError1()
  {
    assertThrows(ParsingException.class, () -> {
      PARSERS.parse(URI.create("urn:0"), streamOf("manifest-error1.xml"));
    });
  }

  @Test
  public void testError2()
  {
    assertThrows(ParsingException.class, () -> {
      PARSERS.parse(URI.create("urn:0"), streamOf("manifest-error2.xml"));
    });
  }

  @Test
  public void testError3()
  {
    assertThrows(ParsingException.class, () -> {
      PARSERS.parse(URI.create("urn:0"), streamOf("manifest-error3.xml"));
    });
  }

  private static InputStream streamOf(
    final String name)
  {
    return ISManifestParserTest.class
      .getResourceAsStream("/com/io7m/ironsegment/tests/%s".formatted(name));
  }
}
