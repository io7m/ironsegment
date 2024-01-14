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

import java.util.Objects;
import java.util.SortedMap;

/**
 * The set of images within a file.
 *
 * @param images The images
 * @param width  The image width
 * @param height The image height
 */

public record ISImages(
  SortedMap<ISImageID, ISImage> images,
  long width,
  long height)
{
  /**
   * The set of images within a file.
   *
   * @param images The images
   * @param width  The image width
   * @param height The image height
   */

  public ISImages
  {
    Objects.requireNonNull(images, "images");

    if (!(width >= 1L && width <= 4294967295L)) {
      throw new IllegalArgumentException(
        "Width %s must be in the range [1, 4294967295]"
          .formatted(Long.toUnsignedString(width))
      );
    }

    if (!(height >= 1L && height <= 4294967295L)) {
      throw new IllegalArgumentException(
        "Width %s must be in the range [1, 4294967295]"
          .formatted(Long.toUnsignedString(height))
      );
    }

    for (final var entry : images.entrySet()) {
      final var k = entry.getKey();
      final var i = entry.getValue().imageId();
      if (!Objects.equals(k, i)) {
        throw new IllegalArgumentException(
          "Image %s should be associated with key %s".formatted(i, k));
      }
    }
  }
}
