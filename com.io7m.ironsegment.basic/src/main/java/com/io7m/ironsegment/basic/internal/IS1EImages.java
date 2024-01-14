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


package com.io7m.ironsegment.basic.internal;

import com.io7m.blackthorne.core.BTElementHandlerConstructorType;
import com.io7m.blackthorne.core.BTElementHandlerType;
import com.io7m.blackthorne.core.BTElementParsingContextType;
import com.io7m.blackthorne.core.BTQualifiedName;
import com.io7m.ironsegment.api.ISImage;
import com.io7m.ironsegment.api.ISImageID;
import com.io7m.ironsegment.api.ISImages;
import org.xml.sax.Attributes;

import java.util.Map;
import java.util.TreeMap;

/**
 * An element handler.
 */

public final class IS1EImages
  implements BTElementHandlerType<ISImage, ISImages>
{
  private final TreeMap<ISImageID, ISImage> images;
  private long width;
  private long height;

  /**
   * An element handler.
   *
   * @param context The context
   */

  public IS1EImages(
    final BTElementParsingContextType context)
  {
    this.images = new TreeMap<>();
  }

  @Override
  public Map<BTQualifiedName, BTElementHandlerConstructorType<?, ? extends ISImage>>
  onChildHandlersRequested(
    final BTElementParsingContextType context)
  {
    return Map.ofEntries(
      Map.entry(IS1Names.qName("Image"), IS1EImage::new)
    );
  }

  @Override
  public void onElementStart(
    final BTElementParsingContextType context,
    final Attributes attributes)
  {
    this.width =
      Long.parseUnsignedLong(attributes.getValue("Width"));
    this.height =
      Long.parseUnsignedLong(attributes.getValue("Height"));
  }

  @Override
  public void onChildValueProduced(
    final BTElementParsingContextType context,
    final ISImage result)
  {
    this.images.put(result.imageId(), result);
  }

  @Override
  public ISImages onElementFinished(
    final BTElementParsingContextType context)
  {
    return new ISImages(
      this.images,
      this.width,
      this.height
    );
  }
}
