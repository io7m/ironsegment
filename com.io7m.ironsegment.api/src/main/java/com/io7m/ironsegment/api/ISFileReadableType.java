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

import java.io.Closeable;
import java.util.List;

/**
 * A readable file.
 */

public interface ISFileReadableType extends Closeable
{
  /**
   * @return The list of sections in the file
   */

  List<ISFileSectionDescription> sections();

  /**
   * @return The file version
   */

  ISVersion version();

  /**
   * Open a section for reading.
   *
   * @param description The section description
   *
   * @return A readable section
   */

  ISSectionReadableType openSection(
    ISFileSectionDescription description);

  /**
   * Obtain the number of trailing octets in the file. This value should always
   * be zero for valid files.
   *
   * @return The number of trailing octets
   */

  long trailingOctets();
}
