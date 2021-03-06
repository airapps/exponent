/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#import "ABI8_0_0RCTXCAssetImageLoader.h"

#import <libkern/OSAtomic.h>

#import "ABI8_0_0RCTUtils.h"

@implementation ABI8_0_0RCTXCAssetImageLoader

ABI8_0_0RCT_EXPORT_MODULE()

- (BOOL)canLoadImageURL:(NSURL *)requestURL
{
  return ABI8_0_0RCTIsXCAssetURL(requestURL);
}

 - (ABI8_0_0RCTImageLoaderCancellationBlock)loadImageForURL:(NSURL *)imageURL
                                               size:(CGSize)size
                                              scale:(CGFloat)scale
                                         resizeMode:(ABI8_0_0RCTResizeMode)resizeMode
                                    progressHandler:(ABI8_0_0RCTImageLoaderProgressBlock)progressHandler
                                  completionHandler:(ABI8_0_0RCTImageLoaderCompletionBlock)completionHandler
{
  __block volatile uint32_t cancelled = 0;
  dispatch_async(dispatch_get_main_queue(), ^{

    if (cancelled) {
      return;
    }
    NSString *imageName = ABI8_0_0RCTBundlePathForURL(imageURL);
    UIImage *image = [UIImage imageNamed:imageName];
    if (image) {
      if (progressHandler) {
        progressHandler(1, 1);
      }
      completionHandler(nil, image);
    } else {
      NSString *message = [NSString stringWithFormat:@"Could not find image named %@", imageName];
      completionHandler(ABI8_0_0RCTErrorWithMessage(message), nil);
    }
  });

  return ^{
    OSAtomicOr32Barrier(1, &cancelled);
  };
}

@end
