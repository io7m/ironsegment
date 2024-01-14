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


package com.io7m.ironsegment.tests.arbitraries;

import com.io7m.ironsegment.api.ISImage;
import com.io7m.ironsegment.api.ISImageID;
import com.io7m.ironsegment.api.ISImageSemantic;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Combinators;

public final class ISArbImage extends ISArbAbstract<ISImage>
{
  public ISArbImage()
  {
    super(ISImage.class, () -> {
      return Combinators.combine(
        Arbitraries.defaultFor(ISImageID.class),
        Arbitraries.defaultFor(ISImageSemantic.class)
      ).as(ISImage::new);
    });
  }
}
