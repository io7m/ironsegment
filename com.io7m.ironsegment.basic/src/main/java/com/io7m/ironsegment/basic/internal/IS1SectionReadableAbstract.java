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


import com.io7m.ironsegment.api.ISFileSectionDescription;
import com.io7m.ironsegment.api.ISSectionDescription;
import com.io7m.ironsegment.api.ISSectionReadableType;
import com.io7m.ironsegment.parser.api.ISParseRequest;
import com.io7m.jbssio.api.BSSReaderRandomAccessType;
import com.io7m.wendover.core.ReadOnlySeekableByteChannel;
import com.io7m.wendover.core.SubrangeSeekableByteChannel;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Objects;

/**
 * An abstract readable section.
 */

public abstract class IS1SectionReadableAbstract
  implements ISSectionReadableType
{
  private final BSSReaderRandomAccessType reader;
  private final ISParseRequest request;
  private final ISFileSectionDescription description;

  protected IS1SectionReadableAbstract(
    final BSSReaderRandomAccessType inReader,
    final ISParseRequest inRequest,
    final ISFileSectionDescription inDescription)
  {
    this.reader =
      Objects.requireNonNull(inReader, "reader");
    this.request =
      Objects.requireNonNull(inRequest, "request");
    this.description =
      Objects.requireNonNull(inDescription, "description");
  }

  protected final ISParseRequest request()
  {
    return this.request;
  }

  @Override
  public final ISFileSectionDescription fileSectionDescription()
  {
    return this.description;
  }

  protected final BSSReaderRandomAccessType reader()
  {
    return this.reader;
  }

  @Override
  public final ISSectionDescription description()
  {
    return this.description.description();
  }

  @Override
  public final SeekableByteChannel sectionDataChannel()
    throws IOException
  {
    final var baseReadable =
      new ReadOnlySeekableByteChannel(this.request.channel());

    final var channel =
      new SubrangeSeekableByteChannel(
        baseReadable,
        this.description.fileOffsetData(),
        this.description.description().size()
      );

    channel.position(0L);
    return channel;
  }

  @Override
  public final void close()
  {

  }

  protected final String errorLimitExceeded(
    final long length,
    final long limit,
    final String limitName)
  {
    final var lineSeparator = System.lineSeparator();
    final var text = new StringBuilder(128);
    text.append("Limit exceeded.");
    text.append(lineSeparator);
    text.append("  At file offset 0x");
    text.append(Long.toUnsignedString(
      this.reader().offsetCurrentAbsolute(),
      16));
    text.append(" we encountered data with a size specified as ");
    text.append(Long.toUnsignedString(length));
    text.append(" octets.");
    text.append(lineSeparator);
    text.append("  The ");
    text.append(limitName);
    text.append(" is configured as ");
    text.append(Long.toUnsignedString(limit));
    text.append(" octets.");
    text.append(lineSeparator);
    return text.toString();
  }
}
