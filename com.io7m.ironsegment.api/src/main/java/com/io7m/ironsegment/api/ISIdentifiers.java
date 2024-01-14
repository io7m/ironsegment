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

package com.io7m.ironsegment.api;

import java.util.Optional;

/**
 * The identifiers for standard sections.
 */

public final class ISIdentifiers
{
  private static final long FILE_IDENTIFIER =
    0x8949_7253_0D0A_1A0AL;
  private static final long SECTION_MANIFEST_IDENTIFIER =
    0x4972_535F_4D4E_4946L;
  private static final long SECTION_IMAGE_IDENTIFIER =
    0x4972_535F_494D_4744L;
  private static final long SECTION_END_IDENTIFIER =
    0x4972_535F_454E_4421L;

  private ISIdentifiers()
  {

  }

  /**
   * @return The identifier used to identify {@code calino} files
   */

  public static long fileIdentifier()
  {
    return FILE_IDENTIFIER;
  }

  /**
   * @return The identifier used to identify {@code end} sections
   */

  public static long sectionEndIdentifier()
  {
    return SECTION_END_IDENTIFIER;
  }

  /**
   * @return The identifier used to identify {@code manifest} sections
   */

  public static long sectionManifestIdentifier()
  {
    return SECTION_MANIFEST_IDENTIFIER;
  }

  /**
   * @return The identifier used to identify {@code image} sections
   */

  public static long sectionImageIdentifier()
  {
    return SECTION_IMAGE_IDENTIFIER;
  }

  /**
   * Determine a humanly-readable name of an identifier.
   *
   * @param identifier The identifier
   *
   * @return A humanly-readable name, if one is known
   */

  public static Optional<String> nameOf(
    final long identifier)
  {
    if (identifier == SECTION_MANIFEST_IDENTIFIER) {
      return Optional.of("MANIFEST");
    }
    if (identifier == SECTION_IMAGE_IDENTIFIER) {
      return Optional.of("IMAGE");
    }
    if (identifier == SECTION_END_IDENTIFIER) {
      return Optional.of("END");
    }
    return Optional.empty();
  }
}
