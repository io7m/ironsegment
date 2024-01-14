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

import com.io7m.blackthorne.core.BTElementHandlerType;
import com.io7m.blackthorne.core.BTElementParsingContextType;
import com.io7m.lanark.core.RDottedName;
import org.xml.sax.Attributes;

import java.util.Map;

/**
 * An element handler.
 */

public final class IS1EMeta
  implements BTElementHandlerType<Object, Map.Entry<RDottedName, String>>
{
  private String valueText;
  private RDottedName key;

  /**
   * An element handler.
   *
   * @param context The context
   */

  public IS1EMeta(
    final BTElementParsingContextType context)
  {
    this.valueText = "";
  }

  @Override
  public void onElementStart(
    final BTElementParsingContextType context,
    final Attributes attributes)
  {
    this.key = new RDottedName(attributes.getValue("Name"));
  }

  @Override
  public Map.Entry<RDottedName, String> onElementFinished(
    final BTElementParsingContextType context)
  {
    return Map.entry(this.key, this.valueText);
  }

  @Override
  public void onCharacters(
    final BTElementParsingContextType context,
    final char[] data,
    final int offset,
    final int length)
  {
    this.valueText = new String(data, offset, length);
  }
}
