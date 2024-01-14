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
import java.io.IOException;
import java.util.List;

import static com.io7m.ironsegment.api.ISIdentifiers.sectionEndIdentifier;
import static com.io7m.ironsegment.api.ISIdentifiers.sectionImageIdentifier;
import static com.io7m.ironsegment.api.ISIdentifiers.sectionManifestIdentifier;

/**
 * A writable file.
 */

public interface ISFileWritableType extends Closeable
{
  /**
   * @return The sections currently declared within the file
   *
   * @throws IOException On errors
   */

  List<ISFileSectionDescription> sections()
    throws IOException;

  /**
   * @return The file version
   */

  ISVersion version();

  /**
   * Create a new section with the given identifier.
   *
   * @param identifier The identifier
   *
   * @return A new section
   *
   * @throws IOException On errors
   */

  ISSectionWritableType createSection(long identifier)
    throws IOException;

  /**
   * Create a new end section.
   *
   * @return A new section
   *
   * @throws IOException On errors
   */

  default ISSectionWritableEndType createSectionEnd()
    throws IOException
  {
    return (ISSectionWritableEndType)
      this.createSection(sectionEndIdentifier());
  }

  /**
   * Create a new info section.
   *
   * @return A new section
   *
   * @throws IOException On errors
   */

  default ISSectionWritableInfoType createSectionInfo()
    throws IOException
  {
    return (ISSectionWritableInfoType)
      this.createSection(sectionManifestIdentifier());
  }

  /**
   * Create a new image section.
   *
   * @return A new section
   *
   * @throws IOException On errors
   */

  default ISSectionWritableImageType createSectionImage()
    throws IOException
  {
    return (ISSectionWritableImageType)
      this.createSection(sectionImageIdentifier());
  }
}
