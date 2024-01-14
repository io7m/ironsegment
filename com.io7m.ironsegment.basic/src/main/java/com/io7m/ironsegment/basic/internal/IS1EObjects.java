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
import com.io7m.ironsegment.api.ISObject;
import com.io7m.ironsegment.api.ISObjectID;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An element handler.
 */

public final class IS1EObjects
  implements BTElementHandlerType<ISObject, IS1EObjects.Holder>
{
  private final TreeMap<ISObjectID, ISObject> objects;

  record Holder(SortedMap<ISObjectID, ISObject> values)
  {

  }

  /**
   * An element handler.
   *
   * @param context The context
   */

  public IS1EObjects(
    final BTElementParsingContextType context)
  {
    this.objects = new TreeMap<>();
  }

  @Override
  public Map<BTQualifiedName, BTElementHandlerConstructorType<?, ? extends ISObject>>
  onChildHandlersRequested(
    final BTElementParsingContextType context)
  {
    return Map.ofEntries(
      Map.entry(IS1Names.qName("Object"), IS1EObject::new)
    );
  }

  @Override
  public void onChildValueProduced(
    final BTElementParsingContextType context,
    final ISObject result)
  {
    this.objects.put(result.objectId(), result);
  }

  @Override
  public Holder onElementFinished(
    final BTElementParsingContextType context)
  {
    return new Holder(this.objects);
  }
}
