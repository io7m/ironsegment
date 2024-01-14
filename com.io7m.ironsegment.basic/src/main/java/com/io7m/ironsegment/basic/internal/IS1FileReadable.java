/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

package com.io7m.ironsegment.basic.internal;


import com.io7m.ironsegment.api.ISFileReadableType;
import com.io7m.ironsegment.api.ISFileSectionDescription;
import com.io7m.ironsegment.api.ISSectionReadableType;
import com.io7m.ironsegment.api.ISVersion;
import com.io7m.ironsegment.parser.api.ISParseRequest;
import com.io7m.jbssio.api.BSSReaderRandomAccessType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.io7m.ironsegment.api.ISIdentifiers.sectionEndIdentifier;


/**
 * The main readable file implementation.
 */

public final class IS1FileReadable implements ISFileReadableType
{
  private final BSSReaderRandomAccessType reader;
  private final ISParseRequest request;
  private final ISVersion version;
  private final List<ISFileSectionDescription> fileSections;
  private final long remainingOctets;

  IS1FileReadable(
    final BSSReaderRandomAccessType inReader,
    final ISParseRequest inRequest,
    final ISVersion inVersion,
    final ArrayList<ISFileSectionDescription> inFileSections,
    final long inRemainingOctets)
  {
    this.reader =
      Objects.requireNonNull(inReader, "reader");
    this.request =
      Objects.requireNonNull(inRequest, "request");
    this.version =
      Objects.requireNonNull(inVersion, "version");
    this.fileSections =
      List.copyOf(
        Objects.requireNonNull(inFileSections, "fileSections"));
    this.remainingOctets = inRemainingOctets;
  }

  @Override
  public List<ISFileSectionDescription> sections()
  {
    this.checkNotClosed();
    return this.fileSections;
  }

  @Override
  public ISVersion version()
  {
    return this.version;
  }

  private void checkNotClosed()
  {
    if (this.reader.isClosed()) {
      throw new IllegalStateException("File is closed.");
    }
  }

  @Override
  public ISSectionReadableType openSection(
    final ISFileSectionDescription description)
  {
    this.checkNotClosed();

    if (!this.fileSections.contains(description)) {
      throw new IllegalArgumentException(
        "File does not contain the provided section.");
    }

    final var identifier =
      description.description().identifier();

    if (identifier == sectionEndIdentifier()) {
      return new IS1SectionReadableEnd(
        this.reader,
        this.request,
        description
      );
    }

    return new IS1SectionReadableOther(this.reader, this.request, description);
  }

  @Override
  public long trailingOctets()
  {
    return this.remainingOctets;
  }

  @Override
  public void close()
    throws IOException
  {
    this.reader.close();
  }
}
