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


package com.io7m.ironsegment.tests;

import com.io7m.ironsegment.api.ISImage;
import com.io7m.ironsegment.api.ISImageID;
import com.io7m.ironsegment.api.ISImageSemantic;
import com.io7m.ironsegment.api.ISImages;
import com.io7m.ironsegment.api.ISManifest;
import com.io7m.ironsegment.api.ISObject;
import com.io7m.ironsegment.api.ISObjectID;
import com.io7m.ironsegment.api.ISVersion;
import com.io7m.ironsegment.basic.IS1Writers;
import com.io7m.ironsegment.writer.api.ISWriteRequest;
import com.io7m.jmulticlose.core.CloseableCollection;
import com.io7m.lanark.core.RDottedName;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public final class ISWriteDemo
{
  private ISWriteDemo()
  {

  }

  public static void main(
    final String[] args)
    throws Exception
  {
    final var writers =
      new IS1Writers();

    final var path =
      Paths.get("/tmp/out.isb");

    try (var resources = CloseableCollection.create()) {
      final var channel =
        resources.add(Files.newByteChannel(
          path,
          TRUNCATE_EXISTING,
          CREATE,
          WRITE));

      final var request =
        new ISWriteRequest(channel, path.toUri(), new ISVersion(1, 0));
      final var writer =
        resources.add(writers.createWriter(request));
      final var writable =
        resources.add(writer.execute());

      try (var section = writable.createSectionInfo()) {
        final var images =
          new TreeMap<ISImageID, ISImage>();
        final var objects =
          new TreeMap<ISObjectID, ISObject>();
        final var metadata =
          new TreeMap<RDottedName, String>();

        final var oid1 = new ISObjectID(1L);
        final var oid2 = new ISObjectID(2L);
        final var oid3 = new ISObjectID(3L);
        objects.put(oid1, new ISObject(oid1, "An object."));
        objects.put(oid2, new ISObject(oid2, "Another object."));
        objects.put(oid3, new ISObject(oid3, "Yet another object."));

        metadata.put(new RDottedName("com.io7m.license"), """
Copyright © 2024 Mark Raynsford <code@io7m.com> https://www.io7m.com

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
""");

        final var iid1 = new ISImageID(1L);
        final var iid2 = new ISImageID(2L);
        final var iid3 = new ISImageID(3L);
        images.put(iid1, new ISImage(iid1, ISImageSemantic.DENOISE_RGB8));
        images.put(iid2, new ISImage(iid2, ISImageSemantic.DEPTH_16));
        images.put(iid3, new ISImage(iid3, ISImageSemantic.OBJECT_ID_32));

        final var info =
          new ISManifest(
            new ISImages(images, 1024L, 1024L),
            objects,
            metadata
          );
        section.setManifest(info);
      }

      try (var section = writable.createSectionImage()) {
        final var imageData =
          section.createImageData(
            new ISImageID(1L),
            1024L,
            1024L,
            ISImageSemantic.DENOISE_RGB8
          );

        try (var c = imageData.channel()) {
          final var data = new byte[4];
          c.write(ByteBuffer.wrap(data));
        }
      }

      try (var section = writable.createSectionImage()) {
        final var imageData =
          section.createImageData(
            new ISImageID(1L),
            1024L,
            1024L,
            ISImageSemantic.DEPTH_16
          );

        try (var c = imageData.channel()) {
          final var data = new byte[4];
          c.write(ByteBuffer.wrap(data));
        }
      }

      try (var section = writable.createSectionImage()) {
        final var imageData =
          section.createImageData(
            new ISImageID(1L),
            1024L,
            1024L,
            ISImageSemantic.MONOCHROME_LINES_8
          );

        try (var c = imageData.channel()) {
          final var data = new byte[4];
          c.write(ByteBuffer.wrap(data));
        }
      }

      try (var section = writable.createSectionEnd()) {

      }
    }
  }
}
