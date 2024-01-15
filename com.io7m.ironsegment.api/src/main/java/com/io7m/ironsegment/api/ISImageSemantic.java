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


package com.io7m.ironsegment.api;

/**
 * The semantic of an image.
 */

public enum ISImageSemantic
{
  /**
   * The image is the source for denoising. RGB, 16 bits per channel,
   * unsigned normalized fixed-point.
   */

  DENOISE_RGB16,

  /**
   * The image is the source for denoising. RGB, 8 bits per channel,
   * unsigned normalized fixed-point.
   */

  DENOISE_RGB8,

  /**
   * The image is the source for denoising. RGBA, 16 bits per channel,
   * unsigned normalized fixed-point.
   */

  DENOISE_RGBA16,

  /**
   * The image is the source for denoising. RGBA, 8 bits per channel,
   * unsigned normalized fixed-point.
   */

  DENOISE_RGBA8,

  /**
   * The image is a depth map, 16 bits per pixel,
   * unsigned normalized fixed-point.
   */

  DEPTH_16,

  /**
   * The image is a depth map, 32 bits per pixel,
   * unsigned normalized fixed-point.
   */

  DEPTH_32,

  /**
   * The image is a monochrome lines image, 8 bits per pixel,
   * unsigned normalized fixed-point.
   */

  MONOCHROME_LINES_8,

  /**
   * The image is an object ID map, 32 bits per pixel.
   */

  OBJECT_ID_32;

  /**
   * @return The size of a pixel in octets
   */

  public long pixelSizeOctets()
  {
    return switch (this) {
      case DENOISE_RGB16 -> 3L * 2L;
      case DENOISE_RGB8 -> 3L;
      case DENOISE_RGBA16 -> 4L * 2L;
      case DENOISE_RGBA8 -> 4L;
      case DEPTH_16 -> 2L;
      case DEPTH_32 -> 4L;
      case MONOCHROME_LINES_8 -> 1L;
      case OBJECT_ID_32 -> 4L;
    };
  }
}
