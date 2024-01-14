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


package com.io7m.ironsegment.basic;

import com.io7m.ironsegment.basic.internal.IS1Writer;
import com.io7m.ironsegment.writer.api.ISWriteRequest;
import com.io7m.ironsegment.writer.api.ISWriterFactoryType;
import com.io7m.ironsegment.writer.api.ISWriterType;
import com.io7m.jbssio.api.BSSWriterProviderType;

import java.io.IOException;
import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * A writer factory supporting major version 1.
 */

public final class IS1Writers implements ISWriterFactoryType
{
  private final BSSWriterProviderType writers;

  /**
   * A writer factory supporting major version 1.
   */

  public IS1Writers()
  {
    this(loadWritersFromServiceLoader());
  }

  /**
   * A writer factory supporting major version 1.
   *
   * @param inWriters A writer provider
   */

  public IS1Writers(
    final BSSWriterProviderType inWriters)
  {
    this.writers = Objects.requireNonNull(inWriters, "readers");
  }

  private static BSSWriterProviderType loadWritersFromServiceLoader()
  {
    return ServiceLoader.load(BSSWriterProviderType.class)
      .findFirst()
      .orElseThrow(() -> new ServiceConfigurationError(
        "No services available of type %s".formatted(BSSWriterProviderType.class))
      );
  }

  @Override
  public int supportedMajorVersion()
  {
    return 1;
  }

  @Override
  public int highestMinorVersion()
  {
    return 0;
  }

  @Override
  public ISWriterType createWriter(
    final ISWriteRequest request)
    throws IOException
  {
    final var rootWriter =
      this.writers.createWriterFromChannel(
        request.target(),
        request.channel(),
        "root"
      );

    return new IS1Writer(this.writers, request, rootWriter);
  }
}
