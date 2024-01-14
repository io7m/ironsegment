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

import com.io7m.anethum.api.SerializationException;
import com.io7m.ironsegment.api.ISImage;
import com.io7m.ironsegment.api.ISImages;
import com.io7m.ironsegment.api.ISManifest;
import com.io7m.ironsegment.api.ISObject;
import com.io7m.ironsegment.api.ISObjectID;
import com.io7m.ironsegment.basic.ISManifestSchemas;
import com.io7m.ironsegment.parser.api.ISManifestSerializerType;
import com.io7m.lanark.core.RDottedName;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.util.Objects;
import java.util.SortedMap;

/**
 * A manifest serializer.
 */

public final class IS1ManifestSerializer implements ISManifestSerializerType
{
  private final XMLOutputFactory factory;
  private final OutputStream stream;
  private final XMLStreamWriter writer;

  /**
   * A manifest serializer.
   *
   * @param inStream The output stream
   */

  public IS1ManifestSerializer(
    final OutputStream inStream)
  {
    this.stream =
      Objects.requireNonNull(inStream, "stream");
    this.factory =
      XMLOutputFactory.newFactory();

    try {
      this.writer =
        this.factory.createXMLStreamWriter(this.stream, "UTF-8");
    } catch (final XMLStreamException e) {
      throw new IllegalStateException(e);
    }
  }

  private void writeMetadata(
    final SortedMap<RDottedName, String> metadata)
    throws XMLStreamException
  {
    this.writer.writeStartElement("Metadata");
    for (final var entry : metadata.entrySet()) {
      this.writer.writeStartElement("Meta");
      this.writer.writeAttribute("Name", entry.getKey().value());
      this.writer.writeCData(entry.getValue());
      this.writer.writeEndElement();
    }
    this.writer.writeEndElement();
  }

  private void writeObjects(
    final SortedMap<ISObjectID, ISObject> objects)
    throws XMLStreamException
  {
    this.writer.writeStartElement("Objects");
    for (final var object : objects.values()) {
      this.writeObject(object);
    }
    this.writer.writeEndElement();
  }

  private void writeObject(
    final ISObject object)
    throws XMLStreamException
  {
    this.writer.writeStartElement("Object");
    this.writer.writeAttribute("ID", object.objectId().toString());
    this.writer.writeCData(object.description());
    this.writer.writeEndElement();
  }

  private void writeImages(
    final ISImages images)
    throws XMLStreamException
  {
    this.writer.writeStartElement("Images");
    this.writer.writeAttribute(
      "Width",
      Long.toUnsignedString(images.width())
    );
    this.writer.writeAttribute(
      "Height",
      Long.toUnsignedString(images.height())
    );
    for (final var image : images.images().values()) {
      this.writeImage(image);
    }
    this.writer.writeEndElement();
  }

  private void writeImage(
    final ISImage image)
    throws XMLStreamException
  {
    this.writer.writeStartElement("Image");
    this.writer.writeAttribute("ID", image.imageId().toString());
    this.writer.writeAttribute("Semantic", image.semantic().toString());
    this.writer.writeEndElement();
  }

  private static String findNS()
  {
    return ISManifestSchemas.schema1().namespace().toString();
  }

  @Override
  public void execute(
    final ISManifest info)
    throws SerializationException
  {
    try {
      this.writer.writeStartDocument("UTF-8", "1.0");
      this.writer.writeStartElement("Manifest");
      this.writer.writeDefaultNamespace(findNS());

      this.writeImages(info.images());
      this.writeObjects(info.objects());
      this.writeMetadata(info.metadata());

      this.writer.writeEndElement();
      this.writer.writeEndDocument();
    } catch (final XMLStreamException e) {
      throw new SerializationException(e.getMessage(), e);
    }
  }

  @Override
  public void close()
  {

  }
}
