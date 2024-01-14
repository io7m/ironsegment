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

/**
 * An object ID
 *
 * @param value The ID value
 */

public record ISObjectID(long value)
  implements Comparable<ISObjectID>
{
  /**
   * An object ID
   *
   * @param value The ID value
   */

  public ISObjectID
  {
    if (!(value >= 1L && value <= 4294967295L)) {
      throw new IllegalArgumentException(
        "Object ID %s must be in the range [1, 4294967295]"
          .formatted(Long.toUnsignedString(value))
      );
    }
  }

  @Override
  public String toString()
  {
    return Long.toUnsignedString(this.value);
  }

  @Override
  public int compareTo(
    final ISObjectID other)
  {
    return Long.compareUnsigned(this.value, other.value);
  }
}
