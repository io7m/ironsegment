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


import com.io7m.ironsegment.api.ISImageID;
import com.io7m.ironsegment.api.ISImageSemantic;
import com.io7m.ironsegment.api.ISSectionWritableImageType;
import com.io7m.ironsegment.api.ISSectionWritableType;
import com.io7m.ironsegment.api.ISWritableImageDataType;
import com.io7m.ironsegment.writer.api.ISWriteRequest;
import com.io7m.jbssio.api.BSSWriterProviderType;
import com.io7m.jbssio.api.BSSWriterRandomAccessType;
import com.io7m.wendover.core.CloseShieldSeekableByteChannel;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;

/**
 * A writable image section.
 */

public final class IS1SectionWritableImage
  extends IS1SectionWritableAbstract
  implements ISSectionWritableImageType
{
  private final BSSWriterProviderType writers;

  /**
   * A writable image info section.
   *
   * @param inWriters    A writer provider
   * @param inOnClose    A function executed on closing
   * @param inRequest    A write request
   * @param inIdentifier An identifier
   * @param inWriter     A writer
   */

  public IS1SectionWritableImage(
    final BSSWriterProviderType inWriters,
    final BSSWriterRandomAccessType inWriter,
    final ISWriteRequest inRequest,
    final long inIdentifier,
    final ISOnCloseOperationType<ISSectionWritableType> inOnClose)
  {
    super(inWriter, inRequest, inIdentifier, inOnClose);
    this.writers = Objects.requireNonNull(inWriters, "inWriters");
  }

  @Override
  public ISWritableImageDataType createImageData(
    final ISImageID imageId,
    final long width,
    final long height,
    final ISImageSemantic semantic)
    throws IOException
  {
    Objects.requireNonNull(imageId, "imageId");
    Objects.requireNonNull(semantic, "semantic");

    final var pixelCount =
      Math.multiplyExact(width, height);
    final var dataSize =
      Math.multiplyExact(semantic.pixelSizeOctets(), pixelCount);

    try (var channel = this.sectionDataChannel()) {
      final var targetURI = this.request().target();
      try (var writer =
             this.writers.createWriterFromChannel(
               targetURI, channel, "image")) {

        writer.writeU32BE(imageId.value());

        writer.skip(dataSize);
        writer.seekTo(writer.offsetCurrentRelative() - 1L);
        writer.writeU8(0);

        writer.seekTo(4L);
      }
    }

    return new ISWritableImageData(
      this.request().channel(),
      this.offsetStartData()
    );
  }

  private static final class ISWritableImageData
    implements ISWritableImageDataType
  {
    private final SeekableByteChannel fileChannel;
    private final long fileSectionDataStart;
    private final CloseShieldSeekableByteChannel subChannel;

    ISWritableImageData(
      final SeekableByteChannel inChannel,
      final long inFileSectionDataStart)
      throws IOException
    {
      this.fileChannel =
        Objects.requireNonNull(inChannel, "channel");
      this.fileSectionDataStart =
        inFileSectionDataStart;

      final var offset = this.fileSectionDataStart + 4L;
      this.fileChannel.position(offset);

      this.subChannel =
        new CloseShieldSeekableByteChannel(this.fileChannel);
    }

    @Override
    public WritableByteChannel channel()
    {
      return this.subChannel;
    }
  }
}
