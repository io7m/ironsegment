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
import com.io7m.ironsegment.api.ISImages;
import com.io7m.ironsegment.api.ISManifest;

import java.util.Map;

import static com.io7m.ironsegment.basic.internal.IS1Names.qName;

/**
 * An element handler.
 */

public final class IS1EManifest
  implements BTElementHandlerType<Object, ISManifest>
{
  private ISImages rImages;
  private IS1EObjects.Holder rObjects;
  private IS1EMetadata.Holder rMeta;

  /**
   * An element handler.
   *
   * @param context The context
   */

  public IS1EManifest(
    final BTElementParsingContextType context)
  {

  }

  @Override
  public Map<BTQualifiedName, BTElementHandlerConstructorType<?, ?>>
  onChildHandlersRequested(
    final BTElementParsingContextType context)
  {
    return Map.ofEntries(
      Map.entry(qName("Objects"), IS1EObjects::new),
      Map.entry(qName("Images"), IS1EImages::new),
      Map.entry(qName("Metadata"), IS1EMetadata::new)
    );
  }

  @Override
  public ISManifest onElementFinished(
    final BTElementParsingContextType context)
    throws Exception
  {
    return new ISManifest(
      this.rImages,
      this.rObjects.values(),
      this.rMeta.values()
    );
  }

  @Override
  public void onChildValueProduced(
    final BTElementParsingContextType context,
    final Object result)
    throws Exception
  {
    switch (result) {
      case final ISImages images -> {
        this.rImages = images;
      }
      case final IS1EObjects.Holder objects -> {
        this.rObjects = objects;
      }
      case final IS1EMetadata.Holder meta -> {
        this.rMeta = meta;
      }
      default -> {
        throw new IllegalStateException(
          "Unrecognized element: %s".formatted(result)
        );
      }
    }
  }
}
