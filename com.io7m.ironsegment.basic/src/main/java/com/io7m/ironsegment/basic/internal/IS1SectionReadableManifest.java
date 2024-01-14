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


import com.io7m.anethum.api.ParsingException;
import com.io7m.ironsegment.api.ISFileSectionDescription;
import com.io7m.ironsegment.api.ISManifest;
import com.io7m.ironsegment.api.ISSectionReadableManifestType;
import com.io7m.ironsegment.basic.ISManifestParsers;
import com.io7m.ironsegment.parser.api.ISParseRequest;
import com.io7m.jbssio.api.BSSReaderRandomAccessType;
import com.io7m.wendover.core.CloseShieldSeekableByteChannel;
import com.io7m.wendover.core.ReadOnlySeekableByteChannel;
import com.io7m.wendover.core.SubrangeSeekableByteChannel;

import java.io.IOException;
import java.nio.channels.Channels;

/**
 * A readable manifest section.
 */

public final class IS1SectionReadableManifest
  extends IS1SectionReadableAbstract implements ISSectionReadableManifestType
{
  private static final ISManifestParsers PARSERS =
    new ISManifestParsers();

  /**
   * A readable manifest section.
   *
   * @param inDescription The description
   * @param inReader      The reader
   * @param inRequest     The request
   */

  public IS1SectionReadableManifest(
    final BSSReaderRandomAccessType inReader,
    final ISParseRequest inRequest,
    final ISFileSectionDescription inDescription)
  {
    super(inReader, inRequest, inDescription);
  }

  @Override
  public ISManifest manifest()
    throws IOException
  {
    final var reader =
      this.reader();
    final var fileOffset =
      this.fileSectionDescription().fileOffset();
    final var sectionSize =
      this.description().size();

    reader.seekTo(fileOffset);
    reader.skip(16L);

    try (var subReader =
           reader.createSubReaderAtBounded(
             "manifest", 0L, sectionSize)) {

      final var dataLength =
        subReader.readU32BE();

      final var baseChannel =
        this.request().channel();

      final var dataOffset = this.determineFileSectionDataOffset();
      baseChannel.position(dataOffset);

      final var closeShield =
        new CloseShieldSeekableByteChannel(baseChannel);
      final var readOnlyChannel =
        new ReadOnlySeekableByteChannel(closeShield);
      final var boundedChannel =
        new SubrangeSeekableByteChannel(
          readOnlyChannel,
          dataOffset,
          dataLength
        );

      final var textStream =
        Channels.newInputStream(boundedChannel);

      try {
        return PARSERS.parse(this.request().source(), textStream);
      } catch (final ParsingException e) {
        throw new IOException(e);
      }
    }
  }

  private long determineFileSectionDataOffset()
  {
    final var fileSectionOffset =
      this.fileSectionDescription()
        .fileOffset();
    return fileSectionOffset + 16L + 4L;
  }
}
