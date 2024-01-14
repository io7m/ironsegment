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

package com.io7m.ironsegment.parser.api;


import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.util.Objects;

/**
 * A parse request.
 *
 * @param channel The file channel
 * @param source  The data source
 */

public record ISParseRequest(
  SeekableByteChannel channel,
  URI source)
{
  /**
   * A parse request.
   *
   * @param channel The file channel
   * @param source  The data source
   */

  public ISParseRequest
  {
    Objects.requireNonNull(channel, "channel");
    Objects.requireNonNull(source, "source");
  }

  /**
   * Create a new mutable request builder.
   *
   * @param inChannel The file channel
   * @param inSource  The data source
   *
   * @return A request builder
   */

  public static ISParseRequestBuilderType builder(
    final SeekableByteChannel inChannel,
    final URI inSource)
  {
    return new Builder(inChannel, inSource);
  }

  private static final class Builder
    implements ISParseRequestBuilderType
  {
    private SeekableByteChannel channel;
    private URI source;

    private Builder(
      final SeekableByteChannel inChannel,
      final URI inSource)
    {
      this.channel =
        Objects.requireNonNull(inChannel, "channel");
      this.source =
        Objects.requireNonNull(inSource, "source");
    }

    @Override
    public SeekableByteChannel channel()
    {
      return this.channel;
    }

    @Override
    public ISParseRequestBuilderType setChannel(
      final SeekableByteChannel inChannel)
    {
      this.channel = Objects.requireNonNull(inChannel, "channel");
      return this;
    }

    @Override
    public URI source()
    {
      return this.source;
    }

    @Override
    public ISParseRequestBuilderType setSource(
      final URI inSource)
    {
      this.source = Objects.requireNonNull(inSource, "source");
      return this;
    }

    @Override
    public ISParseRequest build()
    {
      return new ISParseRequest(
        this.channel,
        this.source
      );
    }
  }
}
