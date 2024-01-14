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
import com.io7m.lanark.core.RDottedName;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An element handler.
 */

public final class IS1EMetadata
  implements BTElementHandlerType<Map.Entry<RDottedName, String>, IS1EMetadata.Holder>
{
  private final TreeMap<RDottedName, String> meta;

  record Holder(SortedMap<RDottedName, String> values)
  {

  }

  /**
   * An element handler.
   *
   * @param context The context
   */

  public IS1EMetadata(
    final BTElementParsingContextType context)
  {
    this.meta = new TreeMap<>();
  }

  @Override
  public Map<BTQualifiedName, BTElementHandlerConstructorType<?, ? extends Map.Entry<RDottedName, String>>>
  onChildHandlersRequested(
    final BTElementParsingContextType context)
  {
    return Map.ofEntries(
      Map.entry(IS1Names.qName("Meta"), IS1EMeta::new)
    );
  }

  @Override
  public void onChildValueProduced(
    final BTElementParsingContextType context,
    final Map.Entry<RDottedName, String> result)
  {
    this.meta.put(result.getKey(), result.getValue());
  }

  @Override
  public Holder onElementFinished(
    final BTElementParsingContextType context)
  {
    return new Holder(this.meta);
  }
}
